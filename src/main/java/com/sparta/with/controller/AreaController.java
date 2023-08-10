package com.sparta.with.controller;

import com.sparta.with.dto.AreaRequestDto;
import com.sparta.with.dto.AreaResponseDto;
import com.sparta.with.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AreaController {

  private final AreaService areaService;

  // 컬럼 생성
  @PostMapping("/columns")
  public ResponseEntity<AreaResponseDto> createArea(
      @RequestBody AreaRequestDto requestDto) {

    AreaResponseDto result = areaService.createArea(requestDto);

    return ResponseEntity.status(201).body(result);
  }
}
