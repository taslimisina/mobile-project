package ir.sharif.mobile.project.ui.repository;

import android.content.Context;

import java.util.Collections;
import java.util.Date;

import ir.sharif.mobile.project.ui.model.ChecklistItem;
import ir.sharif.mobile.project.ui.model.Reminder;
import ir.sharif.mobile.project.ui.model.Task;
import ir.sharif.mobile.project.ui.model.Todo;

/**
 * This class holds all repository in application. use static getter function for retrieve them.
 * Init function must be called start of application.
 */
public class RepositoryHolder {

    private static TaskRepository taskRepository;
    private static RewardRepository rewardRepository;
    private static ReminderRepository reminderRepository;
    private static ChecklistItemRepository checklistItemRepository;

    public static void init(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        NotificationManager notificationManager = new NotificationManager(context);
        taskRepository = new TaskRepository(dbHelper);
        rewardRepository = new RewardRepository(dbHelper);
        reminderRepository = new ReminderRepository(dbHelper, notificationManager);
        checklistItemRepository = new ChecklistItemRepository(dbHelper);
        taskRepository.setChecklistItemRepository(checklistItemRepository)
                .setReminderRepository(reminderRepository);
        taskRepository.findAll(TaskRepository.TaskType.HABIT);

//        Reminder ss = new Reminder().setTime(new Date(System.currentTimeMillis() + 10000));
//        Task sdf = new Todo()
//                .setReminders(Collections.singletonList(ss)).setReward(10).setTitle("sdf");
//        taskRepository.save(sdf);
//        for (Reminder reminder : reminderRepository.findAll()) {
//            reminderRepository.delete(reminder.getId());
//        }
    }

    public static ChecklistItemRepository getChecklistItemRepository() {
        return checklistItemRepository;
    }

    public static ReminderRepository getReminderRepository() {
        return reminderRepository;
    }

    public static RewardRepository getRewardRepository() {
        return rewardRepository;
    }

    public static TaskRepository getTaskRepository() {
        return taskRepository;
    }
}
