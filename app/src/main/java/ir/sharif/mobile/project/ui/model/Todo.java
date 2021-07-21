package ir.sharif.mobile.project.ui.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Todo extends Task {

    private Date dueDate;
    private List<Reminder> reminders = new ArrayList<>();
    private List<ChecklistItem> checklistItems = new ArrayList<>();

    public Date getDueDate() {
        return dueDate;
    }

    public Todo setDueDate(Date dueDate) {
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
