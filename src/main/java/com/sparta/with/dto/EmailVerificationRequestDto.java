package com.sparta.with.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailVerificationRequestDto {
    private String email;
    private String code;

    public EmailVerificationRequestDto(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
