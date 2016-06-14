package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.GoodsForm;
import selling.sunshine.model.Agent;
import selling.sunshine.model.Goods;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.CommodityService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
@RequestMapping("/commodity")
@RestController
public class CommodityController {
    private Logger logger = LoggerFactory.getLogger(CommodityController.class);

    @Autowired
    private CommodityService commodityService;
    
    @Autowired
    private AgentService agentService;

    @RequestMapping(method = RequestMethod.GET, value = "/create")
    public ModelAndView create() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/goods/create");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ModelAndView create(@Valid GoodsForm form, BindingResult result) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/commodity/create");
            return view;
        }
        try {
            Goods goods = new Goods(form.getName(), Double.parseDouble(form.getPrice()), form.getDescription(), form.isBlock());
            ResultData createResponse = commodityService.createCommodity(goods);
            if (createResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                view.setViewName("redirect:/commodity/overview");
                return view;
            } else {
                view.setViewName("redirect:/commodity/create");
                return view;
            }
        } catch (Exception e) {
            view.setViewName("redirect:/commodity/create");
            return view;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/edit/{goodsId}")
    public ModelAndView edit(@PathVariable("goodsId") String goodsId) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("goodsId", goodsId);
        ResultData resultData = commodityService.fetchCommodity(condition);
        if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/commodity/overview");
            return view;
        }
        Goods targetGoods = ((ArrayList<Goods>) resultData.getData()).get(0);
        view.addObject("goods", targetGoods);

        view.setViewName("/backend/goods/update");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/edit/{goodsId}")
    public ModelAndView edit(@PathVariable("goodsId") String goodsId, @Valid GoodsForm goodsForm, BindingResult result) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/commodity/overview");
            return view;
        }
        Goods previousGoods = new Goods(goodsForm.getName(), Double.parseDouble(goodsForm.getPrice()), goodsForm.getDescription(), goodsForm.isBlock());
        previousGoods.setGoodsId(goodsId);
        ResultData resultData = commodityService.updateCommodity(previousGoods);
        if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/commodity/overview");
            return view;
        }

        view.setViewName("redirect:/commodity/overview");
        return view;

    }

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/goods/overview");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public DataTablePage<Goods> overview(DataTableParam param) {
        DataTablePage<Goods> result = new DataTablePage<Goods>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        ResultData fetchResponse = commodityService.fetchCommodity(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<Goods>) fetchResponse.getData();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{goodsId}")
    public ModelAndView view(@PathVariable("goodsId") String goodsId, String agentId) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("goodsId", goodsId);
        condition.put("blockFlag", false);
        ResultData fetchCommodityData = commodityService.fetchCommodity(condition);
        if(fetchCommodityData.getResponseCode() != ResponseCode.RESPONSE_OK){
        	//这里需要一个错误页面!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        	return view;
        }
        Goods goods = ((List<Goods>)fetchCommodityData.getData()).get(0);
        
        if(agentId != null && agentId != ""){
	        condition.clear();
	        condition.put("agentId", agentId);
	        condition.put("granted", 1);
	        condition.put("blockFlag", false);
	        ResultData fetchAgentData = agentService.fetchAgent(condition);
	        if(fetchAgentData.getResponseCode() != ResponseCode.RESPONSE_OK){
	        	//这里需要一个错误页面!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	        	return view;
	        }
	        Agent agent = ((List<Agent>)fetchAgentData.getData()).get(0);
	        view.addObject("agent", agent);
        }
        view.addObject("goods", goods);
        view.setViewName("/customer/goods/detail");
        return view;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/forbid/{goodsId}")
    public ModelAndView forbid(@PathVariable("goodsId") String goodsId) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("goodsId", goodsId);
        ResultData resultData = commodityService.fetchCommodity(condition);
        if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/commodity/overview");
            return view;
        }
        Goods targetGoods = ((ArrayList<Goods>) resultData.getData()).get(0);
        targetGoods.setBlockFlag(true);
        commodityService.updateCommodity(targetGoods);

        view.setViewName("redirect:/commodity/overview");
        return view;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/enable/{goodsId}")
    public ModelAndView enable(@PathVariable("goodsId") String goodsId) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("goodsId", goodsId);
        ResultData resultData = commodityService.fetchCommodity(condition);
        if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/commodity/overview");
            return view;
        }
        Goods targetGoods = ((ArrayList<Goods>) resultData.getData()).get(0);
        targetGoods.setBlockFlag(false);
        commodityService.updateCommodity(targetGoods);

        view.setViewName("redirect:/commodity/overview");
        return view;
    }

}
