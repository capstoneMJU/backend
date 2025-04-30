package capstone.mju.backend.domain.board.controller;

import capstone.mju.backend.domain.board.service.LikeService;
import capstone.mju.backend.domain.user.domain.User;
import capstone.mju.backend.global.auth.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
@Tag(name = "좋아요 API")
public final class LikeController {

    private final LikeService likeService;

    @Operation(summary = "게시글 좋아요", description = "특정 게시글에 좋아요를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "좋아요 등록 성공")
    @PostMapping("/{boardId}")
    public ResponseEntity<Void> likeBoard(
            @AuthenticatedUser @Parameter(hidden = true) User user,
            @PathVariable UUID boardId) {

        likeService.likeBoard(boardId, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
