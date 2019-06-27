package com.demo.test.service;

import com.demo.test.domain.ProductInfo;
import com.demo.test.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Jack
 */
public interface ProductInfoService {

    /**
     * 通过id查询单个商品
     *
     * @param id
     * @return
     */
    ProductInfo findById(String id);

    /**
     * 查询上架的产品
     *
     * @return
     */
    List<ProductInfo> findUpAll();

    /**
     * 查询所有商品 带分页
     *
     * @param pageable
     * @return
     */
    Page<ProductInfo> findAll(Pageable pageable);

    /**
     * 保存一个商品
     *
     * @param productInfo
     * @return
     */
    ProductInfo save(ProductInfo productInfo);

    /**
     * 加库存
     *
     * @param cartDTOList
     */
    void increaseStock(List<CartDTO> cartDTOList);

    /**
     * 减库存
     *
     * @param cartDTOList
     */
    void decreaseStock(List<CartDTO> cartDTOList);


}
