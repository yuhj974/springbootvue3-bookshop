<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox, ElLoading } from 'element-plus'
import { useCartStore } from '@/stores/cart'
import { deleteCarts, getCartByUserId, updateCart } from '@/api/cart' // 添加updateCart接口
import Header from '@/components/Header.vue'
import { addOrder } from '@/api/order'
import { getAddress,addAddress } from '@/api/address'

// 新增状态
const showAddressDialog = ref(false)
const addressList = ref([])
const selectedAddress = ref(null)

const cartStore = useCartStore()
const cartItems = ref([])
const selectedIds = ref([])
const isAllSelected = ref(false)

// 添加加载状态
const loading = ref(false)

// 获取地址（与图书详情页相同）
const loadAddresses = async () => {
  try {
    const res = await getAddress()
    addressList.value = res.data
    const defaultAddr = addressList.value.find(item => item.isDefault)
    if (defaultAddr) selectedAddress.value = defaultAddr.id
  } catch (error) {
    ElMessage.error('获取地址失败')
  }
}

const loadCart = async () => {
  try {
    loading.value = true
    const res = await getCartByUserId()
    // 将每个购物车项转换为响应式对象，并添加更新状态
    cartItems.value = res.data.map(item => reactive({
      ...item,
      isUpdating: false
    }))
  } catch (error) {
    ElMessage.error('获取购物车失败')
  } finally {
    loading.value = false
  }
}

// 新增：更新商品数量
const updateQuantity = async (item, newQuantity) => {
  try {
    if (newQuantity < 1 || item.isUpdating) return

    // 标记为更新中并保存旧值
    item.isUpdating = true
    const oldQuantity = item.quantity
    const oldTotalPrice = item.totalPrice

    // 立即前端更新（假设单价不变）
    const unitPrice = oldTotalPrice / oldQuantity
    item.quantity = newQuantity
    item.totalPrice = unitPrice * newQuantity

    // 后端同步
    const res = await updateCart(item.id, newQuantity)

    // 如果有后端返回数据，更新更精确的值（例如处理价格变化）
    item.quantity = res.data.quantity
    item.totalPrice = res.data.totalPrice
  } catch (error) {
    // 回滚前端修改
    item.quantity = oldQuantity
    item.totalPrice = oldTotalPrice
    ElMessage.error('更新数量失败：' + (error.response?.data?.message || '网络错误'))
  } finally {
    item.isUpdating = false
  }
}

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


// 全选/取消全选
const handleSelectAll = (val) => {
  selectedIds.value = val ? cartItems.value.map(item => item.id) : []
}

// 单个选择
const handleSelect = (selection, row) => {
  if (selection.some(item => item.id === row.id)) {
    selectedIds.value.push(row.id)
  } else {
    selectedIds.value = selectedIds.value.filter(id => id!== row.id)
  }
}

