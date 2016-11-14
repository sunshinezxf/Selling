package selling.sunshine.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class BaiduMapUtils {

	private static String ak = "YkGjlR5HVXkCIg07B02dfKRZYUCpeUPW";

	/*
	 * 根据详细地理位置获取经纬度
	 */
	public static Map<String, BigDecimal> getLatAndLngByAddress(String addr) {
		String address = "";
		String lat = "";
		String lng = "";
		try {
			address = java.net.URLEncoder.encode(addr, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String url = String.format("http://api.map.baidu.com/geocoder/v2/?" + "ak=" + ak + "&output=json&address=%s",
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
				if ((data = br.readLine()) != null) {
					if (data.charAt(10)=='0') {
						lat = data.substring(data.indexOf("\"lat\":") + ("\"lat\":").length(),
								data.indexOf("},\"precise\""));
						lng = data.substring(data.indexOf("\"lng\":") + ("\"lng\":").length(), data.indexOf(",\"lat\""));
						Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
						map.put("lat", new BigDecimal(lat));
						map.put("lng", new BigDecimal(lng));
						return map;
					}					
				}
				insr.close();
			}
		} catch (IOException e) {

		}
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		return map;
	}

	
	/*
	 * 根据经纬度获取详细地理位置
	 */
	public static Map<String, String> getAddressByLatAndLng(String x, String y) throws IOException {
		URL url = new URL("http://api.map.baidu.com/geocoder?" + ak + "=您的密钥" + "&callback=renderReverse&location=" + x
				+ "," + y + "&output=json");
		URLConnection connection = url.openConnection();
		/**
		 * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
		 * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
		 */
		connection.setDoOutput(true);
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
		// remember to clean up
		out.flush();
		out.close();
		// 一旦发送成功，用以下方法就可以得到服务器的回应：
		String res;
		InputStream l_urlStream;
		l_urlStream = connection.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(l_urlStream, "UTF-8"));
		StringBuilder sb = new StringBuilder("");
		while ((res = in.readLine()) != null) {
			sb.append(res.trim());
		}
		String str = sb.toString();
		Map<String, String> map = null;
		if (StringUtils.isNotEmpty(str)) {
			int addStart = str.indexOf("formatted_address\":");
			int addEnd = str.indexOf("\",\"business");
			if (addStart > 0 && addEnd > 0) {
				String address = str.substring(addStart + 20, addEnd);
				map = new HashMap<String, String>();
				map.put("address", address);
				int start=str.indexOf("province\":");
				int end = str.indexOf("\",\"street");
				if(start > 0 && end > 0){
					String province = str.substring(start + 11, end);
					map.put("province", province);
				}
				return map;
			}
		}

		return null;

	}

}
