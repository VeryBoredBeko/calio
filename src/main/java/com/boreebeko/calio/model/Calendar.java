package com.boreebeko.calio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Persistable;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "calendars")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Calendar implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calendars_seq")
    @SequenceGenerator(name = "calendars_seq", sequenceName = "calendars_id_seq", allocationSize = 1)
    @Setter(value = AccessLevel.NONE)
    private Long id;

    @Column(name = "owner_id")
    @NotNull
    private UUID ownerId;

    @NotNull
    @Length(max = 255)
    private String name;

    private String description;

    @Column(name = "is_shared")
    private boolean isShared;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "calendar")
    private Set<Event> events;

    @Override
    public boolean isNew() {
        return id == null;
    }

    public static Calendar createNew(UUID ownerId, String name, String description, boolean isShared) {
        return new Calendar(null, ownerId, name, description, isShared, null, null);
    }
}
