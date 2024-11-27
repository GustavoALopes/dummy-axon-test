package com.gustavo.dummyaxon.query.user.infra.data.repository;

import com.gustavo.dummyaxon.query.user.infra.data.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID>{
}
