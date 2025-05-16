package com.bookserve.service;

import com.bookserve.pojo.Account;
import com.bookserve.pojo.PageResult;
import com.bookserve.pojo.User;

import java.util.List;

public interface UserService {
    PageResult<User> getAllUser(Integer pageNum, Integer pageSize, String username, String phone, Integer status);

    void addUser(User user);

    User getUserById(Integer id);

    void updateUser(User user);
    //int updateUser(User user);
    void deleteUser(List<Integer> ids);

    // Account login(Account account);
    User login(Account account);


    void register(User user);

    void updatePassword(User user);

    User getById(Integer id);
}
