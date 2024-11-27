package com.gustavo.dummyaxon.query.user.application.controllers;

import com.gustavo.dummyaxon.query.user.infra.data.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("UserRestController.Query")
@RequestMapping(value = "/api/v1/users")
public class UserRestController {

    private final UserRepository userRepository;

    public UserRestController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/{id}/exists")
    public ResponseEntity<Void> exists(
            final @PathVariable("id") UUID id
    ) {
        final var exists = this.userRepository.existsById(id);
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
