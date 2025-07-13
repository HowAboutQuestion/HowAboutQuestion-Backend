package com.howaboutquestion.backend.global.filter;

import com.howaboutquestion.backend.domain.usermeta.entity.UserMetaEntity;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * packageName    : com.howaboutquestion.backend.global.util<br>
 * fileName       : GlobalExceptionHandler.java<br>
 * author         : eunchang<br>
 * date           : 2025-06-20<br>
 * description    : Jwt토큰으로 인증하고 SecurityContextHolder에 추가하는 필터를 설정하는 클래스 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.06.20          eunchang           최초생성<br>
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final AntPathMatcher antPathMather = new AntPathMatcher();
    private final JwtUtility jwtUtility;
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final  String BEARER  = "Bearer ";

    private static final String[] ALLOW_URLS = new String[] {
            "/", "/api/auths/**"
    };

    private static final String[] NOT_ALLOW_URLS = new String[] {
            "/",
    };

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
            Authentication auth = getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(auth);

            processValidAccessToken(accessToken);
            filterChain.doFilter(request, response);

        } else {
            SecurityContextHolder.clearContext();
            throw new CustomException(StatusCode.INVALID_TOKEN);
        }

    }

    protected void processValidAccessToken(String accessToken){
        UserType type = jwtUtility.getUserType(accessToken);
        Integer userId = jwtUtility.getUserId(accessToken);

//        UserDetails userDetails;
//        //TODO : 서비스 만들기
//
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication getAuthentication(String token) {
        UserType userType = jwtUtility.getUserType(token);
        Integer userId = jwtUtility.getUserId(token);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userType.name()));

        return new UsernamePasswordAuthenticationToken(userId, null, authorities);
    }

    private boolean checkAllowedUrl(String url) {
        boolean isAllowUrl = Arrays.stream(ALLOW_URLS).anyMatch(pattern -> antPathMather.match(pattern,url));
        boolean isExceptionUrl = Arrays.stream(NOT_ALLOW_URLS).anyMatch(pattern -> antPathMather.match(pattern,url));

        return isAllowUrl && !isExceptionUrl;
    }
}
