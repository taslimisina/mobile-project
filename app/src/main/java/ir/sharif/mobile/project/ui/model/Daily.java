package ir.sharif.mobile.project.ui.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Daily extends Task {
    private Integer every;
    private Date start;

    private List<Reminder> reminders = new ArrayList<>();
    private List<ChecklistItem> checklistItems = new ArrayList<>();

    public Integer getEvery() {
        return every;
    }

    public Daily setEvery(Integer every) {
        this.every = every;
        return this;
    }

    public Date getStart() {
        return start;
    }

    public Daily setStart(Date start) {
        this.start = start;
        return this;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public Daily setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
        return this;
    }

    public List<ChecklistItem> getChecklistItems() {
        return checklistItems;
    }

    public Daily setChecklistItems(List<ChecklistItem> checklistItems) {
        this.checklistItems = checklistItems;
        return this;
    }
}
