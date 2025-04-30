package capstone.mju.backend.domain.board.service;

import capstone.mju.backend.domain.board.entity.Board;
import capstone.mju.backend.domain.board.entity.Like;
import capstone.mju.backend.domain.board.entity.repository.BoardRepository;
import capstone.mju.backend.domain.board.entity.repository.LikeRepository;
import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.ConflictException;
import capstone.mju.backend.domain.common.exception.NotFoundException;
import capstone.mju.backend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void likeBoard(UUID boardId, User user) {
        Board board = findBoardOrThrow(boardId);

        validateDuplicateLike(user, board);

        Like like = Like.builder()
                .user(user)
                .board(board)
                .build();

        likeRepository.save(like);
        log.info("게시글 좋아요 등록 - boardId={}, user={}", boardId, user.getEmail());
    }

    // ---------------- 예외 처리 메서드 ----------------

    private Board findBoardOrThrow(UUID boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND, "게시글이 존재하지 않습니다."));
    }

    private void validateDuplicateLike(User user, Board board) {
        if (likeRepository.existsByUserAndBoard(user, board)) {
            throw new ConflictException(ErrorCode.ALREADY_EXISTS, "이미 좋아요한 게시글입니다.");
        }
    }
}
