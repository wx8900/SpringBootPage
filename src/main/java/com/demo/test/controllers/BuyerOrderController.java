package com.demo.test.controllers;

import com.demo.test.domain.OrderForm;
import com.demo.test.dto.OrderDTO;
import com.demo.test.dto.OrderForm2OrderDTO;
import com.demo.test.enums.ResultEnum;
import com.demo.test.exception.SellException;
import com.demo.test.security.CookieUtils;
import com.demo.test.security.MDUtils;
import com.demo.test.service.BuyerService;
import com.demo.test.service.OrderService;
import com.demo.test.utils.OrderNumberGeneratorLock;
import com.demo.test.utils.TokenUtils;
import com.demo.test.vo.ResultVO;
import com.demo.test.vo.ResultVOUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jack
 * @date 2019/06/24 15:12 PM
 */
@Api("买家订单服务接口")
@RestController
@RequestMapping("/v1/api/buyer/order")
public class BuyerOrderController {

    static Logger logger = LogManager.getLogger(BuyerOrderController.class);

    private OrderNumberGeneratorLock orderNumGenerator = new OrderNumberGeneratorLock();

    @Autowired
    OrderService orderService;

    @Autowired
    BuyerService buyerService;

    @GetMapping(value = "/addOrder")
    public void addOrder() throws InterruptedException {
        String num = orderNumGenerator.getNumber();
        System.out.println(num);
    }

    /**
     * 创建订单
     *
     * @param orderForm
     * @param sign          安全校验位
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/create")
    @ApiOperation(value = "创建订单")
    public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm,
                                                @RequestParam("sign") String sign,
                                                BindingResult bindingResult,
                                                HttpServletRequest request) {
        String token = CookieUtils.getRequestedToken(request);
        if (!TokenUtils.hasToken(token)) {
            logger.error("Please login the system!");
        }
        String sb = "name=" + orderForm.getName() + "&phone=" + orderForm.getPhone();
        String mde5Str = MDUtils.getMD5(sb);
        if (!sign.equals(mde5Str)) {
            logger.error("警告：接口被篡改！");
            return ResultVOUtils.error(400, "警告：接口被篡改！");
        }

        if (bindingResult.hasErrors()) {
            logger.error("【创建订单】参数错误，orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAMS_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        //将 OrderForm 转化为 OrderDTO
        OrderDTO orderDTO = OrderForm2OrderDTO.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            logger.error("【创建订单】购物车为空，orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.CART_EMPTY);
        }

        //创建订单
        OrderDTO result = orderService.create(orderDTO);
        Map<String, String> map = new HashMap<>(8);
        map.put("orderId", result.getOrderId());
        return ResultVOUtils.success(map);
    }

    /**
     * 订单列表
     *
     * @param opendid
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/{openid}")
    @ApiOperation("查询订单列表")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String opendid,
                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (StringUtils.isEmpty(opendid)) {
            logger.error("【订单列表】openid不能为空");
            throw new SellException(ResultEnum.PARAMS_ERROR);
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OrderDTO> list = orderService.findList(opendid, pageRequest);
        return ResultVOUtils.success(list.getContent());
    }

    /**
     * 订单详情
     *
     * @param openid
     * @param orderId
     * @return
     */
    @GetMapping("/detail/{openid}")
    @ApiOperation("查看订单详情")
    public ResultVO<OrderDTO> detail(@RequestParam("openid") String openid,
                                     @RequestParam("orderId") String orderId) {
        if (StringUtils.isEmpty(openid)) {
            logger.error("【订单列表】openid不能为空");
            throw new SellException(ResultEnum.PARAMS_ERROR);
        }
        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);
        return ResultVOUtils.success(orderDTO);
    }

    /**
     * 取消订单
     *
     * @param openid
     * @param orderId
     * @return
     */
    @PostMapping("/cancel/{openid}")
    @ApiOperation("取消订单")
    public ResultVO cancel(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId) {
        if (StringUtils.isEmpty(openid)) {
            logger.error("【订单列表】openid不能为空");
            throw new SellException(ResultEnum.PARAMS_ERROR);
        }
        buyerService.cancelOrder(openid, orderId);
        return ResultVOUtils.success();
    }

}

