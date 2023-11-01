package com.practice.simpleWeb.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "blackList", timeToLive = 60 * 10L)
public class BlackListToken {

    @Id
    private String memberId;

    private String token;

    @Builder
    public BlackListToken(String memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }
}
