package common.sunshine.model.selling.agent.support;

public enum AgentType {
	
	ORDINARY(0),COMPANY(1),SUPPORT(2);
	
    private int code;

	AgentType(int status) {
        this.code = status;
    }

    public int getCode() {
        return code;
    }

}
