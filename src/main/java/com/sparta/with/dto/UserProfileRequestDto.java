package com.sparta.with.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class UserProfileRequestDto {
    private MultipartFile Image;
}