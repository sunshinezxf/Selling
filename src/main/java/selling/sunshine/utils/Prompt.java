package selling.sunshine.utils;

/**
 * Created by sunshine on 4/17/16.
 */
public class Prompt {
    private PromptCode code;
    private String message;
    private String title;
    private String confirmURL;

    public Prompt() {
        code = PromptCode.SUCCESS;
    }

    public Prompt(PromptCode code, String message) {
        this.code = code;
        this.message = message;
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
