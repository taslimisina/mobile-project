package ir.sharif.mobile.project.model;

public class Habit extends Task {

    private static Habit emptyHabit;

    public static Habit getEmptyHabit() {
        if (emptyHabit == null) {
            emptyHabit = new Habit();
            emptyHabit.setTitle("");
            emptyHabit.setDescription("");
            emptyHabit.setReward(0);
        }
        return emptyHabit;
    }
}
