package com.gustavo.dummyaxon.command.user.application.dtos.inputmodel;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class CreateUserInputModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;

    private final String email;

    private final String document;

    public CreateUserInputModel() {
        this.name = "";
        this.email = "";
        this.document = "";
    }

    private CreateUserInputModel(
            final String name,
            final String email,
            final String document
    ) {
        this.name = name;
        this.email = email;
        this.document = document;
    }

    public static CreateUserInputModel create(
            final String name,
            final String email,
            final String document
    ) {
        return new CreateUserInputModel(
                name,
                email,
                document
        );
    }
}
