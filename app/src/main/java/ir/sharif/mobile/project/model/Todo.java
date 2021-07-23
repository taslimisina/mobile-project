package ir.sharif.mobile.project.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Todo extends Task {

    private Date dueDate;
    private List<Reminder> reminders = new ArrayList<>();
    private List<ChecklistItem> checklistItems = new ArrayList<>();
    private static Todo emptyTodo;

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

    public static Todo getEmptyTodo() {
        if (emptyTodo == null) {
            emptyTodo = new Todo();
            emptyTodo.setTitle("");
            emptyTodo.setDescription("");
            emptyTodo.setReward(0);
        }
        return emptyTodo;
    }
}
