package com.vodafone.resource;

import com.vodafone.client.Service1Client;
import com.vodafone.exceptions.ClientOneException;
import com.vodafone.request.Endpoint3Request;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.Duration;

@Path("/service2")
public class Service2Resource {

    @Inject
    @RestClient
    Service1Client service1Client;

    @GET
    @Path("endpoint1")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "Return a welcome message",
            description = "It returns a welcome message after a 2 second delay from the service-one client")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "Welcome message")}
    )
    public String endpoint1() {
        return service1Client.getWelcomeWithDelay();
    }

    @POST
    @Path("endpoint2")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "Return a success message",
            description = "It returns a success message from the service-one client")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "Success message")}
    )
    public String endpoint2() {
        Endpoint3Request request = new Endpoint3Request("sample");
        return service1Client.getSuccessText(request);
    }

    @POST
    @Path("endpoint3")
    @Operation(summary = "Return an exception",
            description = "It returns a controlled exception from the service-one client")
    @APIResponses({@APIResponse(
            name = "Exception Response",
            responseCode = "500",
            description = "Exception message")}
    )
    public Response endpoint3() {
        return Response.ok(service1Client.getException()).build();
    }
}
