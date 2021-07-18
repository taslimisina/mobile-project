package ir.sharif.mobile.project.ui.model;

import java.time.Instant;
import java.util.List;

public class Todo extends Task {

    private Instant dueDate;
    private List<Reminder> reminders;
    private List<ChecklistItem> checklistItems;


    public Instant getDueDate() {
        return dueDate;
    }

    public Todo setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public Todo setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
        return this;
    }

    public List<ChecklistItem> getChecklistItems() {
        return checklistItems;
    }

    public Todo setChecklistItems(List<ChecklistItem> checklistItems) {
        this.checklistItems = checklistItems;
        return this;
    }
}
