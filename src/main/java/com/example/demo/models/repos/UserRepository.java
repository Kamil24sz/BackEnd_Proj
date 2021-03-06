package com.example.demo.models.repos;

import com.example.demo.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findById(long id);
    List<UserEntity> findByEmail(String email);
    UserEntity findFirstByEmail(String email);

}