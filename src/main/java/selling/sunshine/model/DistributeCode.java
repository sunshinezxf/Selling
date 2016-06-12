package selling.sunshine.model;

import selling.sunshine.model.lite.Agent;

/**
 * Created by sunshine on 6/12/16.
 */
public class DistributeCode extends Entity {
    private String codeId;
    private Agent agent;
    private String codeValue;

    public DistributeCode(){
        super();
    }

    public DistributeCode(String codeValue){
        this();
        this.codeValue = codeValue;
    }

    public DistributeCode(String codeValue, Agent agent){
        this(codeValue);
        this.agent = agent;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }
}
