package com.gustavo.dummyaxon.command.bike.domain.entities.bike;

import com.gustavo.dummyaxon.command.bike.domain.commands.CreateBikeCommand;
import com.gustavo.dummyaxon.command.bike.domain.events.BikeCreatedEvent;
import lombok.Getter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Aggregate
public class Bike {

    @AggregateIdentifier
    private UUID id;

    private String name;

    private String description;

    private Status status;

    private Instant createdAt;

    private Instant updatedAt;

    public Bike() {
    }

    @CommandHandler
    public Bike(final CreateBikeCommand command) {
        if(Objects.isNull(command.getName())) {
            throw new IllegalArgumentException("Name cannot be null");
        }

        final var guid = UUID.randomUUID();

        AggregateLifecycle.apply(BikeCreatedEvent.create(
                guid,
                command.getName(),
                command.getDescription()
        ));
    }

    @EventSourcingHandler
    public void on(final BikeCreatedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.status = event.getStatus();
        this.description = event.getDescription();
        this.createdAt = event.getCreatedAt();
    }

    @Getter
    public enum Status {
        AVAIABLE(100),
        RENTED(200),
        MAINTENANCE(300);

        private final int code;

        Status(final int code) {
            this.code = code;
        }
    }
}
