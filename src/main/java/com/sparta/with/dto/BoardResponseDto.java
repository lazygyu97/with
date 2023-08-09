package com.sparta.with.dto;

import com.sparta.with.entity.Area;
import com.sparta.with.entity.Board;
import com.sparta.with.entity.BoardUser;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String name;
    private String author;
    private String color;
    private String info;
    private List<BoardUser> boardUsers;
    private List<Area> areas;


    public static BoardResponseDto of(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .name(board.getName())
                .author(board.getAuthor().getUsername())
                .color(board.getColor())
                .info(board.getInfo())
                .build();
    }
}
