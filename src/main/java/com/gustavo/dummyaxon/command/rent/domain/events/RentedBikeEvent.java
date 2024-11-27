package com.gustavo.dummyaxon.command.rent.domain.events;

import com.gustavo.dummyaxon.command.rent.domain.entities.rent.Rent;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class RentedBikeEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;

    private final UUID userId;

    private final UUID bikeId;

    private final Rent.Status status;

    private final Instant startedAt;

    private final Instant createdAt;


    public RentedBikeEvent() {
        this.id = null;
        this.userId = null;
        this.bikeId = null;
        this.status = null;
        this.startedAt = null;
        this.createdAt = null;
    }

    public RentedBikeEvent(
            final UUID id,
            final UUID userId,
            final UUID bikeId,
            final Rent.Status status,
            final Instant startedAt,
            final Instant createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.bikeId = bikeId;
        this.status = status;
        this.startedAt = startedAt;
        this.createdAt = createdAt;
    }

    public static RentedBikeEvent create(
            final UUID id,
            final UUID userId,
            final UUID bikeId,
            final Instant startedAt
    ) {
        return new RentedBikeEvent(id, userId, bikeId, Rent.Status.RUNNING, startedAt, Instant.now());
    }
}
