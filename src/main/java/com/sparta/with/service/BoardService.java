package com.sparta.with.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.sparta.with.dto.BoardRequestDto;
import com.sparta.with.dto.BoardResponseDto;
import com.sparta.with.entity.Board;
import com.sparta.with.entity.BoardUser;
import com.sparta.with.entity.User;
import com.sparta.with.repository.BoardRepository;
import com.sparta.with.repository.BoardUserRepository;
import com.sparta.with.security.UserDetailsImpl;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardUserRepository boardUserRepository;

    // 보드 생성
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, User author) {
        Board board = boardRequestDto.toEntity(author);

        boardRepository.save(board);
        return BoardResponseDto.of(board);
    }

    // 소유한 보드 전체 조회
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards() {
        return boardRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(BoardResponseDto::of)
            .collect(Collectors.toList());
    }

    // 소유한 보드 단건 조회
    @Transactional(readOnly = true)
    public BoardResponseDto getBoardById(Long id) {
        Board board = findBoard(id);
        return BoardResponseDto.of(board);
    }

    // 보드 이름 수정
    public Board updateBoardName(Board board, BoardRequestDto boardRequestDto) {
        board.updateName(boardRequestDto);
        return board;
    }

    // 보드 배경색상 수정
    public Board updateBoardColor(Board board, BoardRequestDto boardRequestDto) {
        board.updateColor(boardRequestDto);
        return board;
    }

    // 보드 설명 수정 - 추후 작업
    public Board updateBoardInfo(Board board, BoardRequestDto boardRequestDto) {
        board.updateInfo(boardRequestDto);
        return board;
    }

    // 보드 삭제
    @Transactional
    public void deleteBoard(Board board, User author) {
        if(!board.getAuthor().equals(author)) {
            throw new RejectedExecutionException();
        }
        boardRepository.delete(board);
    }

    // 초대 받은 보드 전체 조회
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getCollaboratedBoards(UserDetailsImpl userDetails) {
        List<BoardUser> boardUsers = boardUserRepository.findByCollaborator(userDetails.getUser());

        List<Board> collaboratedBoards = boardUsers.stream()
            .map(BoardUser::getBoard)
            .collect(Collectors.toList());

        return collaboratedBoards.stream().map(BoardResponseDto::of).collect(Collectors.toList());
    }

    // 초대 받은 보드 단건 조회
    @Transactional(readOnly = true)
    public BoardResponseDto getCollaboratedBoardById(Long id) {
        Board board = findBoard(id);
        return BoardResponseDto.of(board);
    }

    // 내 칸반 보드에 협업자 초대
    @Transactional
    public void addCollaborator(Board board, User collaborator) {
        if (board.getBoardUsers().stream().anyMatch(boardUser -> boardUser.getCollaborator().equals(collaborator))) {
            throw new IllegalArgumentException("칸반 보드에 이미 협업자로 등록된 사용자입니다.");
        }

        BoardUser boardUser = new BoardUser(collaborator, board);
        board.getBoardUsers().add(boardUser);
    }

    // 내 칸반 보드의 협업자 명단 수정
    @Transactional
    public void updateCollaborator(BoardUser boardUser, User newCollaborator) {

        //명단에 내가 이미 있어서 또 초대할 필요가 없는지 확인
        if (boardUser.getBoard().getBoardUsers().stream().anyMatch(user -> user.getCollaborator().equals(newCollaborator))) {
            throw new IllegalArgumentException("이미 협업자로 할당된 사용자입니다.");
        }

        boardUser.updateCollaborator(newCollaborator);
    }


    public Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 칸반 보드입니다."));
    }

    public BoardUser findCollaborator(Long boardUserId) {
        return boardUserRepository.findById(boardUserId)
            .orElseThrow(() -> new NotFoundException("해당 칸반 보드에 존재하지 않는 협업자입니다."));
    }
}
