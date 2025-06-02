package capstone.mju.backend.domain.board.entity.repository;

import capstone.mju.backend.domain.board.entity.Board;
import capstone.mju.backend.domain.board.entity.Like;
import capstone.mju.backend.domain.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {

    boolean existsByUserAndBoard(User user, Board board);

    Optional<Like> findByUserAndBoard(User user, Board board);

    Slice<Like> findAllByUser(User user, Pageable pageable);

    boolean existsByBoardAndUser(Board board, User user);
    @Query("SELECT l.board.id FROM Like l WHERE l.user.id = :userId AND l.board.id IN :boardIds")
    List<UUID> findLikedBoardIdsByUserAndBoardIds(@Param("userId") UUID userId,
                                                  @Param("boardIds") List<UUID> boardIds);

}
