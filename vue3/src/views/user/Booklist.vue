<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { booklist, getbooklist } from '@/api/book'
import { getCategory } from '@/api/category'
import { useRouter, useRoute } from 'vue-router'
import Header from '@/components/Header.vue'
import { addCart, getCartByUserId } from '@/api/cart'
import { useCartStore } from '@/stores/cart'
const cartStore = useCartStore()

const router = useRouter()
const route = useRoute()
const cartItems = ref(new Set())
const isLoggedIn = ref(!!localStorage.getItem('logintoken'))

// 获取购物车数据
const fetchCartItems = async () => {
  try {
    if (!isLoggedIn.value) return
    const res = await getCartByUserId()
    cartItems.value = new Set(res.data.map(item => item.bookId))
    const totalQuantity = res.data.reduce((sum, item) => sum + item.quantity, 0)
    cartStore.count = totalQuantity
  } catch (error) {
    if (error.response?.status === 401) {
      handleLogout()
      ElMessage.error('登录已过期，请重新登录')
    }
  }
}

const handleLoginSuccess = () => {
  isLoggedIn.value = true
  fetchCartItems()
}

const handleLogout = () => {
  isLoggedIn.value = false
  cartItems.value.clear()
  localStorage.removeItem('logintoken')
  router.push('/login')
}  

const handleAddToCart = async (bookId) => {
  const tokenInfo = JSON.parse(localStorage.getItem('logintoken'))
  if (!tokenInfo?.token) {
    ElMessage.error('请先登录')
    router.push('/login')
    return
  }
  await addCart({ bookId, quantity: 1 })
  await fetchCartItems()
  cartItems.value = new Set([...cartItems.value, bookId])
  ElMessage.success({ message: '已加入购物车', duration: 2000, showClose: true })
} 

// 响应式数据
const categories = ref([])
const books = ref([])
const searchKeyword = ref('')
const selectedCategory = ref('')
const activeCategoryIndex = ref(0)

const loadCategories = async () => {
  try {
    const res = await getCategory()
    categories.value = res.data
  } catch (error) {
    ElMessage.error('获取分类失败')
  }
}

const loadBooks = async (params = {}) => {
  try {
    const res = await booklist(params)
    const bookList = res.data.rows.filter(item => item.status !== 0)
    
    // 处理空结果
    if (bookList.length === 0 && (params.name || params.categoryName)) {
      ElMessage.warning('没有找到相关图书，已为您展示全部图书')
      
      // 重置搜索条件
      searchKeyword.value = ''
      selectedCategory.value = ''
      activeCategoryIndex.value = 0
      
      // 更新URL为无参数状态
      router.replace({ path: '/booklist' })
      
      // 重新加载全部图书
      await loadBooks({})
      return
    }
    
    books.value = bookList
  } catch (error) {
    ElMessage.error('获取图书失败')
  }
}

const getloadBooks = async (params = {}) => {
  try {
    const res = await getbooklist(params)
    books.value = res.data.filter(item => item.status !== 0)
  } catch (error) {
    ElMessage.error('获取图书失败')
  }
}

// 分类点击处理
const handleCategoryClick = (categoryName, index) => {
  searchKeyword.value = ''
  selectedCategory.value = categoryName
  activeCategoryIndex.value = index
  // 清除URL参数
  router.replace({ path: '/booklist' })
  loadBooks({ categoryName })
}

// 搜索处理
const handleSearch = () => {
  selectedCategory.value = ''
  activeCategoryIndex.value = 0
  if (searchKeyword.value.trim()) {
    // 更新URL参数
    router.replace({
      path: '/booklist',
      query: { keyword: searchKeyword.value.trim() }
    })
    loadBooks({ name: searchKeyword.value.trim() })
  } else {
    // 当搜索关键字为空时，清除URL参数
    router.replace({ path: '/booklist' })
    loadRecommendedBooks()
  }
}

// 加载推荐图书
const loadRecommendedBooks = () => {
  searchKeyword.value = ''
  selectedCategory.value = ''
  activeCategoryIndex.value = 0
  // 清除URL参数
  router.replace({ path: '/booklist' })
  getloadBooks({})
}

// 监听路由变化
watch(
  () => route.query.keyword,
  (newKeyword) => {
    if (newKeyword) {
      searchKeyword.value = newKeyword
      handleSearch()
    }
  },
  { immediate: true }
)

// 在挂载时检查路由参数
onMounted(() => {
  loadCategories()
  
  // 检查路由中是否有搜索关键字
  if (route.query.keyword) {
    searchKeyword.value = route.query.keyword
    handleSearch()
  } else {
    loadRecommendedBooks()
  }
  
  if (isLoggedIn.value) fetchCartItems()
})
</script>

