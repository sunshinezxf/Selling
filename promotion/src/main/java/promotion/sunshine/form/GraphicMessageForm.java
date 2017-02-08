package promotion.sunshine.form;

import javax.validation.constraints.NotNull;

/**
 * Created by wxd on 2017/2/8.
 */
public class GraphicMessageForm {

    @NotNull
    private String[] keywordList;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String url;

    public String[] getKeywordList() {
        return keywordList;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public void setKeywordList(String[] keywordList) {
        this.keywordList = keywordList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
