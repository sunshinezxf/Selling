package common.sunshine.model.selling.agent;

import common.sunshine.model.Entity;

/**
 * 该类为代理商指标类, 用于计算代理商的KPI
 * Created by wangxiaodi
 */
public class AgentKPI extends Entity {
    private String kpiId;

    private String agentId;

    /* 代理商姓名 */
    private String agentName;

    /* 该代理商的顾客数量 */
    private int customerQuantity;

    /* 直接下级代理人数 */
    private int directAgentQuantity;

    /* 代理商的贡献度 */
    private int agentContribution;

    public AgentKPI() {
        super();
    }

    public AgentKPI(String agentId, String agentName, int customerQuantity, int directAgentQuantity, int agentContribution) {
        super();
        this.agentId = agentId;
        this.agentName = agentName;
        this.customerQuantity = customerQuantity;
        this.directAgentQuantity = directAgentQuantity;
        this.agentContribution = agentContribution;
    }

    public String getKpiId() {
        return kpiId;
    }

    public void setKpiId(String kpiId) {
        this.kpiId = kpiId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public int getCustomerQuantity() {
        return customerQuantity;
    }

    public void setCustomerQuantity(int customerQuantity) {
        this.customerQuantity = customerQuantity;
    }

    public int getDirectAgentQuantity() {
        return directAgentQuantity;
    }

    public void setDirectAgentQuantity(int directAgentQuantity) {
        this.directAgentQuantity = directAgentQuantity;
    }

    public int getAgentContribution() {
        return agentContribution;
    }

    public void setAgentContribution(int agentContribution) {
        this.agentContribution = agentContribution;
    }

}
