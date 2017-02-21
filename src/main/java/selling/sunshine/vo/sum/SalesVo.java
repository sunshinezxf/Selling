package selling.sunshine.vo.sum;

/**
 * 每个月的销售金额
 * Created by sunshine on 2017/2/21.
 */
public class SalesVo {
    private String createAt;

    private double price;

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
