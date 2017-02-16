package selling.sunshine.controller;

import common.sunshine.model.selling.admin.Admin;
import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.user.User;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.RefundConfigForm;
import selling.sunshine.model.BackOperationLog;
import selling.sunshine.model.RefundConfig;
import selling.sunshine.model.cashback.CashBackRecord;
import selling.sunshine.model.cashback.support.CashBackLevel;
import selling.sunshine.service.*;
import selling.sunshine.utils.ZipCompressor;
import selling.sunshine.vo.cashback.CashBack4Agent;
import selling.sunshine.vo.cashback.CashBack4AgentPerMonth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sunshine on 8/12/16.
 */
@RestController
@RequestMapping("/cashback")
public class CashBackController {
	private Logger logger = LoggerFactory.getLogger(CashBackController.class);

	@Autowired
	private CashBackService cashBackService;

	@Autowired
	private AgentService agentService;

	@Autowired
	private RefundService refundService;

	@Autowired
	private LogService logService;

	@Autowired
	private CommodityService commodityService;

	@Autowired
	private ToolService toolService;

	@RequestMapping(method = RequestMethod.GET, value = "/month")
	public ModelAndView monthly() {
		ModelAndView view = new ModelAndView();
		Map<String, Object> condition = new HashMap<>();
		ResultData response = cashBackService.fetchCashBackPerMonth(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			double total = 0;
			List<CashBack4AgentPerMonth> list = (List<CashBack4AgentPerMonth>) response.getData();
			for (CashBack4AgentPerMonth item : list) {
				total += item.getAmount();
			}
			view.addObject("total", total);
		}
		view.setViewName("/backend/cashback/cashback_monthly_record");
		return view;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/month")
	public DataTablePage<CashBack4AgentPerMonth> monthly(DataTableParam param) {
		DataTablePage<CashBack4AgentPerMonth> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		ResultData response = cashBackService.fetchCashBackPerMonth(condition, param);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<CashBack4AgentPerMonth>) response.getData();
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{agentId}/month")
	public ModelAndView cashback(@PathVariable("agentId") String agentId) {
		ModelAndView view = new ModelAndView();
		Calendar current = Calendar.getInstance();
		current.add(Calendar.MONTH, -1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
		view.addObject("month", format.format(current.getTime()));
		Map<String, Object> condition = new HashMap<>();
		condition.put("agentId", agentId);
		ResultData response = agentService.fetchAgent(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			view.setViewName("redirect:/cashback/month");
			return view;
		}
		Agent agent = ((List<Agent>) response.getData()).get(0);
		view.addObject("agent", agent);
		response = cashBackService.fetchCashBackPerMonth(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			view.setViewName("redirect:/cashback/month");
			return view;
		}
		CashBack4AgentPerMonth cashback = ((List<CashBack4AgentPerMonth>) response.getData()).get(0);
		view.addObject("cashback", cashback);
		condition.clear();
		// 获取自销的返现记录详情
		condition.put("agentId", cashback.getAgent().getAgentId());
		Calendar date = Calendar.getInstance();
		format = new SimpleDateFormat("yyyy-MM");
		condition.put("createAt", format.format(date.getTime()) + "%");
		condition.put("level", CashBackLevel.SELF.getCode());
		response = refundService.fetchRefundRecord(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			view.setViewName("redirect:/cashback/month");
			return view;
		}
		List<CashBackRecord> self = ((List<CashBackRecord>) response.getData());
		view.addObject("self", self);
		condition.clear();
		// 获取所有的直接拓展代理的返现详情
		condition.put("agentId", agentId);
		condition.put("createAt", format.format(date.getTime()) + "%");
		condition.put("level", CashBackLevel.DIRECT.getCode());
		response = refundService.fetchRefundRecord(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			List<CashBackRecord> direct = (List<CashBackRecord>) response.getData();
			view.addObject("direct", direct);
		}
		condition.clear();
		// 获取所有的间接拓展代理的返现详情
		condition.put("agentId", agentId);
		condition.put("createAt", format.format(date.getTime()) + "%");
		condition.put("level", CashBackLevel.INDIRECT.getCode());
		response = refundService.fetchRefundRecord(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			List<CashBackRecord> indirect = (List<CashBackRecord>) response.getData();
			view.addObject("indirect", indirect);
		}
		view.setViewName("/backend/cashback/cashback_month_detail");
		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/overview")
	public ModelAndView overview() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/backend/cashback/overview");
		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/record")
	public ModelAndView record() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/backend/cashback/cashback_record");
		return view;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/record")
	public DataTablePage<CashBack4Agent> record(DataTableParam param) {
		DataTablePage<CashBack4Agent> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		ResultData response = cashBackService.fetchCashBack(condition, param);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<CashBack4Agent>) response.getData();
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/produce")
	public String produce(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {

		Map<String, Object> condition = new HashMap<>();
		ResultData result = cashBackService.fetchCashBackPerMonth(condition);
		if (result.getResponseCode() != ResponseCode.RESPONSE_OK) {
			return "";
		}
		List<CashBack4AgentPerMonth> list = (List<CashBack4AgentPerMonth>) result.getData();
		ResultData produceResponse = cashBackService.produce(list);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		String date = dateFormat.format(calendar.getTime());
		String summaryPath = produceResponse.getData().toString();

		String path = DeliverController.class.getResource("/").getPath();
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("windows") >= 0) {
			path = path.substring(1);
		}
		int index = path.lastIndexOf("/WEB-INF/classes/");
		String parent = path.substring(0, index);
		String directory = "/material/journal/cashback";

		List<String> pathList = new ArrayList<>();
		StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(date.replaceAll("-", ""));
		pathList.add(sb.toString());
		pathList.add(summaryPath);
		String zipName = "月度返现报表";
		StringBuffer sb2 = new StringBuffer(parent).append(directory).append("/").append(zipName + ".zip");
		ZipCompressor zipCompressor = new ZipCompressor(sb2.toString());
		zipCompressor.compress(pathList);

		File file1 = new File(summaryPath);
		file1.delete();

		// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
		response.setContentType("multipart/form-data");
		// 2.设置文件头：最后一个参数是设置下载文件名
		response.setHeader("Content-Disposition",
				"attachment;fileName=" + URLEncoder.encode(zipName + ".zip", "utf-8"));
		OutputStream out;
		File file = new File(sb2.toString());
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream buff = new BufferedInputStream(fis);
			byte[] b = new byte[1024];// 相当于我们的缓存
			long k = 0;// 该值用于计算当前实际下载了多少字节

			// 3.通过response获取OutputStream对象(out)
			out = response.getOutputStream();
			// 开始循环下载
			while (k < file.length()) {
				int j = buff.read(b, 0, 1024);
				k += j;
				out.write(b, 0, j);
			}
			buff.close();
			fis.close();
			out.close();
			out.flush();
			file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/config")
	public ModelAndView config() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/backend/cashback/cashback_config");
		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/config/list/goods/{goodsId}")
	public ModelAndView configList(@PathVariable("goodsId") String goodsId) {
		ModelAndView view = new ModelAndView();
		view.addObject("goodsId", goodsId);
		view.setViewName("/backend/cashback/cashback_config_list");
		return view;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/config/overview/{goodsId}")
	public DataTablePage<RefundConfig> configOverview(@PathVariable("goodsId") String goodsId, DataTableParam param) {
		DataTablePage<RefundConfig> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("goodsId", goodsId);
		condition.put("blockFlag", false);
		ResultData response = refundService.fetchRefundConfig(condition, param);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<RefundConfig>) response.getData();
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/config/goods/{refundConfigId}")
	public ResultData goodsConfig(@PathVariable("refundConfigId") String refundConfigId) {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("refundConfigId", refundConfigId);
		condition.put("blockFlag", false);
		ResultData fetchResponse = refundService.fetchRefundConfig(condition);
		result.setResponseCode(fetchResponse.getResponseCode());
		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(((List<RefundConfig>) fetchResponse.getData()).get(0));
		}
		return result;
	}

	/**
	 * 修改返现配置
	 * @param configId
	 * @param goodsId
	 * @param form
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/config/goods/{configId}/{goodsId}")
	public ModelAndView config(@PathVariable("configId") String configId, @PathVariable("goodsId") String goodsId,
			@Valid RefundConfigForm form, BindingResult result, HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		if (result.hasErrors()) {
			view.setViewName("redirect:/cashback/config/list/goods/" + goodsId);
			return view;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("goodsId", goodsId);
		ResultData fetchResponse=commodityService.fetchGoods4Customer(condition);
		if (fetchResponse.getResponseCode() != ResponseCode.RESPONSE_OK){
			//相应的商品没有找到，直接返回
			view.setViewName("redirect:/cashback/config/list/goods/" + goodsId);
			return view;
		}
		Goods4Customer goods = ((List<Goods4Customer>) fetchResponse.getData()).get(0);
		RefundConfig config = new RefundConfig(goods, Integer.parseInt(form.getAmountTrigger()),
				Double.parseDouble(form.getLevel1Percent()), Double.parseDouble(form.getLevel2Percent()),
				Double.parseDouble(form.getLevel3Percent()), Integer.parseInt(form.getMonthConfig()));
		config.setRefundConfigId(configId);
		if (form.getAmountTriggerTop()==null || form.getAmountTriggerTop()==""){
			config.setAmountTopTrigger(0);
		}else {
			config.setAmountTopTrigger(Integer.parseInt(form.getAmountTriggerTop()));
		}
		if (form.getApplyMonths() != "" && !form.getApplyMonths().equals("普遍适用")) {
			config.setUniversal(false);
			config.setUniversalMonth(Integer.parseInt(form.getApplyMonths()));
		} else {
			config.setUniversal(true);
			config.setUniversalMonth(1);
		}
		ResultData createResponse = refundService.createRefundConfig(config);
		if (createResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			Subject subject = SecurityUtils.getSubject();
			User user = (User) subject.getPrincipal();
			if (user == null) {
				view.setViewName("redirect:/cashback/config/list/goods/" + goodsId);
				return view;
			}
			Admin admin = user.getAdmin();
			BackOperationLog backOperationLog = new BackOperationLog(admin.getUsername(), toolService.getIP(request),
					"管理员" + admin.getUsername() + "修改了商品名称为" + goods.getName() + "的返现配置");
			logService.createbackOperationLog(backOperationLog);

			view.setViewName("redirect:/cashback/config/list/goods/" + goodsId);
			return view;
		}
		view.setViewName("redirect:/cashback/config/list/goods/" + goodsId);
		return view;
	}

	/**
	 * 新建返现配置
	 * @param goodsId
	 * @param form
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/config/create/{goodsId}")
	public ModelAndView createConfig(@PathVariable("goodsId") String goodsId, @Valid RefundConfigForm form,
			BindingResult result, HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		if (result.hasErrors()) {
			view.setViewName("redirect:/cashback/config/list/goods/" + goodsId);
			return view;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("goodsId", goodsId);
		condition.put("blockFlag", false);
		if (form.getApplyMonths() != "") {
			condition.put("universal", false);
		} else {
			condition.put("universal", true);
		}
		//当数据库中已经存在前n个月的返现配置时，不能新建前m个月的返现配置
		if (form.getApplyMonths() != ""){
			ResultData fetchResponse=refundService.fetchRefundConfig(condition);
			if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
				RefundConfig config=((List<RefundConfig>)fetchResponse.getData()).get(0);
				if (config.getUniversalMonth()!=Integer.parseInt(form.getApplyMonths())){
					view.setViewName("redirect:/cashback/config/list/goods/" + goodsId);
					return view;
				}
			}
		}
		condition.put("amountTrigger", form.getAmountTrigger());
		condition.put("amountTopTrigger", form.getAmountTriggerTop());
		ResultData fetchResponse=refundService.fetchRefundConfig(condition);
		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			//当数据库中相应的返现配置已经存在，则不能添加，直接返回
			view.setViewName("redirect:/cashback/config/list/goods/" + goodsId);
			return view;
		}
		condition.clear();
		condition.put("goodsId", goodsId);
		fetchResponse=commodityService.fetchGoods4Customer(condition);
		if (fetchResponse.getResponseCode() != ResponseCode.RESPONSE_OK){
			//相应的商品没有找到，直接返回
			view.setViewName("redirect:/cashback/config/list/goods/" + goodsId);
			return view;
		}
		Goods4Customer goods = ((List<Goods4Customer>) fetchResponse.getData()).get(0);
		RefundConfig config = new RefundConfig(goods, Integer.parseInt(form.getAmountTrigger()),
				Double.parseDouble(form.getLevel1Percent()), Double.parseDouble(form.getLevel2Percent()),
				Double.parseDouble(form.getLevel3Percent()), Integer.parseInt(form.getMonthConfig()));
		config.setRefundConfigId("null");
		if (form.getAmountTriggerTop()==null || form.getAmountTriggerTop()==""){
			config.setAmountTopTrigger(0);
		}else {
			config.setAmountTopTrigger(Integer.parseInt(form.getAmountTriggerTop()));
		}
		if (form.getApplyMonths() != "") {
			config.setUniversal(false);
			config.setUniversalMonth(Integer.parseInt(form.getApplyMonths()));
		} else {
			config.setUniversal(true);
			config.setUniversalMonth(1);
		}
		ResultData createResponse = refundService.createRefundConfig(config);
		if (createResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			Subject subject = SecurityUtils.getSubject();
			User user = (User) subject.getPrincipal();
			if (user == null) {
				view.setViewName("redirect:/cashback/config/list/goods/" + goodsId);
				return view;
			}
			Admin admin = user.getAdmin();
			BackOperationLog backOperationLog = new BackOperationLog(admin.getUsername(), toolService.getIP(request),
					"管理员" + admin.getUsername() + "添加了商品名称为" + goods.getName() + "的返现配置");
			logService.createbackOperationLog(backOperationLog);

			view.setViewName("redirect:/cashback/config/list/goods/" + goodsId);
			return view;
		}
		view.setViewName("redirect:/cashback/config/list/goods/" + goodsId);
		return view;
	}

	// @RequestMapping(method = RequestMethod.GET, value = "/overview")
	// public ModelAndView overview() {
	// ModelAndView view = new ModelAndView();
	// view.setViewName("/backend/refund/refund_record");
	// return view;
	// }

}
