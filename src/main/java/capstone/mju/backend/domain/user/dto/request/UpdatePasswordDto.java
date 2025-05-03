package capstone.mju.backend.domain.user.dto.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdatePasswordDto {
    private String currentPassword;
    private String newPassword;
}
