package com.sparta.with.controller;

import com.sparta.with.dto.ApiResponseDto;
import com.sparta.with.dto.AreaRequestDto;
import com.sparta.with.dto.AreaResponseDto;
import com.sparta.with.dto.BoardRequestDto;
import com.sparta.with.entity.Area;
import com.sparta.with.entity.Board;
import com.sparta.with.security.UserDetailsImpl;
import com.sparta.with.service.AreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Area Example API", description = "컬럼 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;

    // 컬럼 생성
    @Operation(summary = "create area", description = "에리아 생성")
    @PostMapping("/areas")
    public ResponseEntity<AreaResponseDto> createArea(
        @RequestBody AreaRequestDto requestDto) {

        AreaResponseDto result = areaService.createArea(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 컬럼 이름 수정
    @Operation(summary = "update area's name", description = "에리아 이름 수정")
    @PutMapping("/areas/names/{id}")
    public ResponseEntity<ApiResponseDto> updateAreaName(@PathVariable Long id,@RequestBody AreaRequestDto areaRequestDto) {
        Area area = areaService.findArea(id);
        areaService.updateAreaName(area, areaRequestDto);
        
        return ResponseEntity.ok()
            .body(new ApiResponseDto("에리아 이름이 수정되었습니다.", HttpStatus.OK.value()));
    }

    // 컬럼 삭제
    @Operation(summary = "delete area", description = "에리아 삭제")
    @DeleteMapping("/areas/{id}")
    public ResponseEntity<ApiResponseDto> deleteArea(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        Area area = areaService.findArea(id);
        areaService.deleteAreaName(area);
        return ResponseEntity.ok().body(new ApiResponseDto("에리아 삭제 성공", HttpStatus.OK.value()));
    }

    // 컬럼 순서 이동 - 추후 작업

}
