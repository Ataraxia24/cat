package cn.common;

import java.util.Random;

public class RandomCode {
    /**
     * 获取随机验证码
     * @param length 验证码的长度
     * @return
     */
    public static String getRandomString(int length) {
        String randomStr = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(randomStr.length());
            strBuf.append(randomStr.charAt(number));
        }
        return strBuf.toString();
    }
}
