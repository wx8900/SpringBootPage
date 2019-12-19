package com.demo.test.domain;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class OrderForm implements java.io.Serializable {

    private static final long serialVersionUID = -5629630890901438213L;

    @NotEmpty(message = "姓名必填")
    private String name;

    @NotEmpty(message = "电话必填")
    private String phone;

    @NotEmpty(message = "地址必填")
    private String address;

    @NotEmpty(message = "openid必填")
    private String openid;

    @NotEmpty(message = "购物车不能为空")
    private String items;

    public OrderForm() {}
}

