package com.ravi.utils;

import java.util.Random;

public class OtpUtils {

    public static String generateOtpCode() {
        int optLength=6;

        Random random=new Random();
        StringBuilder sb=new StringBuilder(optLength);

        for(int i=0;i<optLength;i++){
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}
