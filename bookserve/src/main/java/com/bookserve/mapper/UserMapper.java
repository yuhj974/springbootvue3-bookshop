package com.bookserve.mapper;

import com.bookserve.pojo.Account;
import com.bookserve.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> getAllUser(String username, String phone, Integer status);

    void addUser(User user);

    @Select("select * from user where id = #{id}")
    User getUserById(Integer id);

       void updateUser(User user);
        //int  updateUser(User user);


    void deleteUser(List<Integer> ids);

    //Account login(Account account);
    User login(Account account);

    boolean existsByUsername(String username);

    boolean existsByPhone(String phone);

    User findByUsername(String username);

    User selectById(Integer id);
}
