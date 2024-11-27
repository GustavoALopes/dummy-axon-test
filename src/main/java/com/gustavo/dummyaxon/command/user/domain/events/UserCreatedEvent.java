package com.gustavo.dummyaxon.command.user.domain.events;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class UserCreatedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;

    private final String name;

    private final String email;

    private final String document;

    public UserCreatedEvent() {
        this.id = null;
        this.name = "";
        this.email = "";
        this.document = "";
    }

    private UserCreatedEvent(
            final UUID id,
            final String name,
            final String email,
            final String document
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.document = document;
    }

    public static UserCreatedEvent create(
            final UUID id,
            final String name,
            final String email,
            final String document
    ) {
        return new UserCreatedEvent(
                id,
                name,
                email,
                document
        );
    }
}
