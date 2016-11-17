package selling.sunshine.schedule;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import common.sunshine.model.selling.customer.CustomerAddress;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.service.CustomerService;
import selling.sunshine.utils.TenXunMapAPI;

public class CustomerAddressSchedule {
	
	private Logger logger = LoggerFactory.getLogger(CustomerAddressSchedule.class);
	
	@Autowired
	private CustomerService customerService;
	
	public void schedule() {
		Map<String, Object> condition=new HashMap<>();
		ResultData queryData=customerService.fetchCustomerAddress(condition);
		if (queryData.getResponseCode()==ResponseCode.RESPONSE_OK) {
			List<CustomerAddress> addresses=(List<CustomerAddress>)queryData.getData();
			for (CustomerAddress customerAddress:addresses) {
				if (customerAddress.getProvince()==null) {
					String address=customerAddress.getAddress().replace(" ", "");
					address=address.replace("，", "");
					Map<String, String> map =TenXunMapAPI.getDetailInfoByAddress(address);
					if (map.containsKey("province")&&map.containsKey("city")&&map.containsKey("district")) {
						customerAddress.setProvince(map.get("province"));
						customerAddress.setCity(map.get("city"));
						customerAddress.setDistrict(map.get("district"));
					}else {
						if (address.contains("苏州工业园区")) {
							customerAddress.setProvince("江苏");
							customerAddress.setCity("苏州");
							customerAddress.setDistrict("吴中");
						}else if (address.contains("县")) {
							map =TenXunMapAPI.getDetailInfoByAddress(address.substring(0,address.indexOf("县")));
							if (map.containsKey("province")&&map.containsKey("city")&&map.containsKey("district")) {
								customerAddress.setProvince(map.get("province"));
								customerAddress.setCity(map.get("city"));
								customerAddress.setDistrict(map.get("district"));
							}else {
								logger.error(address);
							}
						}
					}
					customerService.updateCustomerAddress(customerAddress);
				}				
			}
		}
	}

}
