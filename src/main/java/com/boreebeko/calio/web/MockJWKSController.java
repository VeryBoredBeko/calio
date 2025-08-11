package com.boreebeko.calio.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@Profile("dev")
public class MockJWKSController {

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> getKeys() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/mock-jwks.json")) {
            return new ObjectMapper().readValue(inputStream, new TypeReference<>() {});
        }
    }
}
