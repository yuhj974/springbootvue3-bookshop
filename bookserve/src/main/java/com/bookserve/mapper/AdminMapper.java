package com.bookserve.mapper;

import com.bookserve.pojo.Account;
import com.bookserve.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {

    List<Admin> getAllAdmin(String username);

    Admin getAdminById(Integer id);

    void updateAdmin(Admin admin);

    void addAdmin(Admin admin);

    void deleteAdmin(Integer id);

    //Account login(Account account);
    Admin login(Account account);

    Admin selectByUsername(String username);

    Admin findByUsername(String username);

    Admin findByAdminname(String username);
}
