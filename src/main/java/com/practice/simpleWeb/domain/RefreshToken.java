package com.practice.simpleWeb.domain;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refreshToken", timeToLive = 30 * 60 * 1000L)
public class RefreshToken {

    @Id
    private String token;

    private Long memberId;

    @Builder
    public RefreshToken(Long memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }
}
