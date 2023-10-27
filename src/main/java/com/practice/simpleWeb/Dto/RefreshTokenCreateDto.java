package com.practice.simpleWeb.Dto;

import com.practice.simpleWeb.domain.RefreshToken;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RefreshTokenCreateDto {

    private String token;

    private String memberId;

    public RefreshToken toDto(RefreshTokenCreateDto refreshTokenCreateDto){
        return RefreshToken.builder()
                .token(refreshTokenCreateDto.getToken())
                .memberId(refreshTokenCreateDto.getMemberId())
                .build();
    }
}
