package com.sparta.with.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CardListResponseDto extends ApiResponseDto {
  private List<CardResponseDto> cardsList;

  public CardListResponseDto(List<CardResponseDto> cardsList) {
    this.cardsList = cardsList;
  }
}