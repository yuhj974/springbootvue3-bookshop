<script setup>
import { ref, onMounted } from 'vue'
import avatarImg from '@/assets/aaa.png'
import router from '@/router/index'
import { jwtDecode } from 'jwt-decode'
import {getAdminByJwt} from '@/api/admin'

// 用户信息响应式对象
const userInfo = ref({
  name: '未登录',
  avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
})

const loadAdminInfo = async () => {
  try {
    const res = await getAdminByJwt()
    userInfo.value = {
      name: res.data.name || '管理员',
      avatar: res.data.avatar || userInfo.value.avatar
    }
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}

onMounted(() => {
  const token = localStorage.getItem('logintoken')
  if (token) {
    try {
      jwtDecode(token) // 验证token有效性
      loadAdminInfo()
    } catch (error) {
      ElMessage.error('登录信息已过期，请重新登录')
      localStorage.removeItem('logintoken')
      router.push('/login')
    }
  }
})
// 处理退出登录事件
const handleLogout = () => {
  console.log('退出登录')
  localStorage.removeItem('token')
  localStorage.removeItem('logintoken')  // 清除用户信息
  router.push('/Login')
}
</script>

<template>
    <div class="common-layout">
        <el-container>
            <!-- Header 区域 -->
            <el-header class="header">
                <span class="title">书店后台管理</span>
                <div class="right_tool">
                    <el-dropdown trigger="click">
                        <!-- 修改后的头像和用户名显示部分 -->
                        <div style="display: flex; align-items: center; cursor: pointer">
                            <el-avatar
                                :size="36"
                                :src="userInfo.avatar"
                                style="margin-right: 8px"
                            />
                            <span style="color: #fff">{{ userInfo.name }}</span>
                        </div>

                        <!-- 下拉菜单 -->
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item divided @click="handleLogout">
                                    <el-icon><SwitchButton /></el-icon>
                                    <span>退出登录</span>
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </el-header>

            <el-container style="margin-top: 60px;"> 
                <!-- 左侧菜单 -->
                <el-aside width="200px" class="aside">
                    <!-- 左侧菜单栏 -->
                    <el-menu router>
                        <!-- 首页菜单 -->
                        <el-menu-item index="/Adminindex">
                            <el-icon><Promotion /></el-icon> 首页
                        </el-menu-item>

                        <!-- 班级管理菜单 -->
                        <el-sub-menu>
                            <template #title>
                                <el-icon><Menu /></el-icon> 图书管理
                            </template>
                            <el-menu-item index="/admin/Adminbook">
                                <el-icon><HomeFilled /></el-icon>书本管理
                            </el-menu-item>
                            <el-menu-item index="/admin/AdminCategory">
                              <el-icon><FirstAidKit /></el-icon>分类管理
                            </el-menu-item>
                        </el-sub-menu>
                        <el-menu-item index="/admin/AdminUser">
                          <el-icon><UserFilled /></el-icon>用户管理
                        </el-menu-item>
                        <el-menu-item index="/admin/Adminadmin">
                            <el-icon><UserFilled /></el-icon>管理员管理
                        </el-menu-item>
                        <el-menu-item index="/admin/Adminreview">
                            <el-icon><Comment /></el-icon>评论管理
                        </el-menu-item>
                        <el-menu-item index="/admin/Adminaddress">
                            <el-icon><Box /></el-icon>收货地址管理
                        </el-menu-item>
                        <el-menu-item index="/admin/Admincart">
                            <el-icon><ShoppingCart /></el-icon>购物车管理
                        </el-menu-item>
                        <el-menu-item index="/admin/Adminorder">
                            <el-icon><ShoppingBag /></el-icon>订单管理
                        </el-menu-item>
                      
                    </el-menu>
                </el-aside>

                <el-main class="main-content">
                    <router-view></router-view>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<style scoped>
.header {
    position: fixed;
    top: 0;
    width: 100%;
    z-index: 1000;
    height: 60px; /* 明确高度 */
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #409EFF;
    padding: 0 20px;
}

.title {
    color: #fff;
    font-size: 20px;
    font-weight: bold;
}

.right_tool {
    display: flex;
    align-items: center;
    gap: 20px;
}

/* 下拉菜单项样式 */
.el-dropdown-menu__item {
    display: flex;
    align-items: center;
    padding: 8px 16px;
}

.el-icon {
    margin-right: 8px;
}

a {
    color: white;
    text-decoration: none;
}

.aside {
    position: fixed;
    left: 0;
    top: 60px; /* 位于头部下方 */
    bottom: 0;
    z-index: 999;
    border-right: 1px solid #ccc;
    height: calc(100vh - 60px); /* 全屏高度减去头部 */
}

.main-content {
    margin-left: 200px; /* 侧边栏宽度 */
    padding: 20px;
    min-height: calc(100vh - 60px); /* 确保内容区域高度 */
    overflow-y: auto; /* 内容过多时内部滚动 */
}
</style>