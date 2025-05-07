package capstone.mju.backend.domain.board.controller;

import capstone.mju.backend.domain.board.dto.board.req.BoardCreateRequest;
import capstone.mju.backend.domain.board.dto.board.req.BoardUpdateRequest;
import capstone.mju.backend.domain.board.dto.board.res.BoardCategoryResponse;
import capstone.mju.backend.domain.board.dto.board.res.BoardDetailResponse;
import capstone.mju.backend.domain.board.dto.board.res.BoardDetailWithCommentsResponse;
import capstone.mju.backend.domain.board.entity.Category;
import capstone.mju.backend.domain.board.service.BoardService;
import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.UnauthorizedException;
import capstone.mju.backend.domain.user.domain.User;
import capstone.mju.backend.global.auth.AuthenticatedUser;
import capstone.mju.backend.global.s3.S3ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
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
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 생성 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 형식", content = @Content),
            @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
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
    @Operation(summary = "게시글 삭제", description = "UUID를 기반으로 본인이 작성한 게시글을 삭제합니다. S3에 등록된 이미지가 있다면 함께 삭제됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "403", description = "해당 게시글에 대한 권한이 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @AuthenticatedUser @Parameter(hidden = true) User user, // Swagger에 사용자 정보 노출 안 함
            @PathVariable @Parameter(description = "삭제할 게시글의 UUID", example = "ec54a7b6-2a47-4c77-b294-72ea4dcb6584") UUID boardId) {

        boardService.deleteBoard(boardId, user);
        return ResponseEntity.noContent().build();
    }
    //게시글 수정
    @Operation(summary = "게시글 수정", description = "인증된 사용자가 본인 게시글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식", content = @Content),
            @ApiResponse(responseCode = "401", description = "로그인되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "403", description = "해당 게시글에 대한 권한이 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })

    @PutMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateBoard(
            @AuthenticatedUser @Parameter(hidden = true) User user,
            @PathVariable UUID boardId,
            @RequestPart("request") @Valid BoardUpdateRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        boardService.updateBoard(boardId, user, request, image);
        return ResponseEntity.ok().build();
    }



//    // 게시글 상세 조회
//    @Operation(summary = "게시글 상세 조회", description = "게시글 ID를 기반으로 상세 내용을 조회합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
//            @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않음", content = @Content),
//            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
//    })
//    @GetMapping("/{boardId}")
//    public ResponseEntity<BoardDetailResponse> getBoardDetail(
//            @PathVariable @Parameter(description = "조회할 게시글 UUID", example = "ec54a7b6-2a47-4c77-b294-72ea4dcb6584") UUID boardId) {
//
//        BoardDetailResponse response = boardService.getBoardDetail(boardId);
//        return ResponseEntity.ok(response);
//    }

    //카테고리별 게시글 조회 ( 최신순 + 무한 스크롤 )
    @Operation(summary = "카테고리별 게시글 목록 조회", description = "카테고리별로 최근 게시글을 10개씩 Slice로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "카테고리 값이 유효하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @GetMapping("/scroll")
    public ResponseEntity<Slice<BoardCategoryResponse>> getBoardsByCategory(
            @RequestParam Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Slice<BoardCategoryResponse> result = boardService.getBoardsByCategory(category, page, size);
        return ResponseEntity.ok(result);
    }

    //통합 조회 : 상세페이지에 있는 데이터들
    @Operation(
            summary = "게시글 상세 + 댓글 조회 + 좋아요/댓글 갯수",
            description = "게시글과 댓글/대댓글을 함께 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글과 댓글/대댓글 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardDetailWithCommentsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글이 존재하지 않음",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{boardId}/full")
    public ResponseEntity<BoardDetailWithCommentsResponse> getFullBoardDetail(
            @AuthenticatedUser @Parameter(hidden = true) User user,
            @PathVariable @Parameter(description = "게시글 UUID", example = "ec54a7b6-2a47-4c77-b294-72ea4dcb6584") UUID boardId) {

        BoardDetailWithCommentsResponse response = boardService.getBoardDetailWithComments(user, boardId);
        return ResponseEntity.ok(response);
    }


}
