package com.sparta.with.dto;

import com.sparta.with.entity.Area;
import java.util.List;
import java.util.stream.Collectors;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AreaResponseDto {
  private String name;
  private List<CardResponseDto> cards;

  public AreaResponseDto(Area area) {
    this.name = area.getName();
    if (area.getCards() != null) {
      this.cards = area.getCards().stream()
              .map(CardResponseDto::of)
              .collect(Collectors.toList());
    }
  }

  public static AreaResponseDto of(Area area) {
    return AreaResponseDto.builder()
            .name(area.getName())
            .cards(area.getCards().stream().map(CardResponseDto::of).toList())
            .build();
  }
}
