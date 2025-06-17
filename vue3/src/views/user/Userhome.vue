<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElCarousel, ElCarouselItem, ElButton } from 'element-plus'
import { Search, ArrowRight } from '@element-plus/icons-vue'
import { getRecommendBooks } from '@/api/book'  
import { listCarousels } from '@/api/carousel'  
import { useRouter } from 'vue-router'
import { getCartByUserId, addCart } from '@/api/cart'  
import Header from '@/components/Header.vue'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart' 

const router = useRouter() 
const searchKeyword = ref('')
const userStore = useUserStore()
const cartStore = useCartStore()

// 轮播图相关
const carousels = ref([])
const carouselHeight = ref("400px")
const recommendedBooks = ref([])
const cartStatus = ref({})


// 获取轮播图数据
const loadCarousels = async () => {
  const res = await listCarousels({ status: 1 })
  carousels.value = res.data?.rows || []
  if (!carousels.value.length) ElMessage.warning('暂无轮播图数据')
}

// 获取推荐图书
const loadRecommendedBooks = async () => {
  const res = await getRecommendBooks({ count: 12 })
  recommendedBooks.value = res.data || []
  
  recommendedBooks.value.forEach(book => {
    book.price = parseFloat(book.price) || 0
  })
  
  if (localStorage.getItem('logintoken')) {
    checkCartStatus()
  }
}

// 检查图书在购物车中的状态
const checkCartStatus = async () => {
  const res = await getCartByUserId()
  const cartItems = res.data || []
  
  const cartBookIds = cartItems.reduce((map, item) => {
    map[item.bookId] = true
    return map
  }, {})
  
  recommendedBooks.value.forEach(book => {
    cartStatus.value[book.id] = cartBookIds[book.id] || false
  })
  
  const totalQuantity = cartItems.reduce((total, item) => total + item.quantity, 0)
  cartStore.updateCount(totalQuantity)
}

// 搜索处理
const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({
      path: '/booklist',
      query: { keyword: searchKeyword.value.trim() } // 添加关键字参数
    })
  } else {
    router.push('/booklist')
  }
}

// 跳转到所有图书页面
const goToAllBooks = () => {
  router.push('/booklist')
}

// 跳转到图书详情 
const goToBookDetail = (bookId) => {
  if (bookId) {
    // 先替换当前路由为全部图书页面
    router.replace({ path: '/booklist' })
    // 然后跳转到详情页
    router.push(`/detail/${bookId}`)
  }
}

// 处理登出
const handleLogout = () => {
  localStorage.removeItem('logintoken')
  userStore.logout()
  cartStatus.value = {}
  cartStore.updateCount(0)
}

// 加入购物车
const addToCart = async (bookId) => {
  const tokenInfo = JSON.parse(localStorage.getItem('logintoken'))
  
  if (!tokenInfo?.token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  const res = await addCart({ bookId, quantity: 1 })
  
  if (res.code === 1) {
    ElMessage.success('已加入购物车')
    cartStatus.value[bookId] = true
    cartStore.updateCount(cartStore.count + 1)
  } else if (res.code === 401) {
    handleLogout()
    ElMessage.error('登录已过期，请重新登录')
    router.push('/login')
  } else {
    ElMessage.error(res.message || '加入购物车失败')
  }
}


// 初始化加载
onMounted(() => {
  loadCarousels()
  loadRecommendedBooks()
  
  // const handleResize = () => {
  //   carouselHeight.value = window.innerWidth < 768 ? '200px' : '400px'
  // }
  // handleResize()
  // window.addEventListener('resize', handleResize)
})
</script>

<template>
  <el-container class="container">
    <Header />
    <el-main class="main-content">
      <div class="content-wrapper">
        <!-- 搜索框 -->
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
              <el-button @click="handleSearch" class="search-button">
                <el-icon><Search /></el-icon>搜索
              </el-button>
            </template>
          </el-input>
        </div>
        
        <!-- 轮播图区域 -->
        <div class="banner-container">
          <el-carousel 
            :height="carouselHeight" 
            indicator-position="outside" 
            arrow="always"
            class="home-carousel"
          >
            <el-carousel-item 
              v-for="item in carousels" 
              :key="item.id"
              @click="item.bookId && goToBookDetail(item.bookId)"
              :style="item.bookId ? 'cursor: pointer;' : ''"
              class="carousel-item"
            >
              <div class="image-container">
                <img 
                  :src="item.imageUrl" 
                  class="banner-image" 
                  :alt="item.title"
                />
              </div>
              <!-- 标题默认隐藏，鼠标滑过显示 -->
              <div 
                class="carousel-title" 
                v-if="item.title"
                :style="item.bookId ? 'cursor: pointer;' : ''"
              >
                <div class="title-bg"></div>
                <div class="title-text">{{ item.title }}</div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
        
        <!-- 推荐图书标题区域 -->
        <div class="section-header">
          <h2>推荐好书</h2>
          <el-button 
            type="primary" 
            plain 
            @click="goToAllBooks"
            class="view-all-btn"
          >
            查看全部图书 <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
        
        <!-- 推荐图书列表 -->
        <div class="book-list">
          <el-row :gutter="20" class="book-grid">
            <el-col 
              v-for="book in recommendedBooks" 
              :key="book.id"
              :xs="12" :sm="8" :md="6" :lg="4" :xl="3"
              class="book-col"
            >
              <el-card class="book-card" shadow="hover">
                <div class="card-content" @click="goToBookDetail(book.id)">
                  <img 
                    :src="book.coverImage" 
                    class="book-cover" 
                    :alt="book.title"
                    onerror="this.src='https://via.placeholder.com/150x200?text=No+Image'"
                  />
                  <div class="book-title">{{ book.title }}</div>
                  <div class="book-author">{{ book.author }}</div>
                </div>
                <div class="book-footer">
                  <span class="book-price">¥{{ book.price.toFixed(2) }}</span>
                  <el-button 
                    :disabled="cartStatus[book.id]"
                    @click.stop="addToCart(book.id)"
                    size="small"
                    :type="cartStatus[book.id] ? 'success' : 'primary'"
                  >
                    {{ cartStatus[book.id] ? '已加入购物车' : '加入购物车' }}
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

/* 轮播图标题美化 - 修改部分 */
.carousel-title {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20px;
  z-index: 2;
  display: flex;
  justify-content: center;
  opacity: 0; /* 默认隐藏标题 */
  transform: translateY(10px); /* 向下偏移 */
  transition: all 0.3s ease; /* 添加过渡效果 */
}

.carousel-item:hover .carousel-title {
  opacity: 1; /* 鼠标滑过时显示标题 */
  transform: translateY(0); /* 恢复位置 */
}

.title-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
  z-index: -1;
}

