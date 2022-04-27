package org.acme.people.stream;

import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.micrometer.core.instrument.MeterRegistry;
import io.smallrye.reactive.messaging.annotations.Broadcast;

@ApplicationScoped
public class NameConverter {

    private static final String[] honorifics = { "Mr.", "Mrs.", "Sir", "Madam", "Lord", "Lady", "Dr.", "Professor",
            "Vice-Chancellor", "Regent", "Provost", "Prefect" };
    private final MeterRegistry registry;

    NameConverter(MeterRegistry registry) {
        this.registry = registry;
    }

    @Incoming("names")
    @Outgoing("my-data-stream")
    @Broadcast
    public String process(String name) {
        String honorific = honorifics[(int) Math.floor(Math.random() * honorifics.length)];
        registry.counter("nameconvert.process.counter").increment();
        registry.timer("nameconvert.process.timer").record(3000, TimeUnit.MILLISECONDS);
        return honorific + " " + name;
    }
}