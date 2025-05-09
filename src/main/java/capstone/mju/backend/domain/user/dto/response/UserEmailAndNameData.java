package capstone.mju.backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserEmailAndNameData {
    private String email;
    private String name;

    public static UserEmailAndNameData from(String email, String name) {
        return new UserEmailAndNameData(email, name);
    }
}
