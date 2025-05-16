<template>
  <div class="header-container">
    <Header />
  </div>
  <div class="book-detail-container">
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
              :disabled="bookDetail.stock === 0"
              class="cart-button"
            >
              加入购物车
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
                  show-score
                  score-template="{value} 分"
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
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useRouter } from 'vue-router'
import { getBookById } from '@/api/book'
import { getReview, addReviews } from '@/api/review'
import { ElMessage, ElLoading,ElMessageBox } from 'element-plus'
import { addCart, getCartByUserId } from '@/api/cart'
import Header from '@/components/Header.vue'
import { addOrder,getOrderByUserId,getOrderById } from '@/api/order'
import { getAddress,addAddress } from '@/api/address'
import {getUserByJwt} from '@/api/user'

const route = useRoute()
const bookId = route.params.id
 const router = useRouter() // 移动到函数内部获取路由实例
// 新增地址相关状态
const showAddressDialog = ref(false)
const addressList = ref([])
const selectedAddress = ref(null)

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

// 新增状态
const showAddAddressDialog = ref(false)
const newAddressForm = ref({
  consignee: '',
  phone: '',
  detailAddress: '',
  isDefault: 0
})


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
      
    }
    
    
  } 


const checkLogin = () => {
  const token = JSON.parse(localStorage.getItem('logintoken'))?.token
  if (!token) {
    ElMessage.error('请先登录')
    router.push('/login')
    return false
  }
  return true
}

// 获取地址列表
const loadAddresses = async () => {
  try {
    if (!checkLogin()) return
    const res = await getAddress()
    addressList.value = res.data
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
    bookDetail.value = res.data
  } catch (error) {
    ElMessage.error('获取书本详情失败')
  }
}

// 获取评论
const loadReviews = async () => {
  try {
    const res = await getReview(bookId)
    reviews.value = res.data
  } catch (error) {
    ElMessage.error('获取评论失败')
  }
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
      //ElMessage.warning(res.msg)
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

  const loadingInstance = ElLoading.service({
    lock: true,
    text: '正在创建订单...',
    background: 'rgba(0, 0, 0, 0.1)'
  })

  // 新增总价计算
  const totalAmount = (bookDetail.value.price * quantity.value).toFixed(2)

  const orderData = {
    bookId: bookDetail.value.id,
    quantity: quantity.value,
    // 新增订单项列表
    orderItems: [{
      bookId: bookDetail.value.id,
      quantity: quantity.value
    }],
    addressId: selectedAddress.value,
    totalAmount: (bookDetail.value.price * quantity.value).toFixed(2)
  };

  const res = await addOrder(orderData);
  loadingInstance.close()

  if (res.code === 1) {
   
    ElMessage.success('订单创建成功')
    showAddressDialog.value = false
    loadBookDetail()
    // 这里可以跳转到订单页面
  } else {
    ElMessage.error(res.msg || '下单失败')
  }
}

