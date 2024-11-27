package com.gustavo.dummyaxon.query.rent.infra.data.repositories;

import com.gustavo.dummyaxon.command.rent.domain.entities.rent.Rent;
import com.gustavo.dummyaxon.query.rent.infra.data.repositories.models.RentModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface IRentRepository extends MongoRepository<RentModel, UUID> {

    boolean existsByIdAndStatus(
            final UUID rentId,
            final Rent.Status status
    );
}
