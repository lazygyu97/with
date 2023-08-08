package com.sparta.with.entity.redishash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "blacklist")
@Getter
@AllArgsConstructor
public class Blacklist {
    @Id
    private String accessToken;

    @TimeToLive
    private Long expirationSeconds;
}
