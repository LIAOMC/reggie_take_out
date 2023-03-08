package com.lmc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lmc.reggie.common.R;
import com.lmc.reggie.dto.SetmealDto;
import com.lmc.reggie.entity.Category;
import com.lmc.reggie.entity.Employee;
import com.lmc.reggie.entity.Setmeal;
import com.lmc.reggie.service.CategoryService;
import com.lmc.reggie.service.SetmealDishService;
import com.lmc.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true)//删除所有setmealCache缓存下的数据
    public R<String> save(@RequestBody SetmealDto setmealDto){
//        log.info("套餐信息：{}",setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐信息成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
//        log.info("page={}，pageSize={}，name={}",page,pageSize,name);
        //构造分页构造器
        Page<Setmeal> pageInfo=new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage=new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name!=null,Setmeal::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //执行查询
        setmealService.page(pageInfo,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list=records.stream().map((item)->{
            SetmealDto setmealDto=new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)//删除所有setmealCache缓存下的数据
    public R<String> delete(@RequestParam List<Long> ids){
//        log.info("ids:{}",ids);
        setmealService.removeWithDish(ids);
        return R.success("套餐数据删除成功！");
    }

    /**
     * 移动端根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    //将数据存入缓存中
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable int status,@RequestParam List<Long> ids){
        log.info("传过来的集合为：ids:{}",ids.toString());
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        Setmeal setmeal=new Setmeal();
        setmeal.setStatus(status);
        setmealService.update(setmeal,queryWrapper);
        return R.success("修改套餐信息成功！");
    }

//    @GetMapping("/{ids}")
//    public R<Setmeal> getSetmealInfo(@PathVariable Long ids){
//        log.info("传过来的值为：{}",ids);
//        return null;
//    }

}
