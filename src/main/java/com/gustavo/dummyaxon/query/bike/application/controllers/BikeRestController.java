package com.gustavo.dummyaxon.query.bike.application.controllers;

import com.gustavo.dummyaxon.query.bike.application.dtos.viewmodel.BikeViewModel;
import com.gustavo.dummyaxon.query.bike.infra.data.repositories.BikeRepository;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController("BikeRestController.Query")
@RequestMapping(value = "/api/v1/bikes")
public class BikeRestController {

    private final BikeRepository bikeRepository;

    private final QueryGateway queryGateway;

    public BikeRestController(
            final BikeRepository bikeRepository,
            final QueryGateway queryGateway
    ) {
        this.bikeRepository = bikeRepository;
        this.queryGateway = queryGateway;
    }

    @GetMapping(value = "/{id}/exists")
    public ResponseEntity<Void> exists(
            final @PathVariable("id") UUID id
    ) {
        final var exists = this.bikeRepository.existsById(id);
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<BikeViewModel>> listAll() throws ExecutionException, InterruptedException {
        final var viewModels = this.queryGateway.query("listBikes", null, ResponseTypes.multipleInstancesOf(BikeViewModel.class)).get();
        return ResponseEntity.ok(viewModels);
    }
}
