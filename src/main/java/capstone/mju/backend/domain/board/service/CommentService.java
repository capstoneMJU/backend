package capstone.mju.backend.domain.board.service;

import capstone.mju.backend.domain.board.dto.comment.req.CommentCreateRequest;
import capstone.mju.backend.domain.board.dto.comment.res.CommentResponse;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public UUID createComment(UUID boardId, User user, CommentCreateRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .board(board)
                .build();

        commentRepository.save(comment);
        return comment.getId();
    }
}