package com.boreebeko.calio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Persistable;

import java.time.OffsetDateTime;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Event implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(insertable = false)
    private Long id;

    @JoinColumn(name = "calendar_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Calendar calendar;

    @NotNull
    @Length(max = 255)
    private String title;

    private String description;

    @Length(max = 255)
    private String location;

    @Column(name = "start_time")
    @NotNull
    private OffsetDateTime startTime;

    @Column(name = "end_time")
    @NotNull
    private OffsetDateTime endTime;

    @Column(name = "is_all_day")
    private boolean isAllDay;

    @Column(name = "recurrence_rule")
    private String recurrenceRule;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
