package com.reloadly.paypro.combined.persistence.repository;

import com.reloadly.paypro.combined.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByAccountNumber(String accountNumber);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByAccountNumber(String accountNumber);
}
