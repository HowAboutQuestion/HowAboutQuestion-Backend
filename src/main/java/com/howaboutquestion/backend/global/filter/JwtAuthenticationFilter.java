package com.howaboutquestion.backend.global.filter;

import com.howaboutquestion.backend.domain.auth.dto.request.TokenUserInfo;
import com.howaboutquestion.backend.domain.user.dto.UserDetail;
import com.howaboutquestion.backend.domain.usermeta.entity.UserType;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import com.howaboutquestion.backend.global.util.JwtUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * packageName    : com.howaboutquestion.backend.global.util<br>
 * fileName       : GlobalExceptionHandler.java<br>
 * author         : eunchang<br>
 * date           : 2025-06-13<br>
 * description    : Jwt토큰으로 인증하고 SecurityContextHolder에 추가하는 필터를 설정하는 클래스 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          eunchang           최초생성<br>
 * 25.07.16          eunchang           DB 조회 로직 개선<br>
 * 25.08.01          eunchang           Url get 메서드 추가<br>
 * 26.05.06          eunchang            토큰 재발급 공개 경로 추가<br>
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final AntPathMatcher antPathMather = new AntPathMatcher();
    private final JwtUtility jwtUtility;
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final  String BEARER  = "Bearer ";

    private static final String[] ALLOW_URLS = new String[] {
            "/", "/api/auths/register", "/api/auths/login", "/api/auths/tokens/refresh",
    };

    private static final String[] NOT_ALLOW_URLS = new String[] {
            "/", "/api/auths/logout",
    };


    /**
     * 필터가 내부적으로 수행할 로직입니다.
     * @param request 사용자 요청
     * @param response 요청에 대한 응답
     * @throws CustomException 토큰 관련 에러를 반환합니다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();


        if(checkAllowedUrl(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HEADER_AUTHORIZATION);
        if(Objects.isNull(authHeader) || !authHeader.startsWith(BEARER)) {
            throw new CustomException(StatusCode.NO_ACCESS_TOKEN);
        }

        String accessToken = authHeader.substring(BEARER.length());
        if(jwtUtility.validateToken(accessToken)) {
            processValidAccessToken(accessToken);
            filterChain.doFilter(request, response);

        } else {
            SecurityContextHolder.clearContext();
            throw new CustomException(StatusCode.INVALID_TOKEN);
        }

    }

    /**
     * AccessToken에 담긴 인증 객체를 생성합니다.
     * @param accessToken 사용자의 토큰
     */
    protected void processValidAccessToken(String accessToken){
        UserType type = jwtUtility.getUserType(accessToken);
        Long userId = jwtUtility.getUserId(accessToken);
        String userName = jwtUtility.getUserName(accessToken);
        String userEmail = jwtUtility.getUserEmail(accessToken);
        String userProfile = jwtUtility.getProfile(accessToken);
        TokenUserInfo tokenUserInfo = TokenUserInfo.builder()
                .userType(type)
                .id(userId)
                .email(userEmail)
                .name(userName)
                .profile(userProfile)
                .build();

        UserDetails userDetails = new UserDetail(tokenUserInfo);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 요청에 담긴 URL이 예외적인 인증이 필요한지 확인합니다.
     * @param url 요청에 담긴 URL
     * @return 요청 수행 가능 여부 반환
     */
    private boolean checkAllowedUrl(String url) {
        boolean isAllowUrl = Arrays.stream(ALLOW_URLS).anyMatch(pattern -> antPathMather.match(pattern,url));
        boolean isExceptionUrl = Arrays.stream(NOT_ALLOW_URLS).anyMatch(pattern -> antPathMather.match(pattern,url));

        return isAllowUrl && !isExceptionUrl;
    }

    public String[] getAllowUrls() {
        return ALLOW_URLS;
    }

    public String[] getNotAllowUrls() {
        return NOT_ALLOW_URLS;
    }
}
