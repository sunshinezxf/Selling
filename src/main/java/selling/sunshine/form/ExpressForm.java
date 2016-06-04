package selling.sunshine.form;

import java.util.List;

import selling.sunshine.model.Express;

public class ExpressForm {

	private List<Express> expressList;
	private String expressNumber;

	public List<Express> getExpressList() {
		return expressList;
	}

	public void setExpressList(List<Express> expressList) {
		this.expressList = expressList;
	}

	public String getExpressNumber() {
		return expressNumber;
	}

	public void setExpressNumber(String expressNumber) {
		this.expressNumber = expressNumber;
	}

}
