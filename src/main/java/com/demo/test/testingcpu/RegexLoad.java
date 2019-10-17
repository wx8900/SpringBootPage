package com.demo.test.testingcpu;

/**
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 该类模拟高CPU占用的情况
 *
 * 用java thread dump来分析CPU高使用率以及线程死锁问题
 * 一般java thread dump用于web开发中分析web容器或是应用服务器的性能问题还是比较常用并有效的
 * 常用的入门级web容器Tomcat，以及高级别的jboss、websphere、weblogic等的性能调优问题都可以使用java thread dump来分析
 *
 * 首先，阐述一下thread dump常用来解决的是何种问题
 * （1）高CPU使用
 * （2）线程死锁
 *
 */
public class RegexLoad {
    public static void main(String[] args) {
        String[] patternMatch = {"([\\w\\s]+)+([+\\-/*])+([\\w\\s]+)",
                "([\\w\\s]+)+([+\\-/*])+([\\w\\s]+)+([+\\-/*])+([\\w\\s]+)"};
        List<String> patternList = new ArrayList<String>();

        patternList.add("Avg Volume Units product A + Volume Units product A");
        patternList.add("Avg Volume Units /  Volume Units product A");
        patternList.add("Avg retailer On Hand / Volume Units Plan / Store Count");
        patternList.add("Avg Hand Volume Units Plan Store Count");
        patternList.add("1 - Avg merchant Volume Units");
        patternList.add("Total retailer shipment Count");

        for (String s : patternList) {

            for (int i = 0; i < patternMatch.length; i++) {
                Pattern pattern = Pattern.compile(patternMatch[i]);

                Matcher matcher = pattern.matcher(s);
                System.out.println(s);
                if (matcher.matches()) {
                    System.out.println("Passed");
                } else {
                    System.out.println("Failed;");
                }
            }

        }
    }
}
