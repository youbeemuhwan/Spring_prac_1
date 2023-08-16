package com.practice.simpleWeb.Dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MemberLoginResponseDto {

    private String email;
    private String username;
    private String access_token;
    private String refresh_token;

    public MemberLoginResponseDto(String email,String username, String access_token, String refresh_token) {
        this.email = email;
        this.username = username;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }
}
