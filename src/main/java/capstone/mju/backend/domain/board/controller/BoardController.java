package capstone.mju.backend.domain.board.controller;

import capstone.mju.backend.domain.board.dto.req.BoardCreateRequest;
import capstone.mju.backend.domain.board.service.BoardService;
import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.UnauthorizedException;
import capstone.mju.backend.domain.user.domain.User;
import capstone.mju.backend.global.auth.AuthenticatedUser;
import capstone.mju.backend.global.s3.S3ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "게시글 API", description = "게시글 관련 API")
public class BoardController {
    private final BoardService boardService; //board service
    private final S3ImageService s3ImageService; //사진 저장

    // 게시글 작성
    @Operation(summary = "게시글 작성", description = "이미지 업로드 포함하여 게시글을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "게시글 생성 성공")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createBoard(
            @AuthenticatedUser User user,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestPart("request") @Valid BoardCreateRequest request) {
        if (user == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER, "로그인 필요");
        }
        //이미지 받기 / 없으면 null
        String imageUrl = (image != null && !image.isEmpty()) ? s3ImageService.upload(image) : null;

        // 게시글 생성 요청에 업로드된 이미지 URL을 추가
        UUID boardId = boardService.createBoard(user, request, imageUrl);

        return ResponseEntity.status(HttpStatus.CREATED).body("BoardID: "+boardId);
    }


    //게시글 삭제
    @Operation(
            summary = "게시글 삭제",
            description = "UUID를 기반으로 본인이 작성한 게시글을 삭제합니다. S3에 등록된 이미지가 있다면 함께 삭제됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글 또는 권한 없음"),
            @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자")
    })
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @AuthenticatedUser @Parameter(hidden = true) User user, // Swagger에 사용자 정보 노출 안 함
            @PathVariable @Parameter(description = "삭제할 게시글의 UUID", example = "ec54a7b6-2a47-4c77-b294-72ea4dcb6584") UUID boardId) {

        boardService.deleteBoard(boardId, user);
        return ResponseEntity.noContent().build();
    }

}
