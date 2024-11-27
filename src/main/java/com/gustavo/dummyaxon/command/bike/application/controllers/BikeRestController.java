package com.gustavo.dummyaxon.command.bike.application.controllers;

import com.gustavo.dummyaxon.command.bike.application.dtos.inputmodel.CreateBikeInputModel;
import com.gustavo.dummyaxon.command.bike.domain.commands.CreateBikeCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("BikeRestController.Command")
@RequestMapping(value = "/api/v1/bikes")
public class BikeRestController {

    private final CommandGateway commandGateway;

    public BikeRestController(
            final CommandGateway commandGateway
    ) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public ResponseEntity<Void> create(
            final @RequestBody CreateBikeInputModel input
    ) {
        this.commandGateway.send(CreateBikeCommand.create(
                input.getName(),
                input.getDescription()
        ));

        return ResponseEntity.accepted().build();
    }
}
