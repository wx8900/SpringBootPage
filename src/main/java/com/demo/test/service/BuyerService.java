package com.demo.test.service;

import com.demo.test.dto.OrderDTO;

/**
 * @author Jack
 * @date 2019/06/27
 */
public interface BuyerService {

    OrderDTO findOrderOne(String openid, String orderId);

    OrderDTO cancelOrder(String openid, String orderId);
}
