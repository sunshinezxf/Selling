package selling.sunshine.utils;

import java.util.Random;

/**
 * Created by sunshine on 5/31/16.
 */
public class PasswordGenerator {
    private static final Random seed = new Random();
    private static final char[] code = new String("1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz").toCharArray();

    private static int num(int min, int max) {
        return min + seed.nextInt(max - min);
    }

    public static char generateCode() {
        return code[num(0, code.length)];
    }

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
