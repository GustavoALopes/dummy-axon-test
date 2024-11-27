package com.gustavo.dummyaxon.command.bike.domain.events;

import com.gustavo.dummyaxon.command.bike.domain.entities.bike.Bike;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BikeCreatedEvent {

    private UUID id;

    private String name;

    private Bike.Status status;

    private String description;

    private Instant createdAt;

    public BikeCreatedEvent() {
    }

    private BikeCreatedEvent(
            final UUID id,
            final String name,
            final String description,
            final Bike.Status status,
            final Instant createdAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static BikeCreatedEvent create(
            final UUID id,
            final String name,
            final String description
    ) {
        return new BikeCreatedEvent(
                id,
                name,
                description,
                Bike.Status.AVAIABLE,
                Instant.now()
        );
    }
}
