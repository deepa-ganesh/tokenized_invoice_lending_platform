package io.github.deepaganesh.kyc.repository;

import io.github.deepaganesh.kyc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> { }
