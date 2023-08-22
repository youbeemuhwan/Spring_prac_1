package com.practice.simpleWeb.Service;

import com.practice.simpleWeb.Dto.MemberCreateRequestDto;
import com.practice.simpleWeb.Dto.MemberListDto;
import com.practice.simpleWeb.Dto.MemberLoginRequestDto;
import com.practice.simpleWeb.Dto.MemberLoginResponseDto;
import com.practice.simpleWeb.Repository.MemberRepository;
import com.practice.simpleWeb.Repository.RefreshTokenRepository;
import com.practice.simpleWeb.Security.JwtProvider;
import com.practice.simpleWeb.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    public void memberSignUp(MemberCreateRequestDto memberCreateRequestDto) {

        if (memberRepository.existsByEmail(memberCreateRequestDto.getEmail())) {
            throw new RuntimeException("존재하는 이메일 입니다.");
        }

        memberCreateRequestDto.setPassword(passwordEncoder.encode(memberCreateRequestDto.getPassword()));

        memberRepository.save(memberCreateRequestDto.toEntity(memberCreateRequestDto));
    }

    public MemberLoginResponseDto memberLogin(MemberLoginRequestDto memberLoginRequestDto) {
        Member loginMember = memberRepository.findByEmail(memberLoginRequestDto.getEmail()).orElseThrow(
                () -> new RuntimeException("잘못된 이메일 또는 패스워드 입니다.")
        );

        if (!passwordEncoder.matches(memberLoginRequestDto.getPassword(), loginMember.getPassword())) {
            throw new RuntimeException("잘못된 이메일 또는 패스워드 입니다.");
        }

        return MemberLoginResponseDto.builder()
                .email(loginMember.getEmail())
                .username(loginMember.getUsername())
                .access_token(jwtProvider.createAccessToken(loginMember.getEmail()))
                .refresh_token(jwtProvider.createRefreshToken(loginMember.getEmail()))
                .build();
    }

    public void memberLogout(Authentication authentication, HttpServletRequest request){


            String refreshToken = request.getHeader("RefreshToken");
            log.info("asas = {}", refreshToken);
            String email = authentication.getName();

            Long idByRefreshToken = jwtProvider.getIdByToken(refreshToken);

            if(!(memberRepository.findByEmail(email).orElseThrow().getId() == idByRefreshToken)){
                throw new RuntimeException("잘못된 접근 입니다.");
            }

            if(!memberRepository.existsById(idByRefreshToken)){
                throw new RuntimeException("잘못된 접근 입니다.");
            }

            refreshTokenRepository.deleteAllById(Collections.singleton(refreshToken));

        }

    public List<MemberListDto> memberList(Pageable pageable){
        Page<Member> all = memberRepository.findAll(pageable);

        List<MemberListDto> MemberList = new ArrayList<>();

        for (Member member : all ){
            MemberListDto buildMember = MemberListDto
                    .builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .email(member.getEmail())
                    .role(member.getRole())
                    .address(member.getAddress())
                    .build();
            MemberList.add(buildMember);
        }

        return MemberList;
    }
}
