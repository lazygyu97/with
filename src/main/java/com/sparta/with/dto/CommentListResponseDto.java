package com.sparta.with.dto;

import com.sparta.with.entity.Comment;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentListResponseDto extends ApiResponseDto {
  private List<CommentResponseDto> commentList;

  public static CommentListResponseDto of(List<Comment> cards){
    List<CommentResponseDto> cardResponseDtos = cards.stream().map(CommentResponseDto::of).toList();
    return CommentListResponseDto.builder()
        .commentList(cardResponseDtos)
        .build();
  }
}
