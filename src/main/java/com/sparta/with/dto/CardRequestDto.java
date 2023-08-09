package com.sparta.with.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRequestDto {
  private Long areaId;
  private String title;
  private String content;
  private String color;
  private LocalDateTime startDate;
  private LocalDateTime dueDate;
  private String image;
}
