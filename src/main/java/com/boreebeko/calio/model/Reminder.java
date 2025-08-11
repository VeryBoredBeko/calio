package com.boreebeko.calio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.time.OffsetDateTime;

@Entity
@Table(name = "reminders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Reminder implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @Column(name = "remind_at")
    @NotNull
    private OffsetDateTime remindAt;

    @Column(name = "reminder_method")
    @Enumerated(value = EnumType.STRING)
    private ReminderMethod reminderMethod;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
