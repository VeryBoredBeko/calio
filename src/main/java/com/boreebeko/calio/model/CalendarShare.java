package com.boreebeko.calio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "calendar_shares")
@NoArgsConstructor
@Getter
public class CalendarShare {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Calendar calendar;

    @NotNull
    private UUID userId;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private CalendarRole calendarRole;
}
