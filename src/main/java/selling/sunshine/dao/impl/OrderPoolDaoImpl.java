package selling.sunshine.dao.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.sunshine.dao.BaseDao;
import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.agent.support.AgentType;
import selling.sunshine.dao.OrderPoolDao;
import selling.sunshine.model.AgentVitality;
import selling.sunshine.model.OrderPool;
import selling.sunshine.model.cashback.CashBackRecord;
import selling.sunshine.model.cashback.support.CashBackLevel;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

public class OrderPoolDaoImpl extends BaseDao implements OrderPoolDao {
	private Logger logger = LoggerFactory.getLogger(OrderItemDaoImpl.class);

	private Object lock = new Object();
	
	@Override
	public ResultData queryOrderPool(Map<String, Object> condition) {
		ResultData result = new ResultData();
		synchronized(lock){
			try{
				List<OrderPool> list = sqlSession.selectList("selling.order.pool.query", condition);
				result.setData(list);
			}catch (Exception e) {
	            logger.error(e.getMessage());
	            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
	            result.setDescription(e.getMessage());
	        } finally {
	            return result;
	        }
		}
	}

	@Override
	public ResultData checkOrderPool(Map<String, Object> condition) {
	
     //   condition.put("blockFlag", false);
     //   condition.put("monthConfig", 4);
		ResultData result = new ResultData();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		//查询过去n个月达到返现标准的order pool记录列表
		try{
		List<OrderPool> poolList = sqlSession.selectList("selling.order.pool.query", condition);
		for (OrderPool orderPool:poolList) {
			//对每一条order pool记录，扫描refundrecord表，检查是否有遗漏的返现记录没有生成
			condition.clear();
			condition.put("orderPoolId", orderPool.getOrderPoolId());
			List<CashBackRecord> list=sqlSession.selectList("selling.refund.record.query", condition);
			if (list.size()==3) {//有三条返现记录表示没有遗漏，不用处理
				continue;
			}else if (list.size()==1||list.size()==2) {//有一条或两条返现记录表示只有需要同时检查上级和上上级代理商看有没有遗漏
				//首先查询有没有上级代理商，没有的话就不用检查了
				condition.clear();
				condition.put("agentId", orderPool.getAgent().getAgentId());
				Agent agent = sqlSession.selectOne("selling.agent.query", condition);
				
				if (agent.getUpperAgent() != null) {
					Map<String,Object> map=new HashMap<>();
					for (CashBackRecord cashBackRecord:list) {
						map.put(cashBackRecord.getAgent().getAgentId(),1 );
					}
					boolean flag1=true,flag2=true;
					condition.put("agentId", agent.getUpperAgent().getAgentId());
					Agent agent2 = sqlSession.selectOne("selling.agent.query", condition);
					Agent agent3=null;
					if (agent2.getUpperAgent()!=null) {//有上上级代理，两个都需要检查
						condition.put("agentId", agent2.getUpperAgent().getAgentId());
						agent3 = sqlSession.selectOne("selling.agent.query", condition);
						 if (map.containsKey(agent3.getAgentId())) {
		                    	flag2=false;
						 }
					}
                    if (map.containsKey(agent2.getAgentId())) {
                    	flag1=false;
					}
                    String date = dateFormat.format(orderPool.getPoolDate());
               	    String time = new SimpleDateFormat("yyyy年MM月dd日").format(orderPool.getCreateAt());
                    if (flag1) {//flag1为true表示上级代理商不在返现记录表里，需要检查
						if (agent2.getAgentType()==AgentType.ORDINARY) {//上级代理商非客服可以有机会得到返现
							condition.clear();
							condition.put("blockFlag", false);
							AgentVitality agentVitality=(AgentVitality)sqlSession.selectOne("selling.agent.vitality.query", condition);
							condition.clear();
	                        condition.put("agentId", agent2.getAgentId());
	                        condition.put("poolDate", orderPool.getPoolDate());
	                        boolean flag=false;
	                        if (!sqlSession.selectList("selling.order.pool.query", condition).isEmpty()) {                                          	     
	                        	 List<OrderPool> list2=sqlSession.selectList("selling.order.pool.query", condition);
                            	 int quantity=0;
                            	 double price=0.0;
                            	 for (OrderPool pool:list2) {
									quantity+=pool.getQuantity();
									price+=pool.getPrice();
								 }
                            	 if (quantity>=agentVitality.getVitalityQuantity()&&price>=agentVitality.getVitalityPrice()) {
                            		 flag=false;//当购买金额和数量都达到活跃度配置标准，就可以获取下级代理商的返现
								 }
	                        }else {
	                   	         if (agentVitality.getVitalityQuantity()==0&&agentVitality.getVitalityPrice()==0.0) {
	                   	        	flag=true;
							     }
						    }
	                        if (flag&&agent2.getAgentType()==AgentType.ORDINARY) {//flag为true表示有遗漏，需要添加上去
	                        	  CashBackRecord refundRecordLevel2 = new CashBackRecord();
                                  refundRecordLevel2.setRecordId(IDGenerator.generate("RFR"));
                                  if (agent3==null) {
                                	  refundRecordLevel2.setAmount(
                                			  orderPool.getQuantity() * orderPool.getRefundConfig().getLevel2Percent());
                                      refundRecordLevel2.setPercent(orderPool.getRefundConfig().getLevel2Percent());
                                      refundRecordLevel2.setDescription("代理商" + agent2.getName() + "与代理商"
                                              + agent.getName() + "直接关联，获得代理商" + agent.getName() + "在" + date + "购买"
                                              + orderPool.getGoods().getName()+orderPool.getQuantity() + "盒的社群拓展奖励，奖励"
                                              + refundRecordLevel2.getAmount() + "元");
								  }else {
									  refundRecordLevel2.setAmount(
											  orderPool.getQuantity() * orderPool.getRefundConfig().getLevel2Percent());
                                      refundRecordLevel2.setPercent(orderPool.getRefundConfig().getLevel2Percent());
                                      refundRecordLevel2.setDescription("代理商" + agent2.getName() + "与代理商"
                                              + agent.getName() + "直接关联，获得代理商" + agent.getName() + "在" + date + "购买"
                                              + orderPool.getGoods().getName()+orderPool.getQuantity() + "盒的社群拓展奖励，奖励"
                                              + refundRecordLevel2.getAmount() + "元");   
								 }
                                  refundRecordLevel2.setOrderPool(orderPool);
                                  refundRecordLevel2.setTitle(time + "社群拓展奖励账单");
                                  refundRecordLevel2.setAgent(new common.sunshine.model.selling.agent.lite.Agent(agent2));
                                  refundRecordLevel2.setLevel(CashBackLevel.DIRECT);
                                  refundRecordLevel2.setBlockFlag(false);
                                  refundRecordLevel2.setCreateAt(orderPool.getCreateAt());
                                  sqlSession.insert("selling.refund.record.insert", refundRecordLevel2);
	                        }
						}
					}
                    if (flag2) {//flag2为true表示上上级代理商存在且不在返现记录表里，需要检查
						if (agent3!=null&&agent3.getAgentType()==AgentType.ORDINARY) {//上上级代理商非客服可以有机会得到返现
							condition.clear();
							condition.put("blockFlag", false);
							AgentVitality agentVitality=(AgentVitality)sqlSession.selectOne("selling.agent.vitality.query", condition);
							condition.clear();
	                        condition.put("agentId", agent3.getAgentId());
	                        condition.put("poolDate", orderPool.getPoolDate());
	                        boolean flag=false;
	                        if (!sqlSession.selectList("selling.order.pool.query", condition).isEmpty()) {                                          	     
	                        	 List<OrderPool> list2=sqlSession.selectList("selling.order.pool.query", condition);
                            	 int quantity=0;
                            	 double price=0.0;
                            	 for (OrderPool pool:list2) {
									quantity+=pool.getQuantity();
									price+=pool.getPrice();
							     }
                            	 if (quantity>=agentVitality.getVitalityQuantity()&&price>=agentVitality.getVitalityPrice()) {
                            		 flag=false;//当购买金额和数量都达到活跃度配置标准，就可以获取下级代理商的返现
								 }
	                        }else {
	                   	         if (agentVitality.getVitalityQuantity()==0&&agentVitality.getVitalityPrice()==0.0) {
	                   	        	flag=true;
							     }
						    }
	                        if (flag&&agent3.getAgentType()==AgentType.ORDINARY) {//flag为true表示有遗漏，需要添加上去
	                        	 CashBackRecord refundRecordLevel3 = new CashBackRecord();
                                 refundRecordLevel3.setRecordId(IDGenerator.generate("RFR"));
                                 refundRecordLevel3.setAmount(
                                		 orderPool.getQuantity() * orderPool.getRefundConfig().getLevel3Percent());
                                 refundRecordLevel3.setPercent(orderPool.getRefundConfig().getLevel3Percent());
                                 refundRecordLevel3.setOrderPool(orderPool);
                                 refundRecordLevel3.setAgent(new common.sunshine.model.selling.agent.lite.Agent(agent3));
                                 refundRecordLevel3
                                         .setTitle(time + "社群拓展奖励账单");
                                 refundRecordLevel3.setDescription("代理商" + agent3.getName() + "与代理商"
                                         + agent.getName() + "间接关联，获得代理商" + agent.getName() + "在" + date + "购买"
                                         + orderPool.getGoods().getName() +orderPool.getQuantity()+ "盒的社群拓展奖励，奖励"
                                         + refundRecordLevel3.getAmount() + "元");
                                 refundRecordLevel3.setLevel(CashBackLevel.INDIRECT);
                                 refundRecordLevel3.setBlockFlag(false);
                                 refundRecordLevel3.setCreateAt(orderPool.getCreateAt());
                                 sqlSession.insert("selling.refund.record.insert", refundRecordLevel3);
							}
						}
					}
				}
            	
			}else {
				//其他非正常情况，说明之前的返现代理有错误，抛出异常
				logger.error("refund record has errors");
	            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
	            result.setDescription("refund record has errors");
			}			
		}
		}catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
	}

}
