package selling.sunshine.dao.impl;

import com.alibaba.fastjson.JSONObject;

import common.sunshine.utils.IDGenerator;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import common.sunshine.dao.BaseDao;
import selling.sunshine.dao.RefundDao;
import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.agent.support.AgentType;
import selling.sunshine.model.AgentVitality;
import selling.sunshine.model.OrderPool;
import selling.sunshine.model.RefundConfig;
import selling.sunshine.model.cashback.support.CashBackLevel;
import selling.sunshine.model.cashback.CashBackRecord;
import common.sunshine.model.selling.goods.Goods4Agent;
import selling.sunshine.model.sum.TotalQuantityAll;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

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
		synchronized (lock) {
			try {
				Map<String, Object> condition = new HashMap<>();
				condition.put("refundConfigId", config.getRefundConfigId());
				RefundConfig target = sqlSession.selectOne("selling.refund.config.query", condition);
				if (target != null) {
					target.setBlockFlag(true);
					sqlSession.update("selling.refund.config.block", target);
				}
				config.setRefundConfigId(IDGenerator.generate("RCG"));
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
	public ResultData queryRefundConfig(Map<String, Object> condition) {
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
			List<CashBackRecord> list = sqlSession.selectList("selling.refund.record.query", condition);
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

//	/**
//	 * 根据order_pool生成refun_record表
//	 * @return
//	 */
//	@Override
//	public ResultData refundRecord() {
//		ResultData result = new ResultData();
//		Calendar now = Calendar.getInstance();
//		String time = new SimpleDateFormat("yyyy年MM月dd日").format(now.getTime());
//		try {
//			Map<String, Object> condition = new HashMap<>();
//			// 查询上个月的订单统计信息
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
//			Calendar current = Calendar.getInstance();
//			current.add(Calendar.MONTH, -1);
//			String currentDate = dateFormat.format(current.getTime());
//			condition.put("blockFlag", false);
//			condition.put("date", currentDate + "%");
//			List<OrderPool> poolList = sqlSession.selectList("selling.order.pool.query", condition);
//			if (!poolList.isEmpty()) {
//				for (int j = 0; j < poolList.size(); j++) {
//					// 返现要求：连续几个月达到返现数量标准
//					String date = dateFormat.format(poolList.get(j).getPoolDate());
//					RefundConfig refundConfig = poolList.get(j).getRefundConfig();
//					int monthConfig = refundConfig.getMonthConfig();
//					boolean monthFlag = false;
//					for (int i = 1; i < monthConfig; i++) {
//						Date d = poolList.get(j).getPoolDate();
//						Calendar calendar = Calendar.getInstance();
//						calendar.setTime(d);
//						calendar.add(Calendar.MONTH, -i);
//						condition.clear();
//						condition.put("agentId", poolList.get(j).getAgent().getAgentId());
//						condition.put("goodsId", poolList.get(j).getGoods().getGoodsId());
//						condition.put("date", dateFormat.format(calendar.getTime()) + "%");
//						condition.put("blockFlag", false);
//						// 若其中某一个月的返现数量没有达标，则返现不成功
//						if (sqlSession.selectList("selling.order.pool.query", condition).isEmpty()) {
//							monthFlag = true;
//							break;
//						}
//					}
//					if (monthFlag) {
//						continue;
//					} else {
//						// 连续几个月都达到了返现标准，生成返现记录
//						common.sunshine.model.selling.agent.lite.Agent agentLite = poolList.get(j).getAgent();
//						condition.clear();
//						condition.put("agentId", agentLite.getAgentId());
//						Agent agent = sqlSession.selectOne("selling.agent.query", condition);
//						// 查询活跃度配置表
//						Map<String, Object> con4 = new HashMap<>();
//						con4.put("blockFlag", false);
//						AgentVitality agentVitality =  sqlSession
//								.selectOne("selling.agent.vitality.query", con4);
//						if (agent.getAgentType() == AgentType.ORDINARY) {
//							CashBackRecord refundRecord = new CashBackRecord();
//							refundRecord.setRecordId(IDGenerator.generate("RFR"));
//							if (agent.getUpperAgent() != null) {
//								// 上级代理商要获得社群拓展奖励的前提是自己当月购买数量也达到返现标准且该上级代理商不是客服
//								condition.put("agentId", agent.getUpperAgent().getAgentId());
//								Agent agent2 = sqlSession.selectOne("selling.agent.query", condition);
//								condition.clear();
//								condition.put("agentId", agent.getUpperAgent().getAgentId());
//								condition.put("poolDate", poolList.get(j).getPoolDate());
//								boolean blockFlag = true;// blockFlag为true表示不能获取下级代理商的返现
//								if (!sqlSession.selectList("selling.order.pool.query", condition).isEmpty()) {
//									List<OrderPool> list = sqlSession.selectList("selling.order.pool.query", condition);
//									int quantity = 0;
//									double price = 0.0;
//									for (OrderPool pool : list) {
//										quantity += pool.getQuantity();
//										price += pool.getPrice();
//									}
//									if (quantity >= agentVitality.getVitalityQuantity()
//											&& price >= agentVitality.getVitalityPrice()) {
//										blockFlag = false;// 当购买金额和数量都达到活跃度配置标准，就可以获取下级代理商的返现
//									}
//								} else {
//									// 当上级代理商本月没有购买时，查看活跃度配置，假如活跃度配置为0，0，同样可以获取返现，否则不能
//									if (agentVitality.getVitalityQuantity() == 0
//											&& agentVitality.getVitalityPrice() == 0.0) {
//										blockFlag = false;
//									}
//								}
//								CashBackRecord refundRecordLevel2 = new CashBackRecord();
//								refundRecordLevel2.setRecordId(IDGenerator.generate("RFR"));
//								if (agent2.getUpperAgent() != null) {
//									condition.clear();
//									condition.put("agentId", agent2.getUpperAgent().getAgentId());
//									Agent agent3 = sqlSession.selectOne("selling.agent.query", condition);
//									condition.clear();
//									condition.put("agentId", agent2.getUpperAgent().getAgentId());
//									condition.put("poolDate", poolList.get(j).getPoolDate());
//									boolean blockFlag2 = true;
//									if (!sqlSession.selectList("selling.order.pool.query", condition).isEmpty()) {
//										List<OrderPool> list = sqlSession.selectList("selling.order.pool.query",
//												condition);
//										int quantity = 0;
//										double price = 0.0;
//										for (OrderPool pool : list) {
//											quantity += pool.getQuantity();
//											price += pool.getPrice();
//										}
//										if (quantity >= agentVitality.getVitalityQuantity()
//												&& price >= agentVitality.getVitalityPrice()) {
//											blockFlag2 = false;// 当购买金额和数量都达到活跃度配置标准，就可以获取下级代理商的返现
//										}
//									} else {
//										if (agentVitality.getVitalityQuantity() == 0
//												&& agentVitality.getVitalityPrice() == 0.0) {
//											blockFlag2 = false;
//										}
//									}
//									if (!blockFlag2) {
//										CashBackRecord refundRecordLevel3 = new CashBackRecord();
//										refundRecordLevel3.setRecordId(IDGenerator.generate("RFR"));
//										refundRecordLevel3.setAmount(
//												poolList.get(j).getQuantity() * refundConfig.getLevel3Percent());
//										refundRecordLevel3.setPercent(refundConfig.getLevel3Percent());
//										refundRecordLevel3.setOrderPool(poolList.get(j));
//										refundRecordLevel3
//												.setAgent(new common.sunshine.model.selling.agent.lite.Agent(agent3));
//										refundRecordLevel3.setTitle(time + "社群拓展奖励账单");
//										refundRecordLevel3.setDescription("代理商" + agent3.getName() + "与代理商"
//												+ agent.getName() + "间接关联，获得代理商" + agent.getName() + "在" + date + "购买"
//												+ poolList.get(j).getGoods().getName() + poolList.get(j).getQuantity()
//												+ "盒的社群拓展奖励，奖励" + refundRecordLevel3.getAmount() + "元");
//										refundRecordLevel3.setLevel(CashBackLevel.INDIRECT);
//										refundRecordLevel3.setBlockFlag(false);
//										if (agent3.getAgentType() == AgentType.ORDINARY) {
//											sqlSession.insert("selling.refund.record.insert", refundRecordLevel3);
//										}
//									}
//
//									// 当前agent为三级代理商
//									refundRecord.setAmount(poolList.get(j).getRefundAmount());
//									refundRecord.setPercent(refundConfig.getLevel1Percent());
//									refundRecord.setDescription("代理商" + agent.getName() + "在" + date + "购买"
//											+ poolList.get(j).getGoods().getName() + poolList.get(j).getQuantity()
//											+ "盒，达到返现标准，返现" + refundRecord.getAmount() + "元");
//									//
//
//									refundRecordLevel2
//											.setAmount(poolList.get(j).getQuantity() * refundConfig.getLevel2Percent());
//									refundRecordLevel2.setPercent(refundConfig.getLevel2Percent());
//									refundRecordLevel2.setDescription("代理商" + agent2.getName() + "与代理商"
//											+ agent.getName() + "直接关联，获得代理商" + agent.getName() + "在" + date + "购买"
//											+ poolList.get(j).getGoods().getName() + poolList.get(j).getQuantity()
//											+ "盒的社群拓展奖励，奖励" + refundRecordLevel2.getAmount() + "元");
//								} else {
//									// 当前agent为二级代理商
//									refundRecord.setAmount(poolList.get(j).getRefundAmount());
//									refundRecord.setPercent(refundConfig.getLevel1Percent());
//									refundRecord.setDescription("代理商" + agent.getName() + "在" + date + "购买"
//											+ poolList.get(j).getGoods().getName() + poolList.get(j).getQuantity()
//											+ "盒，达到返现标准，返现" + refundRecord.getAmount() + "元");
//									//
//									refundRecordLevel2
//											.setAmount(poolList.get(j).getQuantity() * refundConfig.getLevel2Percent());
//									refundRecordLevel2.setPercent(refundConfig.getLevel2Percent());
//									refundRecordLevel2.setDescription("代理商" + agent2.getName() + "与代理商"
//											+ agent.getName() + "直接关联，获得代理商" + agent.getName() + "在" + date + "购买"
//											+ poolList.get(j).getGoods().getName() + poolList.get(j).getQuantity()
//											+ "盒的社群拓展奖励，奖励" + refundRecordLevel2.getAmount() + "元");
//								}
//								refundRecordLevel2.setOrderPool(poolList.get(j));
//								refundRecordLevel2.setTitle(time + "社群拓展奖励账单");
//								refundRecordLevel2.setAgent(new common.sunshine.model.selling.agent.lite.Agent(agent2));
//								refundRecordLevel2.setLevel(CashBackLevel.DIRECT);
//								refundRecordLevel2.setBlockFlag(false);
//								if (!blockFlag) {
//									if (agent2.getAgentType() == AgentType.ORDINARY) {
//										sqlSession.insert("selling.refund.record.insert", refundRecordLevel2);
//									}
//								}
//
//							} else {
//								// 当前agent为一级代理商
//								refundRecord.setAmount(poolList.get(j).getRefundAmount());
//								refundRecord.setPercent(refundConfig.getLevel1Percent());
//								refundRecord.setDescription("代理商" + agent.getName() + "在" + date + "购买"
//										+ poolList.get(j).getGoods().getName() + poolList.get(j).getQuantity()
//										+ "盒，达到返现标准，返现" + refundRecord.getAmount() + "元");
//							}
//							refundRecord.setOrderPool(poolList.get(j));
//							refundRecord.setTitle(time + "返现账单");
//							refundRecord.setAgent(new common.sunshine.model.selling.agent.lite.Agent(agent));
//							refundRecord.setLevel(CashBackLevel.SELF);
//							refundRecord.setBlockFlag(false);
//							sqlSession.insert("selling.refund.record.insert", refundRecord);
//						}
//
//					}
//
//				}
//			}
//			result.setResponseCode(ResponseCode.RESPONSE_OK);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//			result.setDescription(e.getMessage());
//		}
//
//		return result;
//	}

	/**
     * 新版生成返现记录方法
	 * 根据order_pool生成refun_record表
	 * @return
	 */
	@Override
	public ResultData refundRecord() {
		ResultData result = new ResultData();
		Calendar now = Calendar.getInstance();
		String time = new SimpleDateFormat("yyyy年MM月dd日").format(now.getTime());
		try {
			Map<String, Object> condition = new HashMap<>();
			// 查询上个月的订单统计信息
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
			Calendar current = Calendar.getInstance();
			current.add(Calendar.MONTH, -1);
			String currentDate = dateFormat.format(current.getTime());
			condition.put("blockFlag", false);
			condition.put("date", currentDate + "%");
			List<OrderPool> poolList = sqlSession.selectList("selling.order.pool.query", condition);
			if (!poolList.isEmpty()) {
				for (int j = 0; j < poolList.size(); j++) {
					// 返现要求：连续几个月达到返现数量标准
					String date = dateFormat.format(poolList.get(j).getPoolDate());
					RefundConfig refundConfig = poolList.get(j).getRefundConfig();
					int monthConfig = refundConfig.getMonthConfig();
					boolean monthFlag = false;
					for (int i = 1; i < monthConfig; i++) {
						Date d = poolList.get(j).getPoolDate();
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(d);
						calendar.add(Calendar.MONTH, -i);
						condition.clear();
						condition.put("agentId", poolList.get(j).getAgent().getAgentId());
						condition.put("goodsId", poolList.get(j).getGoods().getGoodsId());
						condition.put("date", dateFormat.format(calendar.getTime()) + "%");
						condition.put("blockFlag", false);
						// 若其中某一个月的返现数量没有达标，则返现不成功
						if (sqlSession.selectList("selling.order.pool.query", condition).isEmpty()) {
							monthFlag = true;
							break;
						}
					}
					if (monthFlag) {
						continue;
					} else {
						// 连续几个月都达到了返现标准，生成返现记录
						common.sunshine.model.selling.agent.lite.Agent agentLite = poolList.get(j).getAgent();
						condition.clear();
						condition.put("agentId", agentLite.getAgentId());
						Agent agent = sqlSession.selectOne("selling.agent.query", condition);
                        //统计要满足的总购买数量标准
                        condition.clear();
                        condition.put("agentId", agent.getAgentId());
                        condition.put("poolDate", poolList.get(j).getPoolDate());
                        List<OrderPool> orderPoolList = sqlSession.selectList("selling.order.pool.query", condition);
                        int agentQuantity = 0;
                        for (OrderPool pool : orderPoolList) {
                            agentQuantity += pool.getQuantity();
                        }
						// 查询活跃度配置表
						Map<String, Object> con4 = new HashMap<>();
						con4.put("blockFlag", false);
						AgentVitality agentVitality =  sqlSession
								.selectOne("selling.agent.vitality.query", con4);
						if (agent.getAgentType() == AgentType.ORDINARY) {
							CashBackRecord refundRecord = new CashBackRecord();
							refundRecord.setRecordId(IDGenerator.generate("RFR"));
							if (agent.getUpperAgent() != null) {
								// 上级代理商要获得社群拓展奖励的前提是自己当月购买数量也达到返现标准且该上级代理商不是客服
								condition.put("agentId", agent.getUpperAgent().getAgentId());
								Agent agent2 = sqlSession.selectOne("selling.agent.query", condition);
								condition.clear();
								condition.put("agentId", agent.getUpperAgent().getAgentId());
								condition.put("poolDate", poolList.get(j).getPoolDate());
								boolean blockFlag = true;// blockFlag为true表示不能获取下级代理商的返现
								if (!sqlSession.selectList("selling.order.pool.query", condition).isEmpty()) {
									List<OrderPool> list = sqlSession.selectList("selling.order.pool.query", condition);
									int quantity = 0;
									double price = 0.0;
									for (OrderPool pool : list) {
										quantity += pool.getQuantity();
										price += pool.getPrice();
									}
									if (quantity >= agentVitality.getVitalityQuantity()
											&& price >= agentVitality.getVitalityPrice()) {
										blockFlag = false;// 当购买金额和数量都达到活跃度配置标准，就可以获取下级代理商的返现
									}
								} else {
									// 当上级代理商本月没有购买时，查看活跃度配置，假如活跃度配置为0，0，同样可以获取返现，否则不能
									if (agentVitality.getVitalityQuantity() == 0
											&& agentVitality.getVitalityPrice() == 0.0) {
										blockFlag = false;
									}
								}
								CashBackRecord refundRecordLevel2 = new CashBackRecord();
								refundRecordLevel2.setRecordId(IDGenerator.generate("RFR"));
								if (agent2.getUpperAgent() != null) {
									condition.clear();
									condition.put("agentId", agent2.getUpperAgent().getAgentId());
									Agent agent3 = sqlSession.selectOne("selling.agent.query", condition);
									condition.clear();
									condition.put("agentId", agent2.getUpperAgent().getAgentId());
									condition.put("poolDate", poolList.get(j).getPoolDate());
									boolean blockFlag2 = true;
									if (!sqlSession.selectList("selling.order.pool.query", condition).isEmpty()) {
										List<OrderPool> list = sqlSession.selectList("selling.order.pool.query",
												condition);
										int quantity = 0;
										double price = 0.0;
										for (OrderPool pool : list) {
											quantity += pool.getQuantity();
											price += pool.getPrice();
										}
										if (quantity >= agentVitality.getVitalityQuantity()
												&& price >= agentVitality.getVitalityPrice()) {
											blockFlag2 = false;// 当购买金额和数量都达到活跃度配置标准，就可以获取下级代理商的返现
										}
									} else {
										if (agentVitality.getVitalityQuantity() == 0
												&& agentVitality.getVitalityPrice() == 0.0) {
											blockFlag2 = false;
										}
									}
									if (!blockFlag2) {
										CashBackRecord refundRecordLevel3 = new CashBackRecord();
										refundRecordLevel3.setRecordId(IDGenerator.generate("RFR"));
										refundRecordLevel3.setAmount(
												poolList.get(j).getQuantity() * refundConfig.getLevel3Percent());
										refundRecordLevel3.setPercent(refundConfig.getLevel3Percent());
										refundRecordLevel3.setOrderPool(poolList.get(j));
										refundRecordLevel3
												.setAgent(new common.sunshine.model.selling.agent.lite.Agent(agent3));
										refundRecordLevel3.setTitle(time + "社群拓展奖励账单");
										refundRecordLevel3.setDescription("代理商" + agent3.getName() + "与代理商"
												+ agent.getName() + "间接关联，获得代理商" + agent.getName() + "在" + date + "购买"
												+ poolList.get(j).getGoods().getName() + poolList.get(j).getQuantity()
												+ "盒的社群拓展奖励，奖励" + refundRecordLevel3.getAmount() + "元");
										refundRecordLevel3.setLevel(CashBackLevel.INDIRECT);
										refundRecordLevel3.setBlockFlag(false);
										if (agent3.getAgentType() == AgentType.ORDINARY) {
											sqlSession.insert("selling.refund.record.insert", refundRecordLevel3);
										}
									}
									refundRecordLevel2
											.setAmount(poolList.get(j).getQuantity() * refundConfig.getLevel2Percent());
									refundRecordLevel2.setPercent(refundConfig.getLevel2Percent());
									refundRecordLevel2.setDescription("代理商" + agent2.getName() + "与代理商"
											+ agent.getName() + "直接关联，获得代理商" + agent.getName() + "在" + date + "购买"
											+ poolList.get(j).getGoods().getName() + poolList.get(j).getQuantity()
											+ "盒的社群拓展奖励，奖励" + refundRecordLevel2.getAmount() + "元");
								} else {
									refundRecordLevel2
											.setAmount(poolList.get(j).getQuantity() * refundConfig.getLevel2Percent());
									refundRecordLevel2.setPercent(refundConfig.getLevel2Percent());
									refundRecordLevel2.setDescription("代理商" + agent2.getName() + "与代理商"
											+ agent.getName() + "直接关联，获得代理商" + agent.getName() + "在" + date + "购买"
											+ poolList.get(j).getGoods().getName() + poolList.get(j).getQuantity()
											+ "盒的社群拓展奖励，奖励" + refundRecordLevel2.getAmount() + "元");
								}
								refundRecordLevel2.setOrderPool(poolList.get(j));
								refundRecordLevel2.setTitle(time + "社群拓展奖励账单");
								refundRecordLevel2.setAgent(new common.sunshine.model.selling.agent.lite.Agent(agent2));
								refundRecordLevel2.setLevel(CashBackLevel.DIRECT);
								refundRecordLevel2.setBlockFlag(false);
								if (!blockFlag) {
									if (agent2.getAgentType() == AgentType.ORDINARY) {
										sqlSession.insert("selling.refund.record.insert", refundRecordLevel2);
									}
								}

							}
                            refundRecord.setAmount(poolList.get(j).getRefundAmount());
                            refundRecord.setPercent(refundConfig.getLevel1Percent());
                            refundRecord.setDescription("代理商" + agent.getName() + "在" + date + "购买商品一共" + agentQuantity
                                    +"盒，达到返现标准"+poolList.get(j).getRefundConfig().getAmountTrigger()+"盒，其中购买"
                                    + poolList.get(j).getGoods().getName() + poolList.get(j).getQuantity()
                                    + "盒，返现" + refundRecord.getAmount() + "元");
							refundRecord.setOrderPool(poolList.get(j));
							refundRecord.setTitle(time + "返现账单");
							refundRecord.setAgent(new common.sunshine.model.selling.agent.lite.Agent(agent));
							refundRecord.setLevel(CashBackLevel.SELF);
							refundRecord.setBlockFlag(false);
							sqlSession.insert("selling.refund.record.insert", refundRecord);
						}

					}

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

	/**
	 * 更新代理商余额和返现总额，最后update refund_record
	 * @return
	 */
	public ResultData refund() {
		ResultData resultData = new ResultData();
		try {
			Map<String, Object> condition = new HashMap<>();
			condition.put("blockFlag", false);
			List<CashBackRecord> refundRecordList = sqlSession.selectList("selling.refund.record.query", condition);
			for (CashBackRecord refundRecord : refundRecordList) {
				condition.clear();
				condition.put("agentId", refundRecord.getAgent().getAgentId());
				Agent agent = (Agent) sqlSession.selectList("selling.agent.query", condition).get(0);
				// 更新agent的账户余额以及返现总额
				agent.setCoffer(agent.getCoffer() + refundRecord.getAmount());
				agent.setAgentRefund(agent.getAgentRefund() + refundRecord.getAmount());
				sqlSession.update("selling.agent.update", agent);
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
	public ResultData queryRefundRecordByPage(Map<String, Object> condition, DataTableParam param) {
		ResultData result = new ResultData();
		DataTablePage<CashBackRecord> page = new DataTablePage<>();
		page.setsEcho(param.getsEcho());
		logger.debug(JSONObject.toJSONString(condition));

		if (!StringUtils.isEmpty(param.getsSearch())) {
			condition.put("refundDescription", "%" + param.getsSearch() + "%");
		}
		// if (!StringUtils.isEmpty(condition.get("createAt"))) {
		//
		// }
		ResultData total = queryRefundRecord(condition);
		if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(total.getDescription());
			return result;
		}
		page.setiTotalRecords(((List<CashBackRecord>) total.getData()).size());
		page.setiTotalDisplayRecords(((List<CashBackRecord>) total.getData()).size());
		List<CashBackRecord> current = queryRefundRecordByPage(condition, param.getiDisplayStart(),
				param.getiDisplayLength());
		if (current.size() == 0) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
		}
		page.setData(current);
		result.setData(page);
		return result;
	}

	private List<CashBackRecord> queryRefundRecordByPage(Map<String, Object> condition, int start, int length) {
		List<CashBackRecord> result = new ArrayList<>();
		try {
			result = sqlSession.selectList("selling.refund.record.query", condition, new RowBounds(start, length));
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			return result;
		}
	}

	@Override
	public ResultData statistic(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<Map<String, Object>> list = sqlSession.selectList("selling.refund.record.statistic", condition);
			result.setData(list);
			result.setResponseCode(ResponseCode.RESPONSE_OK);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData calculateRefund(String agentId) {
		ResultData result = new ResultData();

		Calendar c = Calendar.getInstance();
		Timestamp month = new Timestamp(c.getTimeInMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		String date = dateFormat.format(month);
		try {
			Map<String, Object> condition = new HashMap<>();
			condition.put("date", date + "%");
			condition.put("agentId", agentId);
			List<Map<String, Object>> sumOrderList = sqlSession.selectList("selling.order.pool.sumOrder", condition);
			List<Map<String, Object>> sumCustomerOrderList = sqlSession
					.selectList("selling.customer.order.sumCustomerOrder", condition);
			List<Map<String, Object>> resultList = new ArrayList<>();
			if (sumOrderList.size() == 0) {
				resultList = sumCustomerOrderList;
			} else if (sumCustomerOrderList.size() == 0) {
				resultList = sumOrderList;
			} else {
				boolean flag = false;
				for (int i = 0; i < sumOrderList.size(); i++) {
					for (int j = 0; j < sumCustomerOrderList.size(); j++) {
						if (sumOrderList.get(i).get("goods").equals(sumCustomerOrderList.get(j).get("goods"))) {
							flag = true;
							sumCustomerOrderList.get(j).put("quantity",
									Integer.parseInt(sumCustomerOrderList.get(j).get("quantity").toString())
											+ Integer.parseInt(sumOrderList.get(i).get("quantity").toString()));
							break;
						}
					}
					if (!flag) {
						resultList.add(sumOrderList.get(i));
					} else {
						flag = false;
					}
				}
				resultList.addAll(sumCustomerOrderList);
			}
			Map<String, Object> goodsCondition = new HashMap<>();
			Map<String, Object> configCondition = new HashMap<>();
			List<Map<String, Object>> list = new ArrayList<>();
			Map<String, Object> dataMap;
			boolean flag = false;
			List<Goods4Agent> goodsList = sqlSession.selectList("selling.goods.query4Agent", goodsCondition);
			if (resultList.size() == 0) {
				for (int i = 0; i < goodsList.size(); i++) {
					configCondition.put("goodsId", goodsList.get(i).getGoodsId());
					configCondition.put("blockFlag", false);
					if (sqlSession.selectList("selling.refund.config.query", configCondition).size() == 0) {
						dataMap = new HashMap<>();
						list.add(dataMap);
					} else {
						RefundConfig config = (RefundConfig) sqlSession
								.selectList("selling.refund.config.query", configCondition).get(0);
						dataMap = new HashMap<>();
						dataMap.put("amountTrigger", config.getAmountTrigger());
						dataMap.put("quantity", 0);
						dataMap.put("goods", goodsList.get(i).getName());
						list.add(dataMap);
					}
					configCondition.clear();
					list.add(dataMap);
				}
			} else {
				for (int i = 0; i < goodsList.size(); i++) {
					for (int j = 0; j < resultList.size(); j++) {
						if (goodsList.get(i).getGoodsId().equals(resultList.get(j).get("goods"))) {
							configCondition.put("goodsId", goodsList.get(i).getGoodsId());
							configCondition.put("blockFlag", false);
							if (sqlSession.selectList("selling.refund.config.query", configCondition).size() == 0) {
								dataMap = new HashMap<>();
							} else {
								RefundConfig config = (RefundConfig) sqlSession
										.selectList("selling.refund.config.query", configCondition).get(0);
								dataMap = new HashMap<>();
								dataMap.put("amountTrigger", config.getAmountTrigger());
								dataMap.put("quantity", resultList.get(j).get("quantity"));
								dataMap.put("goods", goodsList.get(i).getName());
							}
							configCondition.clear();
							list.add(dataMap);
							flag = true;
							break;
						}
					}
					if (!flag) {
						configCondition.put("goodsId", goodsList.get(i).getGoodsId());
						configCondition.put("blockFlag", false);
						if (sqlSession.selectList("selling.refund.config.query", configCondition).size() == 0) {
							dataMap = new HashMap<>();
						} else {
							RefundConfig config = (RefundConfig) sqlSession
									.selectList("selling.refund.config.query", configCondition).get(0);
							dataMap = new HashMap<>();
							dataMap.put("amountTrigger", config.getAmountTrigger());
							dataMap.put("quantity", 0);
							dataMap.put("goods", goodsList.get(i).getName());
						}
						configCondition.clear();
						list.add(dataMap);
					} else {
						flag = false;
					}
				}
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
	public ResultData calculateQuantity(String agentId) {
		ResultData result = new ResultData();

		Calendar c = Calendar.getInstance();
		Timestamp month = new Timestamp(c.getTimeInMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		String date = dateFormat.format(month);
		try {
			Map<String, Object> condition = new HashMap<>();
			condition.put("date", date + "%");
			condition.put("agentId", agentId);
			List<Map<String, Object>> sumOrderList = sqlSession.selectList("selling.order.pool.calculateOrderQuantity",
					condition);
			List<Map<String, Object>> sumCustomerOrderList = sqlSession
					.selectList("selling.order.pool.calculateCustomerOrderQuantity", condition);
			if ((sumOrderList.get(0) == null) && (sumCustomerOrderList.get(0) == null)) {
				result.setData(0);
				return result;
			} else if (sumOrderList.get(0) == null) {
				result.setData(Integer.parseInt(sumCustomerOrderList.get(0).get("quantity").toString()));
				return result;
			} else if (sumCustomerOrderList.get(0) == null) {
				result.setData(Integer.parseInt(sumOrderList.get(0).get("quantity").toString()));
				return result;
			}
			int quantity = Integer.parseInt(sumOrderList.get(0).get("quantity").toString())
					+ Integer.parseInt(sumCustomerOrderList.get(0).get("quantity").toString());
			result.setData(quantity);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		} finally {
			return result;
		}
	}

	@Override
	public ResultData calculateQuantityAll(String agentId) {
		ResultData result = new ResultData();
		try {
			Map<String, Object> condition = new HashMap<>();
			condition.put("agentId", agentId);
			List<TotalQuantityAll> list = sqlSession.selectList("selling.order.pool.calculateQuantityAll", condition);
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
	public ResultData queryRefundConfig(Map<String, Object> condition, DataTableParam param) {
		ResultData result = new ResultData();
		DataTablePage<RefundConfig> page = new DataTablePage<>();
		page.setsEcho(param.getsEcho());
		logger.debug(JSONObject.toJSONString(condition));

		ResultData total = queryRefundConfig(condition);
		if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(total.getDescription());
			return result;
		}
		page.setiTotalRecords(((List<RefundConfig>) total.getData()).size());
		page.setiTotalDisplayRecords(((List<RefundConfig>) total.getData()).size());
		List<RefundConfig> current = queryRefundConfig(condition, param.getiDisplayStart(), param.getiDisplayLength());
		if (current.size() == 0) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
		}
		page.setData(current);
		result.setData(page);
		return result;
	}

	private List<RefundConfig> queryRefundConfig(Map<String, Object> condition, int start, int length) {
		List<RefundConfig> result = new ArrayList<>();
		try {
			result = sqlSession.selectList("selling.refund.config.query", condition, new RowBounds(start, length));
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			return result;
		}
	}

}
