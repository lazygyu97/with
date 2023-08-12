package com.sparta.with.dto;

import com.sparta.with.entity.BoardUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardUserResponseDto {
    private String collaborator;

    public static BoardUserResponseDto of(BoardUser boardUser) {
        return BoardUserResponseDto.builder()
            .collaborator(boardUser.getCollaborator().getUsername())
            .build();
    }
}
