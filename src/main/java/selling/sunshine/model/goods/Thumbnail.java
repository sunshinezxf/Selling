package selling.sunshine.model.goods;

import selling.sunshine.model.Entity;
import selling.sunshine.model.Goods;

/**
 * Created by sunshine on 6/16/16.
 */
public class Thumbnail extends Entity {
    private String thumbnailId;
    private String path;
    private Goods goods;

    public Thumbnail() {
        super();
    }

    public Thumbnail(String path) {
        this();
        this.path = path;
    }

    public Thumbnail(String path, Goods goods) {
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
