package ir.sharif.mobile.project.ui.model;

public class ChecklistItem {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public ChecklistItem setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ChecklistItem setName(String name) {
        this.name = name;
        return this;
    }
}
