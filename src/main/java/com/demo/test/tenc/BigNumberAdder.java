package com.demo.test.tenc;

/**
 * 两大数相加,思路：
 *
 * 1.反转两个字符串，便于从低位到高位相加和最高位的进位导致和的位数增加；
 * 2.对齐两个字符串，即短字符串的高位用‘0’补齐，便于后面的相加；
 * 3.把两个正整数相加，一位一位的加并加上进位；
 * 4.最高位有进位则补上。
 *
 * @author Jack
 */

public class BigNumberAdder {

    static String s1 = "123456789012543";
    static String s2 = "123456789";

    public static void main(String[] args) {
        System.out.println(add(s1, s2));
    }

    private static String add(String s1,String s2) {
        //反转字符串
        String n1 = new StringBuffer(s1).reverse().toString();
        String n2 = new StringBuffer(s2).reverse().toString();
        int l1 = n1.length();
        int l2 = n2.length();
        int maxL = l1 > l2 ? l1 : l2;

        //补齐0
        if(l1 < l2) {
            for (int i = l1; i < maxL; i++) {
                n1 += "0";
            }
        } else {
            for (int i = l2; i < maxL; i++) {
                n2 += "0";
            }
        }
        System.out.println(n1);
        System.out.println(n2);
        StringBuffer res = new StringBuffer(); //存放的结果
        int c = 0; //进位
        for (int i = 0; i < maxL; i++) {
            int nSum = Integer.parseInt(n1.charAt(i) + "") + Integer.parseInt(n2.charAt(i) + "") + c;
            int ap = nSum % 10;
            res.append(ap);
            c = nSum / 10;
        }
        //最高位进位
        if(c > 0) {
            res.append(c);
        }
        //System.out.println(res);
        return res.reverse().toString();
    }
}
