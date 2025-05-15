package com.bookserve.service;

import com.bookserve.pojo.Account;
import com.bookserve.pojo.Admin;
import com.bookserve.pojo.PageResult;

public interface AdminService {
    PageResult<Admin> getAllAdmin(Integer pageNum, Integer pageSize, String username);

    Admin getAdminById(Integer id);

    void updateAdmin(Admin admin);

    void addAdmin(Admin admin);

    void deleteAdmin(Integer id);

    //Account login(Account account);
   Admin login(Account account);
}
