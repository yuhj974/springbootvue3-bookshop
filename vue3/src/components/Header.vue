<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { jwtDecode } from 'jwt-decode'
import { ElMessage } from 'element-plus'
// import { getUserByJwt } from '@/api/user'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import { storeToRefs } from 'pinia'
const router = useRouter()
const userStore = useUserStore()
const { userInfo } = storeToRefs(userStore)
const cartStore = useCartStore()
const { count } = storeToRefs(cartStore)

onMounted(() => {
  const token = localStorage.getItem('logintoken')
  if (token) {
    try {
      jwtDecode(token)
      userStore.loadUserInfo() // 初始化加载
    } catch (error) {
      handleLogout()
    }
  }
})

const handleLogout = () => {
  localStorage.removeItem('logintoken')
  router.push('/login')
  userStore.reset()  // 使用自定义重置方法
  ElMessage.success('已退出登录')
}
const handleUnauthClick = () => {
  ElMessage.warning('请先登录')
  router.push('/login')
}
</script>

<template>
  <el-header class="header">
    <router-link to="/user/home" class="logo">网上书店</router-link>

    <div class="nav-right">
      <el-badge 
        :value="count"
        :max="99" 
        :show-zero="false"
        class="cart-badge"
      >
        <el-button 
          class="cart-btn" 
          type="text" 
          @click="router.push('/user/cart')"
        >
          <el-icon><ShoppingCartFull /></el-icon>
          购物车
        </el-button>
      </el-badge>

        <template v-if="userInfo.name !== '未登录'">
        <el-dropdown trigger="click">
          <div class="user-info">
           <el-avatar :src="userInfo.avatar ? userInfo.avatar : 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" class="avatar"></el-avatar>
            <span class="username">{{ userInfo.name }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/profile')">个人中心</el-dropdown-item>
              <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
      <div 
        v-else
        class="user-info"
        @click="handleUnauthClick"
      >
        <el-avatar :src="userInfo.avatar ? userInfo.avatar : 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" class="avatar"></el-avatar>
        <span class="username">{{ userInfo.name }}</span>
      </div>
    </div>
  </el-header>
</template>

<style scoped>
.header {
  background: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px; /* 增加左右内边距 */
  height: 60px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0; /* 确保左右贴边 */
  z-index: 1000;
}

.logo {
  font-size: 24px; /* 增大字体 */
  font-weight: 800; /* 加粗 */
  color: #2563eb;
  text-decoration: none;
  transition: opacity 0.3s;
}

.logo:hover {
  opacity: 0.8;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 30px; /* 增大元素间距 */
  
}

.cart-btn {
  padding: 8px 15px; /* 增大按钮内边距 */
  font-size: 15px;
  display: flex;
  align-items: center;
  gap: 6px;
  position: relative; /* 新增：相对定位 */
}

.cart-btn:hover {
  background: #f5f5f5;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px; /* 使用gap替代margin */
  padding: 6px 12px;
  border-radius: 8px;
  transition: background 0.3s;
}

.user-info:hover {
  background: #f5f5f5;
}

.username {
  font-size: 15px;
  font-weight: 500;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
}
.cart-badge :deep(.el-badge__content) {
  top: 0; /* 修改：调整到右上角 */
  right: 0; /* 修改：调整到右上角 */
  transform: translate(50%, -50%); /* 修改：使用transform精确定位 */
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

@media (max-width: 768px) {
  .header {
    padding: 0 20px;
  }
  
  .username {
    display: none; /* 小屏幕隐藏用户名 */
  }
}
</style>