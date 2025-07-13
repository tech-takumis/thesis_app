package com.hashjosh.agripro.user.repository;

import com.hashjosh.agripro.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String s);

    boolean existsByEmail( String email);
}


