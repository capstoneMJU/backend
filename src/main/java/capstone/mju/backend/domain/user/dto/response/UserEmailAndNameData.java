package capstone.mju.backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEmailAndNameData {
    private String email;
    private String name;

    public static UserEmailAndNameData from(String email, String name) {
        return new UserEmailAndNameData(email, name);
    }
}
