-- ENUM для ролей доступа
CREATE TYPE calendar_role AS ENUM ('OWNER', 'EDITOR', 'VIEWER');

-- ENUM для методов напоминания
CREATE TYPE reminder_method AS ENUM ('NOTIFICATION', 'EMAIL', 'SMS');

-- Календарь
CREATE TABLE calendars (
    id SERIAL PRIMARY KEY,
    owner_id UUID NOT NULL, -- ID пользователя из Keycloak
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_shared BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Событие
CREATE TABLE events (
    id SERIAL PRIMARY KEY,
    calendar_id INT NOT NULL REFERENCES calendars(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(255),
    start_time TIMESTAMPTZ NOT NULL,
    end_time TIMESTAMPTZ NOT NULL,
    is_all_day BOOLEAN DEFAULT FALSE,
    recurrence_rule TEXT, -- iCalendar RRULE
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Напоминания
CREATE TABLE reminders (
    id SERIAL PRIMARY KEY,
    event_id INT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    remind_at TIMESTAMPTZ NOT NULL,
    method reminder_method DEFAULT 'NOTIFICATION'
);

-- Теги
CREATE TABLE tags (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Связь события и тега (M:N)
CREATE TABLE event_tags (
    event_id INT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    tag_id INT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (event_id, tag_id)
);

-- Совместный доступ к календарю
CREATE TABLE calendar_shares (
    id SERIAL PRIMARY KEY,
    calendar_id INT NOT NULL REFERENCES calendars(id) ON DELETE CASCADE,
    user_id UUID NOT NULL, -- ID пользователя из Keycloak
    role calendar_role NOT NULL,
    UNIQUE (calendar_id, user_id)
);

-- Интеграции с внешними сервисами
CREATE TABLE integrations (
    id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL, -- ID пользователя из Keycloak
    service_name VARCHAR(100) NOT NULL, -- Google, Outlook, etc.
    access_token TEXT NOT NULL,
    refresh_token TEXT,
    expires_at TIMESTAMPTZ
);
