package com.demo.test.tenc;

//233964219654321
//123456912469332
public class BigNumberAdder2 {
    static String s1 = "123456789012543";
    static String s2 = "123456789";
    public static void main(String[] args) {
        System.out.println(add(s1, s2));
    }

    private static String add(String s1, String s2) {
        StringBuffer res = new StringBuffer();
        s1 = new StringBuffer(s1).reverse().toString();
        s2 = new StringBuffer(s2).reverse().toString();
        int l1 = s1.length();
        int l2 = s2.length();
        int maxLen = l1 > l2 ? l1 : l2;
        if (l2 > l1) {
            for (int i = l1; i < maxLen; i++) {
                s1 += "0";
            }
        } else if (l1 > l2) {
            for (int i = l2; i < maxLen; i++) {
                s2 += "0";
            }
        }
        System.out.println(s1);
        System.out.println(s2);
        int c = 0;
        for (int i = 0; i < maxLen; i++) {
            int sum = Integer.parseInt(s1.charAt(i) + "") + Integer.parseInt(s2.charAt(i) + "") + c;
            int ap = sum % 10;
            res.append(ap);
            c = sum / 10;
        }
        //最后一位进位
        if (c > 0) {
            res.append(c);
        }
        return res.reverse().toString();
    }
}
