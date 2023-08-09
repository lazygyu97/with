package com.sparta.with.dto;

import com.sparta.with.entity.Board;
import com.sparta.with.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardRequestDto {
    private String name;
    private String color;
    private String info;

    public Board toEntity(User author) {
        Board board = Board.builder()
                .name(this.name)
                .color(this.color)
                .info(this.info)
                .author(author)
                .build();
        return board;
    }
}
