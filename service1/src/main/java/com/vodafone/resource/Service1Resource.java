package com.vodafone.resource;

import com.vodafone.exceptions.SampleException;
import com.vodafone.request.Endpoint2Request;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.time.Duration;

@Path("/service1")
public class Service1Resource {

    @Inject
    @ConfigProperty(name="myapplication.texts.welcome",defaultValue
            ="welcome" )
    String welcomeText;

    @Inject
    @ConfigProperty(name="myapplication.texts.exception",defaultValue
            ="exception" )
    String exceptionText;

    @Inject
    @ConfigProperty(name="myapplication.texts.success",defaultValue
            ="success" )
    String successText;

    @GET
    @Path("endpoint1")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "Return a welcome message",
            description = "It returns a welcome message after a 2 second delay")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "Welcome message")}
    )
    public Uni<String> endpoint1() {
        return Uni.createFrom().item(welcomeText).onItem().delayIt().by(Duration.ofSeconds(2));
    }

    @POST
    @Path("endpoint2")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "Return a success message",
            description = "It returns a success message")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "Success message")}
    )
    public Uni<String> endpoint2(Endpoint2Request endpoint2Request) {
        return Uni.createFrom().item(endpoint2Request).onItem().transform(requestBody -> successText);
    }

    @POST
    @Path("endpoint3")
    @Operation(summary = "Return an exception",
            description = "It returns an exception")
    @APIResponses({@APIResponse(
            name = "Exception Response",
            responseCode = "500",
            description = "Welcome message")}
    )
    public Uni<Void> endpoint3() {
        return Uni.createFrom().voidItem().onItem().invoke(Unchecked.consumer((i) -> {
            throw new SampleException(exceptionText);
        }
        ));
    }
}
