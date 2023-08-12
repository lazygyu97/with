package com.sparta.with.service;

import com.sparta.with.dto.AlarmResponseDto;
import com.sparta.with.entity.Alarm;
import com.sparta.with.entity.Comment;
import com.sparta.with.event.AlarmEvent;
import com.sparta.with.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;

    @TransactionalEventListener // 트랜잭션 이벤트 발생시 실행
    @Async // 비동기 실행
    public void alertCreatedComment(AlarmEvent event, Comment comment) {
        Alarm alarm = (Alarm) event.getSource();
        String commentWriter = alarm.getCommentWriter().equals(alarm.getCardWriter())? "카드 작성자" : comment.getAuthor().toString();

        AlarmResponseDto.of(commentWriter, comment.getContent(), comment.getCard().getAuthor(), comment.getCard().getTitle(), "댓글 작성 알림", );

        //log.info("알림 : 우리들의 협업 도구 With에서 {}님에게 알림 '{}'을 보냅니다.", commentWriter);
}
