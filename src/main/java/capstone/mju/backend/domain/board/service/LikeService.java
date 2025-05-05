package capstone.mju.backend.domain.board.service;

import capstone.mju.backend.domain.board.dto.like.res.LikedBoardResponse;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;

    //좋아요
    @Transactional
    public void likeBoard(UUID boardId, User user) {
        Board board = findBoardOrThrow(boardId);

        validateDuplicateLike(user, board);

        Like like = Like.builder()
                .user(user)
                .board(board)
                .build();

        likeRepository.save(like);
        board.increaseLikeCount(); // 좋아요 수 증가
        log.info("게시글 좋아요 등록 - boardId={}, user={}", boardId, user.getEmail());
    }

    // 좋아요 삭제
    @Transactional
    public void unlikeBoard(UUID boardId, User user) {
        Board board = findBoardOrThrow(boardId);
        Like like = findLikeOrThrow(user, board);

        likeRepository.delete(like);
        board.decreaseLikeCount(); // 좋아요 수 감소
        log.info("좋아요 삭제 - user={}, board={}", user.getId(), board.getId());
    }

    //내가 좋아요 누른 항목
    @Transactional(readOnly = true)
    public Slice<LikedBoardResponse> getLikedBoards(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return likeRepository.findAllByUser(user, pageable)
                .map(like -> {
                    Board board = like.getBoard();
                    return LikedBoardResponse.builder()
                            .boardId(board.getId())
                            .title(board.getTitle())
                            .content(board.getContent())
                            .author(board.getUser().getUsername())
                            .build();
                });
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
    private Like findLikeOrThrow(User user, Board board) {
        return likeRepository.findByUserAndBoard(user, board)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND, "좋아요 정보가 존재하지 않습니다."));
    }
}
