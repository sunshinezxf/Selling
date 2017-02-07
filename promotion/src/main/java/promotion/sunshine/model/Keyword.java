package promotion.sunshine.model;

/**
 * Created by sunshine on 2017/2/7.
 */
public class Keyword {
    private String wordId;

    private String content;

    private Article article;

    public Keyword() {
        super();
    }

    public Keyword(String content, Article article) {
        this();
        this.content = content;
        this.article = article;
    }

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
