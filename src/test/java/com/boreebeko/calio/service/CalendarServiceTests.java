package com.boreebeko.calio.service;

import com.boreebeko.calio.dto.CalendarDTO;
import com.boreebeko.calio.model.Calendar;
import com.boreebeko.calio.repository.CalendarRepository;
import com.boreebeko.calio.service.mapper.CalendarMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CalendarServiceTests {

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private UserService userService;

    @Mock
    private CalendarMapper calendarMapper;

    @InjectMocks
    private CalendarService calendarService;

    private Calendar mockCalendar;
    private CalendarDTO mockCalendarDTO;

    private Long id = 1L;
    private UUID mockUserUUID;

    @BeforeEach
    void setup() {
        mockUserUUID = UUID.randomUUID();
        mockCalendar = new Calendar(id, mockUserUUID, "Business-Calendar", "Business-Related Events", false, OffsetDateTime.now(), null);
        mockCalendarDTO = new CalendarDTO(null, "Business-Calendar", "Business-Related Events", false, null);
    }

    // Test createNewCalendar() when calendar valid then create new calendar
    @Test
    public void testCreateNewCalendar_WhenValidCalendar_ThenCreateNewCalendar() {

        when(calendarMapper.toEntity(any(CalendarDTO.class))).thenReturn(mockCalendar);
        when(calendarMapper.toDTO(any(Calendar.class))).thenReturn(mockCalendarDTO);

        when(userService.getCurrentUserUUID()).thenReturn(mockUserUUID);
        when(calendarRepository.save(any(Calendar.class))).thenReturn(mockCalendar);

        CalendarDTO calendarDTO = calendarService.createNewCalendar(mockCalendarDTO);

        assertThat(calendarDTO).isNotNull();
        assertThat(calendarDTO.getName()).isEqualTo(mockCalendarDTO.getName());

        ArgumentCaptor<Calendar> calendarArgumentCaptor = ArgumentCaptor.forClass(Calendar.class);

        verify(calendarRepository).save(calendarArgumentCaptor.capture());

        Calendar captorCalendarEntity = calendarArgumentCaptor.getValue();
        assertThat(captorCalendarEntity.getName()).isEqualTo(mockCalendar.getName());
    }

    @Test
    public void testGetCalendar_WhenValidUUIDIsGiven() {

        when(userService.getCurrentUserUUID()).thenReturn(mockUserUUID);
        when(calendarRepository.findCalendarsByOwnerId(any(UUID.class))).thenReturn(mockCalendar);

        mockCalendarDTO.setId(id);
        when(calendarMapper.toDTO(any(Calendar.class))).thenReturn(mockCalendarDTO);

        CalendarDTO userCalendarDTO = calendarService.getUserCalendar();

        assertThat(userCalendarDTO.getName()).isEqualTo(mockCalendar.getName());
    }
}
