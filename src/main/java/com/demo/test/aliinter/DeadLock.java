package com.demo.test.aliinter;

import java.util.concurrent.TimeUnit;

public class DeadLock {
    public static void main(String[] args) {
        MarkUp markUp0 = new MarkUp("user1",0);
        MarkUp markUp1 = new MarkUp("user2",1);
        markUp0.start();
        markUp1.start();
    }
}

class LipStick {
}

class Mirror {
}

class MarkUp extends Thread {
    private int choice;
    private String userName;

    private static LipStick lipStick = new LipStick();
    private static Mirror mirror = new Mirror();

    MarkUp(String userName, int choice) {
        this.userName = userName;
        this.choice = choice;
    }

    @Override
    public void run() {
        try {
            markUP();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void markUP() throws InterruptedException {
        if (choice == 0) {
            synchronized (lipStick) {
                System.out.println(userName + "拿到了A");
                TimeUnit.SECONDS.sleep(1);
                synchronized (mirror) {
                    System.out.println(userName + "拿到了B");
                }
            }
        }
        if (choice == 1) {
            synchronized (mirror) {
                System.out.println(userName + "拿到了A");
                TimeUnit.SECONDS.sleep(1);
                synchronized (lipStick) {
                    System.out.println(userName + "拿到了B");
                }
            }
        }
    }
}
