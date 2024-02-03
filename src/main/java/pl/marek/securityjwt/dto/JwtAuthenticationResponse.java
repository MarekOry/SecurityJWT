package pl.marek.securityjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtAuthenticationResponse {
    private final String accessToken;
    private String refreshToken;
}