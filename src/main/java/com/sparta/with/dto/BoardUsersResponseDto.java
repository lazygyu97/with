package com.sparta.with.dto;

import com.sparta.with.entity.BoardUser;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardUsersResponseDto {
  private List<BoardUserResponseDto> collaborators;

  public static BoardUsersResponseDto of(List<BoardUser> boardUsers) {
    List<BoardUserResponseDto> boardUserResponseDtos = boardUsers.stream().map(BoardUserResponseDto::of)
        .toList();
    return BoardUsersResponseDto.builder()
        .collaborators(boardUserResponseDtos)
        .build();
  }

}
