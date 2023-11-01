package com.practice.simpleWeb.Security;

import com.practice.simpleWeb.Dto.BlackListTokenDto;
import com.practice.simpleWeb.Dto.RefreshTokenCreateDto;
import com.practice.simpleWeb.Repository.BlackListTokenRepository;
import com.practice.simpleWeb.Repository.MemberRepository;
import com.practice.simpleWeb.Repository.RefreshTokenRepository;
import com.practice.simpleWeb.Service.UserDetailServiceImpl;
import com.practice.simpleWeb.domain.BlackListToken;
import com.practice.simpleWeb.domain.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtProvider {

    private final UserDetailServiceImpl userDetailService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final BlackListTokenRepository blackListTokenRepository;


    private final Long RefreshTokenValidTime = 60 * 20L * 1000;
    private final Long AccessTokenValidTime = 60 * 10L * 1000;

    @Value("${jwt.secret_key}")
    private String secretKey;

    public String createAccessToken(String email) {
        Claims claimsForAccess = Jwts.claims().setSubject(email);
        Optional<Member> member = memberRepository.findByEmail(email);

        claimsForAccess.put("id", member.get().getId());
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        Date now = new Date();
        String accessToken = Jwts.builder()
                .setClaims(claimsForAccess)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + AccessTokenValidTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return accessToken;
    }

    public String createRefreshToken(String email) {
        Claims claimForRefresh = Jwts.claims().setSubject(email);
        Optional<Member> member = memberRepository.findByEmail(email);
        claimForRefresh.put("id", member.get().getId());
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        Date now = new Date();

        String refreshToken = Jwts.builder()
                .setClaims(claimForRefresh)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + RefreshTokenValidTime))
                .signWith(key, SignatureAlgorithm.HS384)
                .compact();

        RefreshTokenCreateDto buildRefreshTokenDto = RefreshTokenCreateDto.builder()
                .token(refreshToken)
                .memberId(String.valueOf(member.get().getId()))
                .build();

        refreshTokenRepository.save(buildRefreshTokenDto.toDto(buildRefreshTokenDto));

        return refreshToken;
    }

    public void logOut(HttpServletRequest request){
        String refreshToken = request.getHeader("refreshToken");
        String accessToken = request.getHeader("accessToken");

        if (refreshToken != null && validateRefreshToken(refreshToken)){
            String idByToken = getIdByToken(refreshToken);
            refreshTokenRepository.deleteById(idByToken);
            if (validateAccessToken(accessToken)){
                BlackListTokenDto blackListTokenDto = BlackListTokenDto.builder()
                        .token(accessToken)
                        .memberId(getIdByToken(accessToken))
                        .build();
                BlackListToken blackListToken = blackListTokenDto.toDto(blackListTokenDto);

                blackListTokenRepository.save(blackListToken);


            }
        }




    }



    public Authentication getAuthentication(String access_token) {

        String email = getEmail(access_token);
        UserDetails userDetails = userDetailService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Authentication getAuthenticationByRefreshToken(String refresh_token) {

        String email = getEmail(refresh_token);
        UserDetails userDetails = userDetailService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmail(String token) {

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public String getIdByToken(String token){
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        Object ObjectId = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id");
        String id = String.valueOf(ObjectId);

        return id;
    }

    public HashMap<String, String> reIssue(String email){
        String accessToken = createAccessToken(email);
        String refreshToken = createRefreshToken(email);
        HashMap<String, String> token = new HashMap<>();
        token.put("accessToken", accessToken);
        token.put("refreshToken", refreshToken);

        return token;

    }

    public String resolveAccessToken(HttpServletRequest request) {

        return request.getHeader("accessToken");
    }

    public String resolveRefreshToken(HttpServletRequest request) {

        return request.getHeader("refreshToken");
    }

    public boolean validateAccessToken(String accessToken) {
        if(validateBlackListToken(accessToken)){
            log.info("해당 토큰은 유효하지 않습니다.");
            return false;
        }

        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);

            return !claims.getBody().getExpiration().before(new Date());

        } catch (Exception e) {

            return false;
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            String id = getIdByToken(refreshToken);
            if(Objects.equals(refreshTokenRepository.findById(id).orElseThrow().getToken(), refreshToken))
            {
                Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);
                return !claims.getBody().getExpiration().before(new Date()); // true : 만료, false : 유효
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean validateBlackListToken(String accessToken){
        return blackListTokenRepository.findById(getIdByToken(accessToken)).isPresent();
    }
}


