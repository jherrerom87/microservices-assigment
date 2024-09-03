package com.vodafone.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
public class Service1ExceptionMapper  implements ExceptionMapper<ClientOneException> {
    @Override
    public Response toResponse(ClientOneException exception) {
        return Response.serverError().entity(exception.getMessage()).build();
    }
}