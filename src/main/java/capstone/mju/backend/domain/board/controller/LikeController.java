package capstone.mju.backend.domain.board.controller;

import capstone.mju.backend.domain.board.dto.like.res.LikedBoardResponse;
import capstone.mju.backend.domain.board.service.LikeService;
import capstone.mju.backend.domain.user.domain.User;
import capstone.mju.backend.global.auth.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
@Tag(name = "좋아요 API")
public final class LikeController {

    private final LikeService likeService;

    //좋아요 기능
    @Operation(summary = "게시글 좋아요", description = "특정 게시글에 좋아요를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "좋아요 등록 성공")
    @PostMapping("/{boardId}")
    public ResponseEntity<Void> likeBoard(
            @AuthenticatedUser @Parameter(hidden = true) User user,
            @PathVariable UUID boardId) {

        likeService.likeBoard(boardId, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //좋아요 취소 기능
    @Operation(summary = "게시글 좋아요 취소", description = "특정 게시글의 좋아요를 취소합니다.")
    @ApiResponse(responseCode = "204", description = "좋아요 취소 성공")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> unlikeBoard(
            @AuthenticatedUser @Parameter(hidden = true) User user,
            @PathVariable UUID boardId) {

        likeService.unlikeBoard(boardId, user);
        return ResponseEntity.noContent().build();
    }

    //내가 좋아요 누른 항목
    @Operation(summary = "좋아요한 게시글 목록 조회", description = "Slice 기반으로 로그인한 사용자가 좋아요한 게시글을 10개씩 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/my")
    public ResponseEntity<Slice<LikedBoardResponse>> getMyLikedBoards(
            @AuthenticatedUser @Parameter(hidden = true) User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Slice<LikedBoardResponse> result = likeService.getLikedBoards(user, page, size);
        return ResponseEntity.ok(result);
    }

}
