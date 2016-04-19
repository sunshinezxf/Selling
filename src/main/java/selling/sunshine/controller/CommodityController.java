package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.GoodsForm;
import selling.sunshine.model.Goods;
import selling.sunshine.service.CommodityService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.validation.Valid;

/**
 * Created by sunshine on 4/8/16.
 */
@RequestMapping("/commodity")
@RestController
public class CommodityController {
    private Logger logger = LoggerFactory.getLogger(CommodityController.class);

    @Autowired
    private CommodityService commodityService;

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
            Goods goods = new Goods(form.getName(), Double.parseDouble(form.getPrice()), form.getDescription());
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

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/goods/overview");
        return view;
    }

}
