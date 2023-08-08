package com.sparta.with.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class SignupRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;
    @Email
    @NotBlank
    private String email;

    @Builder.Default
    private boolean admin = false;
    @Builder.Default
    private String adminToken = "";
}