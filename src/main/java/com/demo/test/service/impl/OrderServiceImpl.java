package com.demo.test.service.impl;

import com.demo.test.dao.OrderDetailRepository;
import com.demo.test.dao.OrderMasterRepository;
import com.demo.test.domain.OrderDetail;
import com.demo.test.domain.OrderMaster;
import com.demo.test.domain.ProductInfo;
import com.demo.test.dto.CartDTO;
import com.demo.test.dto.OrderDTO;
import com.demo.test.dto.OrderMaster2OrderDTO;
import com.demo.test.enums.OrderStatusEnum;
import com.demo.test.enums.PayStatusEnum;
import com.demo.test.enums.ResultEnum;
import com.demo.test.service.OrderService;
import com.demo.test.service.ProductInfoService;
import com.demo.test.utils.KeyUtils;
import com.demo.test.utils.SellException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "orderCache")
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    ProductInfoService productInfoService;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    OrderMasterRepository orderMasterRepository;

    @Override
    @Cacheable
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtils.genUniqueKey();
        //商品总价
        BigDecimal totalPrice = new BigDecimal(BigInteger.ZERO);
        //1. 查询（库存，价格）
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            //获取商品信息
            ProductInfo productInfo = productInfoService.findById(orderDetail.getProductId());
            //商品不存在则抛出异常
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //2. 计算总价
            totalPrice = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(totalPrice);
            //3. 将详情存入数据库
            //注意顺序，拷贝之后null值也会被拷贝，所以在拷贝之后再赋值
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetail.setOrderId(orderId);
            orderDetail.setDetailId(KeyUtils.genUniqueKey());
            orderDetailRepository.save(orderDetail);
        }
        //4. 将订单主表写入数据库
        OrderMaster orderMaster = OrderMaster.builder().build();
        //注意顺序，拷贝之后null值也会被拷贝，所以在拷贝之后再赋值
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(totalPrice);
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMasterRepository.save(orderMaster);
        //5. 减库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
                CartDTO.builder().productId(e.getProductId()).productQuantity(e.getProductQuantity()).build()
        ).collect(Collectors.toList());
        productInfoService.decreaseStock(cartDTOList);

        return orderDTO;
    }

    @Override
    @Cacheable
    public OrderDTO findByOrderId(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findById(orderId).get();
        if (orderMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> detailList = orderDetailRepository.findByOrderId(orderId);
        if (detailList == null) {
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }
        OrderDTO dto = OrderDTO.builder().build();
        BeanUtils.copyProperties(orderMaster, dto);
        dto.setOrderDetailList(detailList);
        return dto;
    }

    @Override
    @Cacheable
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> page = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderMaster> orderMasterList = page.getContent();
        List<OrderDTO> orderDTOS = OrderMaster2OrderDTO.convertList(orderMasterList);
        return new PageImpl<>(orderDTOS, pageable, page.getTotalElements());
    }

    @Override
    @CachePut(key = "#order.id")
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        //判断订单状态。只有新下单可以取消
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单】订单状态不正确，orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //更改订单状态
        OrderMaster orderMaster = OrderMaster.builder().build();
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        //更新失败
        if (updateResult == null) {
            log.error("【取消订单】取消订单失败，orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //还库存
        //先看订单中有没有商品
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单】订单详情为空，orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> CartDTO.builder().productId(e.getProductId()).productQuantity(e.getProductQuantity()).build())
                .collect(Collectors.toList());
        productInfoService.increaseStock(cartDTOList);

        //如果已支付，退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            //TODO 退款
        }

        return orderDTO;
    }

    @Override
    @Cacheable
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【完结订单】订单状态不正确，orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //更改状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISH.getCode());
        OrderMaster orderMaster = OrderMaster.builder().build();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        //保存更新
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【完结订单】完结订单失败，orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    @Cacheable
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【支付订单】订单状态不正确，orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【支付订单】订单支付状态不正确，orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_PAID_STATUS_ERROR);
        }
        OrderMaster orderMaster = OrderMaster.builder().build();
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【支付订单】支付订单失败，orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //更改状态，提交
        return orderDTO;
    }
}

