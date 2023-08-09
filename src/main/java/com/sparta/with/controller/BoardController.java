package com.sparta.with.controller;

import com.sparta.with.dto.ApiResponseDto;
import com.sparta.with.dto.BoardRequestDto;
import com.sparta.with.dto.BoardResponseDto;
import com.sparta.with.dto.BoardsResponseDto;
import com.sparta.with.dto.CollaboratorRequestDto;
import com.sparta.with.entity.Board;
import com.sparta.with.entity.BoardUser;
import com.sparta.with.entity.User;
import com.sparta.with.security.UserDetailsImpl;
import com.sparta.with.service.BoardService;
import com.sparta.with.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.concurrent.RejectedExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Board Example API", description = "칸반 보드 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {
    private final UserService userService;
    private final BoardService boardService;

    // 보드 생성
    @Operation(summary = "create board", description = "칸반 보드 생성")
    @PostMapping("/boards")
    public ResponseEntity<BoardResponseDto> createBoard(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody BoardRequestDto boardRequestDto) {
        try {
            if (userDetails == null) {
                throw new AuthenticationException("User not authenticated.") {
                };
            }

            BoardResponseDto result = boardService.createBoard(boardRequestDto, userDetails.getUser());

            String successMessage = "\nCreate Kanban board Success.";//삭제 가능
            System.out.println(successMessage);//삭제 가능
            return ResponseEntity.status(HttpStatus.CREATED).header("Message", successMessage).body(result);
        } catch (Exception e) {
            String errorMessage = "\nCreate Kanban board Failed. Reason: " + e.getMessage();
            System.err.println(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Message", errorMessage).build();
        }
    }

    // 전체 조회 (본인이 생성한 보드)
    @Operation(summary = "get owner's boards", description = "소유한 칸반 보드 전체 조회")
    @GetMapping("/boards")
    public ResponseEntity<BoardsResponseDto> getBoards() {
        BoardsResponseDto result = new BoardsResponseDto(boardService.getBoards());

        return ResponseEntity.ok().body(result);
    }

    // 단건 조회 (본인이 생성한 보드)
    @Operation(summary = "get owner's board by id", description = "소유한 칸반 보드 단건 조회")
    @GetMapping("/boards/{id}")
    public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable Long id) {
        BoardResponseDto result = boardService.getBoardById(id);

        return ResponseEntity.ok().body(result);
    }

    // 보드 이름 수정
    @Operation(summary = "update Board's Name", description = "칸반 보드 이름 수정")
    @PutMapping("/boards/{id}/names")
    public ResponseEntity<ApiResponseDto> updateBoardName(@PathVariable Long id, @RequestBody BoardRequestDto boardRequestDto) {
        Board board = boardService.findBoard(id);
        boardService.updateBoardName(board, boardRequestDto);

        return ResponseEntity.ok().body(new ApiResponseDto("칸반 보드의 이름이 수정되었습니다.", HttpStatus.OK.value()));
    }

    // 보드 배경색상 수정
    @Operation(summary = "update Board's Color", description = "칸반 보드 배경색상 수정")
    @PutMapping("/boards/{id}/colors")
    public ResponseEntity<ApiResponseDto> updateBoardColor(@PathVariable Long id, @RequestBody BoardRequestDto boardRequestDto) {
        Board board = boardService.findBoard(id);
        boardService.updateBoardColor(board, boardRequestDto);

        return ResponseEntity.ok().body(new ApiResponseDto("칸반 보드의 색상이 수정되었습니다.", HttpStatus.OK.value()));
    }

    // 보드 설명 수정
    @Operation(summary = "update Board's Info", description = "칸반 보드 설명 수정")
    @PutMapping("/boards/{id}/intros")
    public ResponseEntity<ApiResponseDto> updateBoardInfo(@PathVariable Long id, @RequestBody BoardRequestDto boardRequestDto) {
        Board board = boardService.findBoard(id);
        boardService.updateBoardInfo(board, boardRequestDto);

        return ResponseEntity.ok().body(new ApiResponseDto("칸반 보드의 색상이 수정되었습니다.", HttpStatus.OK.value()));
    }

    // 보드 삭제
    @Operation(summary = "delete Board", description = "칸반 보드 삭제")
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<ApiResponseDto> deleteBoard(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        try {
            Board board = boardService.findBoard(id);
            boardService.deleteBoard(board, userDetails.getUser());
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("칸반 보드는 작성자만 삭제 가능합니다.", HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.ok().body(new ApiResponseDto("게시글 삭제 성공", HttpStatus.OK.value()));
    }

    // 전체 조회 (협업 초대 받은 보드)
    @Operation(summary = "get collaborator's boards", description = "협업하고 있는 칸반 보드 전체 조회")
    @GetMapping("/boards/collaborators")
    public ResponseEntity<BoardsResponseDto> getCollaboratedBoards(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 수정: 현재 로그인한 사용자의 정보를 @AuthenticationPrincipal로 받아옴
        BoardsResponseDto result = new BoardsResponseDto(boardService.getCollaboratedBoards(userDetails));

        return ResponseEntity.ok().body(result);
    }

    // 단건 조회 (협업 초대 받은 보드)
    @Operation(summary = "get collaborator's board by id", description = "협업하고 있는 칸반 보드 단건 조회")
    @GetMapping("/boards/collaborators/{id}")
    public ResponseEntity<BoardResponseDto> getCollaboratedBoardById(@PathVariable Long id) {
        BoardResponseDto result = boardService.getCollaboratedBoardById(id);

        return ResponseEntity.ok().body(result);
    }

    // 내 칸반 보드에 협업자 초대
    // 허락받아야 초대 가능한 로직으로 변경하기 - 추후 작업
    @Operation(summary = "assign Collaborator to Board", description = "칸반 보드에 협업자 할당")
    @PostMapping("/boards/collaborators/{boardId}")
    public ResponseEntity<ApiResponseDto> addCollaborator(@PathVariable Long boardId,  @RequestBody CollaboratorRequestDto collaboratorRequestDto) {
        Board board = boardService.findBoard(boardId);

        try {
            User collaborator = userService.findUserByUsername(collaboratorRequestDto.getUsername());
            BoardUser boardUser = boardService.findCollaborator(collaborator.getId());
            boardService.addCollaborator(board, collaborator);

            if (collaborator.equals(board.getAuthor())) {
                return ResponseEntity.badRequest().body(new ApiResponseDto("입력하신 아이디는 칸반 보드의 오너입니다.", HttpStatus.BAD_REQUEST.value()));
            }

            if (boardUser.getBoard().equals(board)) {
                return ResponseEntity.badRequest().body(new ApiResponseDto("해당 칸반 보드에 이미 등록된 협업자입니다.", HttpStatus.BAD_REQUEST.value()));
            }

        } catch (IllegalArgumentException e) {
            log.error("\nERROR : add Collaborator to the board");
            return ResponseEntity.badRequest()
                .body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.ok().body(new ApiResponseDto("칸반 보드에 협업자가 등록되었습니다.", HttpStatus.OK.value()));
    }

    // 내 칸반 보드의 협업자 명단 수정
    @Operation(summary = "update Collaborators of Board", description = "칸반 보드의 협업자 명단 수정")
    @PutMapping("/boards/collaborators/{boardId}/{boardUserId}")
    public ResponseEntity<ApiResponseDto> updateCollaborator(@PathVariable Long boardId, @PathVariable Long boardUserId, @RequestBody CollaboratorRequestDto collaboratorRequestDto
    ) {
        User newCollaborator = userService.findUserByUsername(collaboratorRequestDto.getUsername());
        Board board = boardService.findBoard(boardId);
        BoardUser boardUser = boardService.findCollaborator(boardUserId);

        if (!boardUser.getBoard().equals(board)) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("해당 칸반 보드의 협업자가 아닙니다.", HttpStatus.BAD_REQUEST.value()));
        }

        try {
            boardService.updateCollaborator(boardUser, newCollaborator);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.ok().body(new ApiResponseDto("칸반 보드의 협업자가 수정되었습니다.", HttpStatus.OK.value()));
    }

}
