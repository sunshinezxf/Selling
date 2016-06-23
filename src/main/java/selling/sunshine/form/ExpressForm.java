package selling.sunshine.form;

import java.util.List;

public class ExpressForm {

    private String expressNumber;
    
    private List<ExpressItemForm> expressItem;

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public List<ExpressItemForm> getExpressItem() {
        return expressItem;
    }

    public void setExpressItem(List<ExpressItemForm> expressItem) {
        this.expressItem = expressItem;
    }
}
