package com.sparta.with.service;

import com.sparta.with.dto.CheckListRequestDto;
import com.sparta.with.dto.CheckListResponseDto;
import com.sparta.with.entity.Card;
import com.sparta.with.entity.CheckList;
import com.sparta.with.repository.CardRepository;
import com.sparta.with.repository.CheckListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckListService {

    private final CheckListRepository checkListRepository;
    private final CardRepository cardRepository;

    // 체크리스트 생성
    public CheckListResponseDto createCheckList(CheckListRequestDto requestDto) {
        // 선택한 카드에 체크리스트 등록
        Card card = cardRepository.findById(requestDto.getCardId()).orElseThrow(
            () -> new IllegalArgumentException("해당 카드가 존재하지 않습니다.")
        );

        CheckList checkList = requestDto.toEntity(card);
        var savedCheckList = checkListRepository.save(checkList);

        return CheckListResponseDto.of(savedCheckList);
    }

    // 체크리스트 수정
    @Transactional
    public CheckListResponseDto updateCheckList(Long id, CheckListRequestDto requestDto) {
        CheckList checkList = findCheckList(id);

        checkList.updateContent(requestDto.getContent());

        return CheckListResponseDto.of(checkList);
    }

    // 체크리스트 삭제
    @Transactional
    public void deleteCheckList(Long id) {
        CheckList checkList = findCheckList(id);

        checkListRepository.delete(checkList);
    }
    @Transactional
    public CheckListResponseDto checkContent(Long id) {
        CheckList checkList = findCheckList(id);
        if(checkList.isChecked()){
            checkList.unCheck();
        }else {
            checkList.check();
        }
        return CheckListResponseDto.of(checkList);
    }
    // 해당 체크리스트가 DB에 존재하는지 확인
    private CheckList findCheckList(Long id) {

        return checkListRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("해당 체크리스트가 존재하지 않습니다.")
        );
    }
}