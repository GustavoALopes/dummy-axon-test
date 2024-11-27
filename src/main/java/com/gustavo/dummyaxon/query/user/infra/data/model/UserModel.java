package com.gustavo.dummyaxon.query.user.infra.data.model;

import com.gustavo.dummyaxon.command.user.domain.events.UserCreatedEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "users")
public class UserModel {

    @Id
    private UUID id;

    private String name;

    private String email;

    private String document;

    public UserModel() {
    }

    private UserModel(
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

    public static UserModel from(
        final UserCreatedEvent event
    ) {
        return new UserModel(event.getId(), event.getName(), event.getEmail(), event.getDocument());
    }
}
