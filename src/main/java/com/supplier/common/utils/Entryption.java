package com.supplier.common.utils;

import java.security.MessageDigest;

/**
 * @ClassName Entryption
 * @Description TODO
 * @Author aaters
 * @Date 2018/6/4 上午10:58
 * @Version 1.0
 **/
public class Entryption {

    public static void main(String[] args) {
        System.out.println(encode("123"));
    }

    public static String encode(String plaintext) {
        // 密文中间变量  MD5长度为32位
        int nch[] = new int[32];
        // 存储经过MD5加密后的密码（英文字符）
        String MD5Entryption = null;
        // 存放密文
        String cryp;
        // 存放密码的每个字符
        char pwdLength[] = new char[32];
        //将明文用MD5加密
        MD5Entryption = MD5Encode(plaintext);
        // 将密码付值给字符数组
        pwdLength = MD5Entryption.toCharArray();
        for (int i = 0; i < 32; i++) {
            // 将密码付值给字符数组
            nch[i] = transf(pwdLength[i]);
        }
        cryp = cryptograph(nch, 32);
        return cryp;
    }

    public static int transf(char c) { // 演算密文钥匙
        if (c >= '0' && c <= '9') {
            return c + 1;
        }
        else if (c >= 'a' && c <= 'z') {
            return c + 2;
        }
        else if (c >= 'A' && c <= 'Z') {
            return c + 3;
        }
        else
            return c;
    }
    public static String cryptograph(int[] n, int i) { // 演算密文
        int j; // 用于循环
        int numch = 0; // 数组ch[]中圆熟的实际最大元素个数
        int modnumber = 0;
        char[] ch = new char[i * 3];
        String s = "";
        for (j = 0; j < i; j++) {
            for (int k = 1; k < i; k++) {
                modnumber += n[(i - (j - k)) % i];
            }
            int rightn = 0;
            if (modnumber % 2 == 0) {
                rightn = modnumber + (n[j] >>> 4);
                ch[numch++] = (char) (rightn % 25 + 97);
                rightn = rightn / 10;
                ch[numch++] = (char) (rightn % 25 + 97);
                ch[numch++] = (char) (modnumber % 25 + 97);
            }
            else {
                rightn = modnumber + (n[j] >>> 4);
                ch[numch++] = (char) (rightn % 9 + 48);
                rightn = rightn / 10;
                ch[numch++] = (char) (rightn % 9 + 48);
                ch[numch++] = (char) (modnumber % 9 + 48);
            }
        }
        for (j = 0; j < numch; j++) {
            s = s + ch[j];
        }
        return s;
    }

    /**
     * 将明文作MD5加密处理，加密后长度为32位
     * @param string
     * @return
     */
    public static String MD5Encode(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
