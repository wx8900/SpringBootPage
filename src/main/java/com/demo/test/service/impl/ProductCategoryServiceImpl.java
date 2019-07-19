package com.demo.test.service.impl;

import com.demo.test.dao.ProductCategoryRepository;
import com.demo.test.domain.ProductCategory;
import com.demo.test.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类目服务实现
 *
 * @author Jack
 * @date 2019/07/19
 */
@Service
@CacheConfig(cacheNames = "productCategoryCache")
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    ProductCategoryRepository repository;

    @Override
    @Cacheable
    public ProductCategory findOne(Integer id) {
        return repository.findById(id).get();
    }

    @Override
    @Cacheable
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }

    @Override
    @Cacheable
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> types) {
        return repository.findByCategoryTypeIn(types);
    }
}