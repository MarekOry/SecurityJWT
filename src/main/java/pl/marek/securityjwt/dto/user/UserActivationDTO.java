package pl.marek.securityjwt.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserActivationDTO {
    @NotNull
    private boolean active;

    private String version;
}