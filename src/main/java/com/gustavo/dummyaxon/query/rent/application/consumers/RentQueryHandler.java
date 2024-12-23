package com.gustavo.dummyaxon.query.rent.application.consumers;

import com.gustavo.dummyaxon.command.rent.domain.entities.rent.Rent;
import com.gustavo.dummyaxon.command.rent.domain.events.RentedBikeEvent;
import com.gustavo.dummyaxon.command.rent.domain.events.ReturnedBikeEvent;
import com.gustavo.dummyaxon.query.rent.application.dtos.viewmodels.RentViewModel;
import com.gustavo.dummyaxon.query.rent.infra.data.repositories.IRentRepository;
import com.gustavo.dummyaxon.query.rent.infra.data.repositories.models.RentModel;
import jakarta.transaction.Transactional;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.javatuples.Pair;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ProcessingGroup("rent-group")
public class RentQueryHandler {

    private final IRentRepository repository;

    public RentQueryHandler(final IRentRepository repository) {
        this.repository = repository;
    }

    @QueryHandler(queryName = "existsRunningRent")
    public boolean existsRent(final UUID rentId) {
        return this.repository.existsByIdAndStatus(rentId, Rent.Status.RUNNING);
    }

    @QueryHandler(queryName = "bikeIsRented")
    public boolean bikeIsRented(
            final Pair<UUID, UUID> params
    ) {
        return this.repository.existsByUserIdAndBikeIdAndStatus(params.getValue0(), params.getValue1(), Rent.Status.RUNNING);
    }

    @QueryHandler(queryName = "listAll")
    public Iterable<RentViewModel> listAll() {
        final var rents = this.repository.findAll();
        return rents.stream().map(RentViewModel::from).toList();
    }

    @Transactional
    @EventHandler
    public void on(final RentedBikeEvent event) {
        this.repository.save(RentModel.from(event));
    }

    @EventHandler
    public void on(final ReturnedBikeEvent event) {
        this.repository.findById(event.getRentId())
            .ifPresent(value -> {
                value.on(event);
                this.repository.save(value);
            });
    }
}
