package org.acme.people.rest;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.people.service.GreetingService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micrometer.core.instrument.MeterRegistry;
import io.smallrye.common.annotation.NonBlocking;

@Path("/hello")
public class GreetingResource {

    public static final Logger log = LoggerFactory.getLogger(GreetingResource.class);
    private final MeterRegistry registry;

    GreetingResource(MeterRegistry registry) {
        this.registry = registry;
    }

    @Inject
    GreetingService service;

    @ConfigProperty(name = "greeting.message")
    String message;

    @ConfigProperty(name = "greeting.suffix", defaultValue = "!")
    String suffix;

    @ConfigProperty(name = "greeting.name")
    Optional<String> name;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/greeting/{name}")
    @NonBlocking
    public String greeting(@PathParam("name") String name) {

        return service.greeting(name);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @NonBlocking
    public String hello() {
        registry.counter("greeting.hello.counter").increment();
        return message + " " + name.orElse("world") + suffix;
    }

    @GET
    @Path("/lastletter/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String lastLetter(@PathParam("name") String name) {
        int len = name.length();
        String lastLetter = name.substring(len - 1);
        log.info("Got last letter: " + lastLetter);
        return lastLetter;
    }
}