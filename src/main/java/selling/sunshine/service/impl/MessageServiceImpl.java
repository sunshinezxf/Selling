package selling.sunshine.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import selling.sunshine.service.MessageService;
import selling.sunshine.utils.PlatformConfig;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * Created by sunshine on 6/1/16.
 */
@Service
public class MessageServiceImpl implements MessageService {
    private Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    /**
     * 发送短信给一个号码
     * @param phone
     * @param message
     * @return
     */
    @Override
    public ResultData send(final String phone, final String message) {
        ResultData result = new ResultData();
        try {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Client client = Client.create();
                    client.addFilter(new HTTPBasicAuthFilter("api", PlatformConfig.getValue("message_api_key")));
                    WebResource webResource = client.resource(
                            "http://sms-api.luosimao.com/v1/send.json");
                    MultivaluedMapImpl formData = new MultivaluedMapImpl();
                    formData.add("mobile", phone);
                    formData.add("message", message);
                    ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                            post(ClientResponse.class, formData);
                    int status = response.getStatus();
                    if (status != HttpStatus.OK.value()) {
                        logger.error(JSONObject.toJSONString(response));
                    }
                }
            };
            thread.start();
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    /**
     * 发送短信给一个list的手机号
     * @param phone
     * @param message
     * @return
     */
    @Override
    public ResultData send(final Set<String> phone, final String message) {
        ResultData result = new ResultData();
        try {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    StringBuffer list = new StringBuffer();
                    phone.forEach(item -> list.append(item).append(","));
                    list.deleteCharAt(list.length() - 1);
                    Client client = Client.create();
                    client.addFilter(new HTTPBasicAuthFilter("api", PlatformConfig.getValue("message_api_key")));
                    WebResource webResource = client.resource(
                            "http://sms-api.luosimao.com/v1/send_batch.json");
                    MultivaluedMapImpl formData = new MultivaluedMapImpl();
                    formData.add("mobile_list", list.toString());
                    formData.add("message", message);
                    ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                            post(ClientResponse.class, formData);
                    int status = response.getStatus();
                    if (status != HttpStatus.OK.value()) {
                        logger.error(JSONObject.toJSONString(response));
                    }
                }
            };
            thread.start();
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    /**
     * 测试发送的短信（发送一条）
     * @param phone
     * @param message
     * @return
     */
    @Override
    public ResultData preview(String phone, String message) {
        ResultData result = new ResultData();
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", PlatformConfig.getValue("message_api_key")));
        WebResource webResource = client.resource(
                "http://sms-api.luosimao.com/v1/send.json");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("mobile", phone);
        formData.add("message", message);
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                post(ClientResponse.class, formData);
        int status = response.getStatus();
        if (status != HttpStatus.OK.value()) {
            logger.error(JSONObject.toJSONString(response));
        }
        JSONObject feedback = JSON.parseObject(response.getEntity(String.class));
        int errorCode = feedback.getIntValue("error");
        if (errorCode != 0) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(feedback.getString("msg"));
            logger.error(feedback.getString("msg"));
            return result;
        }
        return result;
    }
}
