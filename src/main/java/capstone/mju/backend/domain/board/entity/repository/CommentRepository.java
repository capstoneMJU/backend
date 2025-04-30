package capstone.mju.backend.domain.board.entity.repository;

import capstone.mju.backend.domain.board.entity.Board;
import capstone.mju.backend.domain.board.entity.Comment;
import capstone.mju.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByBoardIdOrderByCreatedAtAsc(UUID boardId);
    Optional<Comment> findByIdAndUser(UUID id, User user); // 본인 확인
    int countByBoard(Board board);

}
