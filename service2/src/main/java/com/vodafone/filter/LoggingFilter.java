package com.vodafone.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Logger;

@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger logger = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        BufferedInputStream iStream = new BufferedInputStream(requestContext.getEntityStream());
        byte[] inputContent = new byte[iStream.available()];
        iStream.read(inputContent);
        String body = new String(inputContent, "utf-8");
        logger.info("Inside request filter. Message size: "+inputContent.length+"; Message on the way in: "+body);
        requestContext.setEntityStream(new ByteArrayInputStream(inputContent));
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        logger.info("Message on the way out "+ responseContext.getEntity());
    }
}
