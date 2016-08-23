package common.sunshine.model.selling.agent.lite;

/**
 * 轻量级Agent,用于关联对象中
 * Created by sunshine on 5/20/16.
 */
public class Agent {
    private String agentId;
    private String name;
    private String phone;

    public Agent() {
        super();
    }

    public Agent(String name, String phone) {
        this();
        this.name = name;
        this.phone = phone;
    }

    public Agent(common.sunshine.model.selling.agent.Agent agent) {
        if (agent == null) {
            return;
        }
        this.agentId = agent.getAgentId();
        this.name = agent.getName();
        this.phone = agent.getPhone();
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
