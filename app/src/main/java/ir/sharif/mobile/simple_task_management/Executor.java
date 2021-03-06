package ir.sharif.mobile.simple_task_management;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ir.sharif.mobile.simple_task_management.ui.dailies.DailyViewHandler;
import ir.sharif.mobile.simple_task_management.ui.habits.HabitViewHandler;
import ir.sharif.mobile.simple_task_management.model.ChecklistItem;
import ir.sharif.mobile.simple_task_management.model.Daily;
import ir.sharif.mobile.simple_task_management.model.Habit;
import ir.sharif.mobile.simple_task_management.model.Reward;
import ir.sharif.mobile.simple_task_management.model.Task;
import ir.sharif.mobile.simple_task_management.model.Todo;
import ir.sharif.mobile.simple_task_management.repository.RepositoryHolder;
import ir.sharif.mobile.simple_task_management.repository.TaskRepository;
import ir.sharif.mobile.simple_task_management.ui.rewards.RewardViewHandler;
import ir.sharif.mobile.simple_task_management.ui.todo.TodoViewHandler;


public class Executor {
    public static final int SHOW_TOAST = 3;
    public static final int UPDATE_SCORE = 2;
    private static Executor instance = null;
    private final ThreadPoolExecutor threadPool;
    private WeakReference<Handler> handler;

    private Executor() {
        BlockingDeque<Runnable> runnable = new LinkedBlockingDeque<>();
        threadPool = new ThreadPoolExecutor(5, 10, 1000, TimeUnit.MILLISECONDS, runnable);
    }

    public static Executor getInstance() {
        if (instance == null)
            instance = new Executor();
        return instance;
    }

    public void submitTask(Runnable task) {
        threadPool.submit(task);
    }

    public boolean isPoolFull() {
        return threadPool.getPoolSize() == threadPool.getMaximumPoolSize();
    }

    public void loadTasks(TaskRepository.TaskType type) {
        if (handler == null)
            return;

        switch (type) {
            case HABIT:
                if (handler.get() instanceof HabitViewHandler) {
                    List<Habit> habits = RepositoryHolder.getTaskRepository().findAllHabits();
                    Message message = Message.obtain(handler.get(), HabitViewHandler.LOAD_DONE, habits);
                    handler.get().sendMessage(message);
                }
                break;
            case TODO:
                if (handler.get() instanceof TodoViewHandler) {
                    List<Todo> todoList = RepositoryHolder.getTaskRepository().findAllTodo();
                    Message message = Message.obtain(handler.get(), TodoViewHandler.LOAD_DONE, todoList);
                    handler.get().sendMessage(message);
                }
                break;
            case DAILY:
                if (handler.get() instanceof DailyViewHandler) {
                    List<Daily> dailyList = RepositoryHolder.getTaskRepository().findAllDaily();
                    Message message = Message.obtain(handler.get(), DailyViewHandler.LOAD_DONE, dailyList);
                    Log.d("DAILY_HANDLER", message.toString());
                    handler.get().sendMessage(message);
                }
                break;
            default:
                break;
        }

    }

    public void loadRewards() {
        if (handler == null || !(handler.get() instanceof RewardViewHandler))
            return;
        List<Reward> rewards = RepositoryHolder.getRewardRepository().findAll();
        Message message = Message.obtain(handler.get(), HabitViewHandler.LOAD_DONE, rewards);
        handler.get().sendMessage(message);
    }

    public void saveTask(Task task) {
        submitTask(() -> RepositoryHolder.getTaskRepository().save(task));
    }

    public void saveReward(Reward reward) {
        submitTask(() -> RepositoryHolder.getRewardRepository().save(reward));
    }

    public void saveChecklistItem(ChecklistItem item) {
        submitTask(() -> RepositoryHolder.getChecklistItemRepository().save(item));
    }

    public void deleteTask(Task task) {
        submitTask(() -> RepositoryHolder.getTaskRepository().delete(task.getId()));
    }

    public void deleteReward(Reward reward) {
        submitTask(() -> RepositoryHolder.getRewardRepository().delete(reward.getId()));
    }

    public void deleteReminder(Long id) {
        submitTask(() -> RepositoryHolder.getReminderRepository().delete(id));
    }

    public void deleteChecklistItem(Long id) {
        submitTask(() -> RepositoryHolder.getChecklistItemRepository().delete(id));
    }

    public void loadScore() {
        submitTask(() -> {
            int lastScore = RepositoryHolder.getCoinRepository().getLastScore();
            if (handler != null) {
                Message message = Message.obtain(handler.get(), UPDATE_SCORE, lastScore);
                handler.get().sendMessage(message);
            }
        });
    }

    public void addCoin(int amount) {
        submitTask(() -> {
            int lastScore = RepositoryHolder.getCoinRepository().getLastScore();
            if (lastScore + amount < 0 && handler != null) {
                Message message = Message.obtain(handler.get(), SHOW_TOAST, "Not enough coins!");
                handler.get().sendMessage(message);
            } else {
                RepositoryHolder.getCoinRepository().increase(amount);
                loadScore();
            }
        });
    }

    public void subCoin(int amount) {
        submitTask(() -> addCoin(-amount));
    }

    public void undoCoin() {
        submitTask(() -> {
                    RepositoryHolder.getCoinRepository().undo();
                    loadScore();
                }
        );
    }

    public void setHandler(Handler handler) {
        this.handler = new WeakReference<>(handler);
    }
}
