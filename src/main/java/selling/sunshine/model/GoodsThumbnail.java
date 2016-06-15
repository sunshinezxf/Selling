package selling.sunshine.model;

/**
 * Created by sunshine on 6/15/16.
 */
public class GoodsThumbnail extends Entity {
    private String thumbnailId;
    private String path;
    private Goods goods;

    public GoodsThumbnail() {
        super();
    }

    public GoodsThumbnail(String path) {
        this();
        this.path = path;
    }

    public GoodsThumbnail(String path, Goods goods) {
        this(path);
        this.goods = goods;
    }

    public String getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(String thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
