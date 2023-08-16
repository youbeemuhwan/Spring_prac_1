package com.practice.simpleWeb.Service;

import com.practice.simpleWeb.Dto.MemberCreateRequestDto;
import com.practice.simpleWeb.Dto.MemberLoginRequestDto;
import com.practice.simpleWeb.Dto.MemberLoginResponseDto;
import com.practice.simpleWeb.Repository.MemberRepository;
import com.practice.simpleWeb.Security.JwtProvider;
import com.practice.simpleWeb.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

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

    public List<Member> memberList(){
        List<Member> all = memberRepository.findAll();
        return all;
    }


}
