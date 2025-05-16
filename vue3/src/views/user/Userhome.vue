<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { booklist } from '@/api/book'
import { getCategory } from '@/api/category'
import { useRouter } from 'vue-router'
import Header from '@/components/Header.vue'
import { addCart, getCartByUserId } from '@/api/cart'

 const router = useRouter() // 移动到函数内部获取路由实例
// 当前用户的购物车商品ID集合（仅声明一次）
const cartItems = ref(new Set())

// 新增：登录状态管理
const isLoggedIn = ref(false)

// 初始化时从API获取购物车数据
const fetchCartItems = async () => {
  if (!isLoggedIn.value) return

  try {
    const res = await getCartByUserId()
    // 明确处理空数据
    cartItems.value = new Set(
      (res.data || []).map(item => item.bookId) // 默认空数组
    )
  } catch (error) {
    console.error('获取购物车数据失败:', error)
    if (error.response?.status === 401) {
      isLoggedIn.value = false
    }
    cartItems.value = new Set() // 出错时重置
  }
}

// 新增：登录成功后调用此函数
const handleLoginSuccess = () => {
  isLoggedIn.value = true
  fetchCartItems()
}

// 新增：登出函数
const handleLogout = () => {
  isLoggedIn.value = false
  cartItems.value = new Set() // 直接修改响应式数据的值
  localStorage.removeItem('logintoken')
}  

// 加入购物车逻辑
const handleAddToCart = async (bookId) => {
  try {
    const tokenInfo = JSON.parse(localStorage.getItem('logintoken'))
    if (!tokenInfo?.token) {
      ElMessage.error('请先登录')
     
      router.push('/login')
      return
    }

    if (cartItems.value.has(bookId)) {
      ElMessage.warning('该商品已在购物车中，请前往购物车修改数量')
      return
    }

    await addCart({ bookId, quantity: 1 })
    cartItems.value.add(bookId)
    ElMessage.success({
      message: '已加入购物车',
      duration: 2000,
      showClose: true
    })
  } catch (error) {
    console.error('添加购物车错误:', error)
  }
}

// 响应式数据
const categories = ref([])
const books = ref([])
const searchKeyword = ref('')
const selectedCategory = ref('')

// 获取分类数据
const loadCategories = async () => {
  try {
    const res = await getCategory()
    categories.value = res.data
  } catch (error) {
    ElMessage.error('获取分类失败')
  }
}

// 获取图书数据
const loadBooks = async (params = {}) => {
  try {
    const res = await booklist(params)
    books.value = res.data.rows.filter(item => item.status !== 0)
  } catch (error) {
    ElMessage.error('获取图书失败')
  }
}

// 分类点击处理
const handleCategoryClick = (categoryName) => {
  selectedCategory.value = categoryName
  loadBooks({ name: searchKeyword.value, categoryName })
}

// 搜索处理
const handleSearch = () => {
  selectedCategory.value = ''
  loadBooks({ name: searchKeyword.value })
}

// 加载推荐图书
const loadRecommendedBooks = () => {
  selectedCategory.value = ''
  searchKeyword.value = ''
  loadBooks()
}

// 初始化加载
onMounted(() => {
  loadCategories()
  loadRecommendedBooks()
  fetchCartItems() // 新增：获取用户购物车数据
})
</script>

<template>
  <el-container class="container">
    <!-- 使用头部组件 -->
    <Header />

    <!-- 主体内容 -->
    <el-main class="main-content">
      <div class="content-wrapper">
        <!-- 分类侧边栏 -->
        <div class="category-sidebar">
          <div class="category-title">图书分类</div>
          <el-menu class="category-menu">
            <el-menu-item 
              @click="loadRecommendedBooks"
              :class="{ 'is-active': selectedCategory === '' && searchKeyword === '' }"
            >首页</el-menu-item>
            <el-menu-item 
              v-for="category in categories" 
              :key="category.id"
              @click="handleCategoryClick(category.name)"
              :class="{ 'is-active': selectedCategory === category.name }"
            >
              {{ category.name }}
            </el-menu-item>
          </el-menu>
        </div>

        <!-- 图书列表 -->
        <div class="book-list">
          <div class="search-box">
            <el-input 
              v-model="searchKeyword"
              placeholder="请输入图书名称"
              clearable
              @change="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
              <template #append>
                <el-button @click="handleSearch"><el-icon><Position /></el-icon>搜索</el-button>
              </template>
            </el-input>
          </div>
          <el-row :gutter="20" class="book-grid">
            <el-col 
              v-for="book in books" 
              :key="book.id"
              :xs="12" :sm="8" :md="6" :lg="4"
              class="book-col"
            >
              <el-card class="book-card">
                <div class="card-content" @click="router.push(`/detail/${book.id}`)">
                  <img 
                    :src="book.coverImage" 
                    class="book-cover"
                  />
                  <div class="book-title">{{ book.title }}</div>
                  <div class="book-author">{{ book.author }}</div>
                </div>
                <div class="book-footer">
                  <span class="book-price">¥{{ book.price.toFixed(2) }}</span>
                  <!-- 按钮修改 -->
                  <el-button 
  type="primary" 
  size="small"
  @click.stop="handleAddToCart(book.id)"
  :disabled="cartItems.value?.has(book.id)" 
