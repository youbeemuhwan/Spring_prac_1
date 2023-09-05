package com.practice.simpleWeb.Dto;

import com.practice.simpleWeb.domain.Address;
import com.practice.simpleWeb.domain.Role;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;

@Getter
@Setter
@NoArgsConstructor
public class MemberListDto {

    private Long id;

    private String email;

    private String username;

    private Role role;

    private Address address;



    @Builder
    public MemberListDto(Long id, String email, String username,  Role role, Address address) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
        this.address = address;

    }
}
