package com.practice.simpleWeb.Dto;

import com.practice.simpleWeb.domain.BlackListToken;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BlackListTokenDto {

    private String memberId;

    private String token;


    public BlackListToken toDto(BlackListTokenDto blackListTokenDto){
        return BlackListToken.builder()
                .token(blackListTokenDto.getToken())
                .memberId(blackListTokenDto.getMemberId())
                .build();
    }

}

