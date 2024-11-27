package com.gustavo.dummyaxon.query.rent.infra.data.repositories.models;

import com.gustavo.dummyaxon.command.rent.domain.entities.rent.Rent;
import com.gustavo.dummyaxon.command.rent.domain.events.RentedBikeEvent;
import com.gustavo.dummyaxon.command.rent.domain.events.ReturnedBikeEvent;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Getter
@Document("Rent")
public class RentModel {

    @Id
    private UUID id;

    private UUID userId;

    private UUID bikeId;

    private Rent.Status status;

    private Instant startedAt;

    private Instant finishedAt;

    private Instant createdAt;

    private Instant updatedAt;

    public RentModel() {
        this.id = null;
        this.userId = null;
        this.bikeId = null;
        this.status = null;
        this.startedAt = null;
        this.finishedAt = null;
        this.createdAt = null;
        this.updatedAt = null;
    }

    public RentModel(
            final UUID id,
            final UUID userId,
            final UUID bikeId,
            final Rent.Status status,
            final Instant startedAt,
            final Instant finishedAt,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.bikeId = bikeId;
        this.status = status;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RentModel from(final RentedBikeEvent event) {
        return new RentModel(
                event.getId(),
                event.getUserId(),
                event.getBikeId(),
                event.getStatus(),
                event.getStartedAt(),
                null,
                event.getCreatedAt(),
                null
        );
    }

    public void on(final ReturnedBikeEvent event) {
        this.status = event.getStatus();
        this.finishedAt = event.getFinishedAt();
        this.updatedAt = Instant.now();
    }
}
