package com.practice.simpleWeb.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberLoginRequestDto {

    private String email;
    private String password;

}
