package com.lmc.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmc.reggie.entity.Employee;
import com.lmc.reggie.mapper.EmployeeMapper;
import com.lmc.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author 41150
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
