package selling.sunshine.model;

public class BankCard extends Entity{
	private String bankCardId;
	private String bankCardNo;
	private Agent agent;
	private int preferred;
	
	public BankCard(){
		super();
		this.preferred = 1;
	}
	
	public BankCard(String bankCardNo, Agent agent){
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
	public int getPreferred() {
		return preferred;
	}
	public void setPreferred(int preferred) {
		this.preferred = preferred;
	}
	
	
}
