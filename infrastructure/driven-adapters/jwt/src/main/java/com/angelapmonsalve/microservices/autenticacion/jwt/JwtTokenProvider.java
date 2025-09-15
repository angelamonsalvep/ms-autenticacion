package com.angelapmonsalve.microservices.autenticacion.jwt;

import com.angelapmonsalve.microservices.autenticacion.model.login.Login;
import com.angelapmonsalve.microservices.autenticacion.model.login.gateways.TokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import reactor.core.publisher.Mono;
import java.util.Date;

public class JwtTokenProvider implements TokenProvider {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 86400000; // 1 día en ms

    @Override
    public Mono<String> generarToken(Login login) {
        String token = Jwts.builder()
            .setSubject(login.getCorreo())
            .claim("rol", login.getRol().name())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SECRET_KEY)
            .compact();
        return Mono.just(token);
    }
}
