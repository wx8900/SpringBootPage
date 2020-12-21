package com.demo.test.tenc;

public class TestBigNumber {

    static int numberA = 1243462314;

    static int numberB = 1343545632;

    private static int add(int a, int b, boolean position) {
        int sum = 0;
        boolean addPosition = false;
        if (position) {
            if (a + b + 1 <= 9 && a + b + 1 >= 0) {
                sum = a + b + 1;
            } else {
                sum = a + b - 10;
                addPosition = true;
            }
        } else {
            if (a + b <= 9 && a + b >= 0) {
                sum = a + b;
            } else {
                sum = a + b - 10;
                addPosition = true;
            }
        }
        return sum;
    }

    // 123456
    // 1*10^5+2*10^4
    /*@getter
    @Setter
    @NoArgument
    class BigNumber{
        private long base;
        private int ten;
        private long square;

        public long getNumber(){
            return base * 10 ^ square;
        }
    }*/
    public static void main(String[] args) {
        int processLength = 100000;
        int pa = numberA % processLength;
        int pb = numberB % processLength;
        System.out.println(add(pa, pb, false));
    }
}
