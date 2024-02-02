package com.sifradigital.framework;

import com.sifradigital.framework.resource.RootResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class LoggingFilter implements ContainerRequestFilter {

    private static final Logger Log = LoggerFactory.getLogger(LoggingFilter.class);

    private static final int MAX_BODY_SIZE = 20 * 1024;

    public static final int LOG_NOTHING = 0;
    public static final int LOG_REQUESTS_WITH_BODY_ONLY = 1;
    public static final int LOG_ALL_REQUESTS = 2;
    public static final int LOG_ALL_INCLUDING_PING = 3;

    private final int logLevel;

    public LoggingFilter() {
        this.logLevel = LOG_REQUESTS_WITH_BODY_ONLY;
    }

    public LoggingFilter(int logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        if (!shouldLog(containerRequestContext))
            return;
        Log.debug("{} {}", containerRequestContext.getMethod(), parseEndpoint(containerRequestContext));
        if (hasBody(containerRequestContext)) {
            InputStream stream = containerRequestContext.getEntityStream();
            if (!stream.markSupported()) {
                stream = new BufferedInputStream(stream);
            }
            stream.mark(MAX_BODY_SIZE + 1);
            final byte[] bytes = new byte[MAX_BODY_SIZE + 1];
            final int size = stream.read(bytes);
            Log.debug(new String(bytes, 0, Math.min(size, MAX_BODY_SIZE)));
            stream.reset();
            containerRequestContext.setEntityStream(stream);
        }
    }

    private String parseEndpoint(ContainerRequestContext containerRequestContext) {
        URI uri = containerRequestContext.getUriInfo().getRequestUri();
        String path = uri.getPath();
        if (uri.getQuery() != null) {
            path += "?" + uri.getQuery();
        }
        return path;
    }

    private boolean shouldLog(ContainerRequestContext containerRequestContext) {
        switch (logLevel) {
            case LOG_NOTHING:
                return false;
            case LOG_ALL_INCLUDING_PING:
                return true;
            case LOG_REQUESTS_WITH_BODY_ONLY:
                return hasBody(containerRequestContext);
            case LOG_ALL_REQUESTS:
            default:
                return isNotRoot(containerRequestContext);
        }
    }

    private boolean hasBody(ContainerRequestContext containerRequestContext) {
        return (containerRequestContext.getMethod().equals("POST") || containerRequestContext.getMethod().equals("PUT")) && containerRequestContext.hasEntity();
    }

    private boolean isNotRoot(ContainerRequestContext containerRequestContext) {
        if (containerRequestContext.getUriInfo().getMatchedResources().isEmpty())
            return true;
        return !(containerRequestContext.getUriInfo().getMatchedResources().get(0) instanceof RootResource);
    }
}
