package selling.sunshine.service.impl;

import common.sunshine.utils.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import selling.sunshine.service.UploadService;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.utils.SystemTeller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sunshine on 6/2/16.
 */
@Service
public class UploadServiceImpl implements UploadService {
    private Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

    @Override
    public ResultData upload(MultipartFile file, String base) {
        ResultData result = new ResultData();
        try {
            if (file == null || file.getBytes().length == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                return result;
            }
        } catch (IOException e) {
            logger.debug(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }
        String PATH = "/material/upload";
        Date current = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String time = format.format(current);
        StringBuilder builder = new StringBuilder(base);
        builder.append(PATH);
        builder.append("/");
        builder.append(time);
        File directory = new File(builder.toString());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String key = IDGenerator.generate("TH");
        String name = key + suffix;
        String completeName = builder.append(File.separator).append(name).toString();
        File temp = new File(completeName);
        try {
            file.transferTo(temp);
            int index = temp.getPath().indexOf(SystemTeller.tellPath(PATH + "/" + time));
            result.setData(temp.getPath().substring(index));
            
        } catch (IOException e) {
            logger.debug(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }
}
