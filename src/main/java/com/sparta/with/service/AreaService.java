package com.sparta.with.service;

import com.sparta.with.dto.AreaRequestDto;
import com.sparta.with.dto.AreaResponseDto;
import com.sparta.with.entity.Area;
import com.sparta.with.entity.Board;
import com.sparta.with.repository.AreaRepository;
import com.sparta.with.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AreaService {

    private final BoardRepository boardRepository;
    private final AreaRepository areaRepository;

    // 컬럼 생성
    public AreaResponseDto createArea(AreaRequestDto areaRequestDto) {
        try {
            // 선택한 보드에 컬럼 등록
            Board board = boardRepository.findById(areaRequestDto.getBoardId()).orElseThrow(
                () -> new IllegalArgumentException("해당 보드가 존재하지 않습니다.")
            );

            Area area = areaRequestDto.toEntity(board);
            areaRepository.save(area);

            return AreaResponseDto.of(area);
        } catch (Exception e) {
            throw new RuntimeException("에리아 생성에 실패했습니다. 이유 : " + e.getMessage(), e);
        }
    }

    // 컬럼 이름 수정
    @Transactional
    public Area updateAreaName(Area area, AreaRequestDto areaRequestDto) {
        try {
            area.updateName(areaRequestDto);
            return area;
        } catch (Exception e) {
            throw new RuntimeException("에리아 이름 수정에 실패했습니다. 이유 : " + e.getMessage(), e);
        }
    }

    // 컬럼 삭제
    public void deleteAreaName(Area area) {
        try {
            areaRepository.delete(area);
        } catch (Exception e) {
            throw new RuntimeException("에리아 삭제에 실패했습니다. 이유 : " + e.getMessage(), e);
        }
    }

    // 컬럼 순서 이동 - 추후 작업

    public Area findArea(Long id) {
        return areaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 에리아입니다."));
    }
}
