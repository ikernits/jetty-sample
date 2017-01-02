package org.ikernits.sample.http;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonParseException;
import org.ikernits.sample.util.GsonUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public interface HttpClientService {

    enum HttpRequestType {
        Get, Post, Put, Delete
    }

    class HttpRequest {
        private final URI uri;
        private final HttpRequestType type;
        private final Map<String, String> headers;
        private final Map<String, String> params;

        public static Builder builder() {
            return new Builder();
        }

        public static Builder buildGet(String url) {
            return new Builder()
                .setType(HttpRequestType.Get)
                .setUri(url);
        }

        public static Builder buildPost(String url) {
            return new Builder()
                .setType(HttpRequestType.Post)
                .setUri(url);
        }

        private HttpRequest(HttpRequestType type, URI uri, String userAgent, Map<String, String> headers, Map<String, String> params) {
            this.type = type;
            this.uri = uri;
            this.headers = ImmutableMap.copyOf(headers);
            this.params = ImmutableMap.copyOf(params);
        }

        public String getUrl() {
            return uri.toString();
        }

        public URI getUri() {
            return uri;
        }

        public HttpRequestType getType() {
            return type;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public Map<String, String> getParams() {
            return params;
        }



        public static class Builder {
            private HttpRequestType type = HttpRequestType.Get;
            private URI uri = null;
            private String userAgent = null;
            private Map<String, String> headers = new HashMap<>();
            private Map<String, String> params = new HashMap<>();

            public Builder setUri(String url) {
                try {
                    this.uri = new URI(url);
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException("Invalid URL: '" + url + "'");
                }
                return this;
            }

            public Builder setType(HttpRequestType type) {
                this.type = type;
                return this;
            }

            public Builder setUserAgent(String userAgent) {
                this.userAgent = userAgent;
                return this;
            }

            public Builder addHeader(String name, String value) {
                headers.put(name, value);
                return this;
            }

            public Builder addParam(String name, String value) {
                params.put(name, value);
                return this;
            }

            public HttpRequest build() {
                return new HttpRequest(
                    type, uri, userAgent, headers, params
                );
            }
        }
    }


    class HttpResponse {
        private final Integer code;
        private final Map<String, String> headers;
        private final String body;

        public HttpResponse(Integer code, Map<String, String> headers, String body) {
            this.code = code;
            this.headers = headers;
            this.body = body;
        }

        public Integer getCode() {
            return code;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public String getBody() {
            return body;
        }
    }


    HttpResponse execute(HttpRequest request);

    default <T> T executeWithJsonResponse(HttpRequest request, Class<T> responseType) {
        return executeWithJsonResponse(request, responseType, r -> true);
    }

    default <T> T executeWithJsonResponse(HttpRequest request, Class<T> responseType, Predicate<T> verifier) {
        HttpResponse response = execute(request);
        T result;
        try {
            result = GsonUtils.gson.fromJson(response.getBody(), responseType);
        } catch (JsonParseException ex) {
            throw new HttpClientException(
                HttpClientException.Type.ParseError,
                request,
                response,
                "Failed to parse json response of type '" + responseType.getName() + "'",
                ex
            );
        }

        if (!verifier.test(result)) {
            throw new HttpClientException(
                HttpClientException.Type.VerifyError,
                request,
                response,
                "Failed to verify response of type '" + responseType.getName() + "'",
                null
            );
        }

        return result;
    }

    class HttpClientException extends RuntimeException {
        public enum Type {
            IoFailed, Timeout, BadUri, BadHttpCode, ParseError, VerifyError
        }

        private final Type type;
        private final HttpRequest request;
        private final HttpResponse response;

        public HttpClientException(Type type, HttpRequest request, HttpResponse response, String message, Throwable cause) {
            super(message, cause);
            this.type = type;
            this.request = request;
            this.response = response;
        }

        public Type getType() {
            return type;
        }

        public HttpRequest getRequest() {
            return request;
        }

        public HttpResponse getResponse() {
            return response;
        }
    }
}
