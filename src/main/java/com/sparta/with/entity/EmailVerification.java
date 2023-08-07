package com.sparta.with.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "email_verification", timeToLive = 60*24*7)
@Getter
public class EmailVerification {
    @Id
    private String email;
    private String code;
    private boolean verificated = false;


    public EmailVerification(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public void setVerificated() {
        this.verificated = true;
    }
}
