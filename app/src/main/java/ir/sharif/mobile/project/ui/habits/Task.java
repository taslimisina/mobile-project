package ir.sharif.mobile.project.ui.habits;


import java.io.Serializable;

public class Task implements Serializable {
    private int id;
    private String title;
    private String description;
    private int reward;

    public Task(String title, String description, int reward) {
        this.title = title;
        this.description = description;
        this.reward = reward;
    }

    public Task(int id, String title, String description, int reward) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.reward = reward;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getReward() {
        return reward;
    }

}
