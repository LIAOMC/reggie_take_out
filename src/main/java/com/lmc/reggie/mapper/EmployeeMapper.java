package com.lmc.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmc.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 41150
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
