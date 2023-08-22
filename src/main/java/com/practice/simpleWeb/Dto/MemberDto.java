package com.practice.simpleWeb.Dto;

import com.practice.simpleWeb.domain.Address;
import com.practice.simpleWeb.domain.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private String email;

    private String username;

    private Role role;

    private Address address;

    @Builder
    public MemberDto(String email, String username, Role role, Address address) {
        this.email = email;
        this.username = username;
        this.role = role;
        this.address = address;
    }
}
