package com.sparta.with.service;

import com.sparta.with.dto.CardRequestDto;
import com.sparta.with.dto.CardListResponseDto;
import com.sparta.with.dto.CardResponseDto;
import com.sparta.with.entity.Area;
import com.sparta.with.entity.Card;
import com.sparta.with.entity.CardUser;
import com.sparta.with.entity.User;
import com.sparta.with.repository.AreaRepository;
import com.sparta.with.repository.CardRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {
  private final CardRepository cardRepository;
  private final AreaRepository areaRepository;

  // 전체 카드 목록 조회
  public CardListResponseDto getCards() {
    List<CardResponseDto> cardList = cardRepository.findAll().stream()
        .map(CardResponseDto::new)
        .collect(Collectors.toList());

    return new CardListResponseDto(cardList);
  }

  // 카드 생성(제목만)
  public CardResponseDto createCard(CardRequestDto requestDto, User author) {
    // 선택한 area 에 카드 등록
    Area area = areaRepository.findById(requestDto.getAreaId()).orElseThrow(
        () -> new IllegalArgumentException("해당 컬럼이 존재하지 않습니다.")
    );

    Card card = new Card(requestDto);
    card.setArea(area);
    card.setUser(author);

    cardRepository.save(card);

    return new CardResponseDto(card);
  }

  // 카드 상세 페이지 조회
  public CardResponseDto getCard(Long id) {
    Card post = findCard(id);

    return new CardResponseDto(post);
  }

  // 카드 내용 수정
  @Transactional
  public CardResponseDto updateContent(Long id, CardRequestDto requestDto) {
    Card card = findCard(id);
    card.setContent(requestDto.getContent());

    return new CardResponseDto(card);
  }

  // 카드 기간 수정
  @Transactional
  public CardResponseDto updateDates(Long id, CardRequestDto requestDto) {
    Card card = findCard(id);
    card.setStartDate(requestDto.getStartDate());
    card.setDueDate(requestDto.getDueDate());

    return new CardResponseDto(card);
  }

  // 카드 컬러 수정
  @Transactional
  public CardResponseDto updateColor(Long id, CardRequestDto requestDto) {
    Card card = findCard(id);
    card.setColor(requestDto.getContent());

    return new CardResponseDto(card);
  }

  // 카드 이미지 수정
  @Transactional
  public CardResponseDto updateImage(Long id, CardRequestDto requestDto) {
    Card card = findCard(id);
    card.setImage(requestDto.getContent());

    return new CardResponseDto(card);
  }

  // 카드에 작업자 할당
  @Transactional
  public void addCollaborator(Long id, User collaborator) {
    Card card = findCard(id);
    CardUser cardUser = new CardUser(collaborator, card);
    card.getCardUsers().add(cardUser);
  }

  // 카드에 작업자 변경

  // 카드 삭제
  public void deleteCard(Long id) {
    Card card = findCard(id);

    cardRepository.delete(card);
  }

  public Card findCard(long id) {
    return cardRepository.findById(id).orElseThrow(() ->
        new IllegalArgumentException("선택한 카드는 존재하지 않습니다.")
    );
  }
}
