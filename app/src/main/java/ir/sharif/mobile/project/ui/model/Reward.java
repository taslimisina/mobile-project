package ir.sharif.mobile.project.ui.model;

import java.io.Serializable;

public class Reward implements Serializable {
    private Long id;
    private String title;
    private String description;
    private int amount;
    private static Reward emptyReward;

    public Reward setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Reward setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public Reward setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Reward setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public static Reward getEmptyReward() {
        if (emptyReward == null) {
            emptyReward = new Reward();
            emptyReward.setTitle("");
            emptyReward.setDescription("");
            emptyReward.setAmount(0);
        }
        return emptyReward;
    }
}
