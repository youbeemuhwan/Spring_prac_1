package com.practice.simpleWeb.Dto;


import com.practice.simpleWeb.domain.Address;
import com.practice.simpleWeb.domain.Member;
import com.practice.simpleWeb.domain.Role;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter

public class MemberCreateRequestDto {


    private String email;

    private String username;

    private String password;

    private Role role;

    private Address address;



    @Builder
    public Member toEntity(MemberCreateRequestDto memberCreateRequestDto){
        return Member.builder()
                .email(memberCreateRequestDto.getEmail())
                .username(memberCreateRequestDto.getUsername())
                .password(memberCreateRequestDto.getPassword())
                .role(memberCreateRequestDto.getRole())
                .address(memberCreateRequestDto.getAddress())
                .build();
    }


}
