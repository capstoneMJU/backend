package capstone.mju.backend.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateNameDto {
    @Schema(description = "새 이름", example = "홍길동")
    private String newName;
}
