package com.boreebeko.calio.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record EventTagId (Long eventId, Long tagId) {}
