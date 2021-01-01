package com.demo.test.algromith;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MyTest1 {
    public static void main(String[] args) {
        System.out.println(compare(3L));

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Iterator<Integer> iterator = list.iterator();
        if (iterator.hasNext()) {
            int i = iterator.next();
            if (i == 3) {
                iterator.remove();
            }
        }
    }

    private static boolean compare(Long num) {
        int i = 3;
        return i == num;
    }
}
