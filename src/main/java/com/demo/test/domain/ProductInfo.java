package com.demo.test.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class ProductInfo implements java.io.Serializable {

    private static final long serialVersionUID = 8982619411406193222L;
    @Id
    private String productId;

    /** 商品名字 */
    private String productName;

    /** 商品价格 */
    private BigDecimal productPrice;

    /** 商品库存 */
    private Integer productStock;

    /** 商品描述 */
    private String productDescription;

    /** 商品状态 */
    private Integer productStatus;

    /** 商品图标 */
    private String productIcon;

    /** 商品类型 */
    private ProductCategory productCategory;

    public ProductInfo() {}

}
