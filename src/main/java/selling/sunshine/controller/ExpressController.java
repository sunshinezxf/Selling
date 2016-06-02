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
import org.springframework.web.servlet.ModelAndView;

import selling.sunshine.model.Express;
import selling.sunshine.service.ExpressService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

@RequestMapping("/express")
@RestController
public class ExpressController {
	
	private Logger logger = LoggerFactory.getLogger(ExpressController.class);
	
	@Autowired
	private ExpressService expressService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/{orderItemId}")
	public ModelAndView overview(@PathVariable("orderItemId") String orderItemId) {		
		ModelAndView view = new ModelAndView();
		Map<String, Object> condition=new HashMap<String, Object>();
		condition.put("orderItemId", orderItemId);
		ResultData resultData=expressService.fetchExpress(condition);
		if (resultData.getResponseCode()!=ResponseCode.RESPONSE_OK) {
			view.setViewName("/backend/express/express_overview");
			return view;		
		}
		Express express=((List<Express>)resultData.getData()).get(0);
		view.addObject("express", express);
		view.setViewName("/backend/express/express_overview");
		return view;		
	}

}
