package com.demo.test.controllers;

import com.demo.test.domain.ProductCategory;
import com.demo.test.domain.ProductInfo;
import com.demo.test.security.CookieUtils;
import com.demo.test.service.ProductCategoryService;
import com.demo.test.service.ProductInfoService;
import com.demo.test.utils.TokenUtils;
import com.demo.test.vo.ProductInfoVO;
import com.demo.test.vo.ProductVO;
import com.demo.test.vo.ResultVO;
import com.demo.test.vo.ResultVOUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jack
 * @date 2019/06/24 16:02 PM
 */
@Api("买家获取商品接口")
@RestController
@RequestMapping("/v1/api/buyer/product")
public class BuyerProductController {

    static Logger logger = LogManager.getLogger(BuyerProductController.class);

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService categoryService;

    @ApiOperation(value = "获取所有上架商品", notes = "获取所有上架商品,下架商品除外")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultVO list(HttpServletRequest request) {
        String token = CookieUtils.getRequestedToken(request);
        if (!TokenUtils.hasToken(token)) {
            logger.error("Please login the system!");
        }

        // 1.查询所有上架商品
        List<ProductInfo> productInfoList = productInfoService.findUpAll();

        // 2.查询类目（一次性查询）
        //用 java8 的特性获取到上架商品的所有类型
        List<Integer> categoryTypes = productInfoList.stream().map(e -> e.getCategoryType()).collect(Collectors.toList());
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypes);

        List<ProductVO> productVOList = new ArrayList<>();
        //数据拼装
        for (ProductCategory category : productCategoryList) {
            ProductVO productVO = ProductVO.builder().build();
            //属性拷贝
            BeanUtils.copyProperties(category, productVO);
            //把类型匹配的商品添加进去
            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if (productInfo.getCategoryType().equals(category.getCategoryType())) {
                    ProductInfoVO productInfoVO = ProductInfoVO.builder().build();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        return ResultVOUtils.success(productVOList);
    }
}

