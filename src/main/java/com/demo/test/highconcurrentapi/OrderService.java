package com.demo.test.highconcurrentapi;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.*;

/**
 * 提供一个批量接口
 * @author Jack
 */
public class OrderService {

    private RemoteService remoteService;

    private Queue<Request> queue = new LinkedBlockingQueue<>();

    public Map<String,Object> queryOrderInfo(String orderCode)
            throws ExecutionException, InterruptedException {
        //每个请求唯一
        String serialNo = UUID.randomUUID().toString();
        Request request = new Request();
        request.setSerialNo(serialNo);
        request.setOrderCode(orderCode);
        CompletableFuture<Map<String,Object>> future = new CompletableFuture<>();
        request.setFuture(future);
        //将请求添加到队列
        queue.add(request);
        //阻塞返回，直到接口complete才返回
        return future.get();
    }

    @PostConstruct
    public void init(){
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
        scheduled.scheduleAtFixedRate(()->{
            int size = queue.size();
            if(size == 0){
                return;
            }
            List<Request> requests = new ArrayList<>();
            for(int i = 0;i< size ;i++){
                requests.add(queue.poll());
            }
            //组装批量接口参数
            List<Map<String,String>> params = new ArrayList<>();
            for(Request request : requests){
                Map<String,String> map = new HashMap<>();
                map.put("orderCode",request.getOrderCode());
                map.put("serialNo",request.getSerialNo());
                params.add(map);
            }
            List<Map<String,Object>> responses = remoteService.queryOrderInfoByCodeBatch(params);
            HashMap<String,Map<String,Object>> responseMap = new HashMap<>();
            for(Map<String,Object> response:responses){
                String serialNo = response.get("serialNo").toString();
                responseMap.put(serialNo,response);
            }
            for(Request request : requests){
                Map<String,Object> result = responseMap.get(request.getSerialNo());
                request.getFuture().complete(result);
            }
        },0,10, TimeUnit.MILLISECONDS);
    }
}
