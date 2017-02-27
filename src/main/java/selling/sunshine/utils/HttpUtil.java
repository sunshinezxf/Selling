package selling.sunshine.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 该类为中通物流所使用
 *
 * @author sunshine
 */
public class HttpUtil {

    /**
     * 发送POST 请求
     *
     * @param {String} url     请求地址
     * @param {String} charset 编码格式
     * @param {Map}    params  请求参数
     * @return 响应消息
     * @throws IOException
     */
    public static String post(String url, String charset, Map params) throws IOException {
        HttpURLConnection conn;
        OutputStreamWriter out = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer result = new StringBuffer();
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", charset);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            out = new OutputStreamWriter(conn.getOutputStream(), charset);
            out.write(buildQuery(params, charset));
            out.flush();
            inputStream = conn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            String tempLine;
            while ((tempLine = reader.readLine()) != null) {
                result.append(tempLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return result.toString();
    }


    /**
     * post请求，以json格式返回
     *
     * @param {String} url 请求地址
     * @param {String} charset 字符集
     * @param {Map}    params 请求参数
     * @return 响应消息
     * @throws Exception
     */
    public static String postJSON(String url, String charset, JSONObject params) throws Exception {
        HttpURLConnection conn;
        OutputStreamWriter out = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer result = new StringBuffer();
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", charset);
            conn.setRequestProperty("Content-Type", "application/json");
            out = new OutputStreamWriter(conn.getOutputStream(), charset);
            out.write(params.toJSONString());
            out.flush();
            inputStream = conn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            String tempLine;
            while ((tempLine = reader.readLine()) != null) {
                result.append(tempLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return result.toString();
    }

    /**
     * 将map转换为请求字符串
     *
     * @param {Map}    params 请求的参数信息
     * @param {String} charset 字符集
     * @return 构建好的查询条件
     * @throws IOException
     */
    public static String buildQuery(Map<String, Object> params, String charset) throws IOException {
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuffer data = new StringBuffer();
        boolean flag = false;

        for (Entry<String, Object> entry : params.entrySet()) {
            if (flag) {
                data.append("&");
            } else {
                flag = true;
            }
            data.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), charset));
        }
        return data.toString();

    }
}