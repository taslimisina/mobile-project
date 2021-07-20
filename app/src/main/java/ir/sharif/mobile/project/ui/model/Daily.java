package ir.sharif.mobile.project.ui.model;

import java.util.Date;
import java.util.List;

public class Daily extends Task {
    private int every;
    private Date start;

    private List<Reminder> reminders;
    private List<ChecklistItem> checklistItems;

    public int getEvery() {
        return every;
    }

    public Daily setEvery(int every) {
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
