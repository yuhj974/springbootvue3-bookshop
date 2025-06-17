<template>
  <div class="header-container">
    <Header />
  </div>
  <div class="book-detail-container">
    <div class="back-button-container">
    <el-button 
      plain
      icon="ArrowLeft" 
      @click="handleBack"
      class="back-button"
    >
      返回
    </el-button>
  </div>
    <!-- 左侧书本详情 -->
    <div class="book-info">
      <el-card class="book-card">
        
        <div class="book-cover-container">
          <el-image
            :src="bookDetail.coverImage"
            fit="contain"
            class="book-cover"
          >
            <template #error>
              <div class="image-error">图片加载失败</div>
            </template>
          </el-image>
        </div>
        <div class="book-meta">
          <h1 class="title">{{ bookDetail.title }}</h1>
          <div class="author">作者：{{ bookDetail.author }}</div>
          <div class="price">价格：¥{{ bookDetail.price?.toFixed(2) }}</div>
          <div class="stock">库存：{{ bookDetail.stock }} 本</div>
          <div class="category">分类：{{ bookDetail.categoryName }}</div>
          <!-- 数量选择器 -->
          <div class="quantity-selector">
            <span class="label">数量：</span>
            <el-button
              @click="decrement"
              class="quantity-btn"
              :disabled="bookDetail.stock === 0"
            >
              -
            </el-button>
            <el-input
              v-model.number="quantity"
              type="number"
              min="1"
              :max="bookDetail.stock"
              @change="setQuantity"
              :disabled="bookDetail.stock === 0"
              class="quantity-input"
            ></el-input>
            <el-button
              @click="increment"
              class="quantity-btn"
              :disabled="bookDetail.stock === 0"
            >
              +
            </el-button>
          </div>
          <div class="action-buttons">
            <el-button
              type="danger"
              size="large"
              @click="handleBuy"
              :disabled="bookDetail.stock === 0"
              class="buy-button"
            >
              立即购买
            </el-button>
            <el-button
              type="warning"
              size="large"
              @click="addToCart"
              :disabled="bookDetail.stock === 0 || isInCart"
              class="cart-button"
            >
              {{ isInCart ? '已加入购物车' : '加入购物车' }}
            </el-button>
          </div>
          <!-- 详情描述移到购买按钮下方 -->
          <div class="description">
            <h3 class="description-title">详情描述</h3>
            <p class="description-content">{{ bookDetail.description }}</p>
          </div>
        </div>
      </el-card>
      <el-dialog
        v-model="showAddressDialog"
        title="选择收货地址"
        width="40%"
      >
        <div class="address-header">
          <el-button 
            type="primary" 
            size="small"
            @click="showAddAddressDialog = true"
          >
            新增地址
          </el-button>
        </div>
        <el-radio-group v-model="selectedAddress">
          <div
            v-for="address in addressList"
            :key="address.id"
            class="address-item"
          >
            <el-radio :label="address.id">
              <div class="address-info">
                <div class="header">
                  <span class="name">{{ address.consignee }}</span>
                  <span class="phone">{{ address.phone }}</span>
                  <el-tag
                    v-if="address.isDefault"
                    size="mini"
                    type="danger"
                    class="default-tag"
                  >
                    默认
                  </el-tag>
                </div>
                <div class="detail">
                  {{ address.province }}{{ address.city }}
                  {{ address.district }} {{ address.detailAddress }}
                </div>
              </div>
            </el-radio>
          </div>
        </el-radio-group>
        <template #footer>
          <el-button @click="showAddressDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmOrder">立即下单</el-button>
        </template>
      </el-dialog>
      <!-- 新增地址对话框 -->
      <el-dialog
        v-model="showAddAddressDialog"
        title="新增地址"
        width="500px"
      >
        <el-form :model="newAddressForm">
          <el-form-item label="收货人" required>
            <el-input v-model="newAddressForm.consignee" placeholder="请输入收货人姓名" />
          </el-form-item>
          <el-form-item label="联系电话" required>
            <el-input v-model="newAddressForm.phone" placeholder="请输入11位手机号码" />
          </el-form-item>
          <el-form-item label="详细地址" required>
            <el-input 
              v-model="newAddressForm.detailAddress" 
              type="textarea"
              placeholder="请输入详细地址信息"
              :rows="3"
            />
          </el-form-item>
          <el-form-item label="设为默认">
            <el-switch v-model="newAddressForm.isDefault" :active-value="1" :inactive-value="0" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showAddAddressDialog = false">取消</el-button>
          <el-button type="primary" @click="submitNewAddress">保存地址</el-button>
        </template>
      </el-dialog>
    </div>
    <!-- 右侧评论区域 -->
    <div class="review-section">
      <el-card>
        <template #header>
          <span>用户评论（{{ reviews.length }}条）</span>
        </template>
        <!-- 发表评论 -->
        <div class="review-form">
          <el-input
            v-model="newReview.content"
            type="textarea"
            :rows="3"
            placeholder="写下你的书评..."
          />
          <div class="form-footer">
            <el-rate v-model="newReview.rating" />
            <el-button
              type="primary"
              @click="submitReview"
            >
              发表评论
            </el-button>
          </div>
        </div>
        <!-- 评论列表 -->
        <div
          v-for="review in reviews"
          :key="review.id"
          class="review-item"
        >
          <div class="review-header">
            <el-image
              :src="review.avatar || 'https://picsum.photos/200/200'"
              fit="cover"
              class="user-avatar"
              :style="{ '--avatar-size': '48px' }"
            >
              <template #error>
                <div class="avatar-placeholder">
                  <i class="fa fa-user-circle-o" aria-hidden="true"></i>
                </div>
              </template>
            </el-image>
            <div class="user-info">
              <span class="username">{{ review.name }}</span>
              <div class="review-meta">
                <el-rate
                  :model-value="review.rating"
                  disabled
                />
                <span class="review-time">{{ new Date(review.createTime).toLocaleString() }}</span>
              </div>
            </div>
          </div>
          <div class="review-content">{{ review.content }}</div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getBookById } from '@/api/book'
