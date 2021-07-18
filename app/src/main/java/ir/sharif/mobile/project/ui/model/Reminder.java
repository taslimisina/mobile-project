package ir.sharif.mobile.project.ui.model;

import java.time.Instant;

public class Reminder {
    private long id;
    private Instant time;

    public Reminder setTime(Instant time) {
        this.time = time;
        return this;
    }

    public Instant getTime() {
        return time;
    }

    public Reminder setId(long id) {
        this.id = id;
        return this;
    }

    public long getId() {
        return id;
    }
}
