package com.sparta.with.dto;

import com.sparta.with.entity.Area;
import com.sparta.with.entity.Board;
import com.sparta.with.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AreaRequestDto {
    private Long boardId;
    private String name;
//    private Integer position;

    @Builder
    public Area toEntity() {
        return Area.builder()
            .name(this.name)
//            .author(author)
            .build();
    }
}
