<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserByJwt, updatePassword,updateUser } from '@/api/user'
import { getReviewByUserId } from '@/api/review'
import { getOrderByUserId, completeOrder, getOrderById } from '@/api/order'
import { getAddress, updateAddress, getAddressById, deleteAddress, addAddress } from '@/api/address'
import Header from '@/components/Header.vue'



// 响应式数据
const currentTab = ref('info')
const userInfo = ref({})
const reviews = ref([])
const addresses = ref([])
const orders = ref([])
const showAddressDialog = ref(false)
const showOrderDetailDialog = ref(false)
const orderDetails = ref([])
const showUserEditDialog = ref(false)

const userForm = ref({
  name: '',
  email: '',
  phone: '',
  avatar: ''
})

const addressForm = ref({
  id: null,
  consignee: '',
  phone: '',
  detailAddress: '',
  isDefault: 0
})

const passwordForm = ref({
  password: '',
  newPassword: '',
  confirmPassword: ''
})

// 计算属性
const filteredUserInfo = computed(() => {
  const { password, status, createTime, updateTime, role, newPassword, token,avatar, ...rest } = userInfo.value
  return rest
})

const keyLabels = {
  username: '用户名',
  email: '邮箱',
  phone: '手机号',
  name: '姓名'
}

const statusTagType = {
  '已付款': 'warning',
  '已发货': 'success',
  '已完成': ''
}

// 方法
const loadUserInfo = async () => {
  const res = await getUserByJwt()
  userInfo.value = res.data
}

const loadReviews = async () => {
  const res = await getReviewByUserId()
  reviews.value = res.data
}

const loadAddresses = async () => {
  const res = await getAddress()
  addresses.value = res.data
}

const loadOrders = async () => {
  const res = await getOrderByUserId()
  orders.value = res.data
}

// 用户信息编辑功能
const handleAvatarSuccess = (res) => {
  if (res.code) {
    userForm.value.avatar = res.data
    ElMessage.success('头像上传成功')
  }
  
}

const openEditDialog = () => {
  userForm.value = {
    id: userInfo.value.id, // 关键：添加用户ID
    name: userInfo.value.name || '',
    email: userInfo.value.email || '',
    phone: userInfo.value.phone || '',
    avatar: userInfo.value.avatar || ''
  }
  showUserEditDialog.value = true
}


const submitUserForm = async () => {
  try {
    console.log('提交的用户数据:', JSON.stringify(userForm.value))
    await updateUser(userForm.value)
    ElMessage.success('修改成功')
    loadUserInfo()
    showUserEditDialog.value = false
  } catch (error) {
    console.error('修改失败:', error)
    ElMessage.error(error.message || '修改失败')
  }
}

