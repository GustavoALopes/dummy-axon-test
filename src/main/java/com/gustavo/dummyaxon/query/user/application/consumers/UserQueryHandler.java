package com.gustavo.dummyaxon.query.user.application.consumers;

import com.gustavo.dummyaxon.command.user.domain.events.UserCreatedEvent;
import com.gustavo.dummyaxon.query.user.application.dtos.viewmodel.UserViewModel;
import com.gustavo.dummyaxon.query.user.infra.data.model.UserModel;
import com.gustavo.dummyaxon.query.user.infra.data.repository.UserRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@ProcessingGroup("user-group")
public class UserQueryHandler {

    private final UserRepository userRepository;

    public UserQueryHandler(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @QueryHandler(queryName = "existsUser")
    public boolean existsUser(final UUID id) {
        return userRepository.existsById(id);
    }

    @QueryHandler(queryName = "listUsers")
    public Set<UserViewModel> listUsers() {
        return userRepository.findAll().stream().map(UserViewModel::create).collect(Collectors.toSet());
    }

    @EventHandler
    public void on(final UserCreatedEvent event) {
        userRepository.save(UserModel.from(event));
    }
}
