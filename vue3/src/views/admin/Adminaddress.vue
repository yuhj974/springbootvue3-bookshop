<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { addresslist, deleteAddress } from '@/api/address'
import dayjs from 'dayjs'

// 响应式数据
const tableData = ref([])
const selectedIds = ref([])
const loading = ref(false)

// 分页参数
const pageParams = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 查询参数
const queryParams = ref({
  username: '',
  phone: ''
})

// 获取地址列表
const getList = async () => {
  try {
    loading.value = true
    // 修改分页参数命名，确保与后端API期望的参数一致
    const params = {
      // 将currentPage改为pageNum或其他后端期望的参数名
      pageNum: pageParams.value.currentPage,
      pageSize: pageParams.value.pageSize,
      ...queryParams.value
    }
    
    const res = await addresslist(params)
    tableData.value = res.data.rows
    pageParams.value.total = res.data.total
  } finally {
    loading.value = false
  }
}

// 分页变化
const handleSizeChange = (val) => {
  pageParams.value.pageSize = val
  getList()
}

const handleCurrentChange = (val) => {
  pageParams.value.currentPage = val
  getList()
}

// 重置查询
const resetQuery = () => {
  queryParams.value = { username: '', phone: '' }
  getList()
}

// 删除地址
const handleDelete = async (ids) => {
  try {
    await ElMessageBox.confirm('确认要删除选中的地址吗？', '提示', {
      type: 'warning'
    })
    
    await deleteAddress(ids.join(','))
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 表格选择处理
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 初始化加载
onMounted(() => {
  getList()
})
</script>

<template>
  <div class="app-container">
    <h1>收货地址管理</h1>
    
    <!-- 搜索表单 -->
    <el-form :inline="true" class="search-form">
      <el-form-item label="用户名">
        <el-input 
          v-model="queryParams.username" 
          placeholder="请输入用户名"
          clearable
        />
      </el-form-item>
      
      <el-form-item label="手机号">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入手机号"
          clearable
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="getList">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <div class="operation-bar">
      <el-button 
        type="danger" 
        :disabled="!selectedIds.length"
        @click="handleDelete(selectedIds)"
      >
        批量删除
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="consignee" label="收货人" />
      <el-table-column prop="phone" label="手机号">
        <template #default="{row}">
          {{ row.phone.replace(/(\d{3})(\d{4})(\d{4})/, '$1 $2 $3') }}
        </template>
      </el-table-column>
      <el-table-column prop="detailAddress" label="详细地址" />
      <el-table-column prop="isDefault" label="默认地址" width="100">
        <template #default="{row}">
          <el-tag v-if="row.isDefault" type="success">是</el-tag>
          <el-tag v-else type="info">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间">
        <template #default="{row}">
          {{ dayjs(row.createTime).format('YYYY-MM-DD HH:mm') }}
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="更新时间">
        <template #default="{row}">
          {{ dayjs(row.updateTime).format('YYYY-MM-DD HH:mm') }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="{row}">
          <el-button 
            type="danger" 
            size="small"
            @click="handleDelete([row.id])"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :current-page="pageParams.currentPage"
      :page-size="pageParams.pageSize"
      :total="pageParams.total"
      :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<style scoped>
.app-container {
  padding: 20px;
}

.search-form {
  margin-top: 20px;
}

.operation-bar {
  margin: 15px 0;
}

.el-pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>  