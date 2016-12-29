package selling.sunshine.dao.impl;

import common.sunshine.dao.BaseDao;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import selling.sunshine.dao.PurchaseDao;
import selling.sunshine.vo.customer.CustomerPurchase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 2016/12/28.
 */
@Repository
public class PurchaseDaoImpl extends BaseDao implements PurchaseDao {
    private Logger logger = LoggerFactory.getLogger(PurchaseDaoImpl.class);

    @Override
    public ResultData queryCustomerPurchase(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<CustomerPurchase> list = sqlSession.selectList("selling.purchase.customer.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryCustomerPurchaseByPage(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<CustomerPurchase> page = new DataTablePage<>(param);
        condition = handle(condition);
        if (!StringUtils.isEmpty(param.getsSearch())) {
            condition.put("search", new StringBuffer("%").append(param.getsSearch()).append("%").toString());
        }
        ResultData total = queryCustomerPurchase(condition);
        if (total.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List) total.getData()).size());
        page.setiTotalDisplayRecords(((List) total.getData()).size());
        List<CustomerPurchase> current = queryCustomerPurchaseByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        page.setData(current);
        result.setData(page);
        return result;
    }

    private List<CustomerPurchase> queryCustomerPurchaseByPage(Map<String, Object> condition, int start, int length) {
        List<CustomerPurchase> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.purchase.customer.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }
}
