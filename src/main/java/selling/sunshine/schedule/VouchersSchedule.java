package selling.sunshine.schedule;

import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.model.selling.order.support.OrderType;
import common.sunshine.model.selling.vouchers.Vouchers;
import common.sunshine.model.selling.vouchers.support.VouchersType;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.OrderService;
import selling.sunshine.service.VouchersService;
import selling.sunshine.utils.PlatformConfig;
import selling.sunshine.vo.order.OrderItemSum;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wxd on 2017/5/2.
 * 发放代金券
 */
public class VouchersSchedule {

    private Logger logger = LoggerFactory.getLogger(VouchersSchedule.class);

    @Autowired
    private AgentService agentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private VouchersService vouchersService;


    /**
     * 注册即送代金劵
     */
    public void registerSchedule(){

        String date= PlatformConfig.getValue("vouchers_date");
        String days= PlatformConfig.getValue("register_vouchers_days");
        Calendar current = Calendar.getInstance();

        Map<String,Object> condition=new HashMap<>();
        condition.put("granted", true);
        condition.put("blockFlag", false);
        condition.put("vouchersDays", Integer.parseInt(days)+1);
        condition.put("vouchersDate", date);
        ResultData response= agentService.fetchAgent(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
            List<Agent> agentList = (List<Agent>)response.getData();
            for (Agent agent : agentList){
                    condition.clear();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String start = dateFormat.format(agent.getCreateAt());
                    current.add(Calendar.DAY_OF_MONTH,-1);
                    String end = dateFormat.format(current.getTime());
                    condition.put("agentId", agent.getAgentId());
                    condition.put("start", start);
                    condition.put("end", end);
                    List<Integer> statusList = new ArrayList<>(Arrays.asList(OrderItemStatus.PAYED.getCode(), OrderItemStatus.SHIPPED.getCode(), OrderItemStatus.RECEIVED.getCode()));
                    condition.put("statusList", statusList);
                    List<Integer> orderTypeList = new ArrayList<>(Arrays.asList(OrderType.ORDINARY.getCode(),OrderType.CUSTOMER.getCode()));
                    condition.put("orderTypeList", orderTypeList);
                    response=orderService.fetchOrderItemSum(condition);
                    if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
                            Vouchers vouchers=new Vouchers();
                            common.sunshine.model.selling.agent.lite.Agent agentLite=new common.sunshine.model.selling.agent.lite.Agent();
                            agentLite.setAgentId(agent.getAgentId());
                            vouchers.setAgent(agentLite);
                            vouchers.setPrice(50);
                            vouchers.setType(VouchersType.REGISTER);
                            vouchersService.createVouchers(vouchers);
                    }
            }
        }
    }

    /**
     * 奖励代金劵
     */
    public void rewardSchedule(){

        String days= PlatformConfig.getValue("reward_vouchers_days");
        String date= PlatformConfig.getValue("vouchers_date");
        Calendar current = Calendar.getInstance();

        Map<String,Object> condition=new HashMap<>();
        condition.put("granted", true);
        condition.put("blockFlag", false);
        condition.put("vouchersDays", Integer.parseInt(days)+1);
        condition.put("vouchersDate", date);
        ResultData response= agentService.fetchAgent(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
            List<Agent> agentList = (List<Agent>)response.getData();
            for (Agent agent : agentList){
                if (agent.getUpperAgent() != null){
                    condition.clear();
                    condition.put("agentId", agent.getUpperAgent().getAgentId());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    current.add(Calendar.DAY_OF_MONTH,-1);
                    String end = dateFormat.format(current.getTime());
                    condition.put("end", end);
                    current.add(Calendar.DAY_OF_MONTH,-30);
                    String start = dateFormat.format(current.getTime());
                    condition.put("start", start);
                    List<Integer> statusList = new ArrayList<>(Arrays.asList(OrderItemStatus.PAYED.getCode(), OrderItemStatus.SHIPPED.getCode(), OrderItemStatus.RECEIVED.getCode()));
                    condition.put("statusList", statusList);
                    List<Integer> orderTypeList = new ArrayList<>(Arrays.asList(OrderType.ORDINARY.getCode(),OrderType.CUSTOMER.getCode()));
                    condition.put("orderTypeList", orderTypeList);
                    response=orderService.fetchOrderItemSum(condition);
                    if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
                        List<OrderItemSum> list = (List<OrderItemSum>) response.getData();
                        int quantity=0;
                        for (OrderItemSum orderItemSum : list){
                            quantity+=orderItemSum.getGoodsQuantity();
                        }
                        if (quantity >=5 ){
                            current.add(Calendar.DAY_OF_MONTH,-1);
                            end = dateFormat.format(current.getTime());
                            condition.put("end", end);
                            current.add(Calendar.DAY_OF_MONTH, -30);
                            start = dateFormat.format(current.getTime());
                            condition.put("start", start);
                            response=orderService.fetchOrderItemSum(condition);
                            if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
                                List<OrderItemSum> list2 = (List<OrderItemSum>) response.getData();
                                int quantity2=0;
                                for (OrderItemSum orderItemSum : list2){
                                    quantity2+=orderItemSum.getGoodsQuantity();
                                }
                                if (quantity2 >=5 ){
                                    current.add(Calendar.DAY_OF_MONTH,-1);
                                    end = dateFormat.format(current.getTime());
                                    condition.put("end", end);
                                    current.add(Calendar.DAY_OF_MONTH, -30);
                                    start = dateFormat.format(current.getTime());
                                    condition.put("start", start);
                                    response=orderService.fetchOrderItemSum(condition);
                                    if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
                                        List<OrderItemSum> list3 = (List<OrderItemSum>) response.getData();
                                        int quantity3=0;
                                        for (OrderItemSum orderItemSum : list3){
                                            quantity3+=orderItemSum.getGoodsQuantity();
                                        }
                                        if (quantity3 >=5 ){
                                            condition.put("agentId", agent.getAgentId());
                                            start = dateFormat.format(current.getTime());
                                            condition.put("start", start);
                                            current.add(Calendar.DAY_OF_MONTH,30);
                                            end = dateFormat.format(current.getTime());
                                            condition.put("end", end);
                                            response=orderService.fetchOrderItemSum(condition);
                                            if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
                                                List<OrderItemSum> list4 = (List<OrderItemSum>) response.getData();
                                                int quantity4=0;
                                                for (OrderItemSum orderItemSum : list4){
                                                    quantity4+=orderItemSum.getGoodsQuantity();
                                                }
                                                if (quantity4 >=3 ){
                                                    current.add(Calendar.DAY_OF_MONTH,1);
                                                    start = dateFormat.format(current.getTime());
                                                    condition.put("start", start);
                                                    current.add(Calendar.DAY_OF_MONTH,30);
                                                    end = dateFormat.format(current.getTime());
                                                    condition.put("end", end);
                                                    response=orderService.fetchOrderItemSum(condition);
                                                    if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
                                                        List<OrderItemSum> list5 = (List<OrderItemSum>) response.getData();
                                                        int quantity5=0;
                                                        for (OrderItemSum orderItemSum : list5){
                                                            quantity5+=orderItemSum.getGoodsQuantity();
                                                        }
                                                        if (quantity5 >=3 ){
                                                            current.add(Calendar.DAY_OF_MONTH,1);
                                                            start = dateFormat.format(current.getTime());
                                                            condition.put("start", start);
                                                            current.add(Calendar.DAY_OF_MONTH,30);
                                                            end = dateFormat.format(current.getTime());
                                                            condition.put("end", end);
                                                            response=orderService.fetchOrderItemSum(condition);
                                                            if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
                                                                List<OrderItemSum> list6 = (List<OrderItemSum>) response.getData();
                                                                int quantity6=0;
                                                                for (OrderItemSum orderItemSum : list6){
                                                                    quantity6+=orderItemSum.getGoodsQuantity();
                                                                }
                                                                if (quantity6 >=3 ){
                                                                    Vouchers vouchers=new Vouchers();
                                                                    common.sunshine.model.selling.agent.lite.Agent agentLite=new common.sunshine.model.selling.agent.lite.Agent();
                                                                    agentLite.setAgentId(agent.getUpperAgent().getAgentId());
                                                                    vouchers.setAgent(agentLite);
                                                                    vouchers.setPrice(100);
                                                                    vouchers.setType(VouchersType.REWARD);
                                                                    vouchersService.createVouchers(vouchers);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

    }

}