import { getReview, addReviews } from '@/api/review'
import { addCart, getCartByUserId } from '@/api/cart'
import { addOrder, getOrderByUserId, getOrderById,addOrder2 } from '@/api/order'
import { getAddress, addAddress } from '@/api/address'
import { getUserByJwt } from '@/api/user'
import { useCartStore } from '@/stores/cart'
import { ElMessage, ElLoading, ElMessageBox } from 'element-plus'
import Header from '@/components/Header.vue'
import { ArrowLeft } from '@element-plus/icons-vue'
const cartStore = useCartStore()
const route = useRoute()
const router = useRouter()
const bookId = route.params.id

// 书本详情数据
const bookDetail = ref({})
// 评论数据
const reviews = ref([])
// 新评论内容
const newReview = ref({
  content: '',
  rating: 0
})
// 购买数量
const quantity = ref(1)
// 地址相关状态
const showAddressDialog = ref(false)
const addressList = ref([])
const selectedAddress = ref(null)
// 新增地址相关状态
const showAddAddressDialog = ref(false)
const newAddressForm = ref({
  consignee: '',
  phone: '',
  detailAddress: '',
  isDefault: 0
})
// 购物车相关状态
const isInCart = ref(false)
let loadingInstance = null

// 检查用户是否登录
const checkLogin = () => {
  const token = JSON.parse(localStorage.getItem('logintoken'))?.token
  if (!token) {
    ElMessage.error('请先登录')
    router.push('/login')
    return false
  }
  return true
}
// 处理返回操作
const handleBack = () => {
  /* const previousRoute = router.options.history.state.back
  // 检查前一个路由是否是带搜索参数的图书列表页
  if (previousRoute && previousRoute.path === '/booklist' && previousRoute.query.keyword) {
    // 重定向到无参数的图书列表页
    router.replace({ path: '/booklist' })
  } else {
    // 正常返回
    router.back()
  } */
 router.back()
}
// 获取地址列表
const loadAddresses = async () => {
  try {
    if (!checkLogin()) return
    const res = await getAddress()
    addressList.value = res.data || []
    // 设置默认地址
    const defaultAddr = addressList.value.find(item => item.isDefault)
    if (defaultAddr) selectedAddress.value = defaultAddr.id
  } catch (error) {
    ElMessage.error('获取地址失败')
  }
}

