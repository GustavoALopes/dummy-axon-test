package com.gustavo.dummyaxon.command.user.domain.commands;

import lombok.Getter;

@Getter
public class CreateUserCommand {

    private final String name;

    private final String email;

    private final String document;

    private CreateUserCommand(
            final String name,
            final String email,
            final String document
    ) {
        this.name = name;
        this.email = email;
        this.document = document;
    }

    public static CreateUserCommand create(
            final String name,
            final String email,
            final String document
    ) {
        return new CreateUserCommand(name, email, document);
    }
}
