package com.vodafone;

import com.vodafone.request.Endpoint2Request;
import com.vodafone.resource.Service1Resource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestHTTPEndpoint(Service1Resource.class)
class Service1ResourceTest {

    @Inject
    @ConfigProperty(name = "myapplication.texts.welcome", defaultValue
            = "welcome")
    String welcomeText;

    @Inject
    @ConfigProperty(name = "myapplication.texts.success", defaultValue
            = "success")
    String successText;

    @Test
    void testEndpoint1() {
        given()
                .when().get("/endpoint1")
                .then()
                .statusCode(200)
                .body(is(welcomeText));
    }

    @Test
    void testEndpoint2() {
        given()
                .contentType(ContentType.JSON)
                .body(new Endpoint2Request())
                .when().post("/endpoint2")
                .then()
                .statusCode(200)
                .body(is(successText));
    }

    @Test
    void testEndpoint3() {

        given()
                .when().post("/endpoint3")
                .then()
                .statusCode(500);
    }

}