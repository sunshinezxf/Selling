package selling.sunshine.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.agent.AgentKPI;
import common.sunshine.model.selling.customer.Customer;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.service.AgentKPIService;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.CustomerService;
import selling.sunshine.service.OrderService;
import selling.sunshine.vo.order.OrderItemSum;

public class AgentKPISchedule {
	
	private Logger logger = LoggerFactory.getLogger(AgentKPISchedule.class);
	
	@Autowired
	private AgentKPIService agentKPIService;
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OrderService orderService;
	
	public void schedule() {
		Map<String, Object> condition=new HashMap<>();
		ResultData queryData=agentService.fetchAgent(condition);
		if (queryData.getResponseCode()==ResponseCode.RESPONSE_OK) {
			List<Agent> agents=(List<Agent>)queryData.getData();
			for (Agent agent:agents) {
				AgentKPI agentKPI=new AgentKPI();
				agentKPI.setAgentId(agent.getAgentId());
				agentKPI.setAgentName(agent.getName());
				if (!agent.isBlockFlag()&&agent.isGranted()) {
					agentKPI.setBlockFlag(false);
				}else {
					agentKPI.setBlockFlag(true);
				}
				//顾客人数
				condition.clear();
				condition.put("agentId", agent.getAgentId());				
				queryData=customerService.fetchCustomer(condition);
				if (queryData.getResponseCode()==ResponseCode.RESPONSE_OK) {
					List<Customer> customers=(List<Customer>)queryData.getData();
					agentKPI.setCustomerQuantity(customers.size());
				}				
				//下级代理商人数
				condition.clear();
				condition.put("upperAgent", agent);
				queryData=agentService.fetchAgent(condition);
				if (queryData.getResponseCode()==ResponseCode.RESPONSE_OK) {
					List<Agent> directAgents=(List<Agent>)queryData.getData();
					agentKPI.setDirectAgentQuantity(directAgents.size());
				}
				//贡献度
				//贡献度计算公式  贡献度=购买商品总数量*百分比+购买商品总金额*百分比+下级代理商人数*百分比
				condition.clear();
				condition.put("agentId", agent.getAgentId());
				List<Integer> orderTypeList=new ArrayList<>();
				orderTypeList.add(0);
				orderTypeList.add(2);
				condition.put("orderTypeList", orderTypeList);
				List<Integer> statusList=new ArrayList<>();
				statusList.add(1);
				statusList.add(2);
				statusList.add(3);
				condition.put("statusList", statusList);
				queryData=orderService.fetchOrderItemSum(condition);
				if (queryData.getResponseCode()==ResponseCode.RESPONSE_OK) {
					List<OrderItemSum> list=(List<OrderItemSum>)queryData.getData();
					int quantity=0;
					double price=0.0;
					//查询并计算得到购买商品总数和购买商品总金额
					for (OrderItemSum item : list) {
						quantity+=item.getGoodsQuantity();
						price+=item.getOrderItemPrice();
					}
					
				}
				//agentKPI.setAgentContribution(agentContribution);
				agentKPIService.updateAgentKPI(agentKPI);
			}
		}
	}


}
