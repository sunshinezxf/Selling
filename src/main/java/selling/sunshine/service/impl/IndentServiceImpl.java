package selling.sunshine.service.impl;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import selling.sunshine.service.IndentService;
import selling.sunshine.utils.PlatformConfig;
import selling.sunshine.utils.ResultData;

import java.io.File;

/**
 * Created by sunshine on 7/7/16.
 */
@Service
public class IndentServiceImpl implements IndentService {
    private Logger logger = LoggerFactory.getLogger(IndentServiceImpl.class);

    @Override
    public ResultData generateIndent() {
        ResultData result = new ResultData();
        try {
            Workbook workbook = WorkbookFactory.create(new File(PlatformConfig.getValue("indent_template")));
        } catch (Exception e) {
            logger.error(e.getMessage());
            
        }
        return result;
    }
}
