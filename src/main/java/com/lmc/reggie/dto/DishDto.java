package com.lmc.reggie.dto;

import com.lmc.reggie.entity.Dish;
import com.lmc.reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {
    //菜品所对应的口味信息
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
