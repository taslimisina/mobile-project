package ir.sharif.mobile.project.ui.repository;

import android.content.Context;

/**
 * This class holds all repository in application. use static getter function for retrieve them.
 * Init function must be called start of application.
 */
public class RepositoryHolder {

    private static final TaskRepository TASK_REPOSITORY = null;
    private static final RewardRepository rewardRepository = null;
    private static final ReminderRepository reminderRepository = null;
    private static final ChecklistItemRepository checklistItemRepository = null;

    public static void init(Context context) {
    }
}
