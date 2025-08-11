package com.boreebeko.calio.service;

import com.boreebeko.calio.dto.CalendarDTO;
import com.boreebeko.calio.model.Calendar;
import com.boreebeko.calio.repository.CalendarRepository;
import com.boreebeko.calio.service.mapper.CalendarMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CalendarService {

    private static final Logger logger = LoggerFactory.getLogger(CalendarService.class);

    private final CalendarRepository theCalendarRepository;
    private final CalendarMapper calendarMapper = CalendarMapper.instance;

    private final UserService userService;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository, UserService userService) {
        theCalendarRepository = calendarRepository;
        this.userService = userService;
    }

    @Transactional
    public void createNewCalendar(CalendarDTO calendarDTO) {

        Calendar newCalendar = calendarMapper.toEntity(calendarDTO);
        newCalendar.setOwnerId(userService.getCurrentUserUUID());

        logger.debug("Starting operation to create a new calendar for user with id {}", userService.getCurrentUserUUID());

        theCalendarRepository.save(newCalendar);
    }

    public List<CalendarDTO> getAllCalendars() {
        logger.debug("Starting operation to fetch all user-related calendars for user with id {}", userService.getCurrentUserUUID());
        return calendarMapper.toDTOList(theCalendarRepository.findCalendarsByOwnerId(userService.getCurrentUserUUID()));
    }
}
