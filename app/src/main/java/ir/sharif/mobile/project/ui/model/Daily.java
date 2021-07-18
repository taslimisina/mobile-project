package ir.sharif.mobile.project.ui.model;

import java.time.Instant;
import java.util.List;

public class Daily extends Task {
    private int every;
    private Instant start;

    private List<Reminder> reminders;
    private List<ChecklistItem> checklistItems;

    public int getEvery() {
        return every;
    }

    public Daily setEvery(int every) {
        this.every = every;
        return this;
    }

    public Instant getStart() {
        return start;
    }

    public Daily setStart(Instant start) {
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
