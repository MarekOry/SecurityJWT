package pl.marek.securityjwt.controller;

import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import pl.marek.securityjwt.dto.*;
import pl.marek.securityjwt.dto.authentication.UserAuthenticationDTO;
import pl.marek.securityjwt.dto.authentication.UserRegisterDTO;
import pl.marek.securityjwt.exception.RestException;
import pl.marek.securityjwt.security.AppUserDetailsService;
import pl.marek.securityjwt.service.AuthenticationService;
import pl.marek.securityjwt.service.UserService;
import pl.marek.securityjwt.utils.EmailUtil;
import pl.marek.securityjwt.exception.Message;
import pl.marek.securityjwt.utils.JwtUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final EmailUtil emailUtil;
    private final AppUserDetailsService appUserDetailsService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(AuthenticationService authenticationService,
                                    UserService userService,
                                    JwtUtil jwtUtil,
                                    EmailUtil emailUtil,
                                    AppUserDetailsService appUserDetailsService,
                                    AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.emailUtil = emailUtil;
        this.appUserDetailsService = appUserDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<Message> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO, WebRequest webRequest) {
        authenticationService.register(userRegisterDTO);
        emailUtil.sendRegisterEmail(userRegisterDTO.getEmail(), webRequest.getLocale());

        return ResponseEntity.ok(new Message("Registered_successfully"));
    }

    @PostMapping("/authentication")
    public ResponseEntity<JwtAuthenticationResponse> createAuthenticationToken(@Valid @RequestBody UserAuthenticationDTO userAuthenticationDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAuthenticationDTO.getEmail(), userAuthenticationDTO.getPassword()));
        UserDetails userDetails;

        try {
            userDetails = appUserDetailsService.loadUserByUsername(userAuthenticationDTO.getEmail());
        }
        catch (UsernameNotFoundException ex) {
            throw new RestException("Exception.wrong.credentials");
        }

        Long id = this.userService.getUserByEmail(userAuthenticationDTO.getEmail()).getId();
        final String accessToken = jwtUtil.generateAccessToken(userDetails, id);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        LocalDateTime expirationTime = LocalDateTime.ofInstant(jwtUtil.getExpirationDateFromToken(refreshToken).toInstant(), ZoneId.systemDefault());

        authenticationService.saveRefreshToken(refreshToken, userAuthenticationDTO.getEmail(), expirationTime);
        return ResponseEntity.ok(new JwtAuthenticationResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtRefreshTokenResponse> refreshToken(@RequestBody JwtRefreshTokenRequest jwtRefreshRequest) {
        String username;
        try {
            username = jwtUtil.getUsernameFromToken(jwtRefreshRequest.getRefreshToken());
        } catch (JwtException ex) {
            throw new RestException("Exception.refresh.token.is.not.valid");
        }
        if (!authenticationService.checkIfTokenExists(jwtRefreshRequest.getRefreshToken(), username)) {
            throw new RestException("Exception.refresh.token.is.not.valid");
        }
        Long id = userService.getUserByEmail(username).getId();
        final UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
        if(!userDetails.isEnabled()) throw new DisabledException("");
        final String accessToken = jwtUtil.generateAccessToken(userDetails, id);
        return ResponseEntity.ok(new JwtRefreshTokenResponse(accessToken));
    }
}