package com.boreebeko.calio.dto.validation;

import com.boreebeko.calio.dto.EventDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EventTimingValidator implements ConstraintValidator<ValidEventTiming, EventDTO> {

    @Override
    public boolean isValid(EventDTO eventDTO, ConstraintValidatorContext constraintValidatorContext) {
        if (eventDTO.getStartTime() == null || eventDTO.getEndTime() == null) {
            return true;
        }

        return eventDTO.getEndTime().isAfter(eventDTO.getStartTime());
    }
}
