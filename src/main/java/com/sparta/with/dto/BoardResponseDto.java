package com.sparta.with.dto;

import com.sparta.with.entity.Area;
import com.sparta.with.entity.Board;
import com.sparta.with.entity.BoardUser;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String name;
    private String author;
    private String color;
    private String info;
    private String message;
    private Integer statusCode;
    private List<BoardUserResponseDto> boardUsers;
    private List<AreaResponseDto> areas;

    public static BoardResponseDto of(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .name(board.getName())
                .author(board.getAuthor().getUsername())
                .color(board.getColor())
                .info(board.getInfo())
                .boardUsers(board.getBoardUsers().stream().map(BoardUserResponseDto::of).toList())
                .areas(board.getAreas().stream().map(AreaResponseDto::of).toList())
                .build();
    }
}
