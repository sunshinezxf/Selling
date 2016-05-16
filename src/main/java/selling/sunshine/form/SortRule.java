package selling.sunshine.form;

/**
 * Created by sunshine on 5/16/16.
 */
public class SortRule {
    private String name;
    private String method;

    public SortRule(String name, String method) {
        this.name = name;
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
