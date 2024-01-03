package com.sifradigital.framework.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionWrapperMapper implements ExceptionMapper<Throwable> {

    private static final Logger Log = LoggerFactory.getLogger(ExceptionWrapperMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException) {
            final Response response = ((WebApplicationException) exception).getResponse();
            return Response.fromResponse(response)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new ErrorResponse(response.getStatus(), exception.getLocalizedMessage()))
                    .build();
        }

        Log.error("Error handling request", exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErrorResponse(exception.getMessage()))
                .build();
    }
}
