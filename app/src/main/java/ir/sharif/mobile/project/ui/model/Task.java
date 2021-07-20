package ir.sharif.mobile.project.ui.model;

import java.io.Serializable;

public abstract class Task implements Serializable {
    private Long id;
    private String title;
    private String description;
    private int reward;

    public Long getId() {
        return id;
    }

    public Task setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Task setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Task setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getReward() {
        return reward;
    }

    public Task setReward(int reward) {
        this.reward = reward;
        return this;
    }
}
