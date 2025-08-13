package com.boreebeko.calio.web.controller;

import com.boreebeko.calio.dto.CalendarDTO;
import com.boreebeko.calio.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendars")
public class CalendarController {

    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    public ResponseEntity<CalendarDTO> getCalendar() {
        return new ResponseEntity<>(calendarService.getUserCalendar(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createNewCalendar(@RequestBody CalendarDTO calendarDTO) {
        calendarService.createNewCalendar(calendarDTO);
        return ResponseEntity.ok().build();
    }
}
