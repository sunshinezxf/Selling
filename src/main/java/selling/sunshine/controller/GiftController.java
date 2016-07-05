package selling.sunshine.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import selling.sunshine.model.gift.GiftConfig;
import selling.sunshine.model.goods.Goods4Agent;
import selling.sunshine.model.lite.Agent;
import selling.sunshine.service.AgentService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

@RestController
@RequestMapping("/gift")
public class GiftController {

	private Logger logger = LoggerFactory.getLogger(GiftController.class);

	@Autowired
	private AgentService agentService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/{agentId}/{goodsId}")
	public ResultData agentGift(@PathVariable("agentId")String agentId,@PathVariable("goodsId")String goodsId) {
		ResultData resultData=new ResultData();
		Map<String, Object> condition=new HashMap<String, Object>();
		condition.put("agentId", agentId);
		condition.put("goodsId", goodsId);
		resultData=agentService.fetchAgentGift(condition);
		return resultData;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/config/{agentId}/{goodsId}/{amount}")
	public ResultData giftConfig(@PathVariable("agentId")String agentId,@PathVariable("goodsId")String goodsId,@PathVariable("amount")int amount) {
		ResultData resultData=new ResultData();
		Map<String, Object> condition=new HashMap<String, Object>();
		condition.put("agentId", agentId);
		condition.put("goodsId", goodsId);
		ResultData queryData=agentService.fetchAgentGift(condition);
		if (queryData.getResponseCode()==ResponseCode.RESPONSE_NULL) {
            Agent agent=new Agent();
            agent.setAgentId(agentId);
            Goods4Agent goods=new Goods4Agent();
            goods.setGoodsId(goodsId);
			GiftConfig giftConfig=new GiftConfig(agent, goods, amount);
			resultData=agentService.createAgentGift(giftConfig);
		}else {
			GiftConfig giftConfig=((List<GiftConfig>)queryData.getData()).get(0);
			giftConfig.setAmount(amount);
			resultData=agentService.updateAgentGift(giftConfig);
		}

		return resultData;
	}

}
