package com.sparta.with.dto;

import com.sparta.with.entity.Card;
import com.sparta.with.entity.Comment;
import com.sparta.with.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequestDto {
    private long cardId; // 댓글을 남길 카드 ID
    private String content;

    public Comment toEntity(Card card, User author) {
        Comment comment = Comment.builder()
            .content(this.getContent())
            .card(card)
            .author(author)
            .build();
        return comment;
    }
}
