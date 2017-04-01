package common.sunshine.model.selling.agent;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.agent.support.AgentType;
import org.springframework.util.StringUtils;


/**
 * 该类为代理商模型类, 包含代理商的各属性
 * Created by sunshine on 4/7/16.
 */
public class Agent extends Entity {
    private String agentId;

    /* 代理商姓名 */
    private String name;

    /* 代理商性别 M-男 F-女*/
    private String gender;

    /* 手机号 */
    private String phone;

    /* 地址 */
    private String address;

    /* 银行卡信息 */
    private String card;

    /* 账号密码 */
    private String password;

    /* 微信号(open_id) */
    private String wechat;
    
    /* 微信号(用户设置的WeChat ID) */
    private String wechat_id; 

    /* 授权标识位 */
    private boolean granted;

    /* 账户余额 */
    private double coffer;

    /* 代理商累计返现金额 */
    private double agentRefund;

    /* 代理商生成群规模 */
    private int claimScale;

    /* 代理商类别 */
    private AgentType agentType;

    /* 当前代理商的上级代理 */
    private common.sunshine.model.selling.agent.lite.Agent upperAgent;

    /**
     * 默认构造方法, 代理商默认为普通代理, 未授权, 声称群规模为0
     */
    public Agent() {
        super();
        agentType = AgentType.ORDINARY;
        granted = false;
        claimScale = 0;
    }

    public Agent(String phone, String password) {
        this();
        this.phone = phone;
        this.password = password;
    }

    public Agent(String name, String gender, String phone, String address, String card, String password) {
        this();
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.card = card;
        this.password = password;
    }

    public Agent(String name, String gender, String phone, String address, String card, String password, String wechat, String wechat_id) {
        this(name, gender, phone, address, card, password);
        if (!StringUtils.isEmpty(wechat)) {
            this.wechat = wechat;
        }
        if (!StringUtils.isEmpty(wechat_id)) {
            this.wechat_id = wechat_id;
        }
    }

    public Agent(String name, String gender, String phone, String address, String card, String password, String wechat, String wechat_id, int claimScale) {
        this(name, gender, phone, address, card, password, wechat, wechat_id);
        this.claimScale = claimScale;
    }

    public Agent(String agentId, String name, String gender, String phone, String address, String card, String password, String wechat, String wechat_id, boolean granted, double coffer, double agentRefund, int claimScale, AgentType agentType, common.sunshine.model.selling.agent.lite.Agent upperAgent) {
        this(name, gender, phone, address, card, password, wechat, wechat_id, claimScale);
        this.agentId = agentId;
        this.granted = granted;
        this.coffer = coffer;
        this.agentRefund = agentRefund;
        this.agentType = agentType;
        this.upperAgent = upperAgent;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }
    
    public String getWechat_id() {
		return wechat_id;
	}

	public void setWechat_id(String wechat_id) {
		this.wechat_id = wechat_id;
	}

	public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }

    public double getCoffer() {
        return coffer;
    }

    public void setCoffer(double coffer) {
        this.coffer = coffer;
    }

    public common.sunshine.model.selling.agent.lite.Agent getUpperAgent() {
        return upperAgent;
    }

    public void setUpperAgent(common.sunshine.model.selling.agent.lite.Agent upperAgent) {
        this.upperAgent = upperAgent;
    }

    public double getAgentRefund() {
        return agentRefund;
    }

    public void setAgentRefund(double agentRefund) {
        this.agentRefund = agentRefund;
    }

    public int getClaimScale() {
        return claimScale;
    }

    public void setClaimScale(int claimScale) {
        this.claimScale = claimScale;
    }

    public AgentType getAgentType() {
        return agentType;
    }

    public void setAgentType(AgentType agentType) {
        this.agentType = agentType;
    }

}
