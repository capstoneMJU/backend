package capstone.mju.backend.domain.auth.repository;

import capstone.mju.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
}
