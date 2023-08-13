package com.sparta.with.dto;

import com.sparta.with.entity.Card;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CardListResponseDto extends ApiResponseDto {

    private List<CardResponseDto> cardsList;

    public static CardListResponseDto of(List<Card> cards) {
        List<CardResponseDto> cardResponseDtos = cards.stream().map(CardResponseDto::of).toList();
        return CardListResponseDto.builder()
            .cardsList(cardResponseDtos)
            .build();
    }
}