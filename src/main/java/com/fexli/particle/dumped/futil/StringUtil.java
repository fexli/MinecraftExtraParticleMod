package com.fexli.particle.dumped.futil;

public final class StringUtil {
    public static int getCharInString(String str,String checkchar){
        int num = 0;
        char checker = checkchar.toCharArray()[0];
        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) == checker) num++;
        }
        return num;
    }
    public static String removeOuterBracket(String key) {
        int begin = 0;
        int end = key.length() - 1;
//        boolean flag = true;
        while (key.charAt(begin) == '(' && key.charAt(end) == ')') {
            int bracket = 0;
            int bracketEnd = begin;
            for (int i = begin; i <= end; i++) {
                char ch = key.charAt(i);
                if (ch == '(') {
                    bracket++;
                } else if (ch == ')') {
                    bracket--;
                }
                if (bracket == 0) {
                    bracketEnd = i;
                    break;
                }
            }
            if (bracketEnd == end) {
                begin++;
                end--;
            } else {
                break;
            }
        }
        return key.substring(begin, end + 1);
    }
}
