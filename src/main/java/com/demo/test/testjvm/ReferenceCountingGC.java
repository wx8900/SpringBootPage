package com.demo.test.testjvm;

/**
 * @author Jack
 *
 * 在IDEA Run ... VM options里添加 -XX:+PrintGCDetails
 *
 * [GC (System.gc()) [PSYoungGen: 10649K->5297K(76288K)] 10649K->5305K(251392K), 0.0026081 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
 * [Full GC (System.gc()) [PSYoungGen: 5297K->0K(76288K)] [ParOldGen: 8K->5100K(175104K)] 5305K->5100K(251392K), [Metaspace: 3284K->3284K(1056768K)], 0.0054546 secs] [Times: user=0.02 sys=0.01, real=0.01 secs]
 * Heap
 *  PSYoungGen      total 76288K, used 1966K [0x000000076ab00000, 0x0000000770000000, 0x00000007c0000000)
 *   eden space 65536K, 3% used [0x000000076ab00000,0x000000076aceb9e0,0x000000076eb00000)
 *   from space 10752K, 0% used [0x000000076eb00000,0x000000076eb00000,0x000000076f580000)
 *   to   space 10752K, 0% used [0x000000076f580000,0x000000076f580000,0x0000000770000000)
 *  ParOldGen       total 175104K, used 5100K [0x00000006c0000000, 0x00000006cab00000, 0x000000076ab00000)
 *   object space 175104K, 2% used [0x00000006c0000000,0x00000006c04fb090,0x00000006cab00000)
 *  Metaspace       used 3302K, capacity 4496K, committed 4864K, reserved 1056768K
 *   class space    used 359K, capacity 388K, committed 512K, reserved 1048576K
 */
public class ReferenceCountingGC {

    public Object instance = null;

    private static final int _1MB = 1024 * 1024;

    private byte[] bigSize = new byte[2 * _1MB];

    public static void main(String[] args) {
        ReferenceCountingGC objA = new ReferenceCountingGC();
        ReferenceCountingGC objB = new ReferenceCountingGC();
        objA.instance = objB;
        objB.instance = objA;

        System.gc();//System.gc() 只是建议gc，不一定执行
    }
}
