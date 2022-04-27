package org.acme.people.stream;

import javax.enterprise.context.ApplicationScoped;
import org.acme.people.utils.CuteNameGenerator;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.mutiny.Multi;

import java.time.Duration;

@ApplicationScoped
public class NameGenerator {

    @Outgoing("generated-name")
    public Multi<String> generate() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(5))
                .onOverflow().drop()
                .map(tick -> CuteNameGenerator.generate());
    }

}