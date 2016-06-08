package selling.sunshine.service.impl;

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
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.ws.rs.core.MediaType;

/**
 * Created by sunshine on 6/1/16.
 */
@Service
public class MessageServiceImpl implements MessageService {
    private Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Override
    public ResultData send(String phone, String message) {
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
                        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                        result.setDescription(JSONObject.toJSONString(response));
                        logger.error(JSONObject.toJSONString(response));
                    }
                }
            };
            thread.start();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }
}
