package com.gustavo.dummyaxon.command.user.domain.commands;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class RentBikeCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID userId;

    private final UUID bikeId;

    private final Instant startedAt;

    public RentBikeCommand() {
        this.userId = null;
        this.bikeId = null;
        this.startedAt = null;
    }

    private RentBikeCommand(
            final UUID userId,
            final UUID bikeId,
            final Instant startedAt
    ) {
        this.userId = userId;
        this.bikeId = bikeId;
        this.startedAt = startedAt;
    }

    public static RentBikeCommand create(
            final UUID userId,
            final UUID bikeId
    ) {
        return new RentBikeCommand(userId, bikeId, Instant.now());
    }
}
