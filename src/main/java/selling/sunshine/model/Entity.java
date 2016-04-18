package selling.sunshine.model;

import java.sql.Timestamp;

/**
 * Created by sunshine on 4/8/16.
 */
public class Entity {
    private boolean blockFlag;
    private Timestamp createAt;

    public Entity() {
        this.blockFlag = false;
        this.createAt = new Timestamp(System.currentTimeMillis());
    }

    public boolean isBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(boolean blockFlag) {
        this.blockFlag = blockFlag;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
