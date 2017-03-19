package p1.edu.ufcg.worlddiscovery.core;

/**
 * Created by diegotavarez on 3/19/17.
 */

public class Badge {
    private String title;
    private String description;
    private int type;
    private int level;

    public Badge(String title, String description, int type, int level) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Badge{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
