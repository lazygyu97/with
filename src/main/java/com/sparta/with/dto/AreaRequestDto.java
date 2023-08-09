package com.sparta.with.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AreaRequestDto {
  private Long boardId;
  private String name;
  private Integer position;
}
