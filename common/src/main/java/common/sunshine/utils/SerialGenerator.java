package common.sunshine.utils;

import java.util.Random;

/**
 * Created by sunshine on 2017/1/26.
 */
public class SerialGenerator {
    private static final Random seed = new Random();
    private static final char[] code = "1A2B3C4D5E6F7G8H9I0J1K2L3M4N5O6P7Q8R9S0T1U2V3W4X5Y6Z".toCharArray();

    private static int num(int min, int max) {
        return min + seed.nextInt(max - min);
    }

    public static char generatec() {
        return code[num(0, code.length)];
    }

    public static String generate() {
        char[] temp = new char[8];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = generatec();
        }
        StringBuffer result = new StringBuffer();
        result.append(new String(temp));
        return result.toString();
    }
}
