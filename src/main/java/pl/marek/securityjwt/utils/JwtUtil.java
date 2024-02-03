package pl.marek.securityjwt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;


@Component
public class JwtUtil {

    @Value("SecurityWithJWTProjectsSecretKeyThatHasEnoughBytesToAvoidTheWeakKeyException")
    private String secret;

    @Value("${jwt.expirationTimeInS}")
    private int expirationTimeInS;

    @Value("${jwt.refreshExpirationDateInS}")
    private int refreshExpirationDateInS;

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        List<String> roleList = claims.get("roles", List.class);
        if (roleList != null) {
            roleList.forEach(role -> roles.add(new SimpleGrantedAuthority(role)));
        }
        return roles;
    }

    public Long getIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Integer id = claims.get("id", Integer.class);
        return Long.valueOf(id);
    }

    public boolean isAccessToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("isAccessToken", Boolean.class);
    }

    private String doGenerateAccessToken(Map<String, Object> claims, String subject) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInS * 1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationDateInS * 1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(UserDetails userDetails, Long id) {
        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        List<String> roleList = new ArrayList<>();
        roles.forEach(role -> roleList.add(role.toString()));
        claims.put("id", id);
        claims.put("roles", roleList);
        claims.put("isAccessToken", true);
        return doGenerateAccessToken(claims, userDetails.getUsername());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("isAccessToken", false);
        return doGenerateRefreshToken(claims, userDetails.getUsername());
    }

}