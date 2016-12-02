package selling.sunshine.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

public class TencentMapAPI {
     static String key="7VKBZ-HEYRD-D624I-HTE3L-3O5WV-VRBV7";
     
     public static Map<String, String> getDetailInfoByAddress(String address){
    	try {
			address = java.net.URLEncoder.encode(address, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String url = String.format("http://apis.map.qq.com/ws/geocoder/v1/?address=%s" + "&key=" + key,
				address);
		URL myURL = null;
		URLConnection httpsConn = null;
		// 进行转码
		try {
			myURL = new URL(url);
		} catch (MalformedURLException e) {

		}
		try {
			httpsConn = (URLConnection) myURL.openConnection();
			if (httpsConn != null) {
				InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
				BufferedReader br = new BufferedReader(insr);
				String data = null;
				Map<String, String> map = new HashMap<String, String>();
				while ((data = br.readLine()) != null) {
					if (data.contains("province")) {
						data=data.trim();
						if (data.length()>15) {
							String province=data.substring(13, data.length()-2);
							switch (province) {
							case "广西壮族自治区":
								map.put("province", "广西");
								break;
							case "西藏自治区":
								map.put("province", "西藏");
								break;
							case "新疆维吾尔自治区":
								map.put("province", "新疆");
								break;
							case "内蒙古自治区":
								map.put("province", "内蒙古");
								break;
							case "宁夏回族自治区":
								map.put("province", "宁夏");
								break;
							default:
								map.put("province", province.substring(0,province.length()-1));
								break;
							}
						}						
					}
					if (data.contains("city")) {	
						data=data.trim();
						if (data.length()>12) {
							String city=data.substring(9, data.length()-3);
							map.put("city", city);
						}					
					}
					if (data.contains("district")) {
						data=data.trim();	
						if (data.length()>16) {
						   String district=data.substring(13, data.length()-3);
						   map.put("district", district);
						}
					}
				}
				insr.close();
				return map;
			}
		} catch (IOException e) {

		}
		Map<String, String> map = new HashMap<String, String>();
		return map;
     }
     
}
