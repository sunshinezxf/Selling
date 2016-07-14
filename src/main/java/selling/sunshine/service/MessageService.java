package selling.sunshine.service;

import selling.sunshine.utils.ResultData;

import java.util.Set;

/**
 * Created by sunshine on 6/1/16.
 */
public interface MessageService {
    ResultData send(String phone, String message);

    ResultData send(Set<String> phone, String message);

    ResultData preview(String phone, String message);
}
