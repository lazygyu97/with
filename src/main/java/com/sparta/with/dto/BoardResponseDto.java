package com.sparta.with.dto;

import com.sparta.with.entity.Area;
import com.sparta.with.entity.Board;
import com.sparta.with.entity.BoardUser;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardResponseDto {
    private Long id;
    private String name;
    private String author;
    private String color;
    private String info;
    private List<BoardUser> boardUsers;
    private List<Area> areas;

    @Builder
    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.name = board.getName();
        this.color = board.getColor();
        this.info = board.getInfo();
        this.author = board.getAuthor().getUsername();
        this.boardUsers = board.getBoardUsers();
        this.areas = board.getAreas();
    }

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
