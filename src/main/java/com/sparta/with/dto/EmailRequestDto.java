package com.sparta.with.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailRequestDto {
    private String email;

    public EmailRequestDto(String email) {
        this.email = email;
    }
}