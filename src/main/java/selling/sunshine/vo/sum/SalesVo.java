package selling.sunshine.vo.sum;

import java.sql.Timestamp;

/**
 * 每个月的销售金额
 * Created by sunshine on 2017/2/21.
 */
public class SalesVo {
    private Timestamp createAt;

    private double price;

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
