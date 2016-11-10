import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by sunshine on 2016/11/9.
 */
public class ServiceAccountMenu {

    //线上
    //private final static String APPID = "";
    //private final static String SECRET = "";

    //测试
    private final static String APPID = "wx23ff32e7d6b145a1";
    private final static String SECRET = "04b6902b3f1f880cc7a0f8d4d3e1b1a4";

    private final static String PLACE_ORDER_URL = "http://www.yuncaogangmu.com/agent/order/place";
    private final static String BIND_ACCOUNT_URL = "http://www.yuncaogangmu.com/agent/bind";
    private final static String REGISTER_URL = "http://www.yuncaogangmu.com/agent/register";
    private final static String CUSTOMER_PURCHASE_URL = "http://www.yuncaogangmu.com/commodity/COMyfxwez26";
    private final static String LETHE_URL = "http://www.yuncaogangmu.com/agent/lethe";
    private final static String TUOZHAN_URL = "http://www.yuncaogangmu.com/agent/invite";
    private final static String SHARE_URL = "http://www.yuncaogangmu.com/agent/personalsale";
    private final static String ACCOUNT_URL = "http://www.yuncaogangmu.com/account/info";
    private final static String LOGIN_URL = "http://www.yuncaogangmu.com/agent/login";

    /**
     * 调用此方法需传入appid和secret，现默认不使用该方法获取
     *
     * @param appId
     * @param secret
     * @return
     */
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

    public static String createMenu(String token) {

        JSONObject sanqi1 = new JSONObject();
        sanqi1.put("name", "参中之王");
        sanqi1.put("type", "view");
        try {
            sanqi1.put("url", "http://mp.weixin.qq.com/s?__biz=MzIwNjI1OTY2Mg==&mid=2650661098&idx=1&sn=74b3f4690b182c295ba8b8ffc1e65b38&scene=18#wechat_redirect");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject sanqi2 = new JSONObject();
        sanqi2.put("name", "三七功效");
        sanqi2.put("type", "view");
        try {
            sanqi2.put("url", "http://mp.weixin.qq.com/s?__biz=MzIwNjI1OTY2Mg==&mid=2650661094&idx=1&sn=7d3977108078a2a050360754766178a1&scene=18#wechat_redirect");
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
            sanqi4.put("url", "http://mp.weixin.qq.com/s?__biz=MzIwNjI1OTY2Mg==&mid=503177454&idx=1&sn=b0fd026ef2665df3cad873d085cd8a87&scene=18#wechat_redirect");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject ycgm = new JSONObject();
        ycgm.put("name", "制作工艺");
        ycgm.put("type", "view");
        try {
            ycgm.put("url", "http://mp.weixin.qq.com/s?__biz=MzIwNjI1OTY2Mg==&mid=2650661091&idx=1&sn=0179866c87f6161848b311a9398807c4#rd");
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

        JSONObject agent_g = new JSONObject();
        agent_g.put("name", "操作手册");
        agent_g.put("type", "view");
        try {
            agent_g.put("url", "http://mp.weixin.qq.com/s?__biz=MzIwNjI1OTY2Mg==&mid=503177455&idx=1&sn=ad4c39e7083a36e89009e4f72ef4f139#rd");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray sanqi = new JSONArray();
        sanqi.add(sanqi1);
        sanqi.add(sanqi2);
        sanqi.add(sanqi3);
        sanqi.add(sanqi4);

        JSONObject sanqiButton = new JSONObject();
        sanqiButton.put("name", "何为三七");
        sanqiButton.put("sub_button", sanqi);

        JSONArray yc = new JSONArray();
        yc.add(ycgm);
        yc.add(ycgm2);
        yc.add(ycgm3);
        yc.add(agent_g);

        JSONObject ycButton = new JSONObject();
        ycButton.put("name", "云草纲目");
        ycButton.put("sub_button", yc);

        JSONObject agent_login = new JSONObject();
        agent_login.put("name", "登录");
        agent_login.put("type", "view");
        try {
            agent_login.put("url", LOGIN_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject agent_register = new JSONObject();
        agent_register.put("name", "注册");
        agent_register.put("type", "view");
        try {
            agent_register.put("url", REGISTER_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject agent_tuozhan = new JSONObject();
        agent_tuozhan.put("name", "拓展");
        agent_tuozhan.put("type", "view");
        try {
            agent_tuozhan.put("url", TUOZHAN_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject agent_share = new JSONObject();
        agent_share.put("name", "分享");
        agent_share.put("type", "view");
        try {
            agent_share.put("url", SHARE_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject agent_account = new JSONObject();
        agent_account.put("name", "账户");
        agent_account.put("type", "view");
        try {
            agent_account.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx67fcdc4fa58f7578&redirect_uri=" + URLEncoder.encode(ACCOUNT_URL, "utf-8") + "&response_type=code&scope=snsapi_base&state=view#wechat_redirect");
        } catch (Exception e) {
            e.printStackTrace();
        }


        JSONObject agent_operation = new JSONObject();
        agent_operation.put("name", "订单");
        agent_operation.put("type", "view");
        try {
            agent_operation.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx67fcdc4fa58f7578&redirect_uri=&redirect_uri=" + URLEncoder.encode(PLACE_ORDER_URL, "utf-8") + "&response_type=code&scope=snsapi_base&state=view#wechat_redirect");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray agent = new JSONArray();
        agent.add(agent_login);
        agent.add(agent_operation);
        agent.add(agent_account);
        agent.add(agent_tuozhan);
        agent.add(agent_share);

        JSONObject agentButton = new JSONObject();
        agentButton.put("name", "代理商");
        agentButton.put("sub_button", agent);

        JSONArray buttons = new JSONArray();
        buttons.add(sanqiButton);
        buttons.add(ycButton);

        buttons.add(agentButton);
        JSONObject menu = new JSONObject();
        menu.put("button", buttons);
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

    public static String deleteMenu(String token) {
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

    public static void main(String[] args) {
        String deleteMessage = ServiceAccountMenu.deleteMenu("");
        System.out.println("删除操作: " + deleteMessage);
        String createMessage = ServiceAccountMenu.createMenu("");
        System.out.println("创建操作" + createMessage);
    }
}
