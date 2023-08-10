package com.sparta.with.service;

import com.sparta.with.dto.AreaRequestDto;
import com.sparta.with.dto.AreaResponseDto;
import com.sparta.with.entity.Area;
import com.sparta.with.entity.Board;
import com.sparta.with.repository.AreaRepository;
import com.sparta.with.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AreaService {
  private final BoardRepository boardRepository;
  private final AreaRepository areaRepository;

  // 컬럼생성
  public AreaResponseDto createArea(AreaRequestDto requestDto) {
    // 선택한 보드에 컬럼 등록
    Board board = boardRepository.findById(requestDto.getBoardId()).orElseThrow(
        () -> new IllegalArgumentException("해당 보드가 존재하지 않습니다.")
    );

    Area area = new Area(requestDto);
    area.setBoard(board);

    areaRepository.save(area);

    return new AreaResponseDto(area);
  }
}
