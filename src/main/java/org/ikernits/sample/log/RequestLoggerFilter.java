package org.ikernits.sample.log;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.jetty.io.EofException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RequestLoggerFilter implements Filter {

    private Logger requestLogger;
    private Logger errorLogger;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        requestLogger = Logger.getLogger("request");
        errorLogger = Logger.getLogger(RequestLoggerFilter.class);
    }

    private static final Set<String> excludeHeaders = ImmutableSet.of(
        "authorization", "cookie"
    );

    private static final int parameterLengthLimit = 1024;

    private String buildRequestDetails(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        List<String> headers = Collections.list(req.getHeaderNames());
        sb.append("Local IP: ").append(req.getLocalAddr()).append(":").append(req.getLocalPort()).append("\n");
        sb.append("Remote IP: ").append(req.getRemoteAddr()).append(":").append(req.getRemotePort()).append("\n");
        sb.append("Method: ").append(req.getMethod()).append("\n");
        sb.append("URI: ").append(req.getRequestURI()).append("\n");
        sb.append("Path: ").append(req.getServletPath()).append("\n");

        sb.append("Headers:\n");
        headers.stream()
            .filter(h -> !excludeHeaders.contains(h.toLowerCase()))
            .forEach(h -> {
                List<String> values = Collections.list(req.getHeaders(h));
                values.forEach(v -> sb.append(h).append("=").append(v).append("\n"));
            });

        Collections.list(req.getParameterNames());

        sb.append("Parameters:\n");
        List<String> parameters = Collections.list(req.getParameterNames());
        parameters.forEach(p ->
            sb.append(p)
                .append("=")
                .append(StringUtils.substring(req.getParameter(p), 0, parameterLengthLimit))
                .append("\n")
        );

        return sb.toString();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse resp = (HttpServletResponse) response;

        int status = 0;
        try {
            chain.doFilter(request, response);
            status = resp.getStatus();
        } catch (Exception ex) {
            if (resp.getStatus() != 0) {
                status = resp.getStatus();
            } else {
                status = 500;
            }

            final Level level;
            if (ExceptionUtils.indexOfThrowable(ex, EofException.class) != -1) {
                level = Level.WARN;
                status = 0;
            } else {
                level = Level.ERROR;
            }
            errorLogger.log(
                level,
                "Unhandled HTTP request exception\n" +
                "--- Request details ---\n" + buildRequestDetails(req) + "--- Request details end ---\n",
                ex
            );
        } finally {
            final Level level;
            if (status >= 200 && status < 400) {
                level = Level.INFO;
            } else if (status >= 400 && status < 500) {
                level = Level.WARN;
            } else {
                level = Level.ERROR;
            }
            requestLogger.log(level, req.getRemoteAddr() + " - " + req.getMethod() + " - " + req.getRequestURL() + " - " + status);
        }
    }

    @Override
    public void destroy() {
    }
}
