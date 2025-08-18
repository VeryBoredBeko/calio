package com.boreebeko.calio.web.controller;

import com.boreebeko.calio.dto.EventDTO;
import com.boreebeko.calio.dto.ReminderDTO;
import com.boreebeko.calio.model.ReminderMethod;
import com.boreebeko.calio.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendars/{calendarId}/events")
public class EventController {

    private final EventService eventService;
    private final ReminderController reminderController;

    @Autowired
    public EventController(EventService eventService, ReminderController reminderController) {
        this.eventService = eventService;
        this.reminderController = reminderController;
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getUserCreatedEvents() {
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    // TODO: Controller must be responsible only for handling and returning requests and responses
    @PostMapping
    public ResponseEntity<EventDTO> createNewEvent(@PathVariable Long calendarId,
                                                   @RequestBody EventDTO eventDTO,
                                                   @RequestParam(required = false, defaultValue = "false") boolean autoRemind,
                                                   @RequestParam(required = false, defaultValue = "EMAIL") String reminderMethod) {

        EventDTO responseEventDTO = eventService.createNewEvent(eventDTO);

        if (autoRemind) {
            reminderController.createNewReminder(calendarId,
                    responseEventDTO.getId(),
                    new ReminderDTO(responseEventDTO.getStartTime().minusHours(1L), ReminderMethod.valueOf(reminderMethod)));
        }

        return new ResponseEntity<>(responseEventDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
