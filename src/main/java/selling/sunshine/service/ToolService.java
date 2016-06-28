package selling.sunshine.service;

import java.io.OutputStream;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

import selling.sunshine.model.express.Express;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/10/16.
 */
public interface ToolService {
    String getIP(HttpServletRequest request);

    JSONObject getParams(HttpServletRequest request);
    
    ResultData exportExcel(OutputStream os,List<Express> expresseList);
    
}
