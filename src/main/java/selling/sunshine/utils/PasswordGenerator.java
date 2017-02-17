package selling.sunshine.utils;

import java.util.Random;

/**
 * 密码生成器
 * Created by sunshine on 5/31/16.
 * @author sunshine
 */
public class PasswordGenerator {
    private static final Random seed = new Random();//随机数生成器
    private static final char[] code = new String("1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz").toCharArray();//阿拉伯数字和英文字母的字符数组集

    private static int num(int min, int max) {
        return min + seed.nextInt(max - min);
    }

    public static char generateCode() {
        return code[num(0, code.length)];
    }


    /**
     * 生成6位的随机密码
     * @return
     */
    public static String generate() {
        char[] temp = new char[6];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = generateCode();
        }
        StringBuffer result = new StringBuffer();
        Random random = new Random();
        result.append(new String(temp));
        result.append(random.nextInt(99));
        return result.toString();
    }
}
