package pl.marek.securityjwt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAuthenticationDTO {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min=8)
    String password;
}