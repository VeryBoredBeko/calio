package com.boreebeko.calio.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/businesses")
public class BusinessController {

    @GetMapping
    public ResponseEntity<List<String>> getAllBusinesses() {

        List<String> businessesNames = new ArrayList<>(3);

        businessesNames.add("Parking");
        businessesNames.add("Shipping");
        businessesNames.add("Trading");

        return new ResponseEntity<>(businessesNames, HttpStatus.OK);
    }
}
