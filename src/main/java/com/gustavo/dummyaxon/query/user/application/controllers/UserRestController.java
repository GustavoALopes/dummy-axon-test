package com.gustavo.dummyaxon.query.user.application.controllers;

import com.gustavo.dummyaxon.query.user.application.dtos.viewmodel.UserViewModel;
import com.gustavo.dummyaxon.query.user.infra.data.repository.UserRepository;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController("UserRestController.Query")
@RequestMapping(value = "/api/v1/users")
public class UserRestController {

    private final UserRepository userRepository;

    private final QueryGateway queryGateway;

    public UserRestController(
            final UserRepository userRepository,
            final QueryGateway queryGateway
    ) {
        this.userRepository = userRepository;
        this.queryGateway = queryGateway;
    }

    @GetMapping(value = "/{id}/exists")
    public ResponseEntity<Void> exists(
            final @PathVariable("id") UUID id
    ) {
        final var exists = this.userRepository.existsById(id);
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<UserViewModel>> listUsers() throws ExecutionException, InterruptedException {
        final var viewModels = this.queryGateway.query("listUsers", null, ResponseTypes.multipleInstancesOf(UserViewModel.class)).get();
        return ResponseEntity.ok(viewModels);
    }
}
