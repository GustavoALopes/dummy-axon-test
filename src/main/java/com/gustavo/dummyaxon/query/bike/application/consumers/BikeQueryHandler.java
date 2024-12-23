package com.gustavo.dummyaxon.query.bike.application.consumers;

import com.gustavo.dummyaxon.command.bike.domain.events.BikeCreatedEvent;
import com.gustavo.dummyaxon.query.bike.application.dtos.viewmodel.BikeViewModel;
import com.gustavo.dummyaxon.query.bike.infra.data.models.BikeModel;
import com.gustavo.dummyaxon.query.bike.infra.data.repositories.BikeRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@ProcessingGroup("bike-group")
public class BikeQueryHandler {

    private final BikeRepository bikeRepository;

    public BikeQueryHandler(final BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    @EventHandler
    public void on(final BikeCreatedEvent event) {
        bikeRepository.save(BikeModel.from(event));
    }

    @QueryHandler(queryName = "existsBike")
    public boolean existsBike(final UUID bikeId) {
        return bikeRepository.existsById(bikeId);
    }

    @QueryHandler(queryName = "listBikes")
    public List<BikeViewModel> listBikes() {
        return bikeRepository.findAll().stream().map(BikeViewModel::create).collect(Collectors.toList());
    }
}
