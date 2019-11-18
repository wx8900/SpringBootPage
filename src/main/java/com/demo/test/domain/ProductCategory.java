package com.demo.test.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 商品类别
 *
 * @author Jack
 */
@Entity
@DynamicUpdate
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class ProductCategory implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    /**
     * 商品种类名称
     */
    private String categoryName;

    /**
     * 商品类目编号
     */
    private Integer categoryType;

    public ProductCategory() {}

    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }
}
