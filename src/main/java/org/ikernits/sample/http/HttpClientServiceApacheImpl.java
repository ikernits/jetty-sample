package org.ikernits.sample.http;

import com.google.common.base.Charsets;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpClientServiceApacheImpl implements HttpClientService, InitializingBean, DisposableBean {

    protected String userAgent = "HttpClient";
    protected int maxConnections = 32;
    protected int maxConnectionsPerRoute = 8;
    protected int connectionTtlSeconds = 60;
    protected int poolTimeoutMillis = 5000;
    protected int connectTimeoutMillis = 3000;
    protected int socketTimeoutMillis = 5000;
    protected boolean redirectsEnabled = true;
    protected long responseSizeLimitBytes = 1024*1024*16;

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
    }

    public void setConnectionTtlSeconds(int connectionTtlSeconds) {
        this.connectionTtlSeconds = connectionTtlSeconds;
    }

    public void setPoolTimeoutMillis(int poolTimeoutMillis) {
        this.poolTimeoutMillis = poolTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public void setSocketTimeoutMillis(int socketTimeoutMillis) {
        this.socketTimeoutMillis = socketTimeoutMillis;
    }

    public void setRedirectsEnabled(boolean redirectsEnabled) {
        this.redirectsEnabled = redirectsEnabled;
    }

    public void setResponseSizeLimitBytes(long responseSizeLimitBytes) {
        this.responseSizeLimitBytes = responseSizeLimitBytes;
    }

    private CloseableHttpClient httpClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        httpClient = HttpClientBuilder.create()
            .setMaxConnTotal(maxConnections)
            .setMaxConnPerRoute(maxConnectionsPerRoute)
            .setConnectionTimeToLive(connectionTtlSeconds, TimeUnit.SECONDS)
            .setConnectionManager(new PoolingHttpClientConnectionManager())
            .setUserAgent(userAgent)
            .setDefaultConnectionConfig(ConnectionConfig.custom()
                .setCharset(Charsets.UTF_8)
                .build())
            .setDefaultRequestConfig(RequestConfig.custom()
                .setRedirectsEnabled(redirectsEnabled)
                .setConnectionRequestTimeout(poolTimeoutMillis)
                .setConnectTimeout(connectTimeoutMillis)
                .setSocketTimeout(socketTimeoutMillis)
                .build())
            .setDefaultSocketConfig(SocketConfig.custom()
                .setSoTimeout(socketTimeoutMillis)
                .setTcpNoDelay(true)
                .build())
            .build();
    }

    @Override
    public void destroy() throws Exception {
        httpClient.close();
    }

    private HttpResponse executeImpl(HttpRequest request) throws URISyntaxException, IOException {
        final HttpRequestBase httpRequest;

        switch (request.getType()) {
            case Get:
                httpRequest = new HttpGet(request.getUri());
                break;
            case Post:
                httpRequest = new HttpPost(request.getUri());
                break;
            case Put:
                httpRequest = new HttpPut(request.getUri());
                break;
            case Delete:
                httpRequest = new HttpDelete(request.getUri());
                break;
            default:
                throw new IllegalStateException("Cannot process request of type '" + request.getType() + "'");
        }

        httpRequest.setHeader("Accept-Charset", "UTF-8");
        request.getHeaders().forEach(httpRequest::setHeader);

        if (httpRequest instanceof HttpEntityEnclosingRequestBase) {
            HttpEntityEnclosingRequestBase requestBase = (HttpEntityEnclosingRequestBase) httpRequest;
            requestBase.setEntity(new UrlEncodedFormEntity(
                request.getParams().entrySet().stream()
                    .map(e -> new BasicNameValuePair(e.getKey(), e.getValue()))
                    .collect(Collectors.toList()),
                Charsets.UTF_8
            ));
        } else {
            URIBuilder uriBuilder = new URIBuilder(httpRequest.getURI());
            request.getParams().forEach(uriBuilder::setParameter);
            httpRequest.setURI(uriBuilder.build());
        }

        try (CloseableHttpResponse httpResponse = httpClient.execute(httpRequest)) {
            int code = httpResponse.getStatusLine().getStatusCode();
            Map<String, List<String>> headers = Stream.of(httpResponse.getAllHeaders())
                .collect(Collectors.groupingBy(Header::getName, Collectors.mapping(Header::getValue, Collectors.toList())));
            ByteArrayOutputStream bodyStream = new ByteArrayOutputStream();
            StreamUtils.copyRange(
                httpResponse.getEntity().getContent(),
                bodyStream,
                0,
                responseSizeLimitBytes
            );

            HttpResponse response = new HttpResponse(code, headers, bodyStream.toByteArray());
            if (code < 400 || code >= 600) {
                return response;
            } else {
                throw new HttpClientException(
                    HttpClientException.Type.BadHttpCode,
                    request,
                    response,
                    "HTTP response code = " + code,
                    null
                );
            }
        }
    }

    @Override
    public HttpResponse execute(HttpRequest request) {
        try {
            return executeImpl(request);
        } catch (URISyntaxException ex) {
            throw new HttpClientException(
                HttpClientException.Type.BadUri,
                request,
                null,
                "Invalid request URI: '" + request.getUrl() + "'",
                ex
            );
        } catch (IOException ex) {
            throw new HttpClientException(
                HttpClientException.Type.IoFailed,
                request,
                null,
                "Failed to process request: '" + request.getUrl() + "'",
                ex
            );
        }
    }
}
