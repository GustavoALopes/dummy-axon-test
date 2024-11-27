package com.gustavo.dummyaxon.command.user.domain.entities.user;

import com.gustavo.dummyaxon.command.user.domain.events.UserCreatedEvent;
import com.gustavo.dummyaxon.command.user.domain.commands.CreateUserCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Objects;
import java.util.UUID;

@Aggregate
public class User {

    @AggregateIdentifier
    private UUID id;

    private String name;

    private String email;

    private String document;

    public User() {
    }

    @CommandHandler
    public User(final CreateUserCommand command) {
        if(Objects.isNull(command.getDocument())) {
            throw new IllegalArgumentException("Document cannot be null");
        } else if(Objects.isNull(command.getEmail())) {
            throw new IllegalArgumentException("Email cannot be null");
        } else if(Objects.isNull(command.getName())) {
            throw new IllegalArgumentException("Name cannot be null");
        }

        this.id = UUID.randomUUID();

        AggregateLifecycle.apply(UserCreatedEvent.create(
                this.id,
                command.getName(),
                command.getEmail(),
                command.getDocument()
        ));
    }

    @EventSourcingHandler
    public void on(final UserCreatedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.email = event.getEmail();
        this.document = event.getDocument();
    }
}
