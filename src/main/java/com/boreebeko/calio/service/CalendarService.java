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

@Service
@Transactional(readOnly = true)
public class CalendarService {

    private static final Logger logger = LoggerFactory.getLogger(CalendarService.class);

    private final CalendarRepository calendarRepository;
    private final CalendarMapper calendarMapper;

    private final UserService userService;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository, CalendarMapper calendarMapper, UserService userService) {
        this.calendarRepository = calendarRepository;
        this.calendarMapper = calendarMapper;
        this.userService = userService;
    }

    @Transactional
    public CalendarDTO createNewCalendar(CalendarDTO calendarDTO) {

        Calendar newCalendar = calendarMapper.toEntity(calendarDTO);
        newCalendar.setOwnerId(userService.getCurrentUserUUID());

        logger.debug("Creating a new calendar for user with id {}", userService.getCurrentUserUUID());
        Calendar persistedCalendar = calendarRepository.save(newCalendar);

        return calendarMapper.toDTO(persistedCalendar);
    }

    public CalendarDTO getUserCalendar() {
        logger.debug("Fetching calendat for user with id {}", userService.getCurrentUserUUID());
        return calendarMapper.toDTO(calendarRepository.findCalendarsByOwnerId(userService.getCurrentUserUUID()));
    }
}
