package capstone.mju.backend.domain.board.service;

import capstone.mju.backend.domain.board.dto.comment.req.CommentCreateRequest;
import capstone.mju.backend.domain.board.dto.comment.req.CommentUpdateRequest;
import capstone.mju.backend.domain.board.dto.comment.res.CommentResponse;
import capstone.mju.backend.domain.board.dto.comment.res.CommentTreeResponse;
import capstone.mju.backend.domain.board.entity.Board;
import capstone.mju.backend.domain.board.entity.Comment;
import capstone.mju.backend.domain.board.entity.repository.BoardRepository;
import capstone.mju.backend.domain.board.entity.repository.CommentRepository;
import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.NotFoundException;
import capstone.mju.backend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    //댓글 생성
    @Transactional
    public UUID createComment(UUID boardId, User user, CommentCreateRequest request) {
        Board board = findBoardOrThrow(boardId);

        Comment parent = null;
        if (request.getParentId() != null) {
            parent = findParentCommentOrThrow(request.getParentId());
        }
        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .board(board)
                .parent(parent)
                .build();

        commentRepository.save(comment);
        return comment.getId();
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(UUID commentId, User user) {
        Comment comment = findCommentByIdAndUser(commentId, user);

        commentRepository.delete(comment);
        log.info("댓글 삭제 완료 - commentId={}, user={}", commentId, user.getEmail());
    }
    @Transactional
    public void updateComment(UUID commentId, User user, CommentUpdateRequest request) {
        Comment comment = findCommentByIdAndUser(commentId, user);

        comment.updateContent(request.getContent());
        log.info("댓글 수정 완료 - commentId={}, user={}", commentId, user.getEmail());
    }

    //댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByBoard(UUID boardId) {
        return commentRepository.findByBoardIdOrderByCreatedAtAsc(boardId).stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .username(comment.getUser().getUsername())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    //댓글 + 대댓글 선언
    @Transactional(readOnly = true)
    public List<CommentTreeResponse> getCommentsByBoardWithReplies(UUID boardId) {
        List<Comment> comments = commentRepository.findByBoardIdOrderByCreatedAtAsc(boardId);

        return comments.stream()
                .filter(comment -> comment.getParent() == null)
                .map(CommentTreeResponse::from)
                .collect(Collectors.toList());
    }

    // --------------예외처리 ------------
    private Comment findCommentByIdAndUser(UUID commentId, User user) {
        return commentRepository.findByIdAndUser(commentId, user)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND, "댓글이 존재하지 않거나 권한이 없습니다."));
    }
    private Board findBoardOrThrow(UUID boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND, "게시글을 찾을 수 없습니다."));
    }

    private Comment findParentCommentOrThrow(UUID commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND, "부모 댓글을 찾을 수 없습니다."));
    }
}