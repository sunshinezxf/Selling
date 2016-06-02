package selling.sunshine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import selling.sunshine.dao.ExpressDao;
import selling.sunshine.model.Express;
import selling.sunshine.service.ExpressService;
import selling.sunshine.utils.Encryption;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

public class ExpressServiceImpl implements ExpressService {

	@Autowired
	private ExpressDao expressDao;

	@Override
	public ResultData createExpress(Express express) {
		ResultData result = new ResultData();
		ResultData insertResponse = expressDao.insertExpress(express);
		result.setResponseCode(insertResponse.getResponseCode());
		if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(insertResponse.getData());
		} else {
			result.setDescription(insertResponse.getDescription());
		}
		return result;
	}

}
