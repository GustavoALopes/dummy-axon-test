package com.gustavo.dummyaxon.command.user.application.controller;

import com.gustavo.dummyaxon.command.user.application.dtos.inputmodel.CreateUserInputModel;
import com.gustavo.dummyaxon.command.user.domain.commands.CreateUserCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryBus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserRestController {

    private final CommandGateway commandGateway;

    private final QueryBus queryBus;

    public UserRestController(
            final CommandGateway commandGateway,
            final QueryBus queryBus
    ) {
        this.commandGateway = commandGateway;
        this.queryBus = queryBus;
    }

    @PostMapping
    public ResponseEntity<Void> create(
            final @RequestBody CreateUserInputModel inputModel
    ) {
        this.commandGateway.send(CreateUserCommand.create(
                inputModel.getName(),
                inputModel.getEmail(),
                inputModel.getDocument()
        ));
        return ResponseEntity.accepted().build();
    }
}
