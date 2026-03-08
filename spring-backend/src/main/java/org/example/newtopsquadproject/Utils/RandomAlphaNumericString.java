package org.example.newtopsquadproject.Utils;

import java.util.Random;
public class RandomAlphaNumericString {

    private static final char[] CHAR_ARRAY = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
    public static String generateString(int len){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i < len; i++){
            stringBuilder.append(CHAR_ARRAY[new Random().nextInt(CHAR_ARRAY.length)]);
        }
        return stringBuilder.toString();
    }
}



