package com.sparta.with.dto;

import com.sparta.with.entity.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto extends ApiResponseDto{
  private long id;
  private String content;
  private String username;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private String image;

  public static CommentResponseDto of(Comment comment) {
    return CommentResponseDto.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .username(comment.getAuthor().getUsername())
        .image(comment.getAuthor().getImage())
        .build();
  }
}