// 加入购物车
const addToCart = async () => {
  try {
    if (!checkLogin()) return
    // 获取当前登录用户
    const userRes = await getUserByJwt()
    if (!userRes.data) {
      ElMessage.warning('请先登录')
      return
    }
    const userId = userRes.data.id

    // 获取用户购物车
    const cartRes = await getCartByUserId(userId)
    const cartList = cartRes.data || []

    // 检查是否已存在
    const isBookInCart = cartList.some(item => item.bookId === bookDetail.value.id)
    if (isBookInCart) {
      ElMessage.warning('该书籍已在购物车中，请在购物车中增加数量')
      return
    }

    const loadingInstance = ElLoading.service({
      lock: true,
      text: '正在添加到购物车...',
      background: 'rgba(0, 0, 0, 0.1)'
    })

    // 调用API
    const res = await addCart({
      bookId: bookDetail.value.id, // 注意这里要用.value
      quantity: quantity.value,
      userId: userId // 需要传递用户ID
    })

    loadingInstance.close()

    if (res.code === 1) {
      ElMessage.success('加入购物车成功')
    } else {
      ElMessage.error(res.msg || '加入购物车失败') // 注意使用正确的结果字段
    }
  } catch (error) {
    console.error('加入购物车异常:', error)
    ElMessage.error('网络异常，请稍后重试')
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
const setQuantity = (e) => {
  let value = parseInt(e.target.value)
  if (isNaN(value) || value < 1) {
    quantity.value = 1
  } else if (value > bookDetail.value.stock) {
    quantity.value = bookDetail.value.stock
    ElMessage.warning('不能超过库存数量')
  } else {
    quantity.value = value
  }
}

onMounted(() => {
  loadBookDetail()
  loadReviews()
})
</script>

<style scoped>
.book-detail-container {
  display: flex;
  gap: 20px;
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
  margin-top: 20px;
}

.book-info {
  flex: 0 0 400px;
}

.review-section {
  flex: 1;
}

.book-card {
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.book-cover-container {
  display: flex;
  justify-content: center;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px 8px 0 0;
}

.book-cover {
  max-height: 400px;
  max-width: 100%;
  object-fit: contain;
  border-radius: 4px;
  transition: transform 0.3s ease;
}

.book-cover:hover {
  transform: scale(1.02);
}

.book-meta {
  padding: 20px;
}

.title {
  margin: 0 0 15px;
  font-size: 24px;
  color: #333;
  font-weight: 600;
}

.author, .price, .stock, .category {
  margin: 8px 0;
  color: #666;
  line-height: 1.6;
}

.price {
  color: #f56c6c;
  font-size: 22px;
  font-weight: 600;
  margin: 15px 0;
}

.stock {
  color: #909399;
}

.category {
  color: #606266;
}

.quantity-selector {
  display: flex;
  align-items: center;
  margin: 20px 0;
}

.label {
  margin-right: 10px;
  color: #606266;
}

.quantity-btn {
  width: 32px;
  height: 32px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 4px 0 0 4px;
  background-color: #f5f7fa;
  border: 1px solid #dcdfe6;
  cursor: pointer;
  transition: background-color 0.2s;
}

.quantity-btn:hover {
  background-color: #e4e7ed;
}

.quantity-btn:last-child {
  border-radius: 0 4px 4px 0;
}

.quantity-input {
  width: 60px;
  height: 32px;
  text-align: center;
  border-radius: 0;
  border-left: none;
  border-right: none;
}

.action-buttons {
  margin-top: 25px;
  display: flex;
  gap: 15px;
}

.buy-button {
  flex: 1;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.buy-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(245, 108, 108, 0.2);
}

.cart-button {
  flex: 1;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.cart-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(230, 162, 60, 0.2);
}

.description {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.description-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
}

.description-content {
  color: #606266;
  line-height: 1.8;
  text-align: justify;
}

.review-form {
  margin-bottom: 30px;
}

.form-footer {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.review-item {
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.review-item:last-child {
  border-bottom: none;
}

.review-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  gap: 12px;
}

.user-avatar {
  width: var(--avatar-size);
  height: var(--avatar-size);
  border-radius: 50%;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  flex-shrink: 0;
}

.user-avatar:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  background-color: #f0f2f5;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 24px;
}

.user-info {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  min-width: 0;
}

.username {
  font-weight: 600;
  color: #303133;
  margin-bottom: 2px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.review-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #909399;
  font-size: 13px;
}

.review-content {
  color: #606266;
  line-height: 1.6;
  margin-top: 8px;
  white-space: pre-wrap;
}

.header-container {
  margin-bottom: 80px;
}
.address-item {
  padding: 10px;
  margin: 5px 0;
  border-radius: 4px;
  border: 1px solid #eee;
  transition: all 0.3s;
}

.address-item:hover {
  border-color: var(--el-color-primary);
}

.address-info {
  padding-left: 10px;
}

.header {
  margin-bottom: 5px;
}

.name {
  font-weight: 600;
  margin-right: 15px;
}

.phone {
  color: #666;
  margin-right: 15px;
}

.detail {
  color: #888;
  font-size: 14px;
}

.default-tag {
  vertical-align: middle;
  margin-left: 10px;
}
</style>