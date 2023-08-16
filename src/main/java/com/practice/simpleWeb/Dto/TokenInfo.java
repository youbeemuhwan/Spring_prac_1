package com.practice.simpleWeb.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;



@Builder
@Getter
@Setter
public class TokenInfo {

    private String refreshToken;
    private String accessToken;

}
