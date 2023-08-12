package com.sparta.with.service;

import com.sparta.with.dto.CommentRequestDto;
import com.sparta.with.dto.CommentResponseDto;
import com.sparta.with.entity.Card;
import com.sparta.with.entity.Comment;
import com.sparta.with.entity.User;
import com.sparta.with.repository.CardRepository;
import com.sparta.with.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final ApplicationEventPublisher eventPublisher;

    // 댓글 작성
    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        // 선택한 카드에 댓글 등록
        Card card = cardRepository.findById(requestDto.getCardId()).orElseThrow(
            () -> new IllegalArgumentException("해당 카드가 존재하지 않습니다.")
        );

        Comment comment = requestDto.toEntity(card, user);
        var savedComment = commentRepository.save(comment);

        // 댓글 작성될 때마다 알림가게 하기
        savedComment.updateAlarm(eventPublisher);
        log.info("CommentService create comment success");

        return CommentResponseDto.of(savedComment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, User user) {
        Comment comment = findComment(id);

        comment.updateContent(requestDto.getContent());

        // 댓글 수정될 때마다 알림가게 하기
        comment.updateAlarm(eventPublisher);
        log.info("CommentService update comment success");

        return CommentResponseDto.of(comment);
    }

    @Transactional
    // 댓글 삭제
    public void deleteComment(Long id, User user) {
        Comment comment = findComment(id);

        commentRepository.delete(comment);
    }

    // 해당 카드가 DB에 존재하는지 확인
    private Comment findComment(Long id) {

        return commentRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
    }
}
