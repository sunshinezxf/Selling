package selling.sunshine.utils;

import java.security.MessageDigest;


import sun.misc.BASE64Encoder;

/**
 * 生成摘要工具类
 */
public class DigestUtil {
    public static final String GBK="GBK";
    public static final String UTF8="UTF-8";

    /**
     * MD5
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();
    }

    /**
     * base64
     * @param md5
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] md5) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(md5);
    }

    /**
     * 摘要生成
     * @param data 请求数据
     * @param sign 签名秘钥(key或者parternID)
     * @param charset 编码格式
     * @return 摘要
     * @throws Exception
     */
    public static String digest(String data,String sign,String charset) throws Exception {
			String t = encryptBASE64(encryptMD5((data + sign).getBytes(charset)));
		// BASE64编码之后默认有一个回车换行符，这个符号在windows上 与 linux不同
		// 中通后台是按照 Windows 换行符校验的，如果调用者是linux 系统要做个转换
		if (System.getProperty("line.separator").equals("\n")) {
			String t2 = t.replaceAll("\\n", "\r\n");
			return t2;
		} else {
			return t;
		}
    }
}