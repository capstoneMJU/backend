package capstone.mju.backend.domain.board.service;

import capstone.mju.backend.domain.board.dto.req.BoardCreateRequest;
import capstone.mju.backend.domain.board.entity.Board;
import capstone.mju.backend.domain.board.entity.repository.BoardRepository;
import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.ForbiddenException;
import capstone.mju.backend.domain.common.exception.NotFoundException;
import capstone.mju.backend.domain.user.domain.User;
import capstone.mju.backend.domain.user.repository.UserInterface;
import capstone.mju.backend.global.s3.S3ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserInterface userRepository;
    private final S3ImageService s3ImageService;

    // 게시글 생성
    @Transactional
    public UUID createBoard(User user, BoardCreateRequest request, String imageUrl) {

        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category_name(request.getCategoryName())
                .post_image(imageUrl)
                .user(user)
                .build();

        Board savedBoard = boardRepository.save(board);
        return savedBoard.getId();
    }
    // 게시글 삭제
    @Transactional
    public void deleteBoard(UUID boardId, User user) {
        Board board = getBoardOwnedByUser(boardId, user);

        if (board.getPost_image() != null) {
            s3ImageService.deleteImageFromS3(board.getPost_image());
        }

        boardRepository.delete(board);
        log.info("게시글 삭제 완료 - boardId={}, user={}", boardId, user.getEmail());
    }

    //----------------------예외처리------------------------------

    private Board getBoardOwnedByUser(UUID boardId, User user) {
        return boardRepository.findByIdAndUser(boardId, user)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }

}
