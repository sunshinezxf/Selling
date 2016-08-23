package selling.sunshine.service;

import org.springframework.web.multipart.MultipartFile;
import common.sunshine.utils.ResultData;

/**
 * Created by sunshine on 6/2/16.
 */
public interface UploadService {
    ResultData upload(MultipartFile file, String base);
}
