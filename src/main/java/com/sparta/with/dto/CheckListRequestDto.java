package com.sparta.with.dto;

import com.sparta.with.entity.Card;
import com.sparta.with.entity.CheckList;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckListRequestDto {
    private long cardId; // 체크리스트를 남길 카드 ID
    private String content;

    public CheckList toEntity(Card card) {
        CheckList comment = CheckList.builder()
            .content(this.getContent())
            .card(card)
            .build();
        return comment;
    }
}
