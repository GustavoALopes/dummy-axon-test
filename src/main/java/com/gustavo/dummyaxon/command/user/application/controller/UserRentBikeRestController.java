package com.gustavo.dummyaxon.command.user.application.controller;

import com.gustavo.dummyaxon.command.user.application.usecases.RentBikeUseCase;
import com.gustavo.dummyaxon.command.user.application.usecases.ReturnBikeUseCase;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/api/v1/users/{userId}/bikes/{bikeId}")
public class UserRentBikeRestController {

    private final RentBikeUseCase rentBikeUseCase;

    private final ReturnBikeUseCase returnBikeUseCase;

    public UserRentBikeRestController(
            final RentBikeUseCase rentBikeUseCase,
            final ReturnBikeUseCase returnBikeUseCase
    ) {
        this.rentBikeUseCase = rentBikeUseCase;
        this.returnBikeUseCase = returnBikeUseCase;
    }

    @PostMapping(value = "/rent")
    public ResponseEntity<Void> rent(
            final @PathVariable("userId") UUID userId,
            final @PathVariable("bikeId") UUID bikeId
    ) throws ExecutionException, InterruptedException {
        this.rentBikeUseCase.execute(Pair.with(userId, bikeId));
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/rent/{rentId}/return")
    public ResponseEntity<Void> rent(
            final @PathVariable("userId") UUID userId,
            final @PathVariable("bikeId") UUID bikeId,
            final @PathVariable("rentId") UUID rentId
    ) throws ExecutionException, InterruptedException {
        this.returnBikeUseCase.execute(Triplet.with(userId, bikeId, rentId));
        return ResponseEntity.ok().build();
    }
}