// 删除商品（支持批量删除和单个删除）
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除选中商品吗？', '提示', {
      type: 'warning'
    })

    // 统一使用数组参数
    const idsToDelete = Array.isArray(id)? id : [id]
    await deleteCarts(idsToDelete.join(','))

    await loadCart() // 重新加载购物车
    selectedIds.value = selectedIds.value.filter(id =>!idsToDelete.includes(id))
    ElMessage.success('删除成功')
  } catch (error) {
    if (error!== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 计算总价
const totalPrice = computed(() => {
  const sum = cartItems.value
   .filter(item => selectedIds.value.includes(item.id))
   .reduce((sum, item) => sum + item.totalPrice, 0);
  return Math.round(sum * 100) / 100;
})

// 修改后的下单处理
const handleOrder = async () => {
  if (selectedIds.value.length === 0) return

  try {
    await loadAddresses()

    if (addressList.value.length === 0) {
      ElMessage.warning('请先添加收货地址')
      return
    }

    showAddressDialog.value = true
  } catch (error) {
    console.error('下单准备失败:', error)
  }
}

// 购物车下单提交
const confirmCartOrder = async () => {
  if (!selectedAddress.value) {
    ElMessage.warning('请选择收货地址')
    return
  }

  try {
    const loadingInstance = ElLoading.service({
      lock: true,
      text: '正在创建订单...',
      background: 'rgba(0, 0, 0, 0.1)'
    })

    const orderData = {
      addressId: selectedAddress.value,
      totalAmount: totalPrice.value,
      orderItems: cartItems.value
       .filter(item => selectedIds.value.includes(item.id))
       .map(item => ({
          bookId: item.bookId,
          quantity: item.quantity,
          price: parseFloat((item.totalPrice / item.quantity).toFixed(2)) // 保持精度
        }))
    }

    const res = await addOrder(orderData)
    loadingInstance.close()

    if (res.code === 1) {
      ElMessage.success('订单创建成功')
      showAddressDialog.value = false
      await loadCart() // 重新加载购物车
      selectedIds.value = [] // 清空选中
    } else {
      ElMessage.error(res.msg || '下单失败')
    }
  } catch (error) {
    ElMessage.error(error.msg || '下单失败')
  }
}

onMounted(() => {
  loadCart()
})
</script>

<template>
  <Header />
  <h1 style="padding: 80px 0px 0px 0px; color: #68ACE5;">我的购物车</h1>
  <div class="cart-container">
    <el-table
      :data="cartItems"
      v-loading="loading"
      @select-all="handleSelectAll"
      @select="handleSelect"
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" />

      <el-table-column label="商品信息">
        <template #default="{ row }">
          <div class="book-info">
            <img :src="row.coverImage || 'https://via.placeholder.com/80'" class="cover" />
            <div class="meta">
              <div class="title">{{ row.title }}</div>
              <div class="author">{{ row.author }}</div>
            </div>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="单价" width="120">
        <template #default="{ row }">
          ¥{{ (row.totalPrice / row.quantity).toFixed(2) }}
        </template>
      </el-table-column>

      <el-table-column label="数量" width="180">
        <template #default="{ row }">
          <div class="quantity-control">
            <el-button
              :disabled="row.quantity <= 1 || row.isUpdating"
              @click="updateQuantity(row, row.quantity - 1)"
            >
              -
            </el-button>
            <span class="quantity">{{ row.quantity }}</span>
            <el-button
              :disabled="row.isUpdating"
              @click="updateQuantity(row, row.quantity + 1)"
            >
              +
            </el-button>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="小计" width="120">
        <template #default="{ row }">
          ¥{{ row.totalPrice.toFixed(2) }}
        </template>
      </el-table-column>

      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button
            type="danger"
            size="small"
            @click="handleDelete(row.id)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="cart-footer">
      <div class="total-price">
        总计：¥{{ totalPrice }}
      </div>
      <div class="actions">
        <el-button
          type="danger"
          :disabled="selectedIds.length === 0"
          @click="handleDelete(selectedIds)"
        >
          批量删除（{{ selectedIds.length }}）
        </el-button>
        <el-button
          type="primary"
          :disabled="selectedIds.length === 0"
          @click="handleOrder"
        >
          去下单（{{ selectedIds.length }}件）
        </el-button>
      </div>
    </div>
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
                <span class="name">收货人：{{ address.consignee }}</span>
                <span class="phone">手机号：{{ address.phone }}</span>
                <el-tag
                  v-if="address.isDefault"
                  size="mini"
                  type="danger"
                  class="default-tag"
                >
                  默认
                </el-tag>
              </div>
              <div class="detail">详情地址：{{ address.detailAddress }}
              </div>
            </div>
          </el-radio>
        </div>
      </el-radio-group>
      <template #footer>
        <el-button @click="showAddressDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmCartOrder">立即下单</el-button>
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
</template>

<style scoped>
.cart-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

h1 {
  margin-bottom: 20px;
  text-align: center;
}

.book-info {
  display: flex;
  align-items: center;
  padding: 10px 0;

 .cover {
    width: 80px;
    height: 100px;
    object-fit: cover;
    margin-right: 15px;
    border-radius: 4px;
  }

 .meta {
    .title {
      font-size: 16px;
      font-weight: 500;
      margin-bottom: 5px;
    }

    .author {
      color: #666;
      font-size: 14px;
    }
  }
}

.cart-footer {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;

 .total-price {
    font-size: 18px;
    font-weight: bold;
    color: #f56c6c;
  }

 .actions {
    display: flex;
    gap: 15px;
  }
}

.el-table {
  margin-top: 20px;
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

/* 新增数量控制样式 */
.quantity-control {
  display: flex;
  align-items: center;
  gap: 8px;

 .quantity {
    min-width: 40px;
    text-align: center;
    font-weight: 500;
  }

 .el-button {
    padding: 8px 12px;
  }
}

.address-header {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-end;
}

.address-item {
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  transition: all 0.3s;
}

.address-item:hover {
  border-color: #409EFF;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.el-radio {
  width: 100%;
}

</style>