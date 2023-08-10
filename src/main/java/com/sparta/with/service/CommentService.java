package com.sparta.with.service;

import com.sparta.with.dto.CommentRequestDto;
import com.sparta.with.dto.CommentResponseDto;
import com.sparta.with.entity.Card;
import com.sparta.with.entity.Comment;
import com.sparta.with.entity.User;
import com.sparta.with.repository.CardRepository;
import com.sparta.with.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final CardRepository cardRepository;

  // 댓글 작성
  public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
    // 선택한 카드에 댓글 등록
    Card card = cardRepository.findById(requestDto.getCardId()).orElseThrow(
        () -> new IllegalArgumentException("해당 카드가 존재하지 않습니다.")
    );

    Comment comment = requestDto.toEntity(card, user);
    var savedComment = commentRepository.save(comment);

    return CommentResponseDto.of(savedComment);
  }

  // 댓글 수정
  @Transactional
  public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto) {
    Comment comment = findComment(id);

    comment.updateContent(requestDto.getContent());

    return CommentResponseDto.of(comment);
  }

  @Transactional
  // 댓글 삭제
  public void deleteComment(Long id) {
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
