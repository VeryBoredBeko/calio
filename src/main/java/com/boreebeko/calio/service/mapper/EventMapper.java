package com.boreebeko.calio.service.mapper;

import com.boreebeko.calio.dto.EventDTO;
import com.boreebeko.calio.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventMapper extends Mappable<Event, EventDTO> {
    EventMapper instance = Mappers.getMapper(EventMapper.class);
}
