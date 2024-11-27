package com.gustavo.dummyaxon.query.rent.application.dtos.viewmodels;

import com.gustavo.dummyaxon.query.rent.infra.data.repositories.models.RentModel;
import lombok.Getter;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Getter
public class RentViewModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;

    private final UUID userId;

    private final UUID bikeId;

    private final String status;

    private final String createdAt;

    private final String updatedAt;

    public RentViewModel() {
        this.id = null;
        this.userId = null;
        this.bikeId = null;
        this.status = null;
        this.createdAt = null;
        this.updatedAt = null;
    }

    public RentViewModel(
            final UUID id,
            final UUID userId,
            final UUID bikeId,
            final String status,
            final String createdAt,
            final String updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.bikeId = bikeId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RentViewModel create(
            final UUID id,
            final UUID userId,
            final UUID bikeId,
            final String status,
            final String createdAt,
            final String updatedAt
    ) {
        return new RentViewModel(id, userId, bikeId, status, createdAt, updatedAt);
    }

    public static RentViewModel from(final RentModel rentModel) {
        final var updatedAt = rentModel.getUpdatedAt() == null ? null : OffsetDateTime.ofInstant(rentModel.getUpdatedAt(), ZoneOffset.UTC).toString();
        return new RentViewModel(
                rentModel.getId(),
                rentModel.getUserId(),
                rentModel.getBikeId(),
                rentModel.getStatus().name(),
                OffsetDateTime.ofInstant(rentModel.getCreatedAt(), ZoneOffset.UTC).toString(),
                updatedAt
        );
    }
}
