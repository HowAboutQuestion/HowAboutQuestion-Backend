package com.howaboutquestion.backend.domain.user.dto.mapper;

import com.howaboutquestion.backend.domain.auth.dto.response.UserLoginResponse;
import com.howaboutquestion.backend.domain.auth.dto.response.UserRegisterResponse;
import com.howaboutquestion.backend.domain.user.dto.UserDetail;
import com.howaboutquestion.backend.domain.user.dto.resopnse.UserInfoResponse;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserRegisterResponse mapToUserRegisterResponse(UserEntity entity);
    UserDetail mapToUserDetail(UserEntity entity);
    UserInfoResponse mapToUserInfoResponse(UserEntity entity);

}
