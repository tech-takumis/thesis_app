package com.hashjosh.agripro.user.repository;

import com.hashjosh.agripro.user.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findByUsername(String username);

    // Find all non-deleted User
    @Query("SELECT u FROM  User u where u.deleted = false ")
    List<User> findAllActive();

    // Find all deleted users
    @Query("SELECT u from User u where u.deleted = true")
    List<User> findAllDeleted();

    // Soft delete by id
    @Modifying
    @Query("UPDATE User u SET u.deleted = true, u.deletedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    void softDelete(@Param("id") int id);

    // Restore user
    @Modifying
    @Query("UPDATE User u SET u.deleted = false, u.deletedAt = null WHERE u.id = :id")
    void restore(@Param("id")Long id);

    Optional<User> findByEmail(String email);
}


