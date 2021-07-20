package ir.sharif.mobile.project.ui.model;

import java.util.Date;

public class Reminder {
    private Long id;
    private Date time;
    // Don't set this in code, it will be set automatically
    private long taskId;

    public Reminder setTime(Date time) {
        this.time = time;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public Reminder setId(long id) {
        this.id = id;
        return this;
    }

    public Long getId() {
        return id;
    }

    public long getTaskId() {
        return taskId;
    }

    public Reminder setTaskId(long taskId) {
        this.taskId = taskId;
        return this;
    }
}
