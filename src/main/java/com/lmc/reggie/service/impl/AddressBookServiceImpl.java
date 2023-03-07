package com.lmc.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmc.reggie.entity.AddressBook;
import com.lmc.reggie.mapper.AddressBookMapper;
import com.lmc.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService{

}
