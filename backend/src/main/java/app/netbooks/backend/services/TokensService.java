package app.netbooks.backend.services;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokensService {
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public Optional<UUID> validate(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(TokensService.key)
                .build()
                .parseClaimsJws(token)
                .getBody();

            UUID uuid = UUID.fromString(claims.getSubject());
            return Optional.of(uuid);
        } catch (Exception e) {
            return Optional.empty();
        }
    };

    public String generate(UUID uuid) {
        Date currentDate = new Date();
        Date expirationDate = new Date(
            currentDate.getTime() + EXPIRATION_TIME
        );

        return Jwts
            .builder()
            .setSubject(uuid.toString())
            .setIssuedAt(currentDate)
            .setExpiration(expirationDate)
            .signWith(TokensService.key, SignatureAlgorithm.HS512)
            .compact();
    };
};
