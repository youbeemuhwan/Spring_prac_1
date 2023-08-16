package com.practice.simpleWeb.domain;


import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    private String username;
    @NotNull
    private String password;

    private Role role;

    @Embedded
    private Address address;

    @Builder
    public Member(String email, String username, String password, Role role, Address address) {

        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.address = address;
    }
}
