package com.demo.test.base;

public class ValueTest {
    /**
        对于基本类型和引用类型 == 的作用效果是不同的，如下所示：
        基本类型：比较的是值是否相同；
        引用类型：比较的是引用是否相同；
     */
    public static void main(String[] args) {
        String x = "string";
        String y = "string";
        String z = new String("string");
        System.out.println(x==y); // true
        System.out.println(x==z); // false
        System.out.println(x.equals(y)); // true , equals 比较的一直是值，所以结果都为 true
        System.out.println(x.equals(z)); // true , equals 比较的一直是值，所以结果都为 true

        /**
         * equals 本质上就是 ==，只不过 String 和 Integer 等重写了 equals 方法，把它变成了值比较。
         *
         * 原来是 String 重写了 Object 的 equals 方法，把引用比较改成了值比较。
         *
         * 总结 ：== 对于基本类型来说是值比较，对于引用类型来说是比较的是引用；
         * 而 equals 默认情况下是引用比较，只是很多类重新了 equals 方法，比如 String、Integer
         * 等把它变成了值比较，所以一般情况下 equals 比较的是值是否相等。
         */

    }
}
