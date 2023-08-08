package com.sparta.with.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardsResponseDto extends ApiResponseDto {
    private List<BoardResponseDto> boards;

    public BoardsResponseDto(List<BoardResponseDto> boards) {
        this.boards = boards;
    }
}
