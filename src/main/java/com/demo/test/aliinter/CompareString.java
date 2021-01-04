package com.demo.test.aliinter;

/**
 * 比较一个源字符串和一个目标字符串，
 * 如果在源字符串中包含目标字符串全部字符，输出所包含的第一个最小子串；
 * 如果不存在，输出空。
 * source = “BPDAUNZHGAHSIWBADNC”，target = “BDN” 满足要求的解 “BADN”
 *
 * 要求：时间复杂度为O(n^2)
 * @author Jack
 */

public class CompareString {

    public static void main(final String[] args) {
        final String source = "BPDAUNZHGAHSIWBADNC";
        final String target = "BDN";
        System.out.println(findSubStr(source, target));
    }

    public static String findSubStr(final String source, final String target) {
        String result = "";
        if (source == null || target == null) {
            return result;
        }
        final char[] sourceArray = source.toCharArray();
        final char[] targetArray = target.toCharArray();
        final int sourceArrayLen = source.length();
        final int targetArrayLen = target.length();
        int firstIndex = 0;
        int matchTargetCharCount = 0;
        int minLength = sourceArrayLen;
        boolean resetFirstIndex = true;

        //while (i < sourceArryLen && j < targetArryLen) {
        for (int j = 0; j < targetArrayLen; ) {
            for (int i = 0; i < sourceArrayLen; ) {
                if (sourceArray[i] == targetArray[j]) {
                    i++;
                    j++;
                    matchTargetCharCount++;
                    if (resetFirstIndex) {
                        firstIndex = i;
                        resetFirstIndex = false;
                    }

                } else {
                    i++;
                }

                if (matchTargetCharCount == targetArrayLen) {
                    if (minLength > (i - firstIndex + 1)) {
                        minLength = i - firstIndex + 1;
                        result = source.substring(firstIndex - 1, i);
                    }
                    resetFirstIndex = true;
                    matchTargetCharCount = 0;
                    j = 0;
                }
                if (i == sourceArrayLen) {
                    return result;
                }
            }
        }
        return "";
    }
}
