package com.practice.simpleWeb.Service;

import com.practice.simpleWeb.Repository.MemberRepository;
import com.practice.simpleWeb.Security.CustomUserDetails;
import com.practice.simpleWeb.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("해당 유저가 존재하지 않습니다.")
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        return customUserDetails;
    }
}
