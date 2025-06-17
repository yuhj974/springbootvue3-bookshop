package com.bookserve.service.impl;

import com.bookserve.exception.BusinessException;
import com.bookserve.mapper.AdminMapper;
import com.bookserve.pojo.Account;
import com.bookserve.pojo.Admin;
import com.bookserve.pojo.PageResult;
import com.bookserve.service.AdminService;
import com.bookserve.utils.PasswordUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public PageResult<Admin> getAllAdmin(Integer pageNum, Integer pageSize, String username) {
        PageHelper.startPage(pageNum,pageSize);
        List<Admin> adminList = adminMapper.getAllAdmin(username);
        Page<Admin> page = (Page<Admin>) adminList;
        PageResult<Admin> pageResult = new PageResult<>(page.getTotal(),page.getResult());
        return pageResult;
    }

    @Override
    public Admin getAdminById(Integer id) {

        return adminMapper.getAdminById(id);

    }

    @Override
    public void updateAdmin(Admin admin) {
        admin.setUpdateTime(LocalDateTime.now());
        if (StringUtils.isBlank(admin.getPassword())) {
            // 密码为空时保留原加密密码
            Admin existing = adminMapper.getAdminById(admin.getId());
            admin.setPassword(existing.getPassword());
        } else {
            // 密码非空时进行MD5加密
            admin.setPassword(PasswordUtil.md5Encrypt(admin.getPassword()));
        }
        adminMapper.updateAdmin(admin); // 删除重复的更新操作
    }

    @Override
    public void addAdmin(Admin admin) {
       Admin admin1 =  adminMapper.findByAdminname(admin.getUsername());
       if (admin1 != null){
           throw new BusinessException("管理员已存在");
       }
        admin.setCreateTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());
        adminMapper.addAdmin(admin);
    }

    @Override
    public void deleteAdmin(Integer id) {
        adminMapper.deleteAdmin(id);
    }

    @Override
    public Admin login(Account account) {
        // 根据用户名查询管理员（修改后的逻辑）
        Admin admin = adminMapper.findByUsername(account.getUsername());

        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }

        // 加密输入密码进行比对（新增核心逻辑）
        String inputEncrypted = PasswordUtil.md5Encrypt(account.getPassword());
        if (!inputEncrypted.equals(admin.getPassword())) {
            throw new BusinessException("密码错误");
        }

        return admin;
    }
//    public Admin login(Account account) {
//        String username = account.getUsername();  // 账号
//        Admin dbAdmin = adminMapper.selectByUsername(username);
//        if (dbAdmin == null) {  // 没查询到任何用户   说明没有这个账号
//            throw new CustomException("500", "账号不存在");
//        }
//        // 数据库存在这个账号
//        String password = account.getPassword();
//        if (!dbAdmin.getPassword().equals(password)) {  // 用户输入的密码跟数据库账号的密码不匹配
//            throw new CustomException("500", "账号或密码错误");
//        }
//        return dbAdmin;
//    }

}
