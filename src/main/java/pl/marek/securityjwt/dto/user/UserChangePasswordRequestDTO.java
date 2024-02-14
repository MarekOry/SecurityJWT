package pl.marek.securityjwt.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePasswordRequestDTO {

    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}