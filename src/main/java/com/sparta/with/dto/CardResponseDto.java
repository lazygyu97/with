package com.sparta.with.dto;

import com.sparta.with.entity.Card;
import com.sparta.with.entity.CardUser;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardResponseDto extends ApiResponseDto{
  private Long id;
  private String title;
  private String content;
  private LocalDateTime startDate;
  private LocalDateTime dueDate;
  private String username;
  private String color;
  private String image;
  private List<CardUser> cardUsers; // 협업자들
  private List<CommentResponseDto> comments;

  public CardResponseDto(Card card) {
    this.id = card.getId();
    this.title = card.getTitle();
    this.content = card.getContent();
    this.startDate = card.getCreatedAt();
    this.dueDate = card.getModifiedAt();
    this.username = card.getAuthor().getUsername();
    this.cardUsers = card.getCardUsers();
    this.comments = card.getComments().stream()
        .map(CommentResponseDto::new)
        .collect(Collectors.toList());
  }
}