package selling.sunshine.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.model.WithdrawRecord;
import selling.sunshine.service.WithdrawService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 7/4/16.
 */
public class WithdrawSchedule {
    private Logger logger = LoggerFactory.getLogger(WithdrawSchedule.class);

    @Autowired
    private WithdrawService withdrawService;

    public void schedule() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("status", 0);
        ResultData fetchResponse = withdrawService.fetchWithdrawRecord(condition);
        if (fetchResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return;
        }
        List<WithdrawRecord> list = (List<WithdrawRecord>) fetchResponse.getData();
        for (WithdrawRecord item : list) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    withdrawService.createWithdrawRecord(item);
                }
            };
            thread.start();
            try {
                thread.sleep(15000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
