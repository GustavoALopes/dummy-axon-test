package com.gustavo.dummyaxon.query.bike.infra.data.repositories;

import com.gustavo.dummyaxon.query.bike.infra.data.models.BikeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BikeRepository extends JpaRepository<BikeModel, UUID>{
}
