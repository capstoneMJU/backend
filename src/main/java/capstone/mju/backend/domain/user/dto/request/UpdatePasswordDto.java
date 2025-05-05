package capstone.mju.backend.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdatePasswordDto {
    @Schema(description = "현재 비밀번호", example = "oldPassword123!")
    private String currentPassword;

    @Schema(description = "새 비밀번호", example = "newPassword456!")
    private String newPassword;
}
