package capstone.mju.backend.domain.board.controller;

import capstone.mju.backend.domain.board.dto.comment.req.CommentCreateRequest;
import capstone.mju.backend.domain.board.service.CommentService;
import capstone.mju.backend.domain.user.domain.User;
import capstone.mju.backend.global.auth.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
@Tag(name = "댓글 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "게시글에 댓글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "댓글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않음")
    })
    @PostMapping("/{boardId}")
    public ResponseEntity<String> createComment(
            @AuthenticatedUser @Parameter(hidden = true) User user,
            @PathVariable UUID boardId,
            @RequestBody @Valid CommentCreateRequest request) {

        UUID commentId = commentService.createComment(boardId, user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("CommentID: " + commentId);
    }
}
