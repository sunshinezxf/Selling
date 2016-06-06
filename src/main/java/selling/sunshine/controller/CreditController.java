package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import selling.sunshine.service.UploadService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;


/**
 * Created by sunshine on 6/6/16.
 */
@RestController
@RequestMapping("/credit")
public class CreditController {
    private Logger logger = LoggerFactory.getLogger(CreditController.class);

    @Autowired
    private UploadService uploadService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public ResultData upload(MultipartHttpServletRequest request) {
        ResultData result = new ResultData();
        String context = request.getSession().getServletContext().getRealPath("/");
        try {
            MultipartFile file = request.getFile("credit");
            ResultData response = uploadService.upload(file, context);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setData(response.getData());
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }
}
