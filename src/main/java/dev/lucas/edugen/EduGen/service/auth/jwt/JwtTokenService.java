package dev.lucas.edugen.EduGen.service.auth.jwt;

import dev.lucas.edugen.EduGen.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Getter
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.access-token.expiration:900}")
    private Long accessTokenExpiration;

    public String generateAccessToken(User user){
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("EduGen")
                .issuedAt(now)
                .subject(user.getUsername())
                .expiresAt(now.plusSeconds(accessTokenExpiration))
                .claim("userId", user.getId().toString())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }


}
