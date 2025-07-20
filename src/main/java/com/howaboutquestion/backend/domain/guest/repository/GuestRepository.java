package com.howaboutquestion.backend.domain.guest.repository;

import com.howaboutquestion.backend.domain.guest.entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.howaboutquestion.backend.domain.guest.repository<br>
 * fileName       : GuestRepository.java<br>
 * author         : cod0216 <br>
 * date           : 2025-07-13<br>
 * description    : Guest entity 의 repository 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          cod0216           최초생성<br>
 */
public interface GuestRepository extends JpaRepository<GuestEntity, Integer> {

}
