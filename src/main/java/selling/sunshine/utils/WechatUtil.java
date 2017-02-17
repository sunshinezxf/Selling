package selling.sunshine.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import common.sunshine.utils.IDGenerator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import selling.wechat.model.Follower;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sunshine on 5/24/16.
 */
public class WechatUtil {
    private static Logger logger = LoggerFactory.getLogger(WechatUtil.class);

    public static String queryAccessToken() {
        String result = "";
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + PlatformConfig.getValue("wechat_appid") + "&secret=" + PlatformConfig.getValue("wechat_secret");
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
            PlatformConfig.setJsapiTicket(queryJsApiTicket(result));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    public static String queryOauthOpenId(String code) {
        String result = "";
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + PlatformConfig.getValue("wechat_appid") + "&secret=" + PlatformConfig.getValue("wechat_secret") + "&code=" + code + "&grant_type=authorization_code";
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
            result = object.getString("openid");
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
            return result;
        }
    }

    public static Follower queryUserInfo(String openId, String token) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token + "&openid=" + openId + "&lang=zh_CN";
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
            JSONObject json = JSONObject.parseObject(message);
            Follower follower = new Follower(json.getString("openid"), json.getString("nickname"), json.getShort("sex"), json.getString("city"), json.getString("province"));
            return follower;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String queryJsApiTicket(String token) {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + token + "&type=jsapi";
        String result = "";
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
            result = object.getString("ticket");
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
            return result;
        }
    }

    public static String downloadCredit(String media_id, String token, String base) {
        logger.debug("creditdebug");
        String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=" + token + "&media_id=" + media_id;
        String result = "";
        logger.debug(url);
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
            //路径处理
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
            String name = IDGenerator.generate("TH") + ".jpg";
            String completeName = builder.append(File.separator).append(name).toString();
            File temp = new File(completeName);
            //开始下载
            FileOutputStream fileOutputStream = new FileOutputStream(temp);
            byte[] data = new byte[1024];
            int len = 0;
            while ((len = is.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
            //返回路径
            int index = temp.getPath().indexOf(SystemTeller.tellPath(PATH + "/" + time));
            result = temp.getPath().substring(index);
            logger.debug(result);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        } finally {
            return result;
        }
    }
    
    public static final String long2short(String url){
    	JSONObject params = new JSONObject();
    	params.put("action", "long2short");
    	params.put("long_url", url);
    	String json = "";
    	try {
			json = HttpUtil.postJSON("https://api.weixin.qq.com/cgi-bin/shorturl?access_token=" + PlatformConfig.getAccessToken(), "UTF-8", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	JSONObject urlData = JSON.parseObject(json);
    	if(urlData.containsKey("errcode")){
    		int errcode = urlData.getInteger("errcode");
    		if(errcode == 0){
    			return urlData.getString("short_url");
    		}
    	}
    	return null;
    }

    public static final String inputStream2String(InputStream in) throws IOException {
        if (in == null) {
            return "";
        } else {
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];

            int n;
            while ((n = in.read(b)) != -1) {
                out.append(new String(b, 0, n, "UTF-8"));
            }

            return out.toString();
        }
    }
}
