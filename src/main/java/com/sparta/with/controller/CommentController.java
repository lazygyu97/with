package com.sparta.with.controller;

import com.sparta.with.dto.ApiResponseDto;
import com.sparta.with.dto.CommentRequestDto;
import com.sparta.with.dto.CommentResponseDto;
import com.sparta.with.security.UserDetailsImpl;
import com.sparta.with.service.CommentService;
import java.util.concurrent.RejectedExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class CommentController {

  private final CommentService commentService;

  // 댓글 작성
  @PostMapping("/comments")
  public ResponseEntity<CommentResponseDto> createComment(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody CommentRequestDto requestDto) {
    CommentResponseDto result = commentService.createComment(requestDto, userDetails.getUser());

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  // 댓글 수정
  @PutMapping("/comments/{id}")
  public ResponseEntity<ApiResponseDto> updateComment(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id,
      @RequestBody CommentRequestDto requestDto) {
    try {
      CommentResponseDto result = commentService.updateComment(id, requestDto,
          userDetails.getUser());
      return ResponseEntity.ok().body(result);
    } catch (RejectedExecutionException e) {
      return ResponseEntity.badRequest()
          .body(new ApiResponseDto("작성자만 수정 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
    }
  }

  // 댓글 삭제
  @DeleteMapping("/comments/{id}")
  public ResponseEntity<ApiResponseDto> deleteComment(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
    try {
      commentService.deleteComment(id, userDetails.getUser());
      return ResponseEntity.ok().body(new ApiResponseDto("댓글 삭제 성공", HttpStatus.OK.value()));
    } catch (RejectedExecutionException e) {
      return ResponseEntity.badRequest()
          .body(new ApiResponseDto("작성자만 삭제 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
    }
  }

}