<template>
  <el-container class="container">
    <Header />
    <el-main class="main-content">
      <div class="content-wrapper">
         <!-- 加大加长的搜索框 -->
        <div class="search-box-container">
          <el-input 
            v-model="searchKeyword"
            placeholder="请输入图书名称"
            clearable
            @keyup.enter="handleSearch"
            class="large-search"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
            <template #append>
              <el-button @click="handleSearch" class="search-button"><el-icon><Position /></el-icon>搜索</el-button>
            </template>
          </el-input>
        </div>
        <!-- 横向分类导航-->
        <div class="horizontal-category">
          <div class="category-scroll">
            <div class="category-list">
              <!-- 全部图书分类移至左侧 -->
              <div 
                class="category-item" 
                :class="{ 'active': (!selectedCategory &&!searchKeyword) || (selectedCategory === '' && searchKeyword!== '') }"
                @click="loadRecommendedBooks"
              >
                全部图书
              </div>
              <div 
                class="category-item" 
                v-for="(category, index) in categories" 
                :key="category.id"
                :class="{ 'active': selectedCategory === category.name }"
                @click="handleCategoryClick(category.name, index)"
              >
                {{ category.name }}
              </div>
            </div>
          </div>
        </div>

        <!-- 图书列表 -->
        <div class="book-list">
          <el-row :gutter="20" class="book-grid">
            <el-col 
              v-for="book in books" 
              :key="book.id"
              :xs="12" :sm="8" :md="6" :lg="4"
              class="book-col"
            >
              <el-card class="book-card" shadow="hover">
                <div class="card-content" @click="router.push(`/detail/${book.id}`)">
                  <img :src="book.coverImage" class="book-cover" :alt="book.title" />
                  <div class="book-title">{{ book.title }}</div>
                  <div class="book-author">{{ book.author }}</div>
                </div>
                <div class="book-footer">
                  <span class="book-price">¥{{ book.price.toFixed(2) }}</span>
                  <el-button 
                    type="primary" 
                    size="small"
                    :disabled="cartItems.has(book.id)" 
                    @click="handleAddToCart(book.id)"
                  >
                    {{ cartItems.has(book.id) ? '已加入购物车' : '加入购物车' }}
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
/* 基础容器样式 */
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
  position: sticky;
  top: 0;
  z-index: 100;
}

/* 主体内容区域 */
.main-content {
  flex: 1;
  background: #f9fafb;
  padding: 20px 0;
  overflow-x: hidden;
}

.content-wrapper {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}
.banner-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

/* 横向分类导航样式 */
.horizontal-category {
  margin-bottom: 30px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
  padding: 15px 0;
  position: relative;
}

.category-scroll {
  overflow-x: auto;
  white-space: nowrap;
  padding-bottom: 10px;
}

.category-list {
  display: inline-flex;
  gap: 15px;
  padding: 0 20px;
}

.category-item {
  display: inline-block;
  padding: 8px 16px;
  font-size: 14px;
  color: #4b5563;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.category-item:hover {
  background-color: #f3f4f6;
  color: #2563eb;
}

.category-item.active {
  background-color: #2563eb;
  color: white;
  font-weight: 500;
}

/* 加大的搜索框样式 */
.search-box-container {
  margin: 30px 0;
  text-align: center; /* 搜索框居中 */
  background-image: url(''); /* 替换为你的图片路径 */
  background-size: cover; /* 背景图片覆盖整个容器 */
  
}

.large-search {
  width: 100%;
  max-width: 1000px; /* 搜索框最大宽度进一步增加 */
  height: 40px; /* 搜索框高度增加 */
  font-size: 18px;
}

.search-button {
  height: 60px;
  padding: 0 24px;
  font-size: 18px;
}

:deep(.large-search .el-input__inner) {
  height: 60px;
  padding: 0 15px;
  font-size: 18px;
}

:deep(.large-search .el-input__prefix, .large-search .el-input__append) {
  line-height: 60px;
}

/* 图书列表样式 */
.book-list {
  padding-bottom: 50px;
}

.book-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 25px;
}

.book-col {
  padding: 0;
}

/* 图书卡片样式 */
.book-card {
  height: 100%;
  transition: all 0.3s ease;
  border-radius: 10px;
  overflow: hidden;
}

.book-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

.book-cover {
  width: 100%;
  height: 220px;
  object-fit: cover;
  border-radius: 8px 8px 0 0;
  transition: transform 0.5s ease;
}

/* 图书信息样式 */
.book-title {
  font-size: 16px;
  font-weight: 500;
  margin: 10px 0 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #1f2937;
}

.book-author {
  font-size: 13px;
  color: #6b7280;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.book-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 10px;
  border-top: 1px solid #e5e7eb;
}

.book-price {
  color: #ef4444;
  font-weight: 600;
  font-size: 16px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .large-search {
    max-width: 800px;
  }
}

@media (max-width: 768px) {
  .content-wrapper {
    padding: 0 15px;
  }
  
  .large-search {
    max-width: 100%;
  }
  
  .book-cover {
    height: 180px;
  }
}

@media (max-width: 576px) {
  .banner-container {
    height: 180px;
  }
  
  .book-cover {
    height: 160px;
  }
  
  .category-list {
    gap: 10px;
    padding: 0 15px;
  }
  
  .category-item {
    padding: 6px 12px;
    font-size: 13px;
  }
}
</style>