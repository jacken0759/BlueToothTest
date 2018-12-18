package com.example.demo.blueTooth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by Admin on 2017/12/5.
 * 字符串处理方法，静态调用
 */
public class StringUtil {

    /**
     * 判断字符串非空
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串非空
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmptyList(Collection str) {
        if (str == null) {
            return true;
        }
        if (str.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 生成给定长度的字符串
     *
     * @param length
     * @return
     */
    public static String randomString(int length) {
        StringBuffer sBuffer = new StringBuffer();
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxzy0123456789";
        for (int i = 0; i < length; i++) {
            int j = new Random().nextInt(62);
            sBuffer.append(str.charAt(j));
        }
        return sBuffer.toString();
    }

    /**
     * MD5加密
     *
     * @param sourceStr
     * @return
     */
    public static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5" );
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("" );
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0" );
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            //  System.out.println("MD5(" + sourceStr + ",32) = " + result);
            //  System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }


    //随机产生验证码
    public static String randomText() {

        String base = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz0123456789";
        Random random = new Random();
        // StringBuffer sb1 = new StringBuffer();

        StringBuilder sb2 = new StringBuilder();
        for (int k = 0; k < 16; k++) {
            int number = random.nextInt(base.length());

            //   mCheckNum[k] = String.valueOf(base.charAt(number));

            // sb1.append(base.charAt(number));
            sb2.append(base.charAt(number));
        }
        return sb2.toString();

    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }
    /**
     * 判断字符串是否为纯数字
     * @param string
     * @return
     */
    public static boolean isNumberic(String string) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(string).matches();
    }

    /**
     * 判断字符串是否存在三个连续相同的字符
     * @param string
     * @return
     */
    public static boolean hasLinuSameCharacters(String string) {
        int len = string.length();
        boolean has = false;
        for(int i = 0; i < len; i++) {
            if (len - i < 3) {
                has = false;
                break;
            }
            if (string.charAt(i) == string.charAt(i+1) && string.charAt(i) == string.charAt(i + 2)) {
                has = true;
                break;
            }
        }
        return has;
    }

    private static final String[] ERRORS = new String[] { "error", "io", "cte",
            "ste", "ce" };
    /**
     * 检测字符串是否存在于数组中
     * @param string
     * @return
     */
    public static boolean isContained(String string) {
        boolean isContained = false;
        for (String str : ERRORS) {
            if (str.equals(string)) {
                isContained = true;
                break;
            }
        }
        return isContained;
    }

}
