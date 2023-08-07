package com.sparta.with.dto;

import lombok.Getter;

@Getter
public class EmailVerificationRequestDto {
    private String email;
    private String code;
}
