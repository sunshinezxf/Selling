package selling.sunshine.service;

import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 6/1/16.
 */
public interface MessageService {
    ResultData send(String phone, String message);
}
