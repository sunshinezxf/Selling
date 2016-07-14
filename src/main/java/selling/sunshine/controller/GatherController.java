package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.TimeRangeForm;
import selling.sunshine.model.CustomerOrderBill;
import selling.sunshine.model.OrderBill;
import selling.sunshine.service.BillService;
import selling.sunshine.service.GatherService;
import selling.sunshine.utils.DateUtils;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;
import selling.sunshine.utils.ZipCompressor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 7/12/16.
 */
@RequestMapping("/gather")
@RestController
public class GatherController {
    private Logger logger = LoggerFactory.getLogger(GatherController.class);

    @Autowired
    private BillService billService;

    @Autowired
    private GatherService gatherService;

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView gather() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/finance/gather");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public ResultData gather(@Valid TimeRangeForm form, BindingResult result) {
        ResultData data = new ResultData();
        boolean empty = true;
        if (result.hasErrors()) {
            data.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return data;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("start", form.getStart());
        condition.put("end", form.getEnd());
        condition.put("status", 1);
        ResultData queryResponse = billService.fetchOrderBill(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            empty = false;
            List<OrderBill> list = (List<OrderBill>) queryResponse.getData();
            gatherService.produce(list);
        }
        queryResponse = billService.fetchCustomerOrderBill(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            empty = false;
            List<CustomerOrderBill> list = ((List<CustomerOrderBill>) queryResponse.getData());
            gatherService.produce(list);
        }

        if (empty) {
            data.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return data;
        }
        
        String path = GatherController.class.getResource("/").getPath();
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows") >= 0) {
            path = path.substring(1);
        }

        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/gather";
        DateUtils dateUtils = new DateUtils();
        dateUtils.process(form.getStart(), form.getEnd());
        List<String> dateList = dateUtils.getDateList();
        List<String> pathList = new ArrayList<String>();
        dateList.forEach((date) -> {
            StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(date.replaceAll("-", ""));
            pathList.add(sb.toString());
        });
        String zipName = IDGenerator.generate("Gather");
        StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(zipName + ".zip");
        ZipCompressor zipCompressor = new ZipCompressor(sb.toString());
        zipCompressor.compress(pathList);
        data.setData(zipName);

        return data;
    }
    
	@RequestMapping(method = RequestMethod.GET, value = "/download/{fileName}/{tempFileName}")
	public String downlodad(@PathVariable("fileName") String fileName,@PathVariable("tempFileName") String tempFileName, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
		response.setContentType("multipart/form-data");
		// 2.设置文件头：最后一个参数是设置下载文件名
		response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("收款单报表_"+fileName+".zip", "utf-8"));
		OutputStream out;
		// 通过文件路径获得File对象
		System.err.println(tempFileName);
		String path = IndentController.class.getResource("/").getPath();
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("windows") >= 0) {
			path = path.substring(1);
		}
		int index = path.lastIndexOf("/WEB-INF/classes/");
		String parent = path.substring(0, index);
		String directory = "/material/journal/gather";
		StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(tempFileName + ".zip");
		File file = new File(sb.toString());

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
}
