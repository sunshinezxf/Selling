package selling.sunshine.dao;

import java.util.Map;

import common.sunshine.model.selling.util.ShortUrl;
import common.sunshine.utils.ResultData;

public interface ShortUrlDao {
	ResultData insertShortUrl(ShortUrl shortUrl);
	
	ResultData queryShortUrl(Map<String, Object> condition);
}
