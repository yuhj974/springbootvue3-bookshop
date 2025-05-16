<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { orderlist, deleteOrders, getOrderById, deliverOrder } from '@/api/order'
import dayjs from 'dayjs'

// 查询参数
const queryParams = ref({
  username: '',
  status: '',
  detailAddress: '',
  paymentDate: '',
  paymentTime: '',
  deliveryDate: '',
  deliveryTime: '',
  receiveDate: '',
  receiveTime: ''
})

const selectedIds = ref([])



// 分页参数
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 时间选择器显示状态
const showPaymentTime = ref(false)
const showDeliveryTime = ref(false)
const showReceiveTime = ref(false)

// 表格数据
const orderList = ref([])
const multipleSelection = ref([])

// 明细对话框
const detailData = ref([])
const detailDialogVisible = ref(false)

// 时间格式化方法
const formatTime = (time) => {
  return time ? dayjs(time).format('YYYY-MM-DD HH:mm:ss') : ''
}

// 构建时间参数
const buildDateTime = (type) => {
  const date = queryParams.value[`${type}Date`]
  const time = queryParams.value[`${type}Time`]
  
  if (!date) return ''
  
  // 当未选择时间时仅传递日期
  return time ? `${date} ${time}` : date
}


// 获取订单列表
const getList = async () => {
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      username: queryParams.value.username,
      status: queryParams.value.status,
      detailAddress: queryParams.value.detailAddress,
      paymentTime: buildDateTime('payment'),
      deliveryTime: buildDateTime('delivery'),
      receiveTime: buildDateTime('receive')
    }

    if (params.detailAddress) {
      params.detailAddress = `%${params.detailAddress}%`
    }

    Object.keys(params).forEach(key => {
      if (params[key] === '' || params[key] === null) delete params[key]
    })

    const res = await orderlist(params)
    orderList.value = res.data.rows.map(item => ({
      ...item,
      paymentTime: formatTime(item.paymentTime),
      deliveryTime: formatTime(item.deliveryTime),
      receiveTime: formatTime(item.receiveTime),
      createTime: formatTime(item.createTime),
      updateTime: formatTime(item.updateTime)
    }))
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  }
}

// 重置查询
const resetQuery = () => {
  queryParams.value = {
    username: '',
    status: '',
    detailAddress: '',
    paymentDate: '',
    paymentTime: '',
    deliveryDate: '',
    deliveryTime: '',
    receiveDate: '',
    receiveTime: ''
  }
  showPaymentTime.value = false
  showDeliveryTime.value = false
  showReceiveTime.value = false
  getList()
}

// 分页变化
const handleSizeChange = (val) => {
  pageSize.value = val
  getList()
}

const handleCurrentChange = (val) => {
  pageNum.value = val
  getList()
}

// 多选处理
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
  multipleSelection.value = selection.map(item => item.id)
}

