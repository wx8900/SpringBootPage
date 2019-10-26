package com.demo.test.monitor;

/**
 * @author Jack
 */
public class PrintJVMParameters {

    public static void main(String[] args) {
        printXmxXms1();
        printXmxXms2();
        printXmxXms3();
        printXmxXms4();
    }

    public static void printXmxXms1(){
        System.out.print("指定最大堆大小 最大可使用空间 Xmx=");
        System.out.println(Runtime.getRuntime().maxMemory()/1024/1024 + "M");
        System.out.print("当前未使用的空间 free mem=");
        System.out.println(Runtime.getRuntime().freeMemory()/1024/1024 + "M");
        System.out.print("当前可用总mem空间 total mem=");
        System.out.println(Runtime.getRuntime().totalMemory()/1024/1024 + "M");
    }

    public static void printXmxXms2(){
        byte[] b = new byte[1024*1024];
        System.out.println("分配了1M空间给数组");
        System.out.print("Xmx=");
        System.out.println(Runtime.getRuntime().maxMemory()/1024/1024 + "M");
        System.out.print("free mem=");
        System.out.println(Runtime.getRuntime().freeMemory()/1024/1024 + "M");
        System.out.print("total mem=");
        System.out.println(Runtime.getRuntime().totalMemory()/1024/1024 + "M");
    }

    public static void printXmxXms3(){
        byte[] b = new byte[4*1024*1024];
        System.out.println("分配了4M空间给数组");
        System.out.print("Xmx=");
        System.out.println(Runtime.getRuntime().maxMemory()/1024/1024 + "M");
        System.out.print("free mem=");
        System.out.println(Runtime.getRuntime().freeMemory()/1024/1024 + "M");
        System.out.print("total mem=");
        System.out.println(Runtime.getRuntime().totalMemory()/1024/1024 + "M");
    }

    public static void printXmxXms4(){
        byte[] b = new byte[4*1024*1024];
        System.out.println("分配了4M空间给数组");
        System.gc();
        System.out.println("回收内存");
        System.out.print("Xmx=");
        System.out.println(Runtime.getRuntime().maxMemory()/1024/1024 + "M");
        System.out.print("free mem=");
        System.out.println(Runtime.getRuntime().freeMemory()/1024/1024 + "M");
        System.out.print("total mem=");
        System.out.println(Runtime.getRuntime().totalMemory()/1024/1024 + "M");
    }

}