const handleAddress = async (type, id) => {
  if (type === 'edit') {
    const res = await getAddressById(id)
    addressForm.value = res.data
    showAddressDialog.value = true
  } else if (type === 'delete') {
    try {
      await ElMessageBox.confirm('确定删除该地址吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      await deleteAddress([id])
      ElMessage.success('删除成功')
      loadAddresses()
    } catch (error) {
      // 用户点击取消不执行操作
    }
  } else {
    addressForm.value = {
      id: null,
      consignee: '',
      phone: '',
      detailAddress: '',
      isDefault: 0
    }
    showAddressDialog.value = true
  }
}

const submitAddress = async () => {
  try {
    if (addressForm.value.id) {
      await updateAddress(addressForm.value)
    } else {
      await addAddress(addressForm.value)
    }
    ElMessage.success('操作成功')
    loadAddresses()
    showAddressDialog.value = false
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const showOrderDetail = async (orderId) => {
  try {
    const res = await getOrderById(orderId)
    orderDetails.value = res.data
    showOrderDetailDialog.value = true
  } catch (error) {
    ElMessage.error('获取订单明细失败')
  }
}

const handleReceive = async (id) => {
  try {
    await ElMessageBox.confirm('确定已收到商品吗？', '确认收货', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await completeOrder(id)
    ElMessage.success('收货成功')
    loadOrders()
  } catch (error) {
    // 用户点击取消不执行操作
  }
}

const changePassword = async () => {
  try {
    if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
      throw new Error('两次输入密码不一致')
    }
    if  (passwordForm.value.password === '') {
      throw new Error('请输入旧密码')
    }
   const res=  await updatePassword({
      password: passwordForm.value.password,
      newPassword: passwordForm.value.newPassword
    })
    if(res.code){
      ElMessage.success('密码修改成功')
    }
    
    passwordForm.value = {
      password: '',
      newPassword: '',
      confirmPassword: ''
    }
  } catch (error) {
    ElMessage.error(error.message || '密码修改失败')
  }
}

// 生命周期钩子
onMounted(() => {
  loadUserInfo()
  loadReviews()
  loadAddresses()
  loadOrders()
})
</script>

<template>
 <Header />
  <div class="personal-container">
    <div class="personal-header">
      <h1 class="personal-title">个人中心</h1>
      <div class="user-avatar">
        <el-avatar :size="80" :src="userInfo.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
      </div>
    </div>

    <el-tabs v-model="currentTab" class="custom-tabs">
      <!-- 个人信息 -->
      <el-tab-pane label="个人中心" name="info">
        <el-card class="info-card">
          <template #header>
            <div class="card-header">
              <span>个人信息</span>
              <el-button type="primary" size="small" @click="openEditDialog">编辑资料</el-button>
            </div>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item 
              v-for="(value, key) in filteredUserInfo" 
              :key="key"
              :label="keyLabels[key] || key"
              label-class-name="desc-label"
            >
              {{ value || '-' }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-tab-pane>

      <!-- 用户信息编辑对话框 -->
    <el-dialog v-model="showUserEditDialog" title="编辑个人信息" width="600px">
      <el-form 
        :model="userForm" 
        label-width="80px" 
        label-position="left"
        :rules="{
          name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
          email: [
            { required: true, message: '请输入邮箱', trigger: 'blur' },
            { type: 'email', message: '请输入正确的邮箱格式', trigger: ['blur', 'change'] }
          ],
          phone: [
            { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
          ]
        }"
      >
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            action="/api/files/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
          >
            <img v-if="userForm.avatar" :src="userForm.avatar" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name" placeholder="请输入姓名" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>

        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入手机号" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showUserEditDialog = false">取消</el-button>
        <el-button type="primary" @click="submitUserForm">保存修改</el-button>
      </template>
    </el-dialog>


      <!-- 我的评论 -->
      <el-tab-pane label="我的评论" name="reviews">
        <el-card>
          <el-table :data="reviews" style="width: 100%" stripe>
            <el-table-column prop="title" label="书籍名称" width="150" />
            <el-table-column prop="content" label="评论内容"  />
            <el-table-column label="评分" width="200">
              <template #default="{ row }">
                <el-rate v-model="row.rating" disabled show-score />
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="时间" width="180" />
          </el-table>
        </el-card>
      </el-tab-pane>

      <!-- 我的地址 -->
      <el-tab-pane label="我的地址" name="address">
        <el-card>
          <div class="table-actions">
            <el-button type="primary" icon="Plus" @click="handleAddress('add')">新增地址</el-button>
          </div>
          <el-table :data="addresses" style="width: 100%">
            <el-table-column prop="consignee" label="收货人" width="120" />
            <el-table-column prop="phone" label="电话" width="150" />
            <el-table-column prop="detailAddress" label="详细地址" />
            <el-table-column label="默认" width="80">
              <template #default="{ row }">
                <el-tag v-if="row.isDefault" type="success">默认</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button size="small" icon="Edit" @click="handleAddress('edit', row.id)" />
                <el-button type="danger" size="small" icon="Delete" @click="handleAddress('delete', row.id)" />
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <!-- 我的订单 -->
      <el-tab-pane label="我的订单" name="orders">
        <el-card>
          <el-table :data="orders" style="width: 100%" stripe>
            <el-table-column prop="orderNumber" label="订单号" width="220" />
            <el-table-column label="总金额" width="120">
              <template #default="{ row }">
                ￥{{ row.totalAmount.toFixed(2) }}
              </template>
            </el-table-column>
            <el-table-column label="状态" width="120">
              <template #default="{ row }">
                <el-tag :type="statusTagType[row.status]">
                  {{ row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="detailAddress" label="收货地址" />
              <el-table-column prop="consignee" label="收货人"  />
              <el-table-column prop="phone" label="电话" width="150" />
            <el-table-column label="操作" width="180">
              <template #default="{ row }">
                <el-button-group>
                  <el-button size="small" type="primary" @click="showOrderDetail(row.id)">
                    明细
                  </el-button>
                  <el-button 
                    v-if="row.status === '已发货'" 
                    type="success" 
                    size="small"
                    @click="handleReceive(row.id)"
                  >
                    收货
                  </el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <!-- 修改密码 -->
      <el-tab-pane label="修改密码" name="password">
        <el-card class="password-card">
          <el-form 
            :model="passwordForm" 
            label-width="100px"
            label-position="left"
            status-icon
          >
            <el-form-item label="原密码" prop="password">
              <el-input 
                v-model="passwordForm.password" 
                type="password"
                show-password
                placeholder="请输入原密码"
              />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input 
                v-model="passwordForm.newPassword" 
                type="password"
                show-password
                placeholder="至少3位字符"
              />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input 
                v-model="passwordForm.confirmPassword" 
                type="password"
                show-password
                placeholder="请再次输入新密码"
              />
            </el-form-item>
            <el-form-item>
              <el-button 
                type="primary" 
                class="submit-btn"
                @click="changePassword"
              >
                提交修改
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <!-- 订单明细对话框 -->
    <el-dialog v-model="showOrderDetailDialog" title="订单明细" width="60%">
      <el-table :data="orderDetails">
        <el-table-column prop="title" label="书籍名称" />
        <el-table-column prop="quantity" label="数量" width="100" />
        <el-table-column prop="price" label="单价" width="120">
          <template #default="{ row }">￥{{ row.price.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column label="小计" width="120">
          <template #default="{ row }">￥{{ (row.price * row.quantity).toFixed(2) }}</template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 地址编辑对话框 -->
    <el-dialog v-model="showAddressDialog" :title="addressForm.id ? '编辑地址' : '新增地址'">
      <el-form :model="addressForm">
        <el-form-item label="收货人" required>
          <el-input v-model="addressForm.consignee" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" required>
          <el-input v-model="addressForm.phone" placeholder="请输入11位手机号码" />
        </el-form-item>
        <el-form-item label="详细地址" required>
          <el-input 
            v-model="addressForm.detailAddress" 
            type="textarea"
            placeholder="请输入详细地址信息"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="设为默认">
          <el-switch v-model="addressForm.isDefault" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddressDialog = false">取消</el-button>
        <el-button type="primary" @click="submitAddress">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.personal-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 80px;
  
  .personal-header {
    text-align: center;
    margin-bottom: 40px;
    
    .personal-title {
      font-size: 28px;
      color: #303133;
      margin-bottom: 20px;
    }
    
    .user-avatar {
      margin: 20px 0;
      
      :deep(.el-avatar) {
        box-shadow: 0 2px 12px rgba(0,0,0,0.1);
        border: 2px solid #fff;
      }
    }
  }

  .custom-tabs {
    :deep(.el-tabs__nav-wrap) {
      &::after {
        height: 1px;
        background-color: #e4e7ed;
        
      }
    }
    
    :deep(.el-tabs__item) {
      font-size: 16px;
      height: 50px;
      line-height: 50px;
      padding: 0 25px;
      
      &.is-active {
        color: #409EFF;
        font-weight: 500;
      }
    }
  }

  .info-card {
    margin: 20px 0;
    
    :deep(.el-descriptions__title) {
      font-size: 18px;
      margin-bottom: 20px;
      color: #303133;
    }
    
    .desc-label {
      background: #f5f7fa;
      width: 120px;
      color: #606266;
    }
  }

  .table-actions {
    margin-bottom: 20px;
  }

  .password-card {
    max-width: 600px;
    margin: 20px auto;
    padding: 30px 50px;
    
    .submit-btn {
      width: 100%;
      margin-top: 20px;
      height: 40px;
    }
  }

  .el-card {
    margin: 20px 0;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
    transition: box-shadow 0.3s;
    
    &:hover {
      box-shadow: 0 4px 16px 0 rgba(0,0,0,0.12);
    }
    
    :deep(.el-card__header) {
      border-bottom: 1px solid #ebeef5;
      padding: 18px 20px;
    }
    
    :deep(.el-card__body) {
      padding: 20px;
    }
  }
}

@media (max-width: 768px) {
  .personal-container {
    padding: 10px;
    
    .personal-header {
      margin-bottom: 20px;
      
      .personal-title {
        font-size: 24px;
      }
    }
    
    .custom-tabs {
      :deep(.el-tabs__item) {
        font-size: 14px;
        padding: 0 15px;
      }
    }
    
    .password-card {
      padding: 20px;
    }
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.avatar-uploader {
  :deep(.el-upload) {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
    
    &:hover {
      border-color: var(--el-color-primary);
    }
  }
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
  line-height: 120px;
}

.avatar {
  width: 120px;
  height: 120px;
  display: block;
  object-fit: cover;
}
</style>