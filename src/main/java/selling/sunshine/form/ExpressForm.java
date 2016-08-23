package selling.sunshine.form;

import java.util.List;

import common.sunshine.model.selling.express.Express;

public class ExpressForm {

    private String expressNumber;
    
    private List<Express> expressItem;



    public String getExpressNumber() {
		return expressNumber;
	}

	public void setExpressNumber(String expressNumber) {
		this.expressNumber = expressNumber;
	}

	public List<Express> getExpressItem() {
		return expressItem;
	}

	public void setExpressItem(List<Express> expressItem) {
		this.expressItem = expressItem;
	}


}
