package selling.sunshine.dao;

import selling.sunshine.model.gift.GiftApply;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 8/9/16.
 */
public interface GiftApplyDao {
    ResultData insertGiftApply(GiftApply apply);

    ResultData queryGiftApply(Map<String, Object> condition);

    ResultData queryGiftApplyByPage(Map<String, Object> condition, DataTableParam param);
}
