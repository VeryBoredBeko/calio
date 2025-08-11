package com.boreebeko.calio.util.dev;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class DevJwtGenerator {

    public static void main(String[] args) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get("private.pem"));
        String keyPEM = new String(keyBytes)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = java.util.Base64.getDecoder().decode(keyPEM);
        PrivateKey privateKey = KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));

        String jwt = Jwts.builder()
                .setSubject("11111111-1111-1111-1111-111111111111")
                .claim("preferred_username", "devuser")
                .claim("email", "dev@example.com")
                .claim("roles", List.of("CUSTOMER"))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

        System.out.println(jwt);
    }
}

