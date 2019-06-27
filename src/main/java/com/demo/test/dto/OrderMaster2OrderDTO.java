package com.demo.test.dto;

import com.demo.test.domain.OrderMaster;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 转换类 OrderMaster 转 OrderDTO
 */
public class OrderMaster2OrderDTO {

    public static OrderDTO convert(OrderMaster orderMaster) {
        OrderDTO dto = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, dto);
        return dto;
    }

    public static List<OrderDTO> convertList(List<OrderMaster> orderMasterList) {
        return orderMasterList.stream()
                .map(e -> convert(e))
                .collect(Collectors.toList());
    }
}
