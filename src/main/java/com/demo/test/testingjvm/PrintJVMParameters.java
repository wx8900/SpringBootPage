package com.demo.test.testingjvm;

/**
 *
 * oracle官方给出的调优文档
 *
 * -XX:MetaspaceSize=128m （元空间默认大小）
 * -XX:MaxMetaspaceSize=128m （元空间最大大小）
 * -Xms1024m （堆最大大小）
 * -Xmx1024m （堆默认大小）
 * -Xmn256m  （新生代大小）
 * -Xss256k  （栈最大深度大小）
 * -XX:SurvivorRatio=8 （新生代分区比例 8:2）
 * -XX:+UseConcMarkSweepGC （指定使用的垃圾收集器，这里使用CMS收集器）
 * -XX:+PrintGCDetails （打印详细的GC日志）
 *
 * 知识点：
 *
 * JDK8之后把-XX:PermSize 和 -XX:MaxPermGen移除了，取而代之的是
 * -XX:MetaspaceSize=128m    （元空间默认大小）
 * -XX:MaxMetaspaceSize=128m （元空间最大大小）
 *
 * JDK 8开始把类的元数据放到本地化的堆内存(native heap)中，这一块区域就叫Metaspace，中文名叫元空间。
 *
 * 使用本地化的内存有什么好处呢？最直接的表现就是java.lang.OutOfMemoryError: PermGen 空间问题将不复存在，
 * 因为默认的类的元数据分配只受本地内存大小的限制，也就是说本地内存剩余多少，理论上Metaspace就可以有多大
 * （貌似容量还与操作系统的虚拟内存有关？这里不太清楚），这解决了空间不足的问题。
 *
 * 不过，让Metaspace变得无限大显然是不现实的，因此我们也要限制Metaspace的大小：使用-XX:MaxMetaspaceSize
 * 参数来指定Metaspace区域的大小。JVM默认在运行时根据需要动态地设置MaxMetaspaceSize的大小。
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
