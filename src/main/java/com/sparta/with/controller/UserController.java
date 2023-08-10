package com.sparta.with.controller;

import com.sparta.with.dto.ApiResponseDto;
import com.sparta.with.dto.EmailRequestDto;
import com.sparta.with.dto.EmailVerificationRequestDto;
import com.sparta.with.dto.SignupRequestDto;
import com.sparta.with.security.UserDetailsImpl;
import com.sparta.with.service.EmailService;
import com.sparta.with.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name="User Example API", description = "사용자와 관련된 API 예제입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @Operation(summary = "sign up", description = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto> signUp(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {

        // Validation 예외처리 : 아이디와 패스워드가 pattern에 맞게 입력 되었는지 확인.
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ApiResponseDto("입력하신 정보가 회원가입 요건에 맞지 않습니다.", HttpStatus.BAD_REQUEST.value()));
        }

        // Validation 예외 처리후 서비스로 전달.
        userService.signUp(requestDto);

        return ResponseEntity.status(201).body(new ApiResponseDto("회원가입 성공", HttpStatus.CREATED.value()));
    }

    @PostMapping("/login/mail")
    public ResponseEntity mailSend(@RequestBody EmailRequestDto requestDto) throws Exception {
        return ResponseEntity.status(201).body(emailService.sendSimpleMessage(requestDto.getEmail()));
    }

    @GetMapping("/login/mail")
    public ResponseEntity mailVerification(@RequestBody EmailVerificationRequestDto requestDto){
        emailService.mailVerification(requestDto);
        return ResponseEntity.ok().body("이메일이 인증되었습니다.");
    }
    @GetMapping("/logout")
    public ResponseEntity logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request){
        userService.logout(userDetails.getUser(), request);
        return ResponseEntity.ok().body("로그아웃 완료");
    }

    @GetMapping
    public ResponseEntity getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok().body(userService.getUserInfo(userDetails.getUser()));
    }
}
