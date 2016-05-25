package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.RefundDao;
import selling.sunshine.model.Agent;
import selling.sunshine.model.OrderPool;
import selling.sunshine.model.RefundConfig;
import selling.sunshine.model.RefundRecord;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public ResultData refund() {                 
		ResultData result = new ResultData();
		Map<String, Object> con=new HashMap<>();
		List<Map<String, String>> agentGoodsList=sqlSession.selectList("selling.order.pool.queryAgentGoods", con);
		for (int i = 0; i < agentGoodsList.size(); i++) {
			Map<String, Object> condition=new HashMap<>();
			condition.put("agentId", agentGoodsList.get(i).get("agentId"));                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
			condition.put("goodsId", agentGoodsList.get(i).get("goodsId"));
			//按时间顺序从前往后排好的list
			List<OrderPool> poolList=sqlSession.selectList("selling.order.pool.query", condition);
			for (int j = 0; j < poolList.size();j++) {
				//当当前月和后两个月都达到了返现标准，那么就在返现记录里插上当前月的返现记录
				if ((!poolList.get(j).isBlockFlag())&&(!poolList.get(j+1).isBlockFlag())&&(!poolList.get(j+2).isBlockFlag())) {
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
						
						Map<String, Object> poolCondition=new HashMap<>();
						poolCondition.put("agentId", agentLevel2.getAgentId());
						poolCondition.put("goodsId", poolList.get(j).getGoods().getGoodsId());
						poolCondition.put("poolDate", poolList.get(j).getPoolDate());
						OrderPool pool2=(OrderPool)sqlSession.selectList("selling.order.pool", poolCondition).get(0);
						
						if (agentLevel2.getUpperAgent()!=null) {
							RefundRecord refundRecordLevel3=new RefundRecord();
							refundRecordLevel3.setRefundRecordId(IDGenerator.generate("RFR"));
							//当前agent为三级代理商
							refundRecord.setRefundAmount(poolList.get(j).getPrice()*refundConfig.getLevel3Percent());
							refundRecord.setRefundPercent(refundConfig.getLevel3Percent());
							refundRecord.setRefundName("代理商"+agent.getName()+"作为三级代理商在"+poolList.get(j).getPoolDate()+"购买"+poolList.get(j).getGoods().getName()+"达到返现标准，返现"+refundRecord.getRefundAmount()+"元");
							refundRecord.setOrderPool(poolList.get(j));
							sqlSession.insert("selling.refund.record.insert",refundRecord);
							//
							refundRecordLevel2.setRefundAmount(poolList.get(j).getPrice()*refundConfig.getLevel2Percent());
							refundRecordLevel2.setRefundPercent(refundConfig.getLevel2Percent());
							refundRecordLevel2.setOrderPool(pool2);
							refundRecordLevel2.setRefundName("");
							sqlSession.insert("selling.refund.record.insert",refundRecordLevel2);
							//
							poolCondition.put("agentId", agentLevel2.getUpperAgent().getAgentId());
							OrderPool pool3=(OrderPool)sqlSession.selectList("selling.order.pool", poolCondition).get(0);
							refundRecordLevel3.setRefundAmount(poolList.get(j).getPrice()*refundConfig.getLevel1Percent());
							refundRecordLevel3.setRefundPercent(refundConfig.getLevel1Percent());
							refundRecordLevel3.setOrderPool(pool3);
							refundRecordLevel3.setRefundName("");
							sqlSession.insert("selling.refund.record.insert",refundRecordLevel3);												
						}else{
							//当前agent为二级代理商
							refundRecord.setRefundAmount(poolList.get(j).getPrice()*refundConfig.getLevel2Percent());
							refundRecord.setRefundPercent(refundConfig.getLevel2Percent());
							refundRecord.setRefundName("代理商"+agent.getName()+"作为二级代理商在"+poolList.get(j).getPoolDate()+"购买"+poolList.get(j).getGoods().getName()+"达到返现标准，返现"+refundRecord.getRefundAmount()+"元");
							refundRecord.setOrderPool(poolList.get(j));
							sqlSession.insert("selling.refund.record.insert",refundRecord);
							
							//
							refundRecordLevel2.setRefundAmount(poolList.get(j).getPrice()*refundConfig.getLevel1Percent());
							refundRecordLevel2.setRefundPercent(refundConfig.getLevel1Percent());
							refundRecordLevel2.setOrderPool(pool2);
							refundRecordLevel2.setRefundName("");
							sqlSession.insert("selling.refund.record.insert",refundRecordLevel2);
							
						}
					}else {
						//当前agent为一级代理商
						refundRecord.setRefundAmount(poolList.get(j).getPrice()*refundConfig.getLevel1Percent());
						refundRecord.setRefundPercent(refundConfig.getLevel1Percent());
						refundRecord.setRefundName("代理商"+agent.getName()+"作为一级代理商在"+poolList.get(j).getPoolDate()+"购买"+poolList.get(j).getGoods().getName()+"达到返现标准，返现"+refundRecord.getRefundAmount()+"元");
						refundRecord.setOrderPool(poolList.get(j));
						sqlSession.insert("selling.refund.record.insert",refundRecord);
					}
				}
			}
		}
		
		return result;					
	}
}
