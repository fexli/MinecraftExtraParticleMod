package com.fexli.particle.future;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FExpressionCopyVer {
    // https://blog.csdn.net/XXX_xx_xc/article/details/73109199
    /**
     * 计算表达式
     * 支持 +、-、*、/、%、^、!、||、sin、cos、tan、cot、lg、ln等
     * 支持值π、e
     *
     * @param ariStr
     * @return
     * @throws Exception
     */
    public static double arith(String ariStr) throws Exception {
        int lBracket = ariStr.indexOf('(');
        if (lBracket < 0) {
            return arithContainAbs(ariStr);
        }
        int rBracket = matchedRBracket(ariStr);
        String start = ariStr.substring(0, lBracket);
        String middle = ariStr.substring(lBracket + 1, rBracket);
        String end = ariStr.substring(rBracket + 1);
        return arith(start + arith(middle) + end);
    }

    /**
     * 包含||
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static double arithContainAbs(String str) throws Exception {
        int lAbs = str.indexOf("|");
        if (lAbs < 0) {
            return simpleArith(str);
        }
        int rAbs = matchedRAbs(str);
        String start = str.substring(0, lAbs);
        String middle = str.substring(lAbs + 1, rAbs);
        String end = str.substring(rAbs + 1);
        return arithContainAbs(start + Math.abs(arithContainAbs(middle)) + end);
    }

    /**
     * 不包含()和||的运算
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static double simpleArith(String str) throws Exception {
        return arithLevel1(str);
    }

    /**
     * +、-
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static double arithLevel1(String str) throws Exception {
        String[][] sl = arithSplit(str, "\\+", "\\-");
        String[] ns = sl[0];
        String[] os = sl[1];
        double rs = arithLevel2(ns[0]);
        for (int i = 1; i < ns.length; i++) {
            if ("+".equals(os[i - 1])) {
                rs += arithLevel2(ns[i]);
            } else if ("-".equals(os[i - 1])) {
                rs -= arithLevel2(ns[i]);
            }
        }
        return rs;
    }

    /**
     * /、*、%
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static double arithLevel2(String str) throws Exception {
        String[][] sl = arithSplit(str, "\\*", "/", "%");
        String[] ns = sl[0];
        String[] os = sl[1];
        double rs = arithLevel3(ns[0]);
        for (int i = 1; i < ns.length; i++) {
            if ("*".equals(os[i - 1])) {
                rs *= arithLevel3(ns[i]);
            } else if ("/".equals(os[i - 1])) {
                rs /= arithLevel3(ns[i]);
            } else if ("%".equals(os[i - 1])) {
                rs %= arithLevel3(ns[i]);
            }
        }
        return rs;
    }

    /**
     * ^
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static double arithLevel3(String str) throws Exception {
        String[][] sl = arithSplit(str, "\\^");
        String[] ns = sl[0];
        String[] os = sl[1];
        double rs = arithLevel4(ns[0]);
        for (int i = 1; i < ns.length; i++) {
            if ("^".equals(os[i - 1])) {
                rs = Math.pow(rs, arithLevel4(ns[i]));
            }
        }
        return rs;
    }

    /**
     * sin、cos、tan、cot、!、lg、ln
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static double arithLevel4(String str) throws Exception {
        str = str.replaceAll("e", String.valueOf(Math.E)).replaceAll("π", String.valueOf(Math.PI));
        String doubleReg = "[-+]?([0-9]+(\\.[0-9]+)?|π|e)";
        double rs = 0;
        if (str.matches("^\\|" + doubleReg + "\\|$")) {//|-2.3|
            rs = Math.abs(Double.parseDouble(str.substring(1, str.length() - 1)));
        } else if (str.matches("^" + doubleReg + "!$")) {
            if (!str.matches("^[-+]?[0-9]+(\\.0+)?!$")) {
                throw new Exception("int is needed : " + str);
            }
            int l = Integer.parseInt(str.replaceFirst("(\\.0+)?!", ""));
            rs = factorial(l);
        } else if (str.matches("^sin" + doubleReg + "°?$")) {
            rs = Math.sin(getRadians(str.substring(3)));
        } else if (str.matches("^cos" + doubleReg + "°?$")) {
            rs = Math.cos(getRadians(str.substring(3)));
        } else if (str.matches("^tan" + doubleReg + "°?$")) {
            rs = Math.tan(getRadians(str.substring(3)));
        } else if (str.matches("^cot" + doubleReg + "°?$")) {
            rs = 1d / Math.tan(getRadians(str.substring(3)));
        } else if (str.matches("^lg" + doubleReg + "$")) {
            rs = Math.log10(Double.parseDouble(str.substring(2)));
        } else if (str.matches("^ln" + doubleReg + "$")) {
            rs = Math.log(Double.parseDouble(str.substring(2)));
        } else {
            rs = Double.parseDouble(str);
        }
        return rs;
    }

    /**
     * 找到第一个'('对应的')'位置
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static int matchedRBracket(String str) throws Exception {
        int lBracket = str.indexOf('(');
        if (lBracket < 0) {
            return lBracket;
        }
        Stack<Character> stack = new Stack<Character>();
        for (int i = lBracket; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                stack.pop();
            }
            if (stack.isEmpty()) {
                return i;
            }
        }
        throw new Exception("parse error! \"(\" and \")\" not matches");
    }

    /**
     * 找到第一个'|'对应的'|'位置
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static int matchedRAbs(String str) throws Exception {
        if (!str.contains("|")) {
            return -1;
        }
        Stack<String> stack = new Stack<String>();
        Pattern p = Pattern.compile("(?<=(^|[-+*/^])\\|{0,999999999})(\\|)|(\\|)(?=\\|{0,999999999}([-+*/^]|$))");
        Matcher m = p.matcher(str);
        while (m.find()) {
            String group2 = m.group(2);
            String group3 = m.group(3);
            if ("|".equals(group2) && group3 == null) {
                stack.push(group2);
            } else {
                stack.pop();
            }
            if (stack.isEmpty()) {
                return m.start();
            }
        }
        throw new Exception("parse error! \"|\" and \"|\" not matches");
    }

    /**
     * 解析表达式
     *
     * @param str
     * @param opers
     * @return
     */
    public static String[][] arithSplit(String str, String... opers) {
        String prefix = "(?<=([0-9]|π|e|°)[!|]?)";
        String suffix = "(?=(sin|cos|tan|cot|lg|ln|\\|)?[-+]?([0-9]|π|e|°))";
        StringBuilder operStr = new StringBuilder("(");
        for (int i = 0; i < opers.length; i++) {
            operStr.append(i == 0 ? "" : "|").append(opers[i]).append(i == opers.length - 1 ? ")" : "");
        }
        String regex = prefix + operStr.toString() + suffix;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        int start = 0;
        int end = 0;
        StringBuilder nsStr = new StringBuilder();
        StringBuilder osStr = new StringBuilder();
        while (m.find()) {
            end = m.start();
            nsStr.append(str.substring(start, end)).append(",");
            osStr.append(m.group()).append(",");
            start = m.end();
        }
        nsStr.append(str.substring(start)).append(",");
        String[] ns = nsStr.toString().split(",");
        String[] os = osStr.toString().split(",");
        String[][] rs = new String[2][];
        rs[0] = ns;
        rs[1] = os;
        return rs;
    }

    /**
     * 角度转弧度
     *
     * @param str
     * @return
     */
    public static double getRadians(String str) {
        if (str.endsWith("°")) {
            return Math.toRadians(Double.parseDouble(str.substring(0, str.length() - 1)));
        }
        return Double.parseDouble(str);
    }

    /**
     * n!
     *
     * @param l
     * @return
     */
    public static long factorial(int l) {
        if (l == 1 || l == 0) {
            return 1;
        } else if (l > 1) {
            return l * factorial(l - 1);
        } else {
            return -1 * factorial(Math.abs(l));
        }
    }
}
