package com.gustavo.dummyaxon.command.rent.domain.entities.rent;

import com.gustavo.dummyaxon.command.rent.domain.events.RentedBikeEvent;
import com.gustavo.dummyaxon.command.rent.domain.events.ReturnedBikeEvent;
import com.gustavo.dummyaxon.command.user.domain.commands.RentBikeCommand;
import com.gustavo.dummyaxon.command.user.domain.commands.ReturnBikeCommand;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.lang.ref.PhantomReference;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Aggregate
public class Rent {

    @AggregateIdentifier
    private UUID id;

    private UUID userId;

    private UUID bikeId;

    private Status status;

    private Instant startedAt;

    private Instant finishedAt;

    private Instant createdAt;

    private Instant updatedAt;

    public Rent() {
    }

    @CommandHandler
    public Rent(final RentBikeCommand command) {
        Objects.requireNonNull(command.getBikeId());
        Objects.requireNonNull(command.getUserId());
        Objects.requireNonNull(command.getStartedAt());

        AggregateLifecycle.apply(
                RentedBikeEvent.create(
                        UUID.randomUUID(),
                        command.getUserId(),
                        command.getBikeId(),
                        command.getStartedAt()
                )
        );
    }

    @CommandHandler
    public void on(final ReturnBikeCommand command) {
        Objects.requireNonNull(command.getRentId());
        Objects.requireNonNull(command.getBikeId());
        Objects.requireNonNull(command.getUserId());
        Objects.requireNonNull(command.getFinishedAt());

        if(command.getFinishedAt().isBefore(Instant.now()) && command.getFinishedAt().isAfter(this.startedAt)) {
            throw new IllegalArgumentException("Finished at cannot be before started at");
        }

        AggregateLifecycle.apply(
                ReturnedBikeEvent.create(
                        command.getRentId(),
                        command.getUserId(),
                        command.getBikeId(),
                        command.getFinishedAt()
                )
        );
    }

    @EventSourcingHandler
    public void on(final RentedBikeEvent event) {
        this.id = event.getId();
        this.userId = event.getUserId();
        this.bikeId = event.getBikeId();
        this.status = Status.RUNNING;
        this.startedAt = Instant.now();
        this.createdAt = Instant.now();
    }

    @EventSourcingHandler
    public void on(final ReturnedBikeEvent event) {
        this.status = Status.FINISHED;
        this.finishedAt = event.getFinishedAt();
        this.updatedAt = Instant.now();
    }

    public enum Status {
        RUNNING,
        CANCELED,
        FINISHED
    }
}
