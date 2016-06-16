package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import selling.sunshine.model.GoodsThumbnail;
import selling.sunshine.service.CommodityService;
import selling.sunshine.service.UploadService;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;
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
    
    @Autowired
    private CommodityService commodityService;

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
            GoodsThumbnail thumbnail=new GoodsThumbnail();
            thumbnail.setPath(completeName);
            
            int index = temp.getPath().indexOf(SystemTeller.tellPath(PATH + "/" + time));
            result.setData(temp.getPath().substring(index));
            result.setDescription(name);
            result.setData(completeName);
        } catch (IOException e) {
            logger.debug(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }
}
