package com.howaboutquestion.backend.global.util;


import com.howaboutquestion.backend.domain.usermeta.entity.UserMetaEntity;
import com.howaboutquestion.backend.domain.usermeta.entity.UserType;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * packageName    : com.howaboutquestion.backend.global.util<br>
 * fileName       : GlobalExceptionHandler.java<br>
 * author         : eunchang<br>
 * date           : 2025-06-20<br>
 * description    : Jwt Utility 클래스 입니다..<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.06.20          eunchang           최초생성<br>
 * 25.06.16          eunchang           Email, Profile 추가<br>
 */
@Component
public class JwtUtility {
    private static final String CLAIM_USER_ID = "id";
    private static final String CLAIM_USER_NAME = "name";
    private static final String CLAIM_USER_EMAIL = "email";
    private static final String CLAIM_TYPE = "type";
    private static final String CLAIM_PROFILE = "profile";
    private static final String CLAIM_UUID = "uuid";

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validateTime}")
    private long accessTokenValidateTime;

    @Value("${jwt.refresh-token-validateTime}")
    private long refreshTokenValidateTime;

    private Key signingKey;

    /**
     * signingKey를 초기화 합니다.
     */
    @PostConstruct
    protected void init() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        signingKey = Keys.hmacShaKeyFor(Base64.getEncoder().encodeToString(keyBytes).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Access Token 을 생성합니다.
     * @param userId 유저 ID
     * @param userName 유저 네임
     * @param uuid 토큰 고유 ID
     * @return AccessToken 을 반환합니다.
     */
    public String createAccessToken(Integer userId, String userEmail, String userName, UserType userType, String profile, String uuid) {
        return createToken(userId, userEmail, userName, userType, profile, uuid, accessTokenValidateTime * 1000L);
    }

    /**
     * Refresh Token 을 생성합니다.
     * @param userId 유저 ID
     * @param userName 유저 네임
     * @param uuid 토큰 고유 ID
     * @return RefreshToken 을 반환합니다.
     */
    public String createRefreshToken(Integer userId, String userEmail, String userName, UserType userType, String profile, String uuid){
        return createToken(userId, userEmail, userName, userType, profile, uuid, refreshTokenValidateTime * 1000L);
    }


    /**
     * Jwt 토큰을 생성합니다.
     * @param userId 유저 ID
     * @param userName 유저 네임
     * @param userEmail 유저 이메일
     * @param profile 유저 프로필 사진
     * @param uuid 토큰 고유 ID
     * @param validity 유효기간
     * @return jwt 토큰
     */
    private String createToken(Integer userId, String userEmail, String userName, UserType userType, String profile, String uuid, long validity){
        Date createTime = new Date();
        Date expireTime = new Date(createTime.getTime() + validity);

        return Jwts.builder()
                .claim(CLAIM_USER_ID, userId)
                .claim(CLAIM_USER_EMAIL, userEmail)
                .claim(CLAIM_USER_NAME, userName)
                .claim(CLAIM_TYPE, userType)
                .claim(CLAIM_PROFILE, profile)
                .claim(CLAIM_UUID, uuid)
                .setIssuedAt(createTime)
                .setExpiration(expireTime)
                .signWith(signingKey)
                .compact();
    }

    /**
     * Jwt 토큰에서 사용자 Id를 가져옵니다.
     * @param token Jwt 토큰
     * @return 사용자 Id
     */
    public Integer getUserId(String token) { return getClaim(token, CLAIM_USER_ID, Integer.class);}

    /**
     * Jwt 토큰에서 사용자 이메일을 가져옵니다.
     * @param token Jwt 토큰
     * @return 사용자 Email
     */
    public String getUserEmail(String token) { return getClaim(token, CLAIM_USER_EMAIL, String.class);}

    /**
     * Jwt 토큰에서 사용자 이름을 가져옵니다.
     * @param token Jwt 토큰
     * @return 사용자 Name
     */
    public String getUserName(String token) { return getClaim(token, CLAIM_USER_NAME, String.class);}

    /**
     * Jwt 토큰에서 사용자 프로필을 가져옵니다.
     * @param token Jwt 토큰
     * @return 사용자 프로필
     */
    public String getProfile(String token) { return getClaim(token, CLAIM_PROFILE, String.class);}

    /**
     * Jwt 토큰의 사용자 타입을 가져옵니다.
     * @param token Jwt 토큰
     * @return 사용자 Type
     */
    public UserType getUserType(String token) { return getClaim(token, CLAIM_TYPE, UserType.class);}

    /**
     * Jwt 토큰의 고유 번호을 가져옵니다.
     * @param token Jwt 토큰
     * @return Token UUID
     */
    public Integer getUUID(String token) { return getClaim(token, CLAIM_UUID, Integer.class);}

    /**
     * 토큰 유효성을 검사합니다.
     * @param token Jwt 토큰
     * @return 유효한 토큰인 경우 true
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new CustomException(StatusCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new CustomException(StatusCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            throw new CustomException(StatusCode.INVALID_TOKEN);
        }
    }

    /**
     * 토큰의 남은 만료 시간을 반환합니다.
     * @param token Jwt 토큰
     * @return 토큰 남은 시간
     */
    public long getRemainingExpiration(String token) {
        Claims claims = parseClaim(token);
        Date expiration = claims.getExpiration();
        long remainingTime = expiration.getTime() - System.currentTimeMillis();

        if(remainingTime < 0) throw new CustomException(StatusCode.INVALID_TOKEN);
        return remainingTime;
    }

    /**
     * Jwt 토큰에서 클래임 값을 얻습니다.
     * @param token Jwt 토큰
     * @param claimKey 클레임 키
     * @param clazz 반환할 타입
     * @return 해당 클래임 값
     */
    private <T> T getClaim(String token, String claimKey, Class<T> clazz){
        try{
            return parseClaim(token).get(claimKey, clazz);
        }catch(ExpiredJwtException e){
            return e.getClaims().get(claimKey,clazz);
        }
    }

    /**
     * Jwt 토큰에서 클래임음 파싱합니다.
     * @param token Jwt 토큰
     * @return Jwt 클래임 인스턴스
     */
    private Claims parseClaim(String token){
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



}
