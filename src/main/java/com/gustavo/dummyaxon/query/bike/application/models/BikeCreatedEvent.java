package com.gustavo.dummyaxon.query.bike.application.models;


import com.gustavo.dummyaxon.query.bike.infra.data.models.BikeModel;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BikeCreatedEvent {

    private UUID id;

    private String name;

    private BikeModel.Status status;

    private String description;

    private Instant createdAt;

    public BikeCreatedEvent() {
    }

    private BikeCreatedEvent(
            final UUID id,
            final String name,
            final String description,
            final BikeModel.Status status,
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
                BikeModel.Status.AVAIABLE,
                Instant.now()
        );
    }
}
