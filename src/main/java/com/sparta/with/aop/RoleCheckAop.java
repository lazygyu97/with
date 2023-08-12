package com.sparta.with.aop;

import com.sparta.with.entity.Comment;
import com.sparta.with.entity.User;
import com.sparta.with.security.UserDetailsImpl;
import java.util.concurrent.RejectedExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j(topic = "RoleCheckAop")
@Aspect
@Component
public class RoleCheckAop {

    @Pointcut("execution(* com.sparta.with.service.CommentService.updateComment(..))")
    private void updateComment() {
    }

    @Pointcut("execution(* com.sparta.with.service.CommentService.deleteComment(..))")
    private void deleteComment() {
    }

    @Around("updateComment() || deleteComment()")
    public Object executeCommentRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        Comment comment = (Comment) joinPoint.getArgs()[0];

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal().getClass() == UserDetailsImpl.class) {
            // 로그인 회원 정보
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            User loginUser = userDetails.getUser();

            // 댓글 작성자(comment.user) 와 요청자(user) 가 같은지 체크 (아니면 예외발생)
            if (!(comment.getAuthor().equals(loginUser))) {
                log.warn("[AOP] 작성자만 댓글을 수정/삭제 할 수 있습니다.");
                throw new RejectedExecutionException();
            }
        }

        // 핵심기능 수행
        return joinPoint.proceed();
    }
}
