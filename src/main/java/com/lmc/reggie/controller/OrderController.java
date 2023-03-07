package com.lmc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lmc.reggie.common.R;
import com.lmc.reggie.entity.Employee;
import com.lmc.reggie.entity.Orders;
import com.lmc.reggie.service.OrderService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
//        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String number, String beginTime ,String endTime){
        log.info("所有信息为：页数"+page+"单页个数"+pageSize+"单号"+number+"开始时间"+beginTime+"结束时间"+endTime);
        //构造分页构造器
        Page pageInfo=new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(!StringUtils.isEmpty(number),Orders::getNumber,number);
        if(beginTime!=null && endTime!=null){
            queryWrapper.ge(Orders::getOrderTime,beginTime).lt(Orders::getCheckoutTime,endTime);
        }
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);
        //执行查询
        orderService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
}