// 获取书本详情
const loadBookDetail = async () => {
  try {
    const res = await getBookById(bookId)
    bookDetail.value = res.data || {}
    // 加载完成后检查是否已在购物车
    if (checkLogin()) {
      checkBookInCart()
    }
  } catch (error) {
    ElMessage.error('获取书本详情失败')
  }
}

// 获取评论
const loadReviews = async () => {
  try {
    const res = await getReview(bookId)
    reviews.value = res.data || []
  } catch (error) {
    ElMessage.error('获取评论失败')
  }
}

// 检查书籍是否已在购物车
const checkBookInCart = async () => {
  
    const userRes = await getUserByJwt()
    if (!userRes.data) return
    const userId = userRes.data.id
    const cartRes = await getCartByUserId(userId)
    const cartList = cartRes.data || []
    isInCart.value = cartList.some(item => item.bookId === bookDetail.value.id)
  } 

// 提交评论
const submitReview = async () => {
  if (!checkLogin()) return
  if (!newReview.value.content.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  if (!newReview.value.rating) {
    ElMessage.warning('请选择评分')
    return
  }
  try {
    const reviewData = {
      content: newReview.value.content,
      rating: newReview.value.rating
    }
    const res = await addReviews(bookId, reviewData)
    if (res.code === 1) {
      ElMessage.success('评论成功')
      // 重置评论内容和评分
      newReview.value = { content: '', rating: 0 }
      loadReviews()
    } else {
      console.log('错误详情:', res)
      await ElMessageBox.confirm('购买并确认收货后才能发表评论', '请先购买', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      })
    }
  } catch (error) {
    ElMessage.warning('评论失败')
  }
}

// 立即购买
const handleBuy = async () => {
  try {
    if (!checkLogin()) return
    // 先加载地址
    await loadAddresses()

    if (addressList.value.length === 0) {
      ElMessage.warning('请先添加收货地址')
      showAddAddressDialog.value = true
      return
    }

    // 显示地址选择对话框
    showAddressDialog.value = true
  } catch (error) {
    console.error('下单准备失败:', error)
  }
}

// 提交订单
const confirmOrder = async () => {
  if (!checkLogin()) return
  if (!selectedAddress.value) {
    ElMessage.warning('请选择收货地址')
    return
  }

  loadingInstance = ElLoading.service({
    lock: true,
    text: '正在创建订单...',
    background: 'rgba(0, 0, 0, 0.1)'
  })

  const totalAmount = (bookDetail.value.price * quantity.value).toFixed(2)

  const orderData = {
    bookId: bookDetail.value.id,
    quantity: quantity.value,
    orderItems: [{
      bookId: bookDetail.value.id,
      quantity: quantity.value
    }],
    addressId: selectedAddress.value,
    totalAmount: totalAmount
  }

  try {
    const res = await addOrder2(orderData)
    if (res.code === 1) {
      ElMessage.success('订单创建成功')
      showAddressDialog.value = false
      loadBookDetail()
      // 跳转到订单页面
      router.push({ name: 'OrderDetail', params: { id: res.data.id } })
    } else {
      ElMessage.error(res.msg || '下单失败')
    }
  } catch (error) {
    
  } finally {
    loadingInstance.close()
  }
}

// 新增地址方法
const submitNewAddress = async () => {
  // 简单前端验证
  if (!newAddressForm.value.consignee) {
    ElMessage.warning('请输入收货人姓名')
    return
  }
  if (!/^1[3-9]\d{9}$/.test(newAddressForm.value.phone)) {
    ElMessage.warning('请输入正确的手机号码')
    return
  }
  if (!newAddressForm.value.detailAddress) {
    ElMessage.warning('请输入详细地址')
    return
  }

  
    const res = await addAddress(newAddressForm.value)
    if (res.code === 1) {
      ElMessage.success('地址添加成功')
      // 清空表单内容
      newAddressForm.value = {
        consignee: '',
        phone: '',
        detailAddress: '',
        isDefault: 0
      }
      showAddAddressDialog.value = false
      await loadAddresses() // 重新加载地址列表
      // 自动选中新添加的地址
      selectedAddress.value = res.data.id
    } else {
      ElMessage.error(res.msg || '添加地址失败')
    }
  } 


