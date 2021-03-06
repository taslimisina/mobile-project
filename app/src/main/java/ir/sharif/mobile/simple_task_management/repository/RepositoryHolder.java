package ir.sharif.mobile.simple_task_management.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import ir.sharif.mobile.simple_task_management.model.Daily;
import ir.sharif.mobile.simple_task_management.model.Reminder;

/**
 * This class holds all repository in application. use static getter function for retrieve them.
 * Init function must be called start of application.
 */
public class RepositoryHolder {

    private static TaskRepository taskRepository;
    private static RewardRepository rewardRepository;
    private static ReminderRepository reminderRepository;
    private static ChecklistItemRepository checklistItemRepository;
    private static CoinRepository coinRepository;
    private static NotificationManager notificationManager;

    public static void init(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        notificationManager = new NotificationManager(context);
        taskRepository = new TaskRepository(dbHelper);
        rewardRepository = new RewardRepository(dbHelper);
        reminderRepository = new ReminderRepository(dbHelper, notificationManager);
        checklistItemRepository = new ChecklistItemRepository(dbHelper);
        coinRepository = new CoinRepository(dbHelper);

        taskRepository.setChecklistItemRepository(checklistItemRepository)
                .setReminderRepository(reminderRepository);
        taskRepository.findAll(TaskRepository.TaskType.HABIT);
///*

//        coinRepository.increase(1);
//        coinRepository.decrease(2);
//        Log.i("f", "SCORE:" + coinRepository.getLastScore());
//        Reminder ss = new Reminder().setTime(new Date(System.currentTimeMillis() + 10000));
//        Reminder sss = new Reminder().setTime(new Date(System.currentTimeMillis() + 20000));
//        ArrayList<Reminder> reminders = new ArrayList<>();
//        reminders.add(ss);
//        reminders.add(sss);
//        Daily sdf = (Daily) new Daily()
//                .setReminders(reminders).setReward(10).setTitle("sdf");
//
//        taskRepository.save(sdf);
//        taskRepository.findAll();
//        for (Reminder reminder : reminderRepository.findAll()) {
//            reminderRepository.delete(reminder.getId());
//        }
//*/
    }

    public static ChecklistItemRepository getChecklistItemRepository() {
        return checklistItemRepository;
    }

    public static NotificationManager getNotificationManager() {
        return notificationManager;
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

    public static CoinRepository getCoinRepository() {
        return coinRepository;
    }
}
