CREATE OR REPLACE FUNCTION prevent_event_overlap()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM events e
        WHERE e.calendar_id = NEW.calendar_id
            AND e.id <> COALESCE(NEW.id, -1)
            AND e.start_time < NEW.end_time
            AND e.end_time > NEW.start_time
    ) THEN
        RAISE EXCEPTION 'Event overlaps with an existing one for calendar %', NEW.calendar_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_prevent_event_overlap
BEFORE INSERT OR UPDATE ON events
FOR EACH ROW
EXECUTE FUNCTION prevent_event_overlap();
