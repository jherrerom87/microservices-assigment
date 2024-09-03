package com.vodafone.interceptor;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.ext.WriterInterceptor;
import jakarta.ws.rs.ext.WriterInterceptorContext;

import java.io.IOException;

@Provider
public class Service1Interceptor implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext writerInterceptorContext) throws IOException, WebApplicationException {
        writerInterceptorContext.getHeaders().add("sample-header","test");
        writerInterceptorContext.proceed();
    }
}
