package com.howaboutquestion.backend.domain.user.dto;

import com.howaboutquestion.backend.domain.auth.dto.request.TokenUserInfo;
import com.howaboutquestion.backend.domain.auth.dto.response.UserLoginResponse;
import com.howaboutquestion.backend.domain.user.dto.resopnse.UserInfoResponse;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import com.howaboutquestion.backend.domain.usermeta.entity.UserType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * packageName    : com.howaboutquestion.backend.domain.user.dto<br>
 * fileName       : UserDetail.java<br>
 * author         : eunchang <br>
 * date           : 2025.07.13<br>
 * description    : Spring Security에서 사용자 정보를 나타내는 클래스입니다<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          eunchang           최초 생성 <br>
 * 25.07.16          eunchang           TokenUserInfo 클래스 추가 <br>
 */
@Getter
@Builder
public class UserDetail implements UserDetails {

    private final TokenUserInfo user;

    public UserDetail(TokenUserInfo user) {
        this.user = user;
    }

    /**
     * 계정 만료 여부
     * true : 만료 안됨
     * false : 만료
     * @return true(만료 안됨)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠김 여부
     * true : 잠기지 않음
     * false: 잠김
     * @return ture(잠기지 않음)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 비밀번호 만료 여부
     * true : 만료 안됨
     * false : 만료
     * @return true(만료 안됨)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 사용자 활성화 여부
     * true : 활성화
     * false : 비활성화
     * @return ture(활성화)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 해당 유저의 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getUserType().name()));
    }

    /**
     * 회원 비밀번호를 반환합니다.
     * @return 회원 비밀번호
     */
    @Override
    public String getPassword() {
        return "";
    }

    /**
     * 회원 이름을 반환합니다.
     * @return 회원 이름
     */
    @Override
    public String getUsername() {
        return user.getName();
    }

    /**
     * 토큰에서 추출한 회원의 Email을 반환합니다.
     * @return 회원 Email
     */
    public String getUserEmail() {
        return user.getEmail();
    }

    /**
     * 토큰에서 추출한 회원의 프로필을 반환합니다.
     * @return 회원 프로필
     */
    public String getUserProfile() {
        return user.getProfile();
    }

    public String getUserId(){
        return user.getId().toString();
    }

}
