package selling.sunshine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.GoodsForm;
import selling.sunshine.model.Goods;
import selling.sunshine.service.CommodityService;

/**
 * Created by sunshine on 4/8/16.
 */
@RequestMapping("/commodity")
@RestController
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    @RequestMapping(method = RequestMethod.GET, value = "/create")
    public ModelAndView create() {
        ModelAndView view = new ModelAndView();

        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ModelAndView create(GoodsForm form) {
        ModelAndView view = new ModelAndView();

        return view;
    }
}
