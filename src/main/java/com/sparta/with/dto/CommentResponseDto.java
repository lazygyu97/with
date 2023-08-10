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
  private long cardId;
  private String content;
  private String username;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private String image;

  public CommentResponseDto(Comment comment) {
    this.id = comment.getId();
    this.cardId = comment.getCard().getId();
    this.content = comment.getContent();
    this.username = comment.getAuthor().getUsername();
    this.createdAt = comment.getCreatedAt();
    this.modifiedAt = comment.getModifiedAt();
    this.image = comment.getAuthor().getImage();
  }

    public static CommentResponseDto of(Comment comment) {
      return CommentResponseDto.builder()
              .id(comment.getId())
              .cardId(comment.getCard().getId())
              .content(comment.getContent())
              .username(comment.getAuthor().getUsername())
              .createdAt(comment.getCreatedAt())
              .modifiedAt(comment.getModifiedAt())
              .image(comment.getAuthor().getImage())
              .build();
    }
}
