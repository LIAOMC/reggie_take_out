package com.lmc.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmc.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
