package com.boreebeko.calio.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import static org.hamcrest.Matchers.containsString;

public class UserControllerTests {

    @Test
    void containsExactUsername() throws Exception {
        standaloneSetup(new UserController()).defaultResponseCharacterEncoding(UTF_8).build()
                .perform(get("/users").characterEncoding(UTF_8).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().encoding(UTF_8))
                .andExpect(content().string(containsString("John")));
    }
}
