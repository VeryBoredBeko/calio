package com.boreebeko.calio.service.mapper;

import com.boreebeko.calio.dto.CalendarDTO;
import com.boreebeko.calio.model.Calendar;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CalendarMapper extends Mappable<Calendar, CalendarDTO> {
    CalendarMapper instance = Mappers.getMapper(CalendarMapper.class);
}
