package com.practice.simpleWeb.Security;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = jwtProvider.resolveAccessToken(request);
        String refreshToken = jwtProvider.resolveRefreshToken(request);


        if ( jwtProvider.validateAccessToken(accessToken)) {
            Authentication authentication = jwtProvider.getAuthentication(jwtProvider.resolveAccessToken(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("엑세스 토큰이 존재 할때= ");
        } else if (refreshToken.isEmpty() || !jwtProvider.validateRefreshToken(refreshToken)) {
            log.info("엑세스 토큰 만료, 리 프레시 토큰 만료");
        }
        else if (jwtProvider.validateRefreshToken(refreshToken)){
            log.info("리프레시 토큰 유효, 토큰 재발급");
            String email = jwtProvider.getEmail(refreshToken);
            String accessToken1 = jwtProvider.createAccessToken(email);
            String refreshToken1 = jwtProvider.createRefreshToken(email);

            Authentication authentication = jwtProvider.getAuthentication(accessToken1);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            response.setHeader("accessToken", accessToken1);
            response.setHeader("refreshToken", refreshToken1);
        }

        filterChain.doFilter(request, response);
    }

}

