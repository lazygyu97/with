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
  private long Id;
  private long cardId;
  private String content;
  private String username;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private String image;

  public CommentResponseDto(Comment comment) {
    this.Id = comment.getId();
    this.cardId = comment.getCard().getId();
    this.content = comment.getContent();
    this.username = comment.getAuthor().getUsername();
    this.createdAt = comment.getCreatedAt();
    this.modifiedAt = comment.getModifiedAt();
    this.image = comment.getAuthor().getImage();
  }
}
