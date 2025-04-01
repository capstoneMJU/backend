package capstone.mju.backend.domain.user.repository;

import capstone.mju.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserInterface extends JpaRepository<User, UUID> {
}
