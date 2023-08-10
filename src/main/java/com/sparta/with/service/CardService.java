package com.sparta.with.service;

import com.sparta.with.dto.CardRequestDto;
import com.sparta.with.dto.CardListResponseDto;
import com.sparta.with.dto.CardResponseDto;
import com.sparta.with.dto.CardUsersResponseDto;
import com.sparta.with.entity.Area;
import com.sparta.with.entity.Card;
import com.sparta.with.entity.CardUser;
import com.sparta.with.entity.User;
import com.sparta.with.repository.AreaRepository;
import com.sparta.with.repository.CardRepository;
import com.sparta.with.repository.CardUserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

  private final CardRepository cardRepository;
  private final AreaRepository areaRepository;
  private final CardUserRepository cardUserRepository;

  // 전체 카드 목록 조회
  public CardListResponseDto getCards() {
    List<Card> cardList = cardRepository.findAll();

    return CardListResponseDto.of(cardList);

  }

  // 카드 생성(제목만)
  public CardResponseDto createCard(CardRequestDto requestDto, User author) {
    // 선택한 area 에 카드 등록
    Area area = areaRepository.findById(requestDto.getAreaId()).orElseThrow(
        () -> new IllegalArgumentException("해당 컬럼이 존재하지 않습니다.")
    );

    Card card = requestDto.toEntity(area);
    card.updateUser(author);

    cardRepository.save(card);

    return CardResponseDto.of(card);
  }

  // 카드 상세 페이지 조회
  public CardResponseDto getCard(Long id) {
    Card card = findCard(id);

    return CardResponseDto.of(card);
  }

  // 카드 내용 수정
  @Transactional
  public CardResponseDto updateContent(Long id, CardRequestDto requestDto) {
    Card card = findCard(id);
    card.updateContent(requestDto.getContent());

    return CardResponseDto.of(card);
  }

  // 카드 기간 수정
  @Transactional
  public CardResponseDto updateDates(Long id, CardRequestDto requestDto) {
    Card card = findCard(id);
    card.updateStartDate(requestDto.getStartDate());
    card.updateDueDate(requestDto.getDueDate());

    return CardResponseDto.of(card);
  }

  // 카드 컬러 수정
  @Transactional
  public CardResponseDto updateColor(Long id, CardRequestDto requestDto) {
    Card card = findCard(id);
    card.updateColor(requestDto.getContent());

    return CardResponseDto.of(card);
  }

  // 카드 이미지 수정
  @Transactional
  public CardResponseDto updateImage(Long id, CardRequestDto requestDto) {
    Card card = findCard(id);
    card.updateImage(requestDto.getContent());

    return CardResponseDto.of(card);
  }

  // 카드에 협업자 목록 조회
  @Transactional(readOnly = true)
  public CardUsersResponseDto getCardUsers(Long id) {
    List<CardUser> cardUserList = cardUserRepository.findByCard_Id(id);

    return CardUsersResponseDto.of(cardUserList);
  }

  // 카드에 협업자 할당
  @Transactional
  public void addCollaborator(Long id, User collaborator) {
    Card card = findCard(id);

    if (cardUserRepository.existsByCollaboratorAndCard(collaborator, card)) {
      throw new DuplicateRequestException("이미 등록된 작업자입니다.");
    } else {
      CardUser cardUser = new CardUser(collaborator, card);
      cardUserRepository.save(cardUser);
    }
  }

  // 카드에 협업자 삭제
  @Transactional
  public void deleteCollaborator(Long id, User collaborator) {
    Card card = findCard(id);
    Optional<CardUser> cardUserOptional = cardUserRepository.findByCollaboratorAndCard(collaborator,
        card);

    if (cardUserOptional.isPresent()) {
      cardUserRepository.delete(cardUserOptional.get());
    } else {
      throw new IllegalArgumentException("해당 카드에 삭제할 작업자가 없습니다.");
    }
  }

  // 카드 삭제
  public void deleteCard(Long id) {
    Card card = findCard(id);

    cardRepository.delete(card);
  }

  private Card findCard(long id) {
    return cardRepository.findById(id).orElseThrow(() ->
        new IllegalArgumentException("선택한 카드는 존재하지 않습니다.")
    );
  }
}
