package com.bookserve.service;

import com.bookserve.pojo.Address;
import com.bookserve.pojo.PageResult;

import java.util.List;

public interface AddressService {
    PageResult<Address> getAllAddress(Integer pageNum, Integer pageSize, String username, String consignee, String phone);

    void deleteAddress(List<Integer> ids);

    List<Address> getAddressByUserId(Integer userId);

    void addAddress(Address address);

   // void setDefaultAddress(Integer id);

    void updateAddress(Address address);

    Address getAddressById(Integer id);
}
