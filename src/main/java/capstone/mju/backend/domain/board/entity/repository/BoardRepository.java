package capstone.mju.backend.domain.board.entity.repository;

import capstone.mju.backend.domain.board.entity.Board;
import capstone.mju.backend.domain.board.entity.Category;
import capstone.mju.backend.domain.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByIdAndUser(UUID boardId, User user); // 본인 게시글만 삭제 가능하도록
    Optional<Board> findById(UUID id); //ID 조회
    @EntityGraph(attributePaths = {"user"}) //N+1문제 해결
    Slice<Board> findByCategoryNameOrderByCreatedAtDesc(Category category, Pageable pageable);


}
