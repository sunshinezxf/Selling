package selling.sunshine.utils;

/**
 * Created by sunshine on 4/17/16.
 */
public class Prompt {
    private PromptCode code;
    private String message;
    private String title;
    private String confirmURL;
    private String extra;

    private Prompt() {
        code = PromptCode.SUCCESS;
    }

    public Prompt(PromptCode code, String title, String message, String confirmURL) {
        this();
        this.code = code;
        this.message = message;
        this.title = title;
        this.confirmURL = confirmURL;
    }

    public Prompt(PromptCode code, String title, String message, String confirmURL, String extra) {
        this(code, title, message, confirmURL);
        this.extra = extra;
    }

    public Prompt(String title, String message, String confirmURL) {
        this(PromptCode.SUCCESS, title, message, confirmURL);
    }

    public Prompt(String title, String message, String confirmURL, String extra) {
        this(PromptCode.SUCCESS, title, message, confirmURL, extra);
    }

    public PromptCode getCode() {
        return code;
    }

    public void setCode(PromptCode code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConfirmURL() {
        return confirmURL;
    }

    public void setConfirmURL(String confirmURL) {
        this.confirmURL = confirmURL;
    }


}
