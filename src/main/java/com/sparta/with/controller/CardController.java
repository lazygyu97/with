package com.sparta.with.controller;

import com.sparta.with.dto.ApiResponseDto;
import com.sparta.with.dto.CardRequestDto;
import com.sparta.with.dto.CardListResponseDto;
import com.sparta.with.dto.CardResponseDto;
import com.sparta.with.dto.CardUserRequestDto;
import com.sparta.with.dto.CardUsersResponseDto;
import com.sparta.with.entity.User;
import com.sparta.with.security.UserDetailsImpl;
import com.sparta.with.service.CardService;
import com.sparta.with.service.UserService;
import com.sun.jdi.request.DuplicateRequestException;
import java.util.concurrent.RejectedExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CardController {

    private final UserService userService;
    private final CardService cardService;

    // 전체 카드 목록 조회
    @GetMapping("/cards")
    public ResponseEntity<CardListResponseDto> getCards() {
        CardListResponseDto result = cardService.getCards();

        return ResponseEntity.ok().body(result);
    }

    // 카드 생성(제목만)
    @PostMapping("/cards")
    public ResponseEntity<CardResponseDto> createCard(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody CardRequestDto requestDto) {

        CardResponseDto result = cardService.createCard(requestDto, userDetails.getUser());

        return ResponseEntity.status(201).body(result);
    }

    // 카드 상세 페이지 조회
    @GetMapping("/cards/{id}")
    public ResponseEntity<CardResponseDto> getCard(@PathVariable Long id) {
        CardResponseDto result = cardService.getCard(id);

        return ResponseEntity.ok().body(result);
    }

    // 카드 내용 수정
    @PutMapping("/cards/{id}")
    public ResponseEntity<CardResponseDto> updateContent(@PathVariable Long id,
        @RequestBody CardRequestDto requestDto) {

        CardResponseDto result = cardService.updateContent(id, requestDto);
        return ResponseEntity.ok().body(result);
    }

    // 카드 컬러 수정
    @PutMapping("/cards/{id}/color")
    public ResponseEntity<CardResponseDto> updateColor(@PathVariable Long id,
        @RequestBody CardRequestDto requestDto) {

        CardResponseDto result = cardService.updateColor(id, requestDto);
        return ResponseEntity.ok().body(result);
    }

    // 카드 기간 수정
    @PutMapping("/cards/{id}/dates")
    public ResponseEntity<CardResponseDto> updateDates(@PathVariable Long id,
        @RequestBody CardRequestDto requestDto) {

        CardResponseDto result = cardService.updateDates(id, requestDto);
        return ResponseEntity.ok().body(result);
    }

    // 카드 이미지 수정
    @PutMapping("/cards/{id}/image")
    public ResponseEntity<CardResponseDto> updateImage(@PathVariable Long id,
        @RequestBody CardRequestDto requestDto) {

        CardResponseDto result = cardService.updateImage(id, requestDto);
        return ResponseEntity.ok().body(result);
    }

    // 카드에 협업자 목록 보여주기
    @GetMapping("/cards/{id}/collaborators")
    public ResponseEntity<CardUsersResponseDto> getCardUsers(@PathVariable Long id) {
        CardUsersResponseDto cardUser = cardService.getCardUsers(id);

        return ResponseEntity.ok().body(cardUser);
    }

    // 카드에 작업자 할당
    @PostMapping("/cards/{id}/collaborators")
    public ResponseEntity<ApiResponseDto> addCollaborator(@PathVariable Long id,
        @RequestBody CardUserRequestDto requestDto) {
        User collaborator = userService.findUserByUserid(requestDto.getUserId());

        try {
            cardService.addCollaborator(id, collaborator);
        } catch (DuplicateRequestException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.ok()
            .body(new ApiResponseDto("카드에 협업자가 등록되었습니다.", HttpStatus.OK.value()));
    }

    // 카드에 작업자 삭제
    @DeleteMapping("/cards/{id}/collaborators")
    public ResponseEntity<ApiResponseDto> deleteCollaborator(
        @RequestBody CardUserRequestDto requestDto, @PathVariable Long id) {
        User collaborator = userService.findUserByUserid(requestDto.getUserId());
        try {
            cardService.deleteCollaborator(id, collaborator);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(new ApiResponseDto("카드에 협업자가 삭제되었습니다.", HttpStatus.ACCEPTED.value()));
    }


    // 카드 삭제
    @DeleteMapping("/cards/{id}")
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id) {
        try {
            cardService.deleteCard(id);
            return ResponseEntity.ok().body(new ApiResponseDto("카드 삭제 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}