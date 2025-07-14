package com.howaboutquestion.backend.domain.user.repository;

import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findById(Integer userId);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
