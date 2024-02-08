package pl.marek.securityjwt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;


@Data
@NoArgsConstructor
public class JwtRefreshTokenRequest {

    @NotNull
    private String refreshToken;
}