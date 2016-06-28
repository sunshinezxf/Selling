package selling.sunshine.service;



import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;



/**
 * Created by sunshine on 5/10/16.
 */
public interface ToolService {
    String getIP(HttpServletRequest request);

    JSONObject getParams(HttpServletRequest request);

    
}
