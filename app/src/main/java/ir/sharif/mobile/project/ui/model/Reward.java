package ir.sharif.mobile.project.ui.model;

public class Reward {
    private Long id;
    private String title;
    private String description;
    private int amount;

    public Reward setId(long id) {
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
}
