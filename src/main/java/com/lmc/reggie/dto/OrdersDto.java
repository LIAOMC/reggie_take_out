package com.lmc.reggie.dto;


import com.lmc.reggie.entity.OrderDetail;
import com.lmc.reggie.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

}
