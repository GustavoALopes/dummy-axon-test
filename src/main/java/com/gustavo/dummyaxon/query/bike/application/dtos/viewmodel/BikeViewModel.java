package com.gustavo.dummyaxon.query.bike.application.dtos.viewmodel;

import com.gustavo.dummyaxon.query.bike.infra.data.models.BikeModel;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BikeViewModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;

    private final String name;

    private final String description;

    private final int status;

    private final Instant createdAt;

    private final Instant updatedAt;

    public BikeViewModel() {
        this.id = null;
        this.name = "";
        this.description = "";
        this.status = 0;
        this.createdAt = null;
        this.updatedAt = null;
    }

    public BikeViewModel(
            final UUID id,
            final String name,
            final String description,
            final int status,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BikeViewModel create(
            final BikeModel model
    ) {
        return new BikeViewModel(
                model.getId(),
                model.getName(),
                model.getDescription(),
                model.getStatus(),
                model.getCreatedAt(),
                model.getUpdatedAt()
        );
    }
}
