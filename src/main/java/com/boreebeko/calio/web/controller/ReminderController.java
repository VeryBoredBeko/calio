package com.boreebeko.calio.web.controller;

import com.boreebeko.calio.dto.ReminderDTO;
import com.boreebeko.calio.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calendars/{calendarId}/events/{eventId}/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    @Autowired
    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    public ResponseEntity<ReminderDTO> createNewReminder(@PathVariable Long calendarId,
                                                         @PathVariable Long eventId,
                                                         @RequestBody ReminderDTO reminderDTO) {
        try {
            return new ResponseEntity<>(
                    reminderService.createReminder(calendarId, eventId, reminderDTO),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
