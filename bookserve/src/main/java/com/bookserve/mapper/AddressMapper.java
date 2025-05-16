package com.bookserve.mapper;

import com.bookserve.pojo.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper {
    List<Address> getAllAddress(String username, String consignee, String phone);

    void deleteAddress(List<Integer> ids);

    List<Address> getAddressByUserId(Integer userId);

    void updateById(Address a);

    void addAddress(Address address);

    Address getAddressById(Integer id);


    // 根据用户ID查找默认地址
    Address findDefaultByUserId(@Param("userId") Integer userId);

    // 根据地址ID和用户ID验证地址是否存在
    Address findByIdAndUserId(@Param("id") Integer id, @Param("userId") Integer userId);
}
