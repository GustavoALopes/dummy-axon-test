package com.gustavo.dummyaxon.command.user.domain.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReturnBikeCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    @TargetAggregateIdentifier
    private final UUID rentId;

    private final UUID userId;

    private final UUID bikeId;

    private final Instant finishedAt;

    public ReturnBikeCommand() {
        this.rentId = null;
        this.userId = null;
        this.bikeId = null;
        this.finishedAt = null;
    }

    public ReturnBikeCommand(
            final UUID rentId,
            final UUID userId,
            final UUID bikeId,
            final Instant finishedAt
    ) {
        this.rentId = rentId;
        this.userId = userId;
        this.bikeId = bikeId;
        this.finishedAt = finishedAt;
    }

    public static ReturnBikeCommand create(
            final UUID rentId,
            final UUID userId,
            final UUID bikeId
    ) {
        return new ReturnBikeCommand(rentId, userId, bikeId, Instant.now());
    }
}
