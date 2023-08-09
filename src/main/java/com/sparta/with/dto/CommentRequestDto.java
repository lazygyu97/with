package com.sparta.with.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
  private long cardId; // 댓글을 남길 카드 ID
  private String content; // 댓글 내용
}
