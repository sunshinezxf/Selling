package selling.sunshine.model;

public class BankCard extends Entity {
    private String bankCardId;
    private String bankCardNo;
    private Agent agent;
    private boolean preferred;

    public BankCard() {
        super();
        preferred = true;
    }

    public BankCard(String bankCardNo, Agent agent) {
        this();
        this.bankCardNo = bankCardNo;
        this.agent = agent;
    }

    public String getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(String bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public boolean getPreferred() {
        return preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }


}
