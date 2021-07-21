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
            taskRepository = new TaskRepository(context);
        taskRepository.getReadableDatabase();
        rewardRepository = new RewardRepository(context);
        rewardRepository.getReadableDatabase();
        reminderRepository = new ReminderRepository(context);
        reminderRepository.getReadableDatabase();
        checklistItemRepository = new ChecklistItemRepository(context);
        checklistItemRepository.getReadableDatabase();
        taskRepository.setChecklistItemRepository(checklistItemRepository)
                .setReminderRepository(reminderRepository);
        taskRepository.getReadableDatabase();
        checklistItemRepository.findAll();
    }

    public static void close() {
        taskRepository.close();
        checklistItemRepository.close();
        reminderRepository.close();
        rewardRepository.close();
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
