package com.sparta.with.controller;

import com.sparta.with.dto.ApiResponseDto;
import com.sparta.with.dto.CheckListRequestDto;
import com.sparta.with.dto.CheckListResponseDto;
import com.sparta.with.service.CheckListService;
import java.util.concurrent.RejectedExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class CheckListController {

    private final CheckListService checkListService;

    // 체크리스트 작성
    @PostMapping("/checklists")
    public ResponseEntity<CheckListResponseDto> createComment(
        @RequestBody CheckListRequestDto requestDto) {
        CheckListResponseDto result = checkListService.createCheckList(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

//    // 체크리스트 수정
//    // 카드 내용 수정
//    @PutMapping("/checklists/{id}")
//    public ResponseEntity<CheckListResponseDto> updateContent(@PathVariable Long id,
//        @RequestBody CheckListRequestDto requestDto) {
//        CheckListResponseDto result = checkListService.updateCheckList(id, requestDto);
//
//        return ResponseEntity.ok().body(result);
//    }
    // 체크리스트 수정
    // 카드 내용 수정
    @PutMapping("/checklists/{id}")
    public ResponseEntity<CheckListResponseDto> checkContent(@PathVariable Long id) {
        CheckListResponseDto result = checkListService.checkContent(id);
        return ResponseEntity.ok().body(result);
    }

    // 체크리스트 삭제
    @DeleteMapping("/checklists/{id}")
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id) {
        try {
            checkListService.deleteCheckList(id);
            return ResponseEntity.ok().body(new ApiResponseDto("체크리스트 삭제 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
