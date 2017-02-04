package org.ikernits.sample.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestLoggerFilter implements Filter {

    private Logger requestLogger;
    private Logger errorLogger;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        requestLogger = Logger.getLogger("request");
        errorLogger = Logger.getLogger(RequestLoggerFilter.class);
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
            status = 500;
            errorLogger.error("Unhandled HTTP request exception", ex);
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
