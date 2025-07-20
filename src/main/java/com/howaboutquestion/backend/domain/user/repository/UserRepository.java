package com.howaboutquestion.backend.domain.user.repository;

import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

/**
 * packageName    : com.howaboutquestion.backend.domain.user.repository<br>
 * fileName       : UserRepository.java<br>
 * author         : cod0216 <br>
 * date           : 2025-07-13<br>
 * description    : User entity 의 repository 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          cod0216           최초생성<br>
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findById(Integer userId);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