// 删除订单
const handleDelete = async (ids) => {
  try {
    await ElMessageBox.confirm('确认要删除选中的订单吗？', '提示', {
      type: 'warning'
    })
    
    await deleteOrders(ids.join(','))
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 查看明细
const handleDetail = async (id) => {
  const res = await getOrderById(id)
  detailData.value = res.data.map(item => ({
    ...item,
    createTime: formatTime(item.createTime),
    updateTime: formatTime(item.updateTime)
  }))
  detailDialogVisible.value = true
}

// 发货操作
const handleDeliver = (id) => {
  ElMessageBox.confirm('确认发货？', '提示', {
    type: 'warning'
  }).then(async () => {
    await deliverOrder(id)
    ElMessage.success('发货成功')
    getList()
  }).catch(() => {})
}

// 切换时间选择器
const toggleTimePicker = (type) => {
  switch(type) {
    case 'payment':
      showPaymentTime.value = !showPaymentTime.value
      if (!showPaymentTime.value) queryParams.value.paymentTime = ''
      break
    case 'delivery':
      showDeliveryTime.value = !showDeliveryTime.value
      if (!showDeliveryTime.value) queryParams.value.deliveryTime = ''
      break
    case 'receive':
      showReceiveTime.value = !showReceiveTime.value
      if (!showReceiveTime.value) queryParams.value.receiveTime = ''
      break
  }
}

onMounted(() => {
  getList()
})
</script>

<template>
  <div class="app-container">
    <h1 class="page-title">订单管理</h1>
    
    <!-- 查询表单 -->
    <el-card class="filter-container" shadow="never">
      <el-form :model="queryParams" inline>
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
            <el-form-item label="用户名">
              <el-input
                v-model="queryParams.username"
                placeholder="请输入用户名"
                clearable
              />
            </el-form-item>
          </el-col>
          
          <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
            <el-form-item label="状态">
              <el-select
                v-model="queryParams.status"
                placeholder="全部状态"
                clearable
              >
                <el-option label="已付款" value="已付款" />
                <el-option label="已发货" value="已发货" />
                <el-option label="已完成" value="已完成" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
            <el-form-item label="详细地址">
              <el-input
                v-model="queryParams.detailAddress"
                placeholder="请输入地址"
                clearable
              />
            </el-form-item>
          </el-col>

          <!-- 支付时间 -->
          <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
            <el-form-item label="支付时间">
              <div class="datetime-picker-group">
                <el-date-picker
                  v-model="queryParams.paymentDate"
                  type="date"
                  placeholder="选择日期"
                  value-format="YYYY-MM-DD"
                  style="width: 140px"
                />
              <el-time-picker
              v-if="showPaymentTime"
              v-model="queryParams.paymentTime"
              placeholder="选择时间"
              format="HH:mm:ss"
              value-format="HH:mm:ss"
              style="width: 140px"
               
            />
                <el-button
                  :icon="showPaymentTime ? 'Remove' : 'Clock'"
                  @click="toggleTimePicker('payment')"
                  class="time-toggle"
                />
              </div>
            </el-form-item>
          </el-col>

          <!-- 发货时间 -->
          <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
            <el-form-item label="发货时间">
              <div class="datetime-picker-group">
                <el-date-picker
                  v-model="queryParams.deliveryDate"
                  type="date"
                  placeholder="选择日期"
                  value-format="YYYY-MM-DD"
                  style="width: 140px"
                />
                <el-time-picker
                  v-if="showDeliveryTime"
                  v-model="queryParams.deliveryTime"
                  placeholder="选择时间"
                  format="HH:mm:ss"
                  value-format="HH:mm:ss"
                  style="width: 140px"
                />
                <el-button
                  :icon="showDeliveryTime ? 'Remove' : 'Clock'"
                  @click="toggleTimePicker('delivery')"
                  class="time-toggle"
                />
              </div>
            </el-form-item>
          </el-col>

          <!-- 收货时间 -->
          <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
            <el-form-item label="收货时间">
              <div class="datetime-picker-group">
                <el-date-picker
                  v-model="queryParams.receiveDate"
                  type="date"
                  placeholder="选择日期"
                  value-format="YYYY-MM-DD"
                  style="width: 140px"
                />
                <el-time-picker
                  v-if="showReceiveTime"
                  v-model="queryParams.receiveTime"
                  placeholder="选择时间"
                  format="HH:mm:ss"
                  value-format="HH:mm:ss"
                  style="width: 140px"
                />
                <el-button
                  :icon="showReceiveTime ? 'Remove' : 'Clock'"
                  @click="toggleTimePicker('receive')"
                  class="time-toggle"
                />
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item class="form-actions">
              <el-button type="primary" @click="getList">查询</el-button>
              <el-button @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 操作栏 -->
    <div class="operation-container">
      <el-button 
        type="danger" 
        :disabled="!selectedIds.length"
        @click="handleDelete(selectedIds)"
      >
        批量删除
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <el-table
        :data="orderList"
        @selection-change="handleSelectionChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="orderNumber" label="订单号" min-width="180" />
        <el-table-column prop="totalAmount" label="总金额" width="120" align="center">
          <template #default="{row}">
            ¥{{ row.totalAmount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120" align="center">
          <template #default="{row}">
            <el-tag :type="{
              '已付款': 'warning',
              '已发货': 'primary',
              '已完成': 'success'
            }[row.status]">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户" width="120" align="center" />
        <el-table-column prop="phone" label="手机号" width="120" align="center" />
        <el-table-column prop="consignee" label="收货人" width="120" align="center" />
        <el-table-column prop="detailAddress" label="地址" min-width="200" />
        <el-table-column label="支付时间" width="180">
          <template #default="{row}">{{ formatTime(row.paymentTime) }}</template>
        </el-table-column>
        <el-table-column label="发货时间" width="180">
          <template #default="{row}">{{ formatTime(row.deliveryTime) }}</template>
        </el-table-column>
        <el-table-column label="收货时间" width="180">
          <template #default="{row}">{{ formatTime(row.receiveTime) }}</template>
        </el-table-column>
        <el-table-column label="创建时间" width="180">
          <template #default="{row}">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="更新时间" width="180">
          <template #default="{row}">{{ formatTime(row.updateTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right" align="center">
          <template #default="{row}">
            <el-button
              type="primary"
              size="small"
              @click="handleDetail(row.id)"
            >明细</el-button>
            <el-button
              v-if="row.status === '已付款'"
              type="success"
              size="small"
              @click="handleDeliver(row.id)"
            >发货</el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleDelete([row.id])"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next"
          :total="total"
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 订单明细对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="订单明细"
      width="60%"
    >
      <el-table :data="detailData" border>
        <el-table-column prop="orderNumber" label="订单号" width="180" />
        <el-table-column prop="title" label="商品名称" />
        <el-table-column prop="price" label="单价" width="120" align="center">
          <template #default="{row}">¥{{ row.price.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="100" align="center" />
        <el-table-column prop="orderId" label="订单ID" width="120" align="center" />
        <el-table-column label="创建时间" width="180">
          <template #default="{row}">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="更新时间" width="180">
          <template #default="{row}">{{ formatTime(row.updateTime) }}</template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<style scoped>
.app-container {
  padding: 20px;
}

.page-title {
  margin-bottom: 20px;
  color: #333;
}

.filter-container {
  margin-bottom: 20px;
}

.operation-container {
  margin-bottom: 20px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  width: 100%;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.el-table {
  margin-top: 20px;
}

.el-form-item {
  margin-bottom: 18px;
}

.datetime-picker-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.time-toggle {
  padding: 8px;
  margin-left: 4px;
}

.el-date-editor.el-input,
.el-date-editor.el-input__wrapper {
  width: 140px;
}

</style>