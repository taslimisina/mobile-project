package ir.sharif.mobile.project.ui.model;

import java.io.Serializable;

public class ChecklistItem implements Serializable {
    private Long id;
    private String name;
    private boolean checked = false;
    // Don't set this in code, it will be set automatically
    private long taskId;

    public Long getId() {
        return id;
    }

    public ChecklistItem setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ChecklistItem setName(String name) {
        this.name = name;
        return this;
    }

    public ChecklistItem setTaskId(long taskId) {
        this.taskId = taskId;
        return this;
    }

    public long getTaskId() {
        return taskId;
    }

    public ChecklistItem setChecked(boolean checked) {
        this.checked = checked;
        return this;
    }

    public boolean isChecked() {
        return checked;
    }
}
