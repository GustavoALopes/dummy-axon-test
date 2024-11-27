package com.gustavo.dummyaxon.query.bike.application.controllers;

import com.gustavo.dummyaxon.query.bike.infra.data.repositories.BikeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("BikeRestController.Query")
@RequestMapping(value = "/api/v1/bikes")
public class BikeRestController {

    private final BikeRepository bikeRepository;

    public BikeRestController(final BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }
    @GetMapping(value = "/{id}/exists")
    public ResponseEntity<Void> exists(
            final @PathVariable("id") UUID id
    ) {
        final var exists = this.bikeRepository.existsById(id);
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
