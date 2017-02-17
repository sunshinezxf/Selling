package common.sunshine.model.selling.agent;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.agent.lite.Agent;

/**
 * 代理商身份证信息
 * Created by sunshine on 6/1/16.
 */
public class Credit extends Entity {
    private String creditId;

    /* 身份证正面照 */
    private String frontPath;

    /* 身份证反面照 */
    private String backPath;

    /* 代理商 */
    private Agent agent;

    public Credit() {
        super();
    }

    public Credit(String frontPath, String backPath) {
        this();
        this.frontPath = frontPath;
        this.backPath = backPath;
    }

    public Credit(String frontPath, String backPath, Agent agent) {
        this(frontPath, backPath);
        this.agent = agent;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getFrontPath() {
        return frontPath;
    }

    public void setFrontPath(String frontPath) {
        this.frontPath = frontPath;
    }

    public String getBackPath() {
        return backPath;
    }

    public void setBackPath(String backPath) {
        this.backPath = backPath;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