// 加入购物车
const addToCart = async () => {
  if (!checkLogin()) return
  
  try {
    loadingInstance = ElLoading.service({
      lock: true,
      text: '正在添加到购物车...',
      background: 'rgba(0, 0, 0, 0.1)'
    })
    
    const res = await addCart({
      bookId: bookDetail.value.id,
      quantity: quantity.value
    })
    
    if (res.code === 1) {
      ElMessage.success('加入购物车成功')
      isInCart.value = true
      // 更新全局购物车数量
      await cartStore.fetchCartCount()
    } else {
      ElMessage.error(res.msg || '加入购物车失败')
    }
  } catch (error) {
    console.error('加入购物车异常:', error)
    ElMessage.error('网络异常，请稍后重试')
  } finally {
    loadingInstance.close()
  }
}

// 获取购物车项目（更新购物车状态）
const fetchCartItems = async () => {
  try {
    await cartStore.loadCartItems()
  } catch (error) {
    console.error('更新购物车失败:', error)
  }
}

// 增加数量
const increment = () => {
  if (quantity.value < bookDetail.value.stock) {
    quantity.value++
  } else {
    ElMessage.warning('不能超过库存数量')
  }
}

// 减少数量
const decrement = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

// 设置数量
const setQuantity = (value) => {
  if (isNaN(value) || value < 1) {
    quantity.value = 1
  } else if (value > bookDetail.value.stock) {
    quantity.value = bookDetail.value.stock
    ElMessage.warning('不能超过库存数量')
  } else {
    quantity.value = value
  }
}

// 监听登录状态变化，更新购物车状态
watch(() => localStorage.getItem('logintoken'), async (newVal) => {
  if (newVal && bookDetail.value.id) {
    await checkBookInCart()
  }
})
/* // 使用 watch 监听路由变化，并判断是否带有 keyword 参数
watch(
  () => route.query.keyword,
  (newKeyword) => {
    if (newKeyword) {
      // 如果存在 keyword，则跳转去掉参数
      router.replace({ query: {} });
    }
  },
  { immediate: true }
); */

onMounted(() => {
  loadBookDetail()
  loadReviews()
})
</script>

<style scoped>
/* 全局容器样式 */
.header-container {
  background: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.back-button-container {
  max-width: 1200px;
  margin: 15px auto 0;
 /*  padding: 50px 0 0 0; */
}

.back-button {
  /* background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%); */
  background: #fff;
  color: #0e67c0;
  border: none;
  border-radius: 50px;
  /* box-shadow: 0 4px 6px rgba(37, 117, 252, 0.3); */
  transition: all 0.3s ease;
  padding: 10px 20px;
  font-size: 20px;
}

/* .back-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(37, 117, 252, 0.4);
} */

.book-detail-container {
  display: flex;
  max-width: 1200px;
  margin: 20px auto;
  padding: 50px 20px;
  gap: 30px;
}

/* 左侧图书信息区域 */
.book-info {
  flex: 1;
}

.book-card {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  border: none;
  transition: transform 0.3s ease;
}

.book-card:hover {
  transform: translateY(-5px);
}

.book-cover-container {
  display: flex;
  justify-content: center;
  padding: 30px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7eb 100%);
}

.book-cover {
  width: 100%;
  height: 100%;
  border-radius: 8px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  transition: transform 0.4s ease;
}

.book-cover:hover {
  transform: scale(1.03);
}

.image-error {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: #f0f2f5;
  color: #909399;
  font-size: 16px;
}

.book-meta {
  padding: 25px;
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: #1d2129;
  margin-bottom: 15px;
  line-height: 1.3;
  border-bottom: 1px solid #eee;
  padding-bottom: 15px;
}

.author, .price, .stock, .category {
  font-size: 16px;
  color: #4e5969;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
}

.author::before, .price::before, .stock::before, .category::before {
  content: "•";
  color: #409eff;
  margin-right: 8px;
  font-size: 20px;
}

.price {
  color: #ff6b6b;
  font-weight: 600;
  font-size: 18px;
}

.stock {
  color: #00c853;
  font-weight: 500;
}

/* 数量选择器 */
.quantity-selector {
  display: flex;
  align-items: center;
  margin: 25px 0;
}

