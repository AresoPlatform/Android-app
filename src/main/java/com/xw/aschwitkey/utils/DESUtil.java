package com.xw.idld.aschwitkey.utils;

import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DESUtil {
    private static char[] A_z = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private static String key = "***";

    public static String encryptResult(Long timeStamp) {
        String str = timeStamp + "";
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            Character ch1 = str.charAt(i);
            int temp1 = Integer.parseInt(ch1.toString());
            if (i != str.length() - 1) {
                Character ch2 = str.charAt(i + 1);
                int temp2 = Integer.parseInt(ch2.toString());
                int a = temp1 ^ temp2;
                map.put(i, a);
            } else {
                map.put(i, temp1);
            }

        }
        for (int i = 13; i >= 1; i--) {
            map.put(13 - i, map.get(13 - i) * i);
        }
        for (int i = 0; i <= 11; i++) {
            int remainder = map.get(i) % 10;
            map.put(i + 1, map.get(i + 1) + remainder);
        }

        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer keyS = new StringBuffer(key);
        String temp = null;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Random r = new Random();
            int sub = r.nextInt(A_z.length);
            stringBuffer.append("-" + entry.getKey() + "" + A_z[sub] + entry.getValue() + "-");
            if (temp == null) {
                keyS.insert(1, stringBuffer.toString());
            } else {
                keyS.insert(keyS.indexOf(temp) + temp.length() + 1, stringBuffer.toString());
            }
            temp = stringBuffer.toString();
            stringBuffer = new StringBuffer();
        }
        String result = DESUtil.encrypt(keyS.toString().toUpperCase()+"-APP").replace("\n","");
        return result;
    }

    public static String encrypt(String data) {
        byte[] bt = encryptByKey(data.getBytes(), key);
        BASE64Encoder base64en = new BASE64Encoder();
        String strs = new String(base64en.encode(bt));
        return strs;
    }

    private static byte[] encryptByKey(byte[] datasource, String key) {
        try {
            SecureRandom random = new SecureRandom();

            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
