package com.demo.test.algromith;

/**
 * 1. 酷跑运动员爬梯子，每向上梯爬一节梯子需要6秒钟，而向下爬一层需要4秒钟，且每爬完1节，停留5秒钟。
 * 假设运动员初始在地面，爬梯顺序如下：
 * 输入：[1, 2]，输出：17 6+5+6
 * 输入：[1,3,2,3,1]，输出：66 6+5+6+5+6+5+4+5+6+5+4+5+4=33+20+13=53+13=66
 */
public class RunnerTest {

    public static void main(String[] args) {
        int[] arr1 = new int[]{1,2};
        System.out.println(getTime(arr1));
        int[] arr2 = new int[]{1,3,2,3,1};
        System.out.println(getTime(arr2));
    }

    private static int getTime(int[] arr) {
        if (arr == null) {
            return 0;
        }
        int time = 0;
        int base = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] - base > 0) {
                time += (arr[i] - base) * (6 + 5);
            } else if (arr[i] - base < 0) {
                time += Math.abs(arr[i] - base) * (4 + 5);
            }
            base = arr[i];
            if (i == arr.length - 1) {
                time = time - 5;
            }
        }
        return time;
    }

}
