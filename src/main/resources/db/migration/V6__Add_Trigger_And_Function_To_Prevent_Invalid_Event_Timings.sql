CREATE OR REPLACE FUNCTION prevent_invalid_event_timing()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.start_time > NEW.end_time THEN
        RAISE EXCEPTION 'End time of event (%) cannot be before its starting time (%)',
                        NEW.end_time, NEW.start_time;
    END IF;

    IF NEW.start_time < now() THEN
        RAISE EXCEPTION 'Event cannot start in the past (%). Current time: %',
                        NEW.start_time, now();
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_prevent_invalid_event_timing
BEFORE INSERT OR UPDATE ON events
FOR EACH ROW
EXECUTE FUNCTION prevent_invalid_event_timing();
