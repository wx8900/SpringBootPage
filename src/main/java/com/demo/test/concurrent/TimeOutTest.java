package com.demo.test.concurrent;

import java.util.concurrent.*;

public class TimeOutTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final ExecutorService exec = Executors.newFixedThreadPool(1);
        Callable<String> call = new Callable<String>() {
            @Override
            public String call() throws Exception {
                //开始执行耗时操作
                Thread.sleep(1000 * 3);
                return "执行完成!";
            }
        };
        try {
            Future<String> future = exec.submit(call);
            //String obj = future.get(5, TimeUnit.SECONDS); //任务处理超时时间设为 1 秒
            String obj = future.get(2, TimeUnit.SECONDS); //任务处理超时时间设为 1 秒
            System.out.println("任务成功返回:" + obj);
            System.out.println(obj+"2342342342423");
        } catch (TimeoutException ex) {
            System.out.println("处理超时啦....");
            ex.printStackTrace();
        } catch (Exception e) {
            System.out.println("处理失败.");
            e.printStackTrace();
        }
        // 关闭线程池
        exec.shutdown();
    }

}
