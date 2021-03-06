package com.demo.test.service.impl;

import com.demo.test.dto.OrderDTO;
import com.demo.test.enums.ResultEnum;
import com.demo.test.service.BuyerService;
import com.demo.test.service.OrderService;
import com.demo.test.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@CacheConfig(cacheNames = "buyerCache")
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    OrderService orderService;

    @Override
    @Cacheable
    public OrderDTO findOrderOne(String openid, String orderId) {
        return checkOwner(openid, orderId);
    }

    @Override
    @Cacheable
    public OrderDTO cancelOrder(String openid, String orderId) {
        OrderDTO orderDTO = checkOwner(openid, orderId);
        if (orderDTO == null) {
            log.error("【取消订单】订单不存在, orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        return orderService.cancel(orderDTO);
    }

    @Cacheable
    private OrderDTO checkOwner(String openid, String orderId) {
        if (StringUtils.isEmpty(openid)) {
            log.error("【查询订单】openid为空");
            throw new SellException(ResultEnum.PARAMS_ERROR);
        }
        OrderDTO orderDTO = orderService.findByOrderId(orderId);
        if (orderDTO == null) {
            return null;
        }
        if (!orderDTO.getBuyerOpenid().equals(openid)) {
            log.error("【查询订单】订单openid不一致，openid={}, orderDTO={}", openid, orderDTO);
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDTO;
    }
}

