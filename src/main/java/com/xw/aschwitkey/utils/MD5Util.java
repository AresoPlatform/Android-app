package com.xw.aschwitkey.utils;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MD5Util {
    public static String digest(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                int c = b & 0xff; 
                String result = Integer.toHexString(c); 
                if(result.length()<2){
                    sb.append(0); 
                }
                sb.append(result);
            }
            return sb.toString(); 
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
