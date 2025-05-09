package capstone.mju.backend.domain.user.service;

import capstone.mju.backend.domain.auth.repository.AuthRepository;
import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.NotFoundException;
import capstone.mju.backend.domain.common.exception.UnauthorizedException;
import capstone.mju.backend.domain.user.domain.User;
import capstone.mju.backend.domain.user.dto.response.UserEmailAndNameData;
import capstone.mju.backend.global.auth.PasswordHashEncryption;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final AuthRepository authRepository;
    private final PasswordHashEncryption passwordHashEncryption;

    // 이름 수정
    @Transactional
    public String updateName(User user, String newName) {
        log.info("Updating name of user {}", user.getUsername());
        user.setName(newName);
        authRepository.save(user);
        return newName;
    }

    // 비밀번호 수정
    @Transactional
    public void updatePassword(User user, String currentPassword, String newPassword) {
        if (!passwordHashEncryption.matches(currentPassword, user.getPassword())) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }

        String encodedPassword = passwordHashEncryption.encrypt(newPassword);
        user.setPassword(encodedPassword);
        authRepository.save(user);
    }

    public UserEmailAndNameData getUserEmailAndName(User user) {
        return UserEmailAndNameData.from(user.getEmail(), user.getUsername());
    }
}
