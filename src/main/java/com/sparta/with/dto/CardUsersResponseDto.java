package com.sparta.with.dto;

import com.sparta.with.entity.CardUser;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CardUsersResponseDto extends ApiResponseDto {
    private List<CardUserResponseDto> collaborators;

    public static CardUsersResponseDto of(List<CardUser> cardUsers) {
        List<CardUserResponseDto> cardUserResponseDtos = cardUsers.stream().map(CardUserResponseDto::of)
        .toList();
        return CardUsersResponseDto.builder()
            .collaborators(cardUserResponseDtos)
            .build();
    }
}
