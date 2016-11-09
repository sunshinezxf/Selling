import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by sunshine on 2016/11/9.
 */
public class SubscribeAccountMenu {
	//线上
		private final static String APPID = "wx15c59fb90e749972";
	    private final static String SECRET = "6ce3f830dc64ce968d366da3fb8be0be";
		//测试
	    //private final static String APPID = "wx23ff32e7d6b145a1";
		//private final static String SECRET = "04b6902b3f1f880cc7a0f8d4d3e1b1a4";
	    private final static String PLACE_ORDER_URL = "http://www.yuncaogangmu.com/agent/order/place";
	    private final static String BIND_ACCOUNT_URL = "http://www.yuncaogangmu.com/agent/bind";
	    private final static String REGISTER_URL = "http://www.yuncaogangmu.com/agent/register";
	    //线上
	    private final static String CUSTOMER_PURCHASE_URL = "http://www.yuncaogangmu.com/commodity/viewlist";
	    //测试
	    //private final static String CUSTOMER_PURCHASE_URL = "http://measure.yuncaogangmu.com/commodity/viewlist";
	    private final static String LETHE_URL = "http://www.yuncaogangmu.com/agent/lethe";
	    private final static String TUOZHAN_URL = "http://www.yuncaogangmu.com/agent/invite";
	    private final static String SHARE_URL = "http://www.yuncaogangmu.com/agent/personalsale";
	    private final static String ACCOUNT_URL = "http://www.yuncaogangmu.com/account/info";
	    private final static String LOGIN_URL = "http://www.yuncaogangmu.com/agent/login";
	    private final static String SEARCH_URL = "http://www.yuncaogangmu.com/customer/consult";

	    public static String queryAccessToken(String appId, String secret) {
	        String result = "";
	        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + secret;
	        try {
	            URL address = new URL(url);
	            HttpURLConnection connection = (HttpURLConnection) address.openConnection();
	            connection.setRequestMethod("GET");
	            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            connection.setDoOutput(true);
	            connection.setDoInput(true);
	            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
	            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
	            connection.connect();
	            InputStream is = connection.getInputStream();
	            int size = is.available();
	            byte[] bytes = new byte[size];
	            is.read(bytes);
	            String message = new String(bytes, "UTF-8");
	            JSONObject object = JSON.parseObject(message);
	            result = object.getString("access_token");
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            return result;
	        }
	    }

