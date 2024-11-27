package com.gustavo.dummyaxon.command.bike.domain.commands;

import lombok.Getter;

@Getter
public class CreateBikeCommand {

    private final String name;

    private final String description;

    public CreateBikeCommand() {
        this.name = "";
        this.description = "";
    }

    private CreateBikeCommand(
            final String name,
            final String description
    ) {
        this.name = name;
        this.description = description;
    }

    public static CreateBikeCommand create(
            final String name,
            final String description
    ) {
        return new CreateBikeCommand(name, description);
    }
}
