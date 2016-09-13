package selling.sunshine.service;

import java.util.Map;

import common.sunshine.model.selling.util.ShortUrl;
import common.sunshine.utils.ResultData;

public interface ShortUrlService {
	ResultData createShortUrl(ShortUrl shortUrl);
	
	ResultData fetchShortUrl(Map<String, Object> condition);
}