.label {
  font-size: 16px;
  color: #606266;
  margin-right: 15px;
}

.quantity-btn {
  width: 36px;
  height: 36px;
  font-size: 18px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: #f0f7ff;
  color: #409eff;
  border: 1px solid #d9ecff;
}

.quantity-btn:hover {
  background: #409eff;
  color: #fff;
}

.quantity-input {
  width: 60px;
  margin: 0 10px;
}

.quantity-input :deep(.el-input__inner) {
  text-align: center;
  font-weight: 600;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 20px;
  margin: 30px 0;
}

.buy-button {
  flex: 1;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #ff6b6b 0%, #ff8e8e 100%);
  border: none;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.3);
  transition: all 0.3s ease;
}

.buy-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(255, 107, 107, 0.4);
}

.cart-button {
  flex: 1;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #ffc107 0%, #ffd54f 100%);
  border: none;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(255, 193, 7, 0.3);
  color: #333;
  transition: all 0.3s ease;
}

.cart-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(255, 193, 7, 0.4);
}

.cart-button:disabled {
  background: #f0f0f0;
  color: #a8a8a8;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

/* 详情描述 */
.description {
  background: #f9fafc;
  border-radius: 8px;
  padding: 20px;
  margin-top: 25px;
  border-left: 4px solid #409eff;
}

.description-title {
  font-size: 20px;
  color: #1d2129;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.description-title::before {
  content: "";
  display: inline-block;
  width: 4px;
  height: 20px;
  background: #409eff;
  border-radius: 2px;
  margin-right: 10px;
}

.description-content {
  font-size: 15px;
  line-height: 1.8;
  color: #4e5969;
  text-align: justify;
}

/* 右侧评论区域 */
.review-section {
  flex: 0 0 400px;
}

.review-section .el-card {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.review-section :deep(.el-card__header) {
  background: linear-gradient(135deg, #f0f7ff 0%, #d9ecff 100%);
  border-bottom: 1px solid #e4e7ed;
  padding: 16px 20px;
  font-size: 18px;
  font-weight: 600;
  color: #1d2129;
}

.review-form {
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.form-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
}

.form-footer .el-button {
  padding: 10px 25px;
}

.review-item {
  padding: 20px;
  border-bottom: 1px solid #f0f2f5;
  transition: background 0.3s;
}

.review-item:hover {
  background: #f9fafc;
}

.review-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.user-avatar {
  width: var(--avatar-size);
  height: var(--avatar-size);
  border-radius: 50%;
  overflow: hidden;
  margin-right: 15px;
  border: 2px solid #e4e7ed;
}

.avatar-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  background: #f0f2f5;
  color: #c0c4cc;
  font-size: 24px;
}

.user-info {
  flex: 1;
}

.username {
  font-weight: 600;
  color: #1d2129;
  display: block;
  margin-bottom: 5px;
}

.review-meta {
  display: flex;
  align-items: center;
  gap: 15px;
}

.review-time {
  font-size: 13px;
  color: #909399;
}

.review-content {
  font-size: 14px;
  line-height: 1.7;
  color: #4e5969;
  padding-left: 63px; /* 与头像对齐 */
}

/* 地址对话框样式 */
.address-header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 15px;
}

.address-item {
  padding: 15px;
  margin-bottom: 12px;
  border-radius: 8px;
  border: 1px solid #ebeef5;
  transition: all 0.3s;
  cursor: pointer;
}

.address-item:hover {
  border-color: #409eff;
  background: #f5f9ff;
}

.address-info .header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.name {
  font-weight: 600;
  margin-right: 10px;
}

.phone {
  color: #606266;
  margin-right: 15px;
}

.default-tag {
  margin-left: auto;
}

.detail {
  color: #606266;
  line-height: 1.5;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .book-detail-container {
    flex-direction: column;
  }
  
  .review-section {
    flex: 1;
    width: 100%;
    margin-top: 30px;
  }
  
  .book-cover {
    width: 220px;
    height: 300px;
  }
}

@media (max-width: 768px) {
  .action-buttons {
    flex-direction: column;
  }
  
  .book-cover-container {
    padding: 20px 10px;
  }
  
  .review-content {
    padding-left: 0;
  }
}
</style>