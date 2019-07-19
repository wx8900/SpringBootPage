package com.demo.test.dto;

import com.demo.test.domain.OrderMaster;
import lombok.Builder;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 转换类 OrderMaster 转 OrderDTO
 */
@Builder
public class OrderMaster2OrderDTO {

    public static OrderDTO convert(OrderMaster orderMaster) {
        OrderDTO dto = OrderDTO.builder().build();
        BeanUtils.copyProperties(orderMaster, dto);
        return dto;
    }

    public static List<OrderDTO> convertList(List<OrderMaster> orderMasterList) {
        return orderMasterList.stream()
                .map(e -> convert(e))
                .collect(Collectors.toList());
    }
}
