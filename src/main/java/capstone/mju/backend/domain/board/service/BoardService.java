package capstone.mju.backend.domain.board.service;

import capstone.mju.backend.domain.board.dto.board.req.BoardCreateRequest;
import capstone.mju.backend.domain.board.dto.board.req.BoardUpdateRequest;
import capstone.mju.backend.domain.board.dto.board.res.BoardCategoryResponse;
import capstone.mju.backend.domain.board.dto.board.res.BoardDetailResponse;
import capstone.mju.backend.domain.board.dto.board.res.BoardDetailWithCommentsResponse;
import capstone.mju.backend.domain.board.dto.comment.res.CommentTreeResponse;
import capstone.mju.backend.domain.board.entity.Board;
import capstone.mju.backend.domain.board.entity.Category;
import capstone.mju.backend.domain.board.entity.repository.BoardRepository;
import capstone.mju.backend.domain.board.entity.repository.CommentRepository;
import capstone.mju.backend.domain.board.entity.repository.LikeRepository;
import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.NotFoundException;
import capstone.mju.backend.domain.common.exception.UnauthorizedException;
import capstone.mju.backend.domain.user.domain.User;
import capstone.mju.backend.domain.user.repository.UserInterface;
import capstone.mju.backend.global.s3.S3ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final S3ImageService s3ImageService;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;


    // 게시글 생성
    @Transactional
    public UUID createBoard(User user, BoardCreateRequest request, String imageUrl) {

        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .categoryName(request.getCategoryName())
                .post_image(imageUrl)
                .user(user)
                .build();

        Board savedBoard = boardRepository.save(board);
        log.info("Saved board: {}", board.getId());
        return savedBoard.getId();
    }
    // 게시글 삭제
    @Transactional
    public void deleteBoard(UUID boardId, User user) {
        validateAuthenticatedUser(user);
        Board board = getBoardOwnedByUser(boardId, user);

        if (board.getPost_image() != null) {
            s3ImageService.deleteImageFromS3(board.getPost_image());
        }

        boardRepository.delete(board);
        log.info("게시글 삭제 완료 - boardId={}, user={}", board.getId(), user.getEmail());
    }

    //게시글 수정
    @Transactional
    public void updateBoard(UUID boardId, User user, BoardUpdateRequest request, MultipartFile image) {
        validateAuthenticatedUser(user);
        Board board = getBoardOwnedByUser(boardId, user);

        // 기존 이미지 삭제
        if (image != null && !image.isEmpty()) {
            if (board.getPost_image() != null) {
                s3ImageService.deleteImageFromS3(board.getPost_image());
            }
            String newImageUrl = s3ImageService.upload(image);
            board.update(request.getTitle(), request.getContent(), request.getCategoryName(), newImageUrl);
        } else {
            board.update(request.getTitle(), request.getContent(), request.getCategoryName(), null);
        }
        log.info("Update board: {}", board.getId());
    }

    //상세 페이지 조회
    @Transactional(readOnly = true)
    public BoardDetailResponse getBoardDetail(UUID boardId) {
        Board board = findBoardOrThrow(boardId);

        return BoardDetailResponse.builder()
                .title(board.getTitle())
                .nickname(board.getUser().getUsername())
                .content(board.getContent())
                .postImage(board.getPost_image())
                .build();
    }

    //카테고리 별 게시글 조회
    @Transactional(readOnly = true)
    public Slice<BoardCategoryResponse> getBoardsByCategory(Category category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return boardRepository.findByCategoryNameOrderByCreatedAtDesc(category, pageable)
                .map(board -> BoardCategoryResponse.builder()
                        .boardId(board.getId())
                        .title(board.getTitle())
                        .name(board.getUser().getUsername())
                        .content(board.getContent())
                        .likeCount(board.getLikeCount())
                        .commentCount(board.getCommentCount())
                        .build());
    }
    //전체 항목 조회
    @Transactional(readOnly = true)
    public BoardDetailWithCommentsResponse getBoardDetailWithComments(User user, UUID boardId) {
        Board board = findBoardOrThrow(boardId);

        boolean liked = likeRepository.existsByBoardAndUser(board, user);
        int likeCount = board.getLikeCount();
        int commentCount = board.getCommentCount();
        List<CommentTreeResponse> comments = commentService.getCommentsByBoardWithReplies(boardId);

        return BoardDetailWithCommentsResponse.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .nickname(board.getUser().getUsername())
                .content(board.getContent())
                .postImage(board.getPost_image())
                .liked(liked)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .comments(comments)
                .build();
    }
    @Transactional(readOnly = true)
    public Slice<BoardCategoryResponse> getBoardsByUser(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return boardRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(board -> BoardCategoryResponse.builder()
                        .boardId(board.getId())
                        .title(board.getTitle())
                        .name(board.getUser().getUsername())
                        .content(board.getContent())
                        .likeCount(board.getLikeCount())
                        .commentCount(board.getCommentCount())
                        .build());
    }

    //----------------------예외처리------------------------------
    private void validateAuthenticatedUser(User user) {
        if (user == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER, "로그인이 필요합니다.");
        }
    }

    private Board getBoardOwnedByUser(UUID boardId, User user) {
        return boardRepository.findByIdAndUser(boardId, user)
                .orElseThrow(() -> new NotFoundException(ErrorCode.FORBIDDEN_USER, "해당 게시글에 대한 권한이 없습니다."));
    }

    private Board findBoardOrThrow(UUID boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND, "게시글이 존재하지 않습니다."));
    }

}
