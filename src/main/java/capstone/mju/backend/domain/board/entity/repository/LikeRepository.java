package capstone.mju.backend.domain.board.entity.repository;

import capstone.mju.backend.domain.board.entity.Board;
import capstone.mju.backend.domain.board.entity.Like;
import capstone.mju.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {

    boolean existsByUserAndBoard(User user, Board board);

    Optional<Like> findByUserAndBoard(User user, Board board);

    List<Like> findAllByUser(User user);
}
