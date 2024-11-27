package com.gustavo.dummyaxon.command.user.application.usecases;

import com.gustavo.dummyaxon.command.user.application.usecases.interfaces.IUseCase;
import com.gustavo.dummyaxon.command.user.domain.commands.ReturnBikeCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.javatuples.Triplet;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ReturnBikeUseCase implements IUseCase<Triplet<UUID, UUID, UUID>, Void> {

    private final CommandGateway commandGateway;

    private final QueryGateway queryGateway;

    public ReturnBikeUseCase(
            final CommandGateway commandGateway,
            final QueryGateway queryGateway
    ) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @Override
    public Void execute(final Triplet<UUID, UUID, UUID> input) throws ExecutionException, InterruptedException {
        final var userId = input.getValue0();
        final var bikeId = input.getValue1();
        final var rentId = input.getValue2();

        final var existsUser = this.queryGateway.query("existsUser", userId, ResponseTypes.instanceOf(Boolean.class));
        final var existsBike = this.queryGateway.query("existsBike", bikeId, ResponseTypes.instanceOf(Boolean.class));
        final var existsRent = this.queryGateway.query("existsRunningRent", rentId, ResponseTypes.instanceOf(Boolean.class));

        CompletableFuture.allOf(existsUser, existsBike, existsRent).join();

        if (!existsUser.get() || !existsBike.get() || !existsRent.get()) {
            throw new RuntimeException("User, bike or rent not found");
        }

        this.commandGateway.send(ReturnBikeCommand.create(rentId, userId, bikeId));

        return null;
    }
}
