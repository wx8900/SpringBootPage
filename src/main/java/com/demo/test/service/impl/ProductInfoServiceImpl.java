package com.demo.test.service.impl;

import com.demo.test.dao.ProductInfoRepository;
import com.demo.test.domain.ProductInfo;
import com.demo.test.dto.CartDTO;
import com.demo.test.enums.ProductStatusEnum;
import com.demo.test.enums.ResultEnum;
import com.demo.test.service.ProductInfoService;
import com.demo.test.exception.SellException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Jack
 * @date 2019/07/19
 */
@Service
@CacheConfig(cacheNames = "productInfoCache")
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    ProductInfoRepository repository;

    @Override
    @Cacheable
    public ProductInfo findById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    @Cacheable
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    @Cacheable
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Cacheable
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    @Cacheable
    public void increaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDto : cartDTOList) {
            ProductInfo info = findById(Long.valueOf(cartDto.getProductId()));
            //商品为空抛出异常
            if (info == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            int result = info.getProductStock() + cartDto.getProductQuantity();
            info.setProductStock(result);
            save(info);
        }
    }

    @Override
    @Cacheable
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            ProductInfo info = findById(Long.valueOf(cartDTO.getProductId()));
            //商品为空抛出异常
            if (info == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            int result = info.getProductStock() - cartDTO.getProductQuantity();
            //减完后库存小于0抛出库存不足异常
            if (result < 0) {
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            info.setProductStock(result);
            save(info);
        }
    }

    /*@Override
    public List<ProductInfo> findAllProductByCategoryId(Integer id) {
        return repository.findAllProductByCategoryId(id);
    }*/
}

