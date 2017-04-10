package selling.sunshine.schedule;

import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.service.OrderService;
import selling.sunshine.utils.PlatformConfig;
import selling.sunshine.vo.order.OrderItemSum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wxd on 2017/4/10.
 */
public class OrderSchedule {

    private Logger logger = LoggerFactory.getLogger(OrderSchedule.class);

    @Autowired
    private OrderService orderService;

    /**
     * 用来处理将过期未自动转化为已签收状态的已发货订单的状态改为已签收
     */
    public void schedule() {

        String date= PlatformConfig.getValue("order_date");
        Map<String,Object> condition = new HashMap<>();
        condition.put("status", OrderItemStatus.SHIPPED.getCode());
        condition.put("scheduleDate",Integer.parseInt(date));
        ResultData fetchResponse=orderService.fetchOrderItemSum(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK){
            List<OrderItemSum> list = (List<OrderItemSum>) fetchResponse.getData();
            for (OrderItemSum orderItemSum : list){
                String orderId=orderItemSum.getOrderId();
                if (orderId.startsWith("ORI")){
                    OrderItem orderItem=new OrderItem();
                    orderItem.setStatus(OrderItemStatus.RECEIVED);
                    orderItem.setOrderItemId(orderId);
                    orderService.updateOrderItem(orderItem);
                }else if (orderId.startsWith("CUO")){
                    CustomerOrder customerOrder=new CustomerOrder();
                    customerOrder.setOrderId(orderId);
                    customerOrder.setStatus(OrderItemStatus.RECEIVED);
                    orderService.updateCustomerOrder(customerOrder);
                }
            }
        }

    }
}
