package selling.sunshine.dao.impl;

import common.sunshine.dao.BaseDao;
import common.sunshine.model.selling.express.Express;
import common.sunshine.model.selling.express.Express4Agent;
import common.sunshine.model.selling.express.Express4Application;
import common.sunshine.model.selling.express.Express4Customer;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import selling.sunshine.dao.ExpressDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ExpressDaoImpl extends BaseDao implements ExpressDao {
    private Logger logger = LoggerFactory.getLogger(ExpressDaoImpl.class);

    private Object lock = new Object();

    /**
     * 添加快递单记录
     *
     * @param express
     * @return
     */
    @Transactional
    @Override
    public ResultData insertExpress4Agent(Express4Agent express) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                express.setExpressId(IDGenerator.generate("EPA"));
                sqlSession.insert("selling.express.insertExpress4Agent", express);
                result.setData(express);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    @Transactional
    @Override
    public ResultData insertExpress4Customer(Express4Customer express) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                express.setExpressId(IDGenerator.generate("EPC"));
                sqlSession.insert("selling.express.insertExpress4Customer", express);
                result.setData(express);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    /**
     * 根据条件查询快递列表
     *
     * @param condition
     * @return
     */
    @Override
    public ResultData queryExpress(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Express> list = sqlSession.selectList("selling.express.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData queryExpress4Agent(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Express> list = sqlSession.selectList("selling.express.query4Agent", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData queryExpress4Customer(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Express4Agent> list = sqlSession.selectList("selling.express.query4Customer", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData insertExpress4Application(Express4Application express) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                express.setExpressId(IDGenerator.generate("EPE"));
                sqlSession.insert("selling.express.insertExpress4Application", express);
                result.setData(express);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData queryExpress4Application(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Express4Application> list = sqlSession.selectList("selling.express.query4Application", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

	@Override
	public ResultData queryExpressByPage(Map<String, Object> condition, DataTableParam param) {
		ResultData result = new ResultData();
		DataTablePage<Express> page = new DataTablePage<>();
        page.setsEcho(param.getsEcho());
        if (!StringUtils.isEmpty(param.getParams())) {
            JSONObject json = JSON.parseObject(param.getParams());
            if (json.containsKey("start")) {
                condition.put("start", json.get("start"));            
            }
            if (json.containsKey("end")) {
            	 condition.put("end", json.get("end"));
			}
        }
        ResultData total = queryExpress(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List) total.getData()).size());
        page.setiTotalDisplayRecords(((List) total.getData()).size());
        List<Express> current = queryExpressByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
	}
	
    private List<Express> queryExpressByPage(Map<String, Object> condition, int start, int length) {
        List<Express> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.express.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }
}
