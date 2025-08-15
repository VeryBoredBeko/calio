package com.boreebeko.calio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.domain.Persistable;

import java.time.OffsetDateTime;

@Entity
@Table(name = "reminders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reminder implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reminder_id_generator")
    @SequenceGenerator(name = "reminder_id_generator", sequenceName = "reminders_id_seq", allocationSize = 1)
    @Setter(value = AccessLevel.PROTECTED)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Event event;

    @Column(name = "remind_at")
    @NotNull
    private OffsetDateTime remindAt;

    @Column(name = "method")
    @Enumerated(value = EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ReminderMethod reminderMethod;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
