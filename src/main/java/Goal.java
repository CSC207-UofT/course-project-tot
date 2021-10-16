import java.text.DateFormat;
import java.util.Date;

public class Goal {
    public String summary;
    public Date date;
    public String description;
    public int time_required;
    public int priority;

    public Goal(String summary, Date date) {
        this.summary = summary;
        this.date = date;
        this.description = "";
        this.time_required = -1;
    }

    public Goal(String summary, String description, Date date) {
        this.summary = summary;
        this.date = date;
        this.description = description;
        this.time_required = -1;
    }

    public Goal(String summary, int time_required, Date date) {
        this.summary = summary;
        this.date = date;
        this.description = "";
        this.time_required = time_required;
    }

    public Goal(String summary, String description, int time_required, Date date) {
        this.summary = summary;
        this.date = date;
        this.description = description;
        this.time_required = time_required;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public int getTime_required() {
        return time_required;
    }

    public int getPriority() {
        return priority;
    }
}
