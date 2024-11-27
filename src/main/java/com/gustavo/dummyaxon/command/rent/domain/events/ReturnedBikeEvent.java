package com.gustavo.dummyaxon.command.rent.domain.events;

import com.gustavo.dummyaxon.command.rent.domain.entities.rent.Rent;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReturnedBikeEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID rentId;

    private final UUID userId;

    private final UUID bikeId;

    private final Rent.Status status;

    private final Instant finishedAt;

    public ReturnedBikeEvent() {
        this.rentId = null;
        this.userId = null;
        this.bikeId = null;
        this.status = null;
        this.finishedAt = null;
    }

    public ReturnedBikeEvent(
            final UUID rentId,
            final UUID userId,
            final UUID bikeId,
            final Rent.Status status,
            final Instant finishedAt
    ) {
        this.rentId = rentId;
        this.userId = userId;
        this.bikeId = bikeId;
        this.status = status;
        this.finishedAt = finishedAt;
    }

    public static ReturnedBikeEvent create(
            final UUID rentId,
            final UUID userId,
            final UUID bikeId,
            final Instant finishedAt
    ) {
        return new ReturnedBikeEvent(rentId, userId, bikeId, Rent.Status.FINISHED, finishedAt);
    }
}
