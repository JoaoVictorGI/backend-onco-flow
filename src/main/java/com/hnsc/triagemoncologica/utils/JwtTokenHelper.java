package com.hnsc.triagemoncologica.utils;

import com.hnsc.triagemoncologica.ApplicationProperties;
import com.hnsc.triagemoncologica.dto.UsuarioSemSenhaDTO;
import java.time.Instant;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenHelper {
    private final JwtEncoder encoder;
    private final ApplicationProperties properties;

    public JwtTokenHelper(JwtEncoder encoder, ApplicationProperties properties) {
        this.encoder = encoder;
        this.properties = properties;
    }

    public String generateToken(UsuarioSemSenhaDTO dto) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.jwt().issuer())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(properties.jwt().expiresAt()))
                .subject(dto.email())
                .claim("usuario_id", dto.id())
                .claim("role", dto.role())
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
