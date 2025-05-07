package capstone.mju.backend.domain.board.controller;

import capstone.mju.backend.domain.board.dto.comment.req.CommentCreateRequest;
import capstone.mju.backend.domain.board.dto.comment.req.CommentUpdateRequest;
import capstone.mju.backend.domain.board.dto.comment.res.CommentResponse;
import capstone.mju.backend.domain.board.dto.comment.res.CommentTreeResponse;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
@Tag(name = "댓글 API")
public class CommentController {

    private final CommentService commentService;

    //댓글 생성
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


    //댓글 삭제
    @Operation(summary = "댓글 삭제", description = "본인의 댓글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "댓글이 존재하지 않음 또는 권한 없음")
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticatedUser @Parameter(hidden = true) User user,
            @PathVariable UUID commentId) {

        commentService.deleteComment(commentId, user);
        return ResponseEntity.noContent().build();
    }


    //댓글 수정
    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "본인의 댓글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "댓글이 존재하지 않음 또는 권한 없음")
    })
    public ResponseEntity<Void> updateComment(
            @AuthenticatedUser @Parameter(hidden = true) User user,
            @PathVariable UUID commentId,
            @RequestBody @Valid CommentUpdateRequest request) {

        commentService.updateComment(commentId, user, request);
        return ResponseEntity.ok().build();
    }

    //댓글 조회
    @Operation(summary = "댓글 목록 조회", description = "게시글 ID에 해당하는 모든 댓글을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않음")
    })
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByBoard(
            @PathVariable UUID boardId) {

        List<CommentResponse> comments = commentService.getCommentsByBoard(boardId);
        return ResponseEntity.ok(comments);
    }

    //댓글 + 대댓글 조회
    @Operation(summary = "댓글 + 대댓글 계층 조회", description = "게시글의 댓글 및 대댓글을 계층 구조로 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/board/{boardId}/comment")
    public ResponseEntity<List<CommentTreeResponse>> getCommentsTree(
            @PathVariable UUID boardId) {

        List<CommentTreeResponse> tree = commentService.getCommentsByBoardWithReplies(boardId);
        return ResponseEntity.ok(tree);
    }
}
