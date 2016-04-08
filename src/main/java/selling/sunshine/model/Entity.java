package selling.sunshine.model;

import java.sql.Timestamp;

/**
 * Created by sunshine on 4/8/16.
 */
public class Entity {
    private Timestamp createAt;

    public Entity() {
        this.createAt = new Timestamp(System.currentTimeMillis());
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
