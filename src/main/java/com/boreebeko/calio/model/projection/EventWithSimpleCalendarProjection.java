package com.boreebeko.calio.model.projection;

import java.util.UUID;

public interface EventWithSimpleCalendarProjection {

    Long getId();
    CalendarIdAndOwnerIdProjection getCalendar();

    interface CalendarIdAndOwnerIdProjection {
        Long getId();
        UUID getOwnerId();
    }
}
