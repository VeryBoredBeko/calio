package com.boreebeko.calio.web.controller;

import com.boreebeko.calio.dto.EventDTO;
import com.boreebeko.calio.model.ReminderMethod;
import com.boreebeko.calio.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendars/{calendarId}/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getUserCreatedEvents() {
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    // TODO: Controller must be responsible only for handling and returning requests and responses
    @PostMapping
    public ResponseEntity<EventDTO> createNewEvent(@PathVariable Long calendarId,
                                                   @Valid @RequestBody EventDTO eventDTO,
                                                   @RequestParam(required = false, defaultValue = "false") boolean autoRemind,
                                                   @RequestParam(required = false, defaultValue = "EMAIL") ReminderMethod reminderMethod) {

        EventDTO responseEventDTO = eventService.createNewEvent(eventDTO, autoRemind, reminderMethod);
        return new ResponseEntity<>(responseEventDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {

        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
