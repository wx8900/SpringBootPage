package com.demo.test.dao;

import com.demo.test.domain.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductInfoRepository extends PagingAndSortingRepository<ProductInfo, Long> {

    List<ProductInfo> findByProductStatus(Integer status);

    //List<ProductInfo> findAllProductByCategoryId(Integer id);
}
