package com.vodafone;

import com.vodafone.client.Service1Client;
import com.vodafone.resource.Service2Resource;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestHTTPEndpoint(Service2Resource.class)
class Service2ResourceTest {

    @InjectMock
    @RestClient
    Service1Client service1Client;

    private static final String welcomeText = "welcome";
    private static final String successText = "success";
    private static final String errorText = "error";

    @Test
    void testEndpoint1() {
        Mockito.when(service1Client.getWelcomeWithDelay()).thenReturn(welcomeText);
        given()
                .when().get("/endpoint1")
                .then()
                .statusCode(200)
                .body(is(welcomeText));
    }

    @Test
    void testEndpoint2() {
        Mockito.when(service1Client.getSuccessText(Mockito.any())).thenReturn(successText);
        given()
                .when().post("/endpoint2")
                .then()
                .statusCode(200)
                .body(is(successText));
    }

    @Test
    void testEndpoint3() {
        Mockito.when(service1Client.getException()).thenReturn(errorText);
        given()
                .when().post("/endpoint3")
                .then()
                .body(is(errorText));
    }

}