package com.demo.test.jol;

import org.openjdk.jol.info.ClassLayout;

public class JolTest {
    public static void main(String[] args) {
        JolTest jolTest = new JolTest();
        System.out.println(ClassLayout.parseInstance(jolTest).toPrintable());

        synchronized (jolTest) {
            System.out.println(ClassLayout.parseInstance(jolTest).toPrintable());
        }
    }
}
