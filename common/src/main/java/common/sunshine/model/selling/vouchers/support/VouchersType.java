package common.sunshine.model.selling.vouchers.support;

/**
 * Created by wxd on 2017/5/2.
 * 代金券类型
 */
public enum VouchersType {

    /*注册送的代金券，社群拓展奖励的代金券*/
    REGISTER(0),REWARD(1);

    private int code;

    VouchersType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
