package com.gustavo.dummyaxon.command.bike.application.dtos.inputmodel;

import lombok.Getter;
import org.axonframework.commandhandling.RoutingKey;

import java.io.Serializable;

@Getter
public class CreateBikeInputModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;

    private final String description;

    public CreateBikeInputModel() {
        this.name = "";
        this.description = "";
    }

    private CreateBikeInputModel(
            final String name,
            final String description
    ) {
        this.name = name;
        this.description = description;
    }

    public static CreateBikeInputModel create(
            final String name,
            final String description
    ) {
        return new CreateBikeInputModel(name, description);
    }
}
