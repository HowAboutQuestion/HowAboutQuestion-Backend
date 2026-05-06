package com.howaboutquestion.backend.domain.user.dto.mapper;

import com.howaboutquestion.backend.domain.auth.dto.response.UserLoginResponse;
import com.howaboutquestion.backend.domain.auth.dto.response.UserRegisterResponse;
import com.howaboutquestion.backend.domain.user.dto.UserDetail;
import com.howaboutquestion.backend.domain.user.dto.resopnse.UserInfoResponse;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * packageName    : com.howaboutquestion.backend.domain.user.dto.mapper<br>
 * fileName       : UserMapper.java<br>
 * author         : eunchang<br>
 * date           : 2025-07-13<br>
 * description    : User entity 의 Map struct 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.14          eunchang           최초생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserRegisterResponse mapToUserRegisterResponse(UserEntity entity);
    UserDetail mapToUserDetail(UserEntity entity);
    UserInfoResponse mapToUserInfoResponse(UserEntity entity);

}
