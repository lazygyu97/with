package com.sparta.with.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.sparta.with.dto.BoardRequestDto;
import com.sparta.with.dto.BoardResponseDto;
import com.sparta.with.dto.BoardsResponseDto;
import com.sparta.with.entity.Board;
import com.sparta.with.entity.BoardUser;
import com.sparta.with.entity.User;
import com.sparta.with.repository.BoardRepository;
import com.sparta.with.repository.BoardUserRepository;
import com.sparta.with.security.UserDetailsImpl;
import com.sun.jdi.request.DuplicateRequestException;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
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
        try {
            Board board = boardRequestDto.toEntity(author);
            boardRepository.save(board);
            return BoardResponseDto.of(board);
        } catch (Exception e) {
            throw new RuntimeException("칸반 보드 생성에 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 보드 전체 조회 (본인이 생성한 보드)
    @Transactional(readOnly = true)
    public BoardsResponseDto getAllBoards(User author) {
        try {
            List<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc(author);
            return BoardsResponseDto.of(boards);
        } catch (Exception e) {
            throw new RuntimeException("모든 칸반 보드 조회를 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 보드 단건 조회 (본인이 생성한 보드)
    @Transactional(readOnly = true)
    public BoardResponseDto getBoardById(User author, Long id) {
        try {
            Board board = findBoard(author, id);
            return BoardResponseDto.of(board);
        } catch (Exception e) {
            throw new RuntimeException("칸반 보드 조회를 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 보드 삭제 (본인이 생성한 보드)
    @Transactional
    public void deleteBoard(Board board, User author) {
        try {
            if (!board.getAuthor().equals(author)) {
                throw new RejectedExecutionException("칸반 보드는 작성자만 삭제 가능합니다.");
            }
            boardRepository.delete(board);
        } catch (Exception e) {
            throw new RuntimeException("칸반 보드 삭제에 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 보드 이름 수정
    @Transactional
    public Board updateBoardName(Board board, BoardRequestDto boardRequestDto) {
        try {
            board.updateName(boardRequestDto);
            return board;
        } catch (Exception e) {
            throw new RuntimeException("칸반 보드 이름 수정에 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 보드 배경색상 수정
    @Transactional
    public Board updateBoardColor(Board board, BoardRequestDto boardRequestDto) {
        try {
            board.updateColor(boardRequestDto);
            return board;
        } catch (Exception e) {
            throw new RuntimeException("칸반 보드 배경색상 수정에 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 보드 설명 수정
    @Transactional
    public Board updateBoardInfo(Board board, BoardRequestDto boardRequestDto) {
        try {
            board.updateInfo(boardRequestDto);
            return board;
        } catch (Exception e) {
            throw new RuntimeException("칸반 보드 설명 수정에 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 보드 전체 조회 (협업 초대 받은 보드)
    @Transactional(readOnly = true)
    public BoardsResponseDto getCollaboratedBoards(UserDetailsImpl userDetails) {
        try {
            List<BoardUser> boardUsers = boardUserRepository.findByCollaborator(userDetails.getUser());

            List<Board> collaboratedBoards = boardUsers.stream()
                .map(BoardUser::getBoard)
                .collect(Collectors.toList());

            List<BoardResponseDto> boardResponseDtos = collaboratedBoards.stream()
                .map(BoardResponseDto::of)
                .collect(Collectors.toList());

            return BoardsResponseDto.of(collaboratedBoards);
        } catch (Exception e) {
            throw new RuntimeException("협업 중인 칸반 보드 조회를 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 보드 단건 조회 (협업 초대 받은 보드)
    @Transactional(readOnly = true)
    public BoardResponseDto getCollaboratedBoardById(User author, Long id) {
        try {
            Board board = findBoard(author, id);
            return BoardResponseDto.of(board);
        } catch (Exception e) {
            throw new RuntimeException("협업 중인 칸반 보드 조회를 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 보드 협업자 등록
    // 허락받아야 초대 가능한 로직으로 변경하기 - 추후 작업
    @Transactional
    public void addCollaborator(Board board, User collaborator) {
        try {
            if (board.getBoardUsers().stream().anyMatch(boardUser -> boardUser.getCollaborator().equals(collaborator))) {
                throw new IllegalArgumentException("칸반 보드에 이미 협업자로 등록된 사용자입니다.");
            }

            if (collaborator.equals(board.getAuthor())) {
                throw new DuplicateRequestException("입력하신 아이디는 칸반 보드의 오너입니다.");
            }

            BoardUser boardUser = new BoardUser(collaborator, board);
            board.getBoardUsers().add(boardUser);
        } catch (Exception e) {
            throw new RuntimeException("협업자 등록에 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 보드 협업자 명단 수정
    @Transactional
    public void updateCollaborator(Board board, BoardUser boardUser, User newCollaborator) {
        try {
            if (!boardUser.getBoard().equals(board)) {
                throw new IllegalArgumentException("해당 칸반 보드의 협업자가 아닙니다.");
            }
            //명단에 내가 이미 있어서 또 초대할 필요가 없는지 확인
            if (boardUser.getBoard().getBoardUsers().stream().anyMatch(user -> user.getCollaborator().equals(newCollaborator))) {
                throw new DuplicateRequestException("이미 협업자로 할당된 사용자입니다.");
            }
            boardUser.updateCollaborator(newCollaborator);
        } catch (Exception e) {
            throw new RuntimeException("협업자 수정에 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    // 보드 협업자 삭제
    public void deleteCollaborator(Board board, BoardUser boardUser) {
        try {
            if (!boardUser.getBoard().equals(board)) {
                throw new IllegalArgumentException("해당 칸반 보드의 협업자가 아닙니다.");
            }
            boardUserRepository.delete(boardUser);
        } catch (Exception e) {
            throw new RuntimeException("협업자 삭제에 실패했습니다. 이유: " + e.getMessage(), e);
        }
    }

    public Board findBoard(User author, Long id) {
        if (author==null) {
            throw new AuthenticationServiceException("로그인 후 보드를 조회할 수 있습니다.");
        }
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 칸반 보드입니다."));
    }

    public BoardUser findCollaborator(Long boardUserId) {
        return boardUserRepository.findById(boardUserId)
            .orElseThrow(() -> new NotFoundException("해당 칸반 보드에 존재하지 않는 협업자입니다."));
    }
}
