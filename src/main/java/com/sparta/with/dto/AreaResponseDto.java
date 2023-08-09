package com.sparta.with.dto;

import com.sparta.with.entity.Area;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AreaResponseDto {
  private String name;
  private List<CardResponseDto> cards;

  public AreaResponseDto(Area area) {
    this.name = area.getName();
    this.cards = area.getCards().stream()
        .map(CardResponseDto::new)
        .collect(Collectors.toList());
  }
}
