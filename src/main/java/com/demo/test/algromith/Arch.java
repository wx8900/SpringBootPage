package com.demo.test.algromith;

/**
 * 打印如下数字三角形
 * n=5
 * 1
 * 2 12
 * 3 13 11
 * 4 14 15 10
 * 5  6  7   8   9
 * <p>
 * https://tool.lu/coderunner
 */
public class Arch {

    /*import sys
            n = 5
    num = -1
            for i in range(1,n+1,1):
    print(i)
    if i == n:
            for j in range(1,n,1):
    print("  ")
    print(n + j)
    else:
            for j in range(i - 1, 0, -1):
    print(" ")
    end = n * 2 - 1 + n - i
            if num == -1:
    num = end
            if j == 1:
    print(end)
            else:
    num = num + 1
    print(num)
    print()*/

    public static void main(final String[] args) {
        final int n = 5;
        int num = -1;
        for (int i = 1; i < n + 1; i++) {
            System.out.print(i);
            if (i == n) {
                for (int j = 1; j < n; j++) {
                    System.out.print("  ");
                    System.out.print(n + j);
                }
            } else {
                for (int j = i - 1; j > 0; j--) {
                    System.out.print(" ");
                    final int end = n * 2 - 1 + n - i;
                    if (num == -1) {
                        num = end;
                    }
                    if (j == 1) {
                        System.out.print(end);
                    } else {
                        num = num + 1;
                        System.out.print(num);
                    }
                }
            }
            /*换行dao*/
            System.out.println();
        }
    }
}
