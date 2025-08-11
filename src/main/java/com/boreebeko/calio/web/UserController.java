package com.boreebeko.calio.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public ResponseEntity<List<String>> getUsernames() {

        List<String> usernames = new ArrayList<>(3);

        usernames.add("John");
        usernames.add("Maria");
        usernames.add("Rychell");

        return new ResponseEntity<>(usernames, HttpStatus.OK);
    }
}
