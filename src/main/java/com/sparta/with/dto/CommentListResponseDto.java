package com.sparta.with.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CommentListResponseDto extends ApiResponseDto {
  private List<CommentResponseDto> commentList;

  public CommentListResponseDto(List<CommentResponseDto> commentList) {
    this.commentList = commentList;
  }
}
