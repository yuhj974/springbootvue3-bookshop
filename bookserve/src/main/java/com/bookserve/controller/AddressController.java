package com.bookserve.controller;

import com.bookserve.pojo.Address;
import com.bookserve.pojo.PageResult;
import com.bookserve.service.AddressService;
import com.bookserve.utils.JwtUtils;
import com.bookserve.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/addresses")
@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;

    /*
    * 查询所有地址
    * */
    @GetMapping
    public Result getAllAddress(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize, String username,String consignee, String phone) {
          PageResult<Address> pageResult = addressService.getAllAddress(pageNum,pageSize,username,consignee,phone);
          return Result.success(pageResult);

    }

    /*
    * 批量删除地址
    *
    * */
    @DeleteMapping("/{ids}")
    public Result deleteAddress(@PathVariable("ids") List<Integer> ids) {
        addressService.deleteAddress(ids);
        return Result.success();
    }

    /*
    * 根据jwt令牌用户id查询地址
    * */
    @GetMapping("/me")
    public Result getAddressByUserId(@RequestHeader("token") String token) {
        try {
            String token1 = token.replace("token ", "");
            Integer userId = JwtUtils.extractUserId(token1);
            List<Address> addresses = addressService.getAddressByUserId(userId);
            return Result.success(addresses);
        } catch (Exception e) {
            return Result.error("令牌解析失败"+e);
        }
    }
    /*
    * 根据jwt令牌用户id添加地址
    * */
    @PostMapping
    public Result addAddress(@RequestBody Address address, @RequestHeader("token") String token) {
        try {
            String token1 = token.replace("token ", "");
            Integer userId = JwtUtils.extractUserId(token1);
            address.setUserId(userId);
            addressService.addAddress(address);
            return Result.success();
            } catch (Exception e) {
            return Result.error("令牌解析失败"+e);
            }
        }


     /*
     * 修改地址
     * */
    @PutMapping
    public Result updateAddress(@RequestBody Address address) {
        addressService.updateAddress(address);
        return Result.success();
    }

    /*
    * 根据地址id查询地址
    * */
    @GetMapping("/{id}")
    public Result getAddressById(@PathVariable Integer id) {
        Address address = addressService.getAddressById(id);
        return Result.success(address);
    }


}
