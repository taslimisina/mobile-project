package ir.sharif.mobile.project.ui.repository;

import android.content.Context;

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
            taskRepository = new TaskRepository(dbHelper);
        rewardRepository = new RewardRepository(dbHelper);
        reminderRepository = new ReminderRepository(dbHelper);
        checklistItemRepository = new ChecklistItemRepository(dbHelper);
        taskRepository.setChecklistItemRepository(checklistItemRepository)
                .setReminderRepository(reminderRepository);
        taskRepository.findAll(TaskRepository.TaskType.HABIT);
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
