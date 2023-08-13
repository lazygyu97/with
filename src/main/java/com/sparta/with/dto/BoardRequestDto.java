package com.sparta.with.dto;

import com.sparta.with.entity.Board;
import com.sparta.with.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardRequestDto {
    @NotBlank
    private String name;

    private String color;
    private String info;

    public Board toEntity(User author) {
        return Board.builder()
                .name(this.name)
                .color(this.color)
                .info(this.info)
                .author(author)
                .build();
    }
}
