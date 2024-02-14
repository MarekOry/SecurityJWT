package pl.marek.securityjwt.dto.authentication;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserPasswordDTO {
    @NotNull
    @Size(min = 8)
    private String password;
}