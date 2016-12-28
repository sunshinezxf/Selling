package selling.sunshine.dao;

import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 2016/12/28.
 */
public interface PurchaseDao {
    ResultData queryCustomerPurchase(Map<String, Object> condition);

    ResultData queryCustomerPurchaseByPage(Map<String, Object> condition, DataTableParam param);
}
