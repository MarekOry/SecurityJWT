package pl.marek.securityjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtRefreshTokenResponse {

    private final String accessToken;
}