package com.sparta.with.dto;

import com.sparta.with.entity.Area;
import com.sparta.with.entity.Card;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CardRequestDto {
  private Long areaId;
  private String title;
  private String content;
  private String color;
  private LocalDateTime startDate;
  private LocalDateTime dueDate;
  private String image;

  public Card toEntity(Area area) {
    Card board = Card.builder()
        .area(area)
        .title(this.title)
        .content(this.content)
        .color(this.color)
        .dueDate(this.getDueDate())
        .startDate(this.getStartDate())
        .image(this.image)
        .build();
    return board;
  }
}
