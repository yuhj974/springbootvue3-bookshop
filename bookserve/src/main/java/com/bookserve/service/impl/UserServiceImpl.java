package com.bookserve.service.impl;

import com.bookserve.exception.BusinessException;
import com.bookserve.mapper.UserMapper;
import com.bookserve.pojo.Account;
import com.bookserve.pojo.PageResult;
import com.bookserve.pojo.User;
import com.bookserve.service.UserService;
import com.bookserve.utils.PasswordUtil;
import com.bookserve.utils.Result;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult<User> getAllUser(Integer pageNum, Integer pageSize, String username, String phone, Integer status) {

        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userMapper.getAllUser(username, phone, status);
        Page<User> page = (Page<User>) userList;
        PageResult<User> pageResult = new PageResult<>(page.getTotal(), page.getResult());
        return pageResult;
    }

    @Override
    public void addUser(User user) {
        // 检查用户名是否存在（原有逻辑）
        if (userMapper.existsByUsername(user.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        // 检查手机号是否存在（原有逻辑）
        if (userMapper.existsByPhone(user.getPhone())) {
            throw new BusinessException("手机号已存在");
        }

        // 加密密码（新增核心逻辑）
        String encryptedPwd = PasswordUtil.md5Encrypt(user.getPassword());
        user.setPassword(encryptedPwd);

        // 设置默认角色为USER（原有逻辑）
        user.setRole("USER");
        user.setStatus(1);

        // 生成默认用户名（原有逻辑）
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            String randomNum = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
            user.setName("user" + randomNum);
        }

        // 设置时间戳（原有逻辑）
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        // 插入用户（原有逻辑）
        userMapper.addUser(user);
    }

    @Override
    public User getUserById(Integer id) {

        User user = userMapper.getUserById(id);
        return user;
    }

        @Override
    public void updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateUser(user);
    }
//    @Override
//    public int updateUser(User user) {
//        return userMapper.updateUser(user);
//    }


    @Override
    public void deleteUser(List<Integer> ids) {
        userMapper.deleteUser(ids);
    }

    @Override
    public User login(Account account) {
        // 根据用户名查询用户
        User user = userMapper.findByUsername(account.getUsername());

        // 用户不存在或密码错误
        if (user == null ||
                !user.getPassword().equals(PasswordUtil.md5Encrypt(account.getPassword()))) {
            throw new BusinessException("用户名或密码错误");
        }

        // 检查状态是否为0
        if (user.getStatus() == 0) {
            throw new BusinessException("用户已被封禁！");
        }


        return user;
    }

    @Override
    public void register(User user) {
        this.addUser(user);
    }

    @Override
    public void updatePassword(User user) {
        // 获取目标用户
        User dbUser = this.getUserById(user.getId());

        // 验证用户是否存在
        if (dbUser == null) {
            throw new BusinessException("用户不存在");
        }
        if (!dbUser.getPassword().equals(PasswordUtil.md5Encrypt(user.getPassword()))) {
            throw new BusinessException("旧密码错误");
        }
        if (!user.getNewPassword().equals(user.getNewPassword())) {
            throw new BusinessException("两次密码不一致");
        }
        dbUser.setPassword(PasswordUtil.md5Encrypt(user.getNewPassword()));
        this.updateUser(dbUser);


    }

    public User getById(Integer id) {
        return userMapper.selectById(id);
    }
}
