package selling.sunshine.form;

import javax.validation.constraints.NotNull;

/**
 * Created by sunshine on 7/12/16.
 */
public class TimeRangeForm {
    @NotNull
    private String start;

    @NotNull
    private String end;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
