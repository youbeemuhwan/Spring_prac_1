package com.practice.simpleWeb.Security;

import com.practice.simpleWeb.Dto.RefreshTokenCreateDto;
import com.practice.simpleWeb.Dto.TokenInfo;
import com.practice.simpleWeb.Repository.MemberRepository;
import com.practice.simpleWeb.Repository.RefreshTokenRepository;
import com.practice.simpleWeb.Service.UserDetailServiceImpl;
import com.practice.simpleWeb.domain.Member;
import com.practice.simpleWeb.domain.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtProvider {

    private final UserDetailServiceImpl userDetailService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    private final Long RefreshTokenValidTime = 1000L * 60 * 30;
    private final Long AccessTokenValidTime = 1000L * 60 * 30;

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
                .memberId(member.get().getId())
                .build();

        refreshTokenRepository.save(buildRefreshTokenDto.toDto(buildRefreshTokenDto));


        return refreshToken;
    }

    public Authentication getAuthentication(String access_token) {
        String email = getEmail(access_token);
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


    public String resolveAccessToken(HttpServletRequest request) {

        return request.getHeader("AccessToken");
    }

    public String resolveRefreshToken(HttpServletRequest request) {

        return request.getHeader("RefreshToken");
    }

    public boolean validateAccessToken(String accessToken) {

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
            if(Objects.equals(refreshTokenRepository.findById(refreshToken).orElseThrow().getToken(), refreshToken))
            {
                Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);
                return !claims.getBody().getExpiration().before(new Date()); // true : 만료, false : 유효
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }
}

