package selling.sunshine.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.csvreader.CsvReader;

import selling.sunshine.dao.CustomerOrderDao;
import selling.sunshine.model.*;
import selling.sunshine.model.express.Express;
import selling.sunshine.model.express.Express4Agent;
import selling.sunshine.model.express.Express4Customer;
import selling.sunshine.service.ExpressService;
import selling.sunshine.service.OrderService;
import selling.sunshine.utils.DigestUtil;
import selling.sunshine.utils.HttpUtil;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/express")
@RestController
public class ExpressController {

	private Logger logger = LoggerFactory.getLogger(ExpressController.class);

	@Autowired
	private ExpressService expressService;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CustomerOrderDao customerOrderDao;

	@RequestMapping(method = RequestMethod.POST, value = "/detail/{id}")
	public ResultData detail(@PathVariable("id") String id) {
		ResultData resultData = new ResultData();
		Map<String, Object> dataMap = new HashMap<>();
		Map<String, Object> condition = new HashMap<>();
		if (!StringUtils.isEmpty(id) && id.startsWith("ORI")) {
			condition.put("orderItemId", id);
			ResultData queryResponse = expressService
					.fetchExpress4Agent(condition);
			if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
				Express4Agent express = ((List<Express4Agent>) queryResponse
						.getData()).get(0);
				dataMap.put("express", express);
				resultData.setData(dataMap);
			} else {
				resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
		} else if (id.startsWith("CUO")) {
			condition.put("orderId", id);
			ResultData queryResponse = expressService
					.fetchExpress4Customer(condition);
			if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
				Express4Customer express = ((List<Express4Customer>) queryResponse
						.getData()).get(0);
				dataMap.put("express", express);
				resultData.setDescription("test");
				resultData.setData(dataMap);
			} else {
				resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
		}
		return resultData;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/upload")
	public ModelAndView overview() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/backend/express/express_upload");
		return view;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public ModelAndView overview(MultipartHttpServletRequest request)
			throws IOException {
		ResultData resultData = new ResultData();
		ModelAndView view = new ModelAndView();
		MultipartFile file = request.getFile("excelFile");
        if (file.isEmpty()) {
            resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            view.setViewName("/backend/express/express_upload");
    		return view;
        }
		InputStream input = file.getInputStream();

		CsvReader csvReader=new CsvReader(input, Charset.forName("GBK"));
		List<String[]> csvList = new ArrayList<String[]>(); // 用来保存数据
        //读取表头
		csvReader.readHeaders();
        //逐条读取记录，直至读完
        while (csvReader.readRecord()) {
        	 csvList.add(csvReader.getValues());  
        }
        csvReader.close();
        for(int row=0;row<csvList.size()-1;row++){
        	if (csvList.get(row).length<36) {
        		view.setViewName("/backend/express/express_upload");
        		return view;
			}
        	Map<String, Object> con=new HashMap<>();
        	con.put("expressNumber", csvList.get(row)[7]);
        	if (expressService.fetchExpress(con).getData()==null) {
                String  linkID = csvList.get(row)[8]; //取得第row行第0列的数据  
                if (linkID.startsWith("ORI")) {
                	Express4Agent express = new Express4Agent(csvList.get(row)[7],
                			csvList.get(row)[14], csvList.get(row)[15],
                			csvList.get(row)[20], csvList.get(row)[0],
                			csvList.get(row)[1], csvList.get(row)[6],
                			csvList.get(row)[27]);
                    OrderItem temp = new OrderItem();
                    Map<String, Object> condition = new HashMap<>();
                    condition.put("orderItemId", linkID);
                    ResultData fetchResponse =
                            orderService.fetchOrderItem(condition);
                    if (fetchResponse.getResponseCode() ==
                            ResponseCode.RESPONSE_OK) {
                    	temp=((List<OrderItem>) fetchResponse.getData()).get(0);
                    }
                    temp.setStatus(OrderItemStatus.SHIPPED);
                    express.setItem(temp);
                    expressService.createExpress(express);
                    orderService.updateOrderItem(temp);
                    condition.clear();
                    condition.put("orderId", temp.getOrder().getOrderId());                
                    fetchResponse = orderService.fetchOrder(condition);
                    if (fetchResponse.getResponseCode() ==ResponseCode.RESPONSE_OK) {
                            Order order = ((List<Order>) fetchResponse.getData()).get(0);
                            order.setStatus(OrderStatus.FULLY_SHIPMENT);
                            orderService.modifyOrder(order);                   
                    }
    			}else {
                    Express4Customer express = new Express4Customer(
                    		csvList.get(row)[7],
                			csvList.get(row)[14], csvList.get(row)[15],
                			csvList.get(row)[20], csvList.get(row)[0],
                			csvList.get(row)[1], csvList.get(row)[6],
                			csvList.get(row)[27]);
                    CustomerOrder temp = new CustomerOrder();
                    Map<String, Object> condition = new HashMap<>();
                    condition.put("orderId", linkID);
                    ResultData fetchResponse =customerOrderDao.queryOrder(condition);
                    if (fetchResponse.getResponseCode() ==
                            ResponseCode.RESPONSE_OK) {
                    	temp=((List<CustomerOrder>) fetchResponse.getData()).get(0);
    				}
                    temp.setStatus(OrderItemStatus.SHIPPED);
                    express.setOrder(temp);
                    expressService.createExpress(express);
                    customerOrderDao.updateOrder(temp);
    			}        
			}
        } 
        resultData.setResponseCode(ResponseCode.RESPONSE_OK);
        view.setViewName("redirect:/order/check");
		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/queryExpress/{orderId}")
	public ResultData queryExpress(@PathVariable("orderId") String orderId) {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<String, Object>();
		ResultData fetchExpressResponse = null;
		if (orderId.startsWith("ORI")) {
			condition.put("orderItemId", orderId);
			fetchExpressResponse = expressService.fetchExpress4Agent(condition);
		} else if (orderId.startsWith("CUO")) {
			condition.put("orderId", orderId);
			fetchExpressResponse = expressService
					.fetchExpress4Customer(condition);
		}
		if (fetchExpressResponse == null) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
			result.setDescription("未找到订单");
			return result;
		}
		if (fetchExpressResponse.getResponseCode() != ResponseCode.RESPONSE_OK
				|| ((List<Express>) fetchExpressResponse.getData()).isEmpty()) {
			result.setResponseCode(fetchExpressResponse.getResponseCode());
			result.setDescription(fetchExpressResponse.getDescription());
			return result;
		}
		Express express = ((List<Express>) fetchExpressResponse.getData())
				.get(0);
		String expressNumber = express.getExpressNumber();
		if (expressNumber == null || expressNumber.equals("")) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
			result.setDescription("没有快递信息");
			return result;
		}
		Map<String, String> map = new HashMap<String, String>();
		String data = "[" + expressNumber + "]";
		map.put("data", data);
		map.put("msg_type", "TRACES");
		try {
			map.put("data_digest", DigestUtil.digest(data,
					"AA076973A63D4CD2BBEFB60544FC1262", DigestUtil.UTF8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("company_id", "5be4f9cacac84d3d9dace29dd9026a09");
		try {
			result.setData(HttpUtil.post(
					"http://japi.zto.cn/zto/api_utf8/traceInterface", "UTF-8",
					map));
		} catch (IOException e) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			e.printStackTrace();
		} finally {
			return result;
		}
	}

}
