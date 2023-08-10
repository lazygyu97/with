package com.sparta.with.dto;

import com.sparta.with.entity.Card;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardResponseDto extends ApiResponseDto{
  private Long id;
  private String title;
  private String content;
  private LocalDateTime startDate;
  private LocalDateTime dueDate;
  private String username;
  private String color;
  private String image;
  private List<CardUserResponseDto> cardUsers; // 협업자들
  private List<CommentResponseDto> comments;

  public static CardResponseDto of(Card card) {
    return CardResponseDto.builder()
        .id(card.getId())
        .title(card.getTitle())
        .content(card.getContent())
        .startDate(card.getStartDate())
        .dueDate(card.getDueDate())
        .username(card.getAuthor().getUsername())
        .cardUsers(card.getCardUsers().stream().map(CardUserResponseDto::of)
            .collect(Collectors.toList()))
        .comments(card.getComments().stream()
        .map(CommentResponseDto::of)
        .collect(Collectors.toList()))
        .build();
  }

    public static CardResponseDto of(Card card) {
      return CardResponseDto.builder()
              .id(card.getId())
              .title(card.getTitle())
              .content(card.getContent())
              .startDate(card.getStartDate())
              .dueDate(card.getDueDate())
              .username(card.getAuthor().getUsername())
              .cardUsers(card.getCardUsers())
              .comments(card.getComments().stream().map(CommentResponseDto::of).toList())
              .build();
    }
}