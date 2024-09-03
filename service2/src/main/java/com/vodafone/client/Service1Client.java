package com.vodafone.client;

import com.vodafone.exceptions.ClientOneException;
import com.vodafone.request.Endpoint3Request;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;

@RegisterRestClient(baseUri = "http://localhost:8081/service1")
public interface Service1Client {

    @GET
    @Path("endpoint1")
    @Produces(MediaType.TEXT_PLAIN)
    @Timeout(unit = ChronoUnit.SECONDS, value = 10)
    @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = 2, delay = 1)
    @Fallback(fallbackMethod = "emptyBodyFallback")
    @CircuitBreaker(requestVolumeThreshold = 4)
    String getWelcomeWithDelay();

    @POST
    @Path("endpoint2")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timeout(unit = ChronoUnit.SECONDS, value = 10)
    @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = 2, delay = 1)
    @Fallback(fallbackMethod = "endpoint2Fallback")
    @CircuitBreaker(requestVolumeThreshold = 4)
    String getSuccessText(Endpoint3Request request);

    @POST
    @Path("endpoint3")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timeout(unit = ChronoUnit.SECONDS, value = 10)
    @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = 2, delay = 1)
    @Fallback(fallbackMethod = "emptyBodyFallback")
    @CircuitBreaker(requestVolumeThreshold = 4)
    String getException();

    default String emptyBodyFallback() throws ClientOneException {
        throw new ClientOneException();
    }
    default String endpoint2Fallback(Endpoint3Request request) throws ClientOneException {
        throw new ClientOneException();
    }
}