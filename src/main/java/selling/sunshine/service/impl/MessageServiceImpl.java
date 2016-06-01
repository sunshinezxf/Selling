package selling.sunshine.service.impl;

import org.springframework.stereotype.Service;
import selling.sunshine.service.MessageService;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 6/1/16.
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Override
    public ResultData send(String phone, String message) {
        ResultData result = new ResultData();
        
        return result;
    }
}
