package by.arvisit.modsenlibapp.securityservice.service;

import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import by.arvisit.modsenlibapp.securityservice.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtService {

    private static final String AUTHORITIES_CLAIMS_KEY = "authorities";

    @Value("${spring.security.jwt.secret}")
    private String jwtSecret;
    @Value("${spring.security.jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSecretKey() {
        String base64EncodedSecretKey = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateAccessToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_CLAIMS_KEY, user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .claims(claims)
                .signWith(getSecretKey())
                .compact();
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> getAuthorities(String token) {
        return getClaims(token).get(AUTHORITIES_CLAIMS_KEY, List.class)
                .stream()
                .map(o -> new SimpleGrantedAuthority((String) o))
                .toList();
    }

    @SuppressWarnings("unchecked")
    public UserDto extractUser(String token) {
        Claims claims = getClaims(token);
        List<String> roleNames = claims.get(AUTHORITIES_CLAIMS_KEY, List.class);
        return new UserDto(claims.getSubject(), roleNames);
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Bad JWT credentials - {}", e.getMessage());
            return false;
        }
    }
}
