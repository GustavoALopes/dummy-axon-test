package com.gustavo.dummyaxon.query.bike.infra.data.models;

import com.gustavo.dummyaxon.command.bike.domain.entities.bike.Bike;
import com.gustavo.dummyaxon.command.bike.domain.events.BikeCreatedEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "bikes")
public class BikeModel {

    @Id
    private UUID id;

    private String name;

    private String description;

    private int status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public BikeModel() {
    }

    private BikeModel(
            final UUID uuid,
            final String name,
            final String description,
            final Bike.Status status,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.id = uuid;
        this.name = name;
        this.description = description;
        this.status = status.getCode();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BikeModel from(
            final BikeCreatedEvent event
    ) {
        return new BikeModel(event.getId(), event.getName(), event.getDescription(), event.getStatus(), event.getCreatedAt(), null);
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
