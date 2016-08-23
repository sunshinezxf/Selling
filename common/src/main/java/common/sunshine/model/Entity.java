package common.sunshine.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.sql.Timestamp;

/**
 * Created by sunshine on 4/8/16.
 */
public abstract class Entity {
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

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,SerializerFeature.DisableCircularReferenceDetect);
    }
}
