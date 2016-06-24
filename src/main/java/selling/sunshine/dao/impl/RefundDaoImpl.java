package selling.sunshine.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.quartz.impl.jdbcjobstore.HSQLDBDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.RefundDao;
import selling.sunshine.model.Agent;
import selling.sunshine.model.OrderPool;
import selling.sunshine.model.RefundConfig;
import selling.sunshine.model.RefundRecord;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.NewCookie;

/**
 * Created by sunshine on 5/17/16.
 */
public class RefundDaoImpl extends BaseDao implements RefundDao {

    private Logger logger = LoggerFactory.getLogger(RefundDaoImpl.class);

    private Object lock = new Object();

    @Transactional
    @Override
    public ResultData insertRefundConfig(RefundConfig config) {
        ResultData result = new ResultData();
        config.setRefundConfigId(IDGenerator.generate("RCG"));
        synchronized (lock) {
            try {
                Map<String, Object> condition = new HashMap<>();
                condition.put("goodsId", config.getGoods().getGoodsId());
                condition.put("blockFlag", false);                                      
                RefundConfig target = sqlSession.selectOne("selling.refund.config.query", condition);
                if (target != null) {
                    target.setBlockFlag(true);
                    sqlSession.update("selling.refund.config.block", target);
                }
                sqlSession.insert("selling.refund.config.insert", config);
                result.setData(config);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    @Override
    public ResultData queryRefundConfig(Map<String,	 Object> condition) {
        ResultData result = new ResultData();
        condition = handle(condition);
        try {
            List<RefundConfig> list = sqlSession.selectList("selling.refund.config.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
    
    @Override
	public ResultData queryRefundRecord(Map<String, Object> condition) {
    	 ResultData result = new ResultData();
         condition = handle(condition);
         try {
             List<RefundRecord> list = sqlSession.selectList("selling.refund.record.query", condition);
             result.setData(list);
         } catch (Exception e) {
             logger.error(e.getMessage());
             result.setResponseCode(ResponseCode.RESPONSE_ERROR);
             result.setDescription(e.getMessage());
         }
         return result;
	}

	@Override
	public ResultData refundRecord() {                 
		ResultData result = new ResultData();
		Calendar now = Calendar.getInstance(); 
		int year=now.get(Calendar.YEAR);
		int month=now.get(Calendar.MONTH)+1;
		int day=now.get(Calendar.DAY_OF_MONTH);
		try{
		Map<String, Object> condition=new HashMap<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		String currentDate=dateFormat.format(new Timestamp(System.currentTimeMillis()));
		condition.put("date", currentDate + "%");
		List<OrderPool> poolList=sqlSession.selectList("selling.order.pool.query", condition);
		if (poolList.size()>0) {
			Set<OrderPool> h = new HashSet<OrderPool>(poolList);  
			poolList.clear();
			poolList.addAll(h);
			for (int j = 0; j < poolList.size();j++) {
		        String date = dateFormat.format(poolList.get(j).getPoolDate());
				RefundConfig refundConfig= poolList.get(j).getRefundConfig();
				Agent agent=poolList.get(j).getAgent();
				RefundRecord refundRecord=new RefundRecord();
				refundRecord.setRefundRecordId(IDGenerator.generate("RFR"));
				if (agent.getUpperAgent()!=null) {
					RefundRecord refundRecordLevel2=new RefundRecord();
					refundRecordLevel2.setRefundRecordId(IDGenerator.generate("RFR"));
					
					Map<String, Object> level2Con=new HashMap<>();
					level2Con.put("agentId", agent.getUpperAgent().getAgentId());				
					Agent agentLevel2=(Agent)sqlSession.selectList("selling.agent.query",level2Con).get(0);
					
					if (agentLevel2.getUpperAgent()!=null) {
						Map<String, Object> level3Con=new HashMap<>();
						level3Con.put("agentId", agentLevel2.getUpperAgent().getAgentId());				
						Agent agentLevel3=(Agent)sqlSession.selectList("selling.agent.query",level3Con).get(0);
						RefundRecord refundRecordLevel3=new RefundRecord();
						refundRecordLevel3.setRefundRecordId(IDGenerator.generate("RFR"));
						//当前agent为三级代理商
						refundRecord.setRefundAmount(poolList.get(j).getRefundAmount());
						refundRecord.setRefundPercent(refundConfig.getLevel1Percent());
						refundRecord.setRefundDescription("代理商"+agent.getName()+"在"+date+"购买"+poolList.get(j).getGoods().getName()+"达到返现标准，返现"+refundRecord.getRefundAmount()+"元");
						//
						refundRecordLevel2.setRefundAmount(poolList.get(j).getQuantity()*refundConfig.getLevel2Percent());
						refundRecordLevel2.setRefundPercent(refundConfig.getLevel2Percent());
						refundRecordLevel2.setRefundDescription("代理商"+agentLevel2.getName()+"与代理商"+agent.getName()+"直接关联，获得代理商"+agent.getName()+"在"+date+"购买"+poolList.get(j).getGoods().getName()+"的社群拓展奖励，奖励"+refundRecordLevel2.getRefundAmount()+"元");
						//
						refundRecordLevel3.setRefundAmount(poolList.get(j).getQuantity()*refundConfig.getLevel3Percent());
						refundRecordLevel3.setRefundPercent(refundConfig.getLevel3Percent());
						refundRecordLevel3.setOrderPool(poolList.get(j));
						refundRecordLevel3.setAgent(new selling.sunshine.model.lite.Agent(agentLevel3));
						refundRecordLevel3.setRefundName(year+"年"+month+"月"+day+"日"+"社群拓展奖励账单");
						refundRecordLevel3.setRefundDescription("代理商"+agentLevel3.getName()+"与代理商"+agent.getName()+"间接关联，获得代理商"+agent.getName()+"在"+date+"购买"+poolList.get(j).getGoods().getName()+"的社群拓展奖励，奖励"+refundRecordLevel3.getRefundAmount()+"元");
						refundRecordLevel3.setBlockFlag(false);
						sqlSession.insert("selling.refund.record.insert",refundRecordLevel3);	
//						//更新agentLevel3的账户余额以及返现总额
//						agentLevel3.setAgentRefund(agentLevel3.getAgentRefund()+refundRecordLevel3.getRefundAmount());
//						agentLevel3.setCoffer(agentLevel3.getCoffer()+refundRecordLevel3.getRefundAmount());
//						sqlSession.update("selling.agent.update",agentLevel3);
					}else{
						//当前agent为二级代理商
						refundRecord.setRefundAmount(poolList.get(j).getRefundAmount());
						refundRecord.setRefundPercent(refundConfig.getLevel1Percent());
						refundRecord.setRefundDescription("代理商"+agent.getName()+"在"+date+"购买"+poolList.get(j).getGoods().getName()+"达到返现标准，返现"+refundRecord.getRefundAmount()+"元");						
						//
						refundRecordLevel2.setRefundAmount(poolList.get(j).getQuantity()*refundConfig.getLevel2Percent());
						refundRecordLevel2.setRefundPercent(refundConfig.getLevel2Percent());
						refundRecordLevel2.setRefundDescription("代理商"+agentLevel2.getName()+"与代理商"+agent.getName()+"直接关联，获得代理商"+agent.getName()+"在"+date+"购买"+poolList.get(j).getGoods().getName()+"的社群拓展奖励，奖励"+refundRecordLevel2.getRefundAmount()+"元");							
					}
					refundRecordLevel2.setOrderPool(poolList.get(j));
					refundRecordLevel2.setRefundName(year+"年"+month+"月"+day+"日"+"社群拓展奖励账单");
					refundRecordLevel2.setAgent(new selling.sunshine.model.lite.Agent(agentLevel2));
					refundRecordLevel2.setBlockFlag(false);
					sqlSession.insert("selling.refund.record.insert",refundRecordLevel2);	
//					//更新agentLevel2的账户余额以及返现总额
//					agentLevel2.setAgentRefund(agentLevel2.getAgentRefund()+refundRecordLevel2.getRefundAmount());
//					agentLevel2.setCoffer(agentLevel2.getCoffer()+refundRecordLevel2.getRefundAmount());
//					sqlSession.update("selling.agent.update",agentLevel2);
				}else {
					//当前agent为一级代理商
					refundRecord.setRefundAmount(poolList.get(j).getRefundAmount());
					refundRecord.setRefundPercent(refundConfig.getLevel1Percent());		
					refundRecord.setRefundDescription("代理商"+agent.getName()+"在"+date+"购买"+poolList.get(j).getGoods().getName()+"达到返现标准，返现"+refundRecord.getRefundAmount()+"元");								
				}
				refundRecord.setOrderPool(poolList.get(j));
				refundRecord.setRefundName(year+"年"+month+"月"+day+"日"+"返现账单");
				refundRecord.setAgent(new selling.sunshine.model.lite.Agent(agent));
				refundRecord.setBlockFlag(false);
				sqlSession.insert("selling.refund.record.insert",refundRecord);
//				//更新agent的账户余额以及返现总额
//				agent.setAgentRefund(agent.getAgentRefund()+refundRecord.getRefundAmount());
//				agent.setCoffer(agent.getCoffer()+refundRecord.getRefundAmount());
//				sqlSession.update("selling.agent.update",agent);
			}
		}
		 result.setResponseCode(ResponseCode.RESPONSE_OK);
		} catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
		
		return result;							
	}
	
	public ResultData refund() {
		ResultData resultData=new ResultData();
		try {			
		Map<String, Object> condition=new HashMap<>();
		condition.put("blockFlag", false);
		List<RefundRecord> refundRecordList=sqlSession.selectList("selling.refund.record.query", condition);
		for (RefundRecord refundRecord:refundRecordList) {
			condition.clear();
			condition.put("agentId", refundRecord.getAgent().getAgentId());
			Agent agent=(Agent)sqlSession.selectList("selling.agent.query",condition).get(0);
			//更新agent的账户余额以及返现总额
			agent.setCoffer(agent.getCoffer()+refundRecord.getRefundAmount());
			agent.setAgentRefund(agent.getAgentRefund()+refundRecord.getRefundAmount());	
			sqlSession.update("selling.agent.update",agent);
		}
		
		sqlSession.update("selling.refund.record.updateBatch");		
		resultData.setResponseCode(ResponseCode.RESPONSE_OK);
		} catch (Exception e) {
            logger.error(e.getMessage());
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription(e.getMessage());
		}
		return resultData;
	}

	@Override
	public ResultData queryRefundRecordByPage(Map<String, Object> condition,
			DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<RefundRecord> page = new DataTablePage<RefundRecord>();
        page.setsEcho(param.getsEcho());
        logger.debug(JSONObject.toJSONString(condition));
        
        if (!StringUtils.isEmpty(param.getsSearch())) {      	 
        	 condition.put("refundDescription", "%"+param.getsSearch()+"%");
		}
//        if (!StringUtils.isEmpty(condition.get("createAt"))) {      
//        	
//        }  
        ResultData total = queryRefundRecord(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List<RefundRecord>) total.getData()).size());
        page.setiTotalDisplayRecords(((List<RefundRecord>) total.getData()).size());
        List<RefundRecord> current = queryRefundRecordByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
	}
	
    private List<RefundRecord> queryRefundRecordByPage(Map<String, Object> condition, int start, int length) {
        List<RefundRecord> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.refund.record.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

	
}
