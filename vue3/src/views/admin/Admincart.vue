<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { cartlist, deleteCarts } from '@/api/cart'
import dayjs from 'dayjs'

// 查询参数
const queryParams = ref({
  username: ''
})

// 分页参数
const pageParams = ref({
  page: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref([])
// 加载状态
const loading = ref(false)
// 选中行ID
const selectedIds = ref([])

// 获取数据
const getList = async () => {
  try {
    loading.value = true
    const params = {
      ...queryParams.value,
      page: pageParams.value.page,
      size: pageParams.value.size
    }
    const res = await cartlist(params)
    tableData.value = res.data.rows
    pageParams.value.total = res.data.total
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('数据加载失败')
  } finally {
    loading.value = false
  }
}

// 重置查询
const handleReset = () => {
  queryParams.value.username = ''
  getList()
}

// 删除操作
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除选中数据吗？', '提示', {
      type: 'warning'
    })
    
    const ids = id ? [id] : selectedIds.value
    if (ids.length === 0) {
      ElMessage.warning('请选择要删除的数据')
      return
    }
    
    await deleteCarts(ids.join(','))
    ElMessage.success('删除成功')
    selectedIds.value = []
    await getList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 处理选中行
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 分页变化
const handleSizeChange = (size) => {
  pageParams.value.size = size
  getList()
}

const handleCurrentChange = (page) => {
  pageParams.value.page = page
  getList()
}

// 时间格式化
const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
}

// 价格格式化
const formatPrice = (price) => {
  return `¥${Number(price).toFixed(2)}`
}

onMounted(() => {
  getList()
})
</script>

<template>
  <div class="app-container">
    <h1 class="page-title">购物车管理</h1>
    
    <el-form :inline="true" class="search-form">
      <el-form-item label="用户名称">
        <el-input 
          v-model="queryParams.username" 
          placeholder="请输入用户名称"
          clearable
          @keyup.enter="getList"
        />
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" @click="getList">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="operation-bar">
      <el-button 
        type="danger" 
        @click="handleDelete()"
        :disabled="selectedIds.length === 0"
      >
        批量删除
      </el-button>
    </div>

    <el-table 
      :data="tableData" 
      @selection-change="handleSelectionChange"
      v-loading="loading"
      stripe
      border
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="id" label="ID" width="80" align="center" />
      
      <el-table-column prop="username" label="用户名" width="120" />
      
      <el-table-column label="封面图" width="120">
        <template #default="{ row }">
          <el-image 
            :src="row.coverImage" 
            style="width: 60px; height: 80px"
            fit="contain"
            :preview-src-list="[row.coverImage]"
            hide-on-click-modal
          >
            <template #error>
              <div class="image-error">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </template>
      </el-table-column>
      
      <el-table-column prop="title" label="书名" min-width="150" show-overflow-tooltip />
      <el-table-column prop="author" label="作者" width="120" />
      <el-table-column prop="quantity" label="数量" width="100" align="center" />
      
      <el-table-column label="总金额" width="120" align="right">
        <template #default="{ row }">
          <span class="price-text">{{ formatPrice(row.totalPrice) }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="创建时间" width="180">
        <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
      </el-table-column>
      
      <el-table-column label="更新时间" width="180">
        <template #default="{ row }">{{ formatTime(row.updateTime) }}</template>
      </el-table-column>
      
      <el-table-column label="操作" width="120" align="center" fixed="right">
        <template #default="{ row }">
          <el-button 
            type="danger" 
            size="small"
            @click="handleDelete(row.id)"
            plain
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="pagination"
      background
      :layout="pageParams.total > 100 ? 'total, sizes, prev, pager, next, jumper' : 'total, sizes, prev, pager, next'"
      v-model:current-page="pageParams.page"
      v-model:page-size="pageParams.size"
      :total="pageParams.total"
      :page-sizes="[10, 20, 50, 100]"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<style scoped>
.app-container {
  padding: 20px;
}

.page-title {
  margin-bottom: 24px;
  color: #303133;
}

.search-form {
  margin-bottom: 20px;
}

.operation-bar {
  margin-bottom: 16px;
}

.el-image {
  background-color: #f5f7fa;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.image-error {
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
}

.price-text {
  color: #f56c6c;
  font-weight: 500;
}

.pagination {
  margin-top: 24px;
  justify-content: flex-end;
}
</style>