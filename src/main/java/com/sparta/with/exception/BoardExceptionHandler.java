package com.sparta.with.exception;

import com.sparta.with.dto.ApiResponseDto;
import java.util.concurrent.RejectedExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;

@Slf4j
@RestControllerAdvice
public class BoardExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorMessage = "서버에서 오류가 발생했습니다.";

        if (e instanceof AuthenticationServiceException) {
            // 로그인을 실패한 경우
            // 또는 로그인하지 않는 사용자가 회원의 기능을 사용하려 할 때
            status = HttpStatus.UNAUTHORIZED;
            errorMessage = "로그인 후 사용해주세요. 이유: " + e.getMessage();
        } else if (e instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
            errorMessage = "요청하신 것을 찾을 수 없습니다. 이유: " + e.getMessage();
        } else if (e instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
            errorMessage = "요청에 문제가 있습니다. 이유: " + e.getMessage();
        }

        log.error(errorMessage, e);
        return ResponseEntity.status(status)
            .body(new ApiResponseDto(errorMessage, status.value()));
    }
}
