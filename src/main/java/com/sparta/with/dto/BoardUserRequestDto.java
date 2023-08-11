package com.sparta.with.dto;

import com.sparta.with.entity.Board;
import com.sparta.with.entity.BoardUser;
import com.sparta.with.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardUserRequestDto {
    private Long userId;

    public static BoardUser toEntity(User collaborator, Board board) {
        return BoardUser.builder()
            .id(collaborator.getId())
            .collaborator(collaborator)
            .board(board)
            .build();
    }
}
