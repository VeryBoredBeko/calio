package com.boreebeko.calio.service.mapper;

import com.boreebeko.calio.dto.ReminderDTO;
import com.boreebeko.calio.model.Reminder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReminderMapper extends Mappable<Reminder, ReminderDTO> {
    ReminderMapper INSTANCE = Mappers.getMapper(ReminderMapper.class);
}
