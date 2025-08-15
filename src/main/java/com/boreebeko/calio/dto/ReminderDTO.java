package com.boreebeko.calio.dto;

import com.boreebeko.calio.model.ReminderMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReminderDTO {
    private OffsetDateTime remindAt;
    private ReminderMethod reminderMethod;
}
