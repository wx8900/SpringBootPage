package com.demo.test.jol;

import org.openjdk.jol.info.ClassLayout;

public class JolTest {
    public static void main(String[] args) {
        JolTest jolTest = new JolTest();
        System.out.println(ClassLayout.parseInstance(jolTest).toPrintable());

        synchronized (jolTest) {
            System.out.println(ClassLayout.parseInstance(jolTest).toPrintable());
        }

        int size = 10;
        List<Integer> list = new ArrayList<Integer>(size);
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        //虚拟机信息
        out.println(VMSupport.vmDetails());
        //打印类内部的占用
        out.println(ClassLayout.parseClass(ArrayList.class).toPrintable());
        //打印实例内部的占用
        out.println(ClassLayout.parseClass(ArrayList.class).toPrintable(list));
        //打印实例外部的占用
        out.println(GraphLayout.parseInstance(list).toPrintable());
        //打印实例各个依赖的占用,并汇总
        out.println(GraphLayout.parseInstance(list).toFootprint())
    }
}
