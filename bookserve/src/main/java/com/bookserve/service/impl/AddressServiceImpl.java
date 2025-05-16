package com.bookserve.service.impl;

import com.bookserve.mapper.AddressMapper;
import com.bookserve.pojo.Address;
import com.bookserve.pojo.PageResult;
import com.bookserve.service.AddressService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;

    @Override
    public PageResult<Address> getAllAddress(Integer pageNum, Integer pageSize, String username, String consignee, String phone) {
        PageHelper.startPage(pageNum,pageSize);
        List<Address> addressList = addressMapper.getAllAddress(username, consignee, phone);
        Page<Address> page = (Page<Address>) addressList;
        PageResult<Address> pageResult = new PageResult<>(page.getTotal(),page.getResult());
        return pageResult;

    }

    @Override
    public void deleteAddress(List<Integer> ids) {
        addressMapper.deleteAddress(ids);
    }

    @Override
    public List<Address> getAddressByUserId(Integer userId) {
        return addressMapper.getAddressByUserId(userId);

    }

    @Override
    public void addAddress(Address address) {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 设置创建时间（假设创建时间在对象创建时或者插入数据库时设置，这里简单认为插入时设置）
        if (address.getCreateTime() == null) {
            address.setCreateTime(now);
        }
        // 设置修改时间，每次操作都更新修改时间
        address.setUpdateTime(now);

        Integer isDefault = address.getIsDefault();
        if (isDefault != null && isDefault == 1) {
            List<Address> addressList = addressMapper.getAddressByUserId(address.getUserId());
            for (Address a : addressList) {
                a.setIsDefault(0);
                a.setUpdateTime(now);
                addressMapper.updateById(a);
            }
        }
        addressMapper.addAddress(address);
    }

    @Override
    public void updateAddress(Address address) {
        Address addressById = addressMapper.getAddressById(address.getId());
        if (addressById != null) {
            if (address.getIsDefault() != null && address.getIsDefault() == 1) {
                List<Address> addressList = addressMapper.getAddressByUserId(address.getUserId());
                for (Address a : addressList) {
                    if (!a.getId().equals(address.getId())) { // 跳过当前更新的地址
                        a.setIsDefault(0);
                        addressMapper.updateById(a);
                    }
                    address.setIsDefault(1);
                    addressMapper.updateById(address);

                }
            }
            addressMapper.updateById(address);

        }

//    @Override
//    public void setDefaultAddress(Integer id) {
//        Address address = addressMapper.getAddressById(id);
//        if (address != null) {
//            List<Address> addressList = addressMapper.getAddressByUserId(address.getUserId());
//            for (Address a : addressList) {
//                a.setIsDefault(0);
//                addressMapper.updateById(a);
//            }
//        }
//        address.setIsDefault(1);
//        addressMapper.updateById(address);
//
//
//    }
    }

    @Override
    public Address getAddressById(Integer id) {
        return addressMapper.getAddressById(id);

    }
}
