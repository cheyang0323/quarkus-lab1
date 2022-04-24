package org.acme.people;

import io.quarkus.test.junit.QuarkusTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.acme.people.service.GreetingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

@QuarkusTest
public class GreetingResourceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger("GreetingResourceTest");

    @Inject
    GreetingService service;

    @Test
    public void testGreetingService() {
        LOGGER.info("greeting: " + service.greeting("Quarkus"));
        Assertions.assertTrue(service.greeting("Quarkus").startsWith("hello Quarkus"));
    }

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(startsWith("hello"));
    }

    // add more tests
    @Test
    public void testGreetingEndpoint() {
        String uuid = UUID.randomUUID().toString();
        given()
                .pathParam("name", uuid)
                .when().get("/hello/greeting/{name}")
                .then()
                .statusCode(200)
                .body(startsWith("hello " + uuid));
    }
}