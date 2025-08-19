package com.boreebeko.calio.dto;

import com.boreebeko.calio.dto.validation.ValidEventTiming;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidEventTiming
public class EventDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String title;
    private String description;
    private String location;

    @FutureOrPresent
    private OffsetDateTime startTime;

    @Future
    private OffsetDateTime endTime;

    private boolean isAllDay;
    private String recurrenceRole;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime createdAt;
}
