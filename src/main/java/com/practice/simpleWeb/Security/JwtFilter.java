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


        if (accessToken != null && jwtProvider.validateAccessToken(accessToken)) {
            Authentication authentication = jwtProvider.getAuthentication(jwtProvider.resolveAccessToken(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("엑세스 토큰이 존재할때= {}", response.getHeader("accessToken"));
        }

        if (refreshToken != null && jwtProvider.validateRefreshToken(refreshToken)) {

            String newAccessToken = jwtProvider.createAccessToken(jwtProvider.getEmail(refreshToken));
            response.addHeader("accessToken", newAccessToken);

                jwtProvider.validateAccessToken(newAccessToken);
                Authentication authentication = jwtProvider.getAuthentication(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("액세스 토큰이 없을때 리프레시 토큰으로 액세스 토큰 생성", response.getHeader("newAccessToken"));
            }
        filterChain.doFilter(request, response);
    }
}

