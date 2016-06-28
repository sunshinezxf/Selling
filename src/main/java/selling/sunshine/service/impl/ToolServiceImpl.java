package selling.sunshine.service.impl;

import com.alibaba.fastjson.JSONObject;

import org.springframework.stereotype.Service;


import selling.sunshine.service.ToolService;


import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;

import java.io.IOException;



/**
 * Created by sunshine on 5/10/16.
 */
@Service
public class ToolServiceImpl implements ToolService {

    @Override
    public String getIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    @Override
    public JSONObject getParams(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        try {
            StringBuffer sb = new StringBuffer();
            BufferedReader reader = request.getReader();
            char[] buff = new char[1024];
            int length;
            while ((length = reader.read(buff)) != -1) {
                sb.append(buff, 0, length);
            }
            result = JSONObject.parseObject(sb.toString());
        } catch (IOException e) {
            return result;
        }
        return result;
    }


}
