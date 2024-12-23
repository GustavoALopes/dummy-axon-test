package com.gustavo.dummyaxon.query.user.application.dtos.viewmodel;

import com.gustavo.dummyaxon.query.user.infra.data.model.UserModel;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
public class UserViewModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;

    private final String name;

    private final String email;

    private final String document;

    public UserViewModel() {
        this.id = null;
        this.name = "";
        this.email = "";
        this.document = "";
    }

    private UserViewModel(
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

    public static UserViewModel create(
            final UserModel model
    ) {
        return new UserViewModel(model.getId(), model.getName(), model.getEmail(), model.getDocument());
    }
}
