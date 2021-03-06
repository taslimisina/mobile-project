package ir.sharif.mobile.simple_task_management.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Daily extends Task {
    private static Daily emptyDaily;

    private Integer every;
    private Date start;
    private Date lastCheckedDate;
    private Boolean isChecked = false;

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

    public Daily setLastCheckedDate(Date lastCheckedDate) {
        this.lastCheckedDate = lastCheckedDate;
        return this;
    }

    public Date getLastCheckedDate() {
        return lastCheckedDate;
    }

    public Boolean isChecked() {
        return isChecked;
    }

    public Daily setChecked(Boolean checked) {
        isChecked = checked;
        return this;
    }

    public static Daily getEmptyDaily() {
        if (emptyDaily == null) {
            emptyDaily = new Daily();
            emptyDaily.setTitle("");
            emptyDaily.setDescription("");
            emptyDaily.setReward(0);
            emptyDaily.setEvery(1);
        }
        return emptyDaily;
    }
}
