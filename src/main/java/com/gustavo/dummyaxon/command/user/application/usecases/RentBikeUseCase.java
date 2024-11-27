package com.gustavo.dummyaxon.command.user.application.usecases;

import com.gustavo.dummyaxon.command.user.application.usecases.interfaces.IUseCase;
import com.gustavo.dummyaxon.command.user.domain.commands.RentBikeCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class RentBikeUseCase implements IUseCase<Pair<UUID, UUID>, Void> {

    private final CommandGateway commandGateway;

    private final QueryGateway queryGateway;

    public RentBikeUseCase(
            final CommandGateway commandGateway,
            final QueryGateway queryGateway
    ) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @Override
    public Void execute(final Pair<UUID, UUID> params) throws ExecutionException, InterruptedException {
        final var userId = params.getValue0();
        final var bikeId = params.getValue1();

        final var existsUser = this.queryGateway.query("existsUser", userId, ResponseTypes.instanceOf(Boolean.class));
        final var existsBike = this.queryGateway.query("existsBike", bikeId, ResponseTypes.instanceOf(Boolean.class));

        CompletableFuture.allOf(existsUser, existsBike).join();

        if(!existsUser.get() || !existsBike.get()) {
            throw new RuntimeException("User or Bike not found");
        }

        this.commandGateway.send(RentBikeCommand.create(userId, bikeId));

        return null;
    }
}
