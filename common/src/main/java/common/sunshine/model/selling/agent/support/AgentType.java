package common.sunshine.model.selling.agent.support;

/**
 * 代理商类型枚举类型
 * Created by wangxiaodi
 */
public enum AgentType {

    /*ORDINARY为普通代理, COMPANY为公司代理, SUPPORT为客服*/
    ORDINARY(0), COMPANY(1), SUPPORT(2);

    private int code;

    AgentType(int status) {
        this.code = status;
    }

    public int getCode() {
        return code;
    }

}