.title-text {
  color: white;
  font-size: 24px;
  font-weight: 600;
  text-align: center;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
  padding: 10px 20px;
  border-radius: 8px;
  max-width: 80%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 添加悬停效果 */
.banner-image:hover {
  opacity: 0.95;
  cursor: pointer;

}

/* 响应式设计 */
/* @media (max-width: 992px) {
  .title-text {
    font-size: 20px;
    padding: 8px 15px;
  }
}

@media (max-width: 768px) {
  .title-text {
    font-size: 18px;
    padding: 6px 12px;
  }
}

@media (max-width: 576px) {
  .title-text {
    font-size: 16px;
    padding: 5px 10px;
  }
} */
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

/* 加大的搜索框样式 */
.search-box-container {
  margin: 30px 0;
  text-align: center;
  padding: 30px 0 0 0;
 
}

.large-search {
  width: 100%;
  /* max-width: 800px; */
  margin: 0 auto;
}

.search-button {
  height: 40px;
  padding: 0 24px;
  font-size: 16px;
  border-radius: 20px;
  background-color: blue;
  
}

:deep(.large-search .el-input__inner) {
  height: 50px;
  padding: 0 15px;
  font-size: 16px;
}

:deep(.large-search .el-input__prefix, .large-search .el-input__append) {
  line-height: 50px;
}

/* 轮播图样式 - 修改部分 */
.banner-container {
  margin: 10px 0;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  position: relative;
}

.home-carousel {
  border-radius: 10px;
}

/* 新增图片容器 */
.image-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa; /* 添加背景色 */
}

.banner-image {
  /* 确保图片完整填充容器 */
  width: 100%;
  height: 100%;
  background-size: contain;
  background-size: cover;
  background-size: 100% 100%;
 
  
  /* background-position: center;
  background-repeat: no-repeat; */
}

/* 响应式设计 */
@media (max-width: 992px) {
  .banner-image {
    object-fit: contain; /* 在小屏幕上完整显示 */
  }
}

/* 推荐图书标题区域 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 40px 0 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #eee;
}

.section-header h2 {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.view-all-btn {
  font-weight: normal;
}

/* 图书列表样式 */
.book-list {
  padding-bottom: 50px;
}

.book-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.book-col {
  padding: 0;
}

/* 图书卡片样式 */
.book-card {
  height: 100%;
  transition: all 0.3s ease;
  border-radius: 8px;
  overflow: hidden;
  border: none;
}

.book-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

.card-content {
  cursor: pointer;
}

.book-cover {
  width: 100%;
  height: 220px;
  object-fit: cover;
  border-radius: 6px;
  display: block;
  margin: 0 auto;
  background: #f5f7fa;
}

.book-title {
  font-size: 16px;
  font-weight: 500;
  margin: 12px 0 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #1f2937;
  text-align: center;
}

.book-author {
  font-size: 13px;
  color: #6b7280;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-align: center;
}

.book-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 5px 0;
  border-top: 1px solid #f0f0f0;
}

.book-price {
  color: #e53935;
  font-weight: 600;
  font-size: 16px;
}

.added-to-cart {
  background-color: #67c23a !important;
  border-color: #67c23a !important;
  color: white !important;
  cursor: not-allowed !important;
}

.added-to-cart:hover {
  background-color: #67c23a !important;
  border-color: #67c23a !important;
  color: white !important;
}


</style>    