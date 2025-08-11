package com.boreebeko.calio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "integrations")
@NoArgsConstructor
@Getter
public class Integration {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "user_id")
    @NotNull
    private UUID userId;

    @Column(name = "service_name")
    @NotNull
    private String serviceName;

    @Column(name = "access_token")
    @NotNull
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;
}