	    public static String createMenu() {

	        JSONObject sanqi1 = new JSONObject();
	        sanqi1.put("name", "参中之王");
	        sanqi1.put("type", "view");
	        try {
	            sanqi1.put("url", "http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=2247483669&idx=1&sn=9a01fe45578d9bbd3f3503dfd46d7fc2&mpshare=1&scene=1&srcid=1025NErdEfuwNNzZ5lC6rYy5#rd");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        JSONObject sanqi2 = new JSONObject();
	        sanqi2.put("name", "三七功效");
	        sanqi2.put("type", "view");
	        try {
	            sanqi2.put("url", "http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=2247483670&idx=1&sn=ff34c12e664c54bcb6a9135d8cb6a54c&mpshare=1&scene=1&srcid=1025yMbiWIR3Teu7yF5PC3ak#rd");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        JSONObject sanqi3 = new JSONObject();
	        sanqi3.put("name", "养生美食");
	        sanqi3.put("type", "view");
	        try {
	            sanqi3.put("url", "http://mp.weixin.qq.com/s?__biz=MzIwNjI1OTY2Mg==&mid=503177452&idx=1&sn=a69770f51e67758a86b4990b2fe01b3a&scene=18#wechat_redirect");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        JSONObject sanqi4 = new JSONObject();
	        sanqi4.put("name", "火眼金睛");
	        sanqi4.put("type", "view");
	        try {
	            sanqi4.put("url", "http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=2247483671&idx=1&sn=98320ebb0c91a287ebb89cae94843506&mpshare=1&scene=1&srcid=1025vqvzkaP8enYsviA3hEvY#rd");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        JSONObject ycgm = new JSONObject();
	        ycgm.put("name", "制作工艺");
	        ycgm.put("type", "view");
	        try {
	            ycgm.put("url", "http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=2247483673&idx=1&sn=2f5f641373d96503219dae12eda7f4d4&mpshare=1&scene=1&srcid=1025dzEyrAx9wrjAykesjEwi#rd");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        JSONObject ycgm2 = new JSONObject();
	        ycgm2.put("name", "招聘信息");
	        ycgm2.put("type", "view");
	        try {
	            ycgm2.put("url", "http://mp.weixin.qq.com/s?__biz=MzIwNjI1OTY2Mg==&mid=2650661085&idx=1&sn=a3120a6c436ab6c5706e8cdc1b102994&scene=18#wechat_redirect");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        JSONObject ycgm3 = new JSONObject();
	        ycgm3.put("name", "我的奋斗");
	        ycgm3.put("type", "view");
	        try {
	            ycgm3.put("url", "http://mp.weixin.qq.com/s?__biz=MzIwNjI1OTY2Mg==&mid=2650661078&idx=1&sn=2bb670e20b5c19f778ef8d805e0d4d78&scene=18#wechat_redirect");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        JSONArray sanqi = new JSONArray();
	        sanqi.add(sanqi1);
	        sanqi.add(sanqi2);
	        sanqi.add(sanqi3);
	        sanqi.add(sanqi4);
	        sanqi.add(ycgm);
	        
	        JSONObject sanqiButton = new JSONObject();
	        sanqiButton.put("name", "三七");
	        sanqiButton.put("sub_button", sanqi);
	        
	        JSONArray yc = new JSONArray();
	        yc.add(ycgm);
	        yc.add(ycgm2);
	        yc.add(ycgm3);
	        
	        JSONObject ycButton = new JSONObject();
	        ycButton.put("name", "云草纲目");
	        ycButton.put("sub_button",yc);
	        

//	        JSONObject agent_bind = new JSONObject();
//	        agent_bind.put("name", "账号绑定");
//	        agent_bind.put("type", "view");
//	        try {
//	            agent_bind.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx67fcdc4fa58f7578&redirect_uri=" + URLEncoder.encode(BIND_ACCOUNT_URL, "utf-8") + "&response_type=code&scope=snsapi_base&state=view#wechat_redirect");
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
	        
	        
//	        JSONObject giftEvent = new JSONObject();
//			giftEvent.put("name", "中秋活动");
//			giftEvent.put("type", "click");
//			giftEvent.put("key", "giftEvent");
//			
//			JSONObject queryEvent = new JSONObject();
//			queryEvent.put("name", "查询申请");
//			queryEvent.put("type", "view");
//			queryEvent.put("url", "http://event.yuncaogangmu.com/event/consultapplication");
//			
//			JSONObject eventTip = new JSONObject();
//			eventTip.put("name", "申请指南");
//			eventTip.put("type", "view");
//			eventTip.put("url", "http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=2247483897&idx=1&sn=462889d76e9dc55e4fbe9650d2a097d8&scene=0#wechat_redirect");
			
//			JSONArray eventArray = new JSONArray();
//			eventArray.add(giftEvent);
//			eventArray.add(queryEvent);
//			eventArray.add(eventTip);
			
//			JSONObject eventButton = new JSONObject();
//			eventButton.put("name", "活动");
//			eventButton.put("sub_button", eventArray);
	        
	        
	        
	        JSONObject purchase = new JSONObject();
			purchase.put("name", "三七购买");
			purchase.put("type", "view");
			try {   
				//线上
				purchase.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx67fcdc4fa58f7578&redirect_uri=" + URLEncoder.encode(CUSTOMER_PURCHASE_URL, "utf-8") + "&response_type=code&scope=snsapi_base&state=view#wechat_redirect");
				//测试
				//purchase.put("url", CUSTOMER_PURCHASE_URL);
				//purchase.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx23ff32e7d6b145a1&redirect_uri=" + URLEncoder.encode(CUSTOMER_PURCHASE_URL, "utf-8") + "&response_type=code&scope=snsapi_base&state=view#wechat_redirect");
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			
			JSONObject search = new JSONObject();
			search.put("name", "订单查询");
			search.put("type", "view");
			try {     
				//线上
				search.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx67fcdc4fa58f7578&redirect_uri=" + URLEncoder.encode(SEARCH_URL, "utf-8") + "&response_type=code&scope=snsapi_base&state=view#wechat_redirect");
				//测试
				//search.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx67fcdc4fa58f7578&redirect_uri=" + URLEncoder.encode(SEARCH_URL, "utf-8") + "&response_type=code&scope=snsapi_base&state=view#wechat_redirect");
			} catch (Exception e) {
			    e.printStackTrace();
			}
	      
			JSONArray customer = new JSONArray();
			customer.add(purchase);
			customer.add(search);
			
			JSONObject customerButton = new JSONObject();
			customerButton.put("name", "商城");
			customerButton.put("sub_button", customer);
	        
	        JSONArray buttons = new JSONArray();
	        buttons.add(sanqiButton);
	       // buttons.add(eventButton);
	        buttons.add(customerButton);
	        JSONObject menu = new JSONObject();
	        menu.put("button", buttons);
	        String token = queryAccessToken(APPID, SECRET);
	        String link = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + token;
	        try {
	            URL url = new URL(link);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            connection.setDoOutput(true);
	            connection.setDoInput(true);
	            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
	            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
	            connection.connect();
	            OutputStream os = connection.getOutputStream();
	            os.write(menu.toString().getBytes());
	            os.flush();
	            os.close();

	            InputStream is = connection.getInputStream();
	            int size = is.available();
	            byte[] bytes = new byte[size];
	            is.read(bytes);
	            String message = new String(bytes, "UTF-8");
	            return "返回信息" + message;
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return "创建菜单失败";
	    }

	    public static String deleteMenu() {
	        String token = queryAccessToken(APPID, SECRET);
	        String link = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + token;
	        try {
	            URL url = new URL(link);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");
	            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            connection.setDoOutput(true);
	            connection.setDoInput(true);
	            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
	            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
	            connection.connect();
	            OutputStream os = connection.getOutputStream();
	            os.flush();
	            os.close();
	            InputStream is = connection.getInputStream();
	            int size = is.available();
	            byte[] bytes = new byte[size];
	            is.read(bytes);
	            String message = new String(bytes, "UTF-8");
	            return "返回信息:" + message;
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return "删除菜单失败";
	    }
	    
	    String deleteMessage = SubscribeAccountMenu.deleteMenu();
        System.out.println("删除操作: " + deleteMessage);
        String createMessage = SubscribeAccountMenu.createMenu();
        System.out.println("创建操作" + createMessage);
}
