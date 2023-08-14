package com.sparta.with.dto;

import com.sparta.with.entity.CardUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CardUserResponseDto extends ApiResponseDto {
    private Long id;
   private String collaborator;

    public static CardUserResponseDto of(CardUser cardUser) {
        return CardUserResponseDto.builder()
            .id(cardUser.getCollaborator().getId())
            .collaborator(cardUser.getCollaborator().getUsername())
            .build();
    }
}
