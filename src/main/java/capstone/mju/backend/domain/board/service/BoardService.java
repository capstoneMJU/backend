package capstone.mju.backend.domain.board.service;

import capstone.mju.backend.domain.board.dto.req.BoardCreateRequest;
import capstone.mju.backend.domain.board.entity.Board;
import capstone.mju.backend.domain.board.entity.repository.BoardRepository;
import capstone.mju.backend.domain.common.error.ErrorCode;
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

}
