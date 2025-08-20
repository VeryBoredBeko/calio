package com.boreebeko.calio.service;

import com.boreebeko.calio.dto.EventDTO;
import com.boreebeko.calio.exception.NoSuchEventEntityException;
import com.boreebeko.calio.model.Calendar;
import com.boreebeko.calio.model.Event;
import com.boreebeko.calio.model.projection.CalendarIdProjection;
import com.boreebeko.calio.model.projection.EventWithSimpleCalendarProjection;
import com.boreebeko.calio.repository.CalendarRepository;
import com.boreebeko.calio.repository.EventRepository;
import com.boreebeko.calio.service.impl.EventServiceImpl;
import com.boreebeko.calio.service.mapper.EventMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTests {

    @Mock
    private EventRepository mockEventRepository;

    @Mock
    private EventMapper mockEventMapper;

    @Mock
    private UserService mockUserService;

    @Mock
    private ReminderService mockReminderService;

    @Mock
    private CalendarRepository mockCalendarRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private UUID userId = UUID.randomUUID();
    private final Long eventId = 1L;
    private final String startTime = "";
    private final String endTime = "";
    private Event eventEntity;
    private EventDTO eventDTO;

    private final Long calendarId = 1L;
    private Calendar calendarEntity;

    private CalendarIdProjection calendarIdProjection;

    private Optional<EventWithSimpleCalendarProjection> validProjectionOptional =
            Optional.of(
                    new EventWithSimpleCalendarProjection() {
                        @Override
                        public Long getId() {
                            return eventId;
                        }

                        @Override
                        public CalendarIdAndOwnerIdProjection getCalendar() {
                            return new CalendarIdAndOwnerIdProjection() {
                                @Override
                                public Long getId() {
                                    return calendarId;
                                }

                                @Override
                                public UUID getOwnerId() {
                                    return userId;
                                }
                            };
                        }
                    }
            );

    private Optional<EventWithSimpleCalendarProjection> invalidProjectionOptional =
            Optional.of(
                    new EventWithSimpleCalendarProjection() {
                        @Override
                        public Long getId() {
                            return eventId;
                        }

                        @Override
                        public CalendarIdAndOwnerIdProjection getCalendar() {
                            return new CalendarIdAndOwnerIdProjection() {
                                @Override
                                public Long getId() {
                                    return calendarId;
                                }

                                @Override
                                public UUID getOwnerId() {
                                    return UUID.randomUUID();
                                }
                            };
                        }
                    }
            );

    @BeforeEach
    public void setup() {
        eventDTO = new EventDTO(eventId, "Event-Title", "EVent-Description", "Event-Location", null, null, false, null, null);
        eventEntity = new Event(eventId, null, "Event-Title", "Event-Description", "Event-Location", null, null, false, null, null);
        calendarEntity = new Calendar(calendarId, userId, "Calendar-Name", "Calendar-Description", false, null, null);
        calendarIdProjection = new CalendarIdProjection() {
            @Override
            public Long getId() {
                return calendarId;
            }
        };
    }

    @Test
    public void testCreateNewEvent_WhenValidEventDTO() {

        when(mockEventMapper.toEntity(any(EventDTO.class))).thenReturn(eventEntity);
        when(mockUserService.getCurrentUserUUID()).thenReturn(userId);
        when(mockCalendarRepository.findCalendarsByOwnerId(userId)).thenReturn(calendarEntity);
        when(mockEventRepository.save(any(Event.class))).thenReturn(eventEntity);
        when(mockEventMapper.toDTO(any(Event.class))).thenReturn(eventDTO);

        EventDTO returnedEventDTO = eventService.createNewEvent(eventDTO, false, null);

        assertThat(returnedEventDTO.getId()).isEqualTo(eventDTO.getId());
        assertThat(returnedEventDTO.getTitle()).isEqualTo(eventDTO.getTitle());
    }

    @Test
    public void testGetAllEvents() {

        List<Event> returnedEvents = new ArrayList<>(List.of(eventEntity, eventEntity, eventEntity));
        List<EventDTO> returnedEventDTOs = new ArrayList<>(List.of(eventDTO, eventDTO, eventDTO));

        when(mockUserService.getCurrentUserUUID()).thenReturn(userId);
        when(mockCalendarRepository.findCalendarIdByOwnerId(userId)).thenReturn(calendarIdProjection);
        when(mockEventRepository.findEventsByCalendarId(calendarIdProjection.getId())).thenReturn(returnedEvents);
        when(mockEventMapper.toDTOList(anyList())).thenReturn(returnedEventDTOs);

        assertThat(eventService.getAllEvents()).hasSize(3);
    }

    @Test
    public void testDeleteEvent_WhenInvalidEventIdPassed() {

        when(mockUserService.getCurrentUserUUID()).thenReturn(userId);
        when(mockEventRepository.findEventProjectionById(eventId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.deleteEvent(eventId))
                .isInstanceOf(NoSuchEventEntityException.class);
    }

    @Test
    public void testDeleteEvent_WhenNotAuthorizedUserCalled() {

        when(mockUserService.getCurrentUserUUID()).thenReturn(userId);
        when(mockEventRepository.findEventProjectionById(eventId))
                .thenReturn(invalidProjectionOptional);

        assertThatThrownBy(() -> eventService.deleteEvent(eventId))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    public void testDeleteEvent_WhenValidEventIdPassed_And_AuthorizedClientCalled() {

        when(mockUserService.getCurrentUserUUID()).thenReturn(userId);
        when(mockEventRepository.findEventProjectionById(eventId))
                .thenReturn(validProjectionOptional);
        doNothing().when(mockEventRepository).deleteById(eventId);
        doNothing().when(mockReminderService).deleteReminderByEventId(eventId);

        eventService.deleteEvent(eventId);
        verify(mockEventRepository).deleteById(eventId);
    }
}