>
  {{ cartItems.value?.has(book.id) ? '已加入购物车' : '加入购物车' }}
</el-button>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-main>
  </el-container>
</template>
<style scoped>
.card-content {
  cursor: pointer;
  margin-bottom: 10px;
}

.book-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 10px;
  border-top: 1px solid #eee;
}
/* 容器布局 */
.container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 头部样式 */
.header {
  background: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 60px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #2563eb;
  text-decoration: none;
}

/* 导航右侧 */
.nav-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.cart-btn {
  padding: 0 10px;
  font-size: 14px;
}

/* 用户信息 */
.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.avatar {
  margin-right: 10px;
}

.username {
  color: #4b5563;
  font-size: 14px;
}

/* 主体内容 */
.main-content {
  flex: 1;
  background: #f3f4f6;
  padding: 80px 0px 0px;
  /* margin: 50px 0px 200px; */
}

.content-wrapper {
  display: flex;
  gap: 20px;
  height: 100%;
 
}

/* 分类侧边栏 */
.category-sidebar {
  width: 260px;
  background: #ffffff;
  padding: 24px 0;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  margin-left: 20px;
  position: sticky;
  top: 80px;
  height: calc(100vh - 80px);
}

.category-title {
  font-weight: 600;
  color: #1f2937;
  font-size: 18px;
  padding: 0 24px 16px;
  border-bottom: 2px solid #f3f4f6;
  margin-bottom: 8px;
}

/* 分类菜单样式 */
:deep(.category-menu.el-menu) {
  border-right: none;
  padding: 0 16px;
}

:deep(.category-menu .el-menu-item) {
  height: 48px;
  line-height: 48px;
  margin: 4px 0;
  border-radius: 8px;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  color: #4b5563;
  display: flex;
  align-items: center;
}

:deep(.category-menu .el-menu-item:hover) {
  background-color: #f3f4f6;
  color: #2563eb;
  transform: translateX(4px);
}

:deep(.el-menu-item.is-active) {
  background: #2563eb !important;
  color: white !important;
  font-weight: 500;
  position: relative;
}

:deep(.el-menu-item.is-active::before) {
  content: '';
  position: absolute;
  left: -16px;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 24px;
  background: #2563eb;
  border-radius: 2px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .category-sidebar {
    width: 100%;
    margin-left: 0;
    position: static;
    top: auto;
  }
  
  :deep(.category-menu .el-menu-item) {
    height: 44px;
    line-height: 44px;
  }
}

/* 图书列表 */
.book-list {
  flex: 1;
}

.search-box {
  margin-bottom: 20px;
  width: 100%;
}

/* 图书网格布局 */
.book-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.book-col {
  padding: 0 10px;
  margin-bottom: 20px;
}

/* 图书卡片 */
.book-card {
  height: 100%;
  transition: all 0.3s;
}

.book-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.book-cover {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 10px;
}

/* 图书信息 */
.book-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.book-author {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.book-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.book-price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .content-wrapper {
    flex-direction: column;
  }

  .category-sidebar {
    width: 100%;
    margin-bottom: 15px;
  }

  .book-cover {
    height: 150px;
  }

  :deep(.el-col) {
    width: 100%;
  }
}

/* 卡片内部样式 */
:deep(.el-card__body) {
  padding: 15px !important;
}

/* 下拉菜单样式 */
:deep(.el-dropdown-menu__item) {
  padding: 0 16px !important;
  line-height: 36px;
}
:deep(.search-box .el-input) {
  width: 100%;
}


</style>