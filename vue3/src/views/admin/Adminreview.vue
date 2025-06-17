<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { reviewlist, deleteReviews } from '@/api/review'
import dayjs from 'dayjs'

// 响应式数据
const tableData = ref([])
const selectedIds = ref([])
const loading = ref(false)

// 分页参数
const pageParams = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 查询参数
const queryParams = ref({
  username: '',
  rating: ''
})

// 获取评论列表
const getList = async () => {
  try {
    loading.value = true
    const params = {
      pageNum: pageParams.value.pageNum, // 修正参数名
      pageSize: pageParams.value.pageSize, // 修正参数名
      ...queryParams.value
    }
    
    const res = await reviewlist(params)
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
  pageParams.value.pageNum = val
  getList()
}

// 重置查询
const resetQuery = () => {
  queryParams.value = { username: '', rating: '' }
  getList()
}

// 删除评论
const handleDelete = async (ids) => {
  try {
    await ElMessageBox.confirm('确认要删除选中的评论吗？', '提示', {
      type: 'warning'
    })
    
    await deleteReviews(ids.join(','))
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
    <h1>评论管理</h1>
    
    <!-- 搜索表单 -->
    <el-form :inline="true" class="search-form">
      <el-form-item label="用户名">
        <el-input 
          v-model="queryParams.username" 
          placeholder="请输入用户名"
          clearable
        />
      </el-form-item>
      
      <el-form-item label="评分">
        <el-select 
          v-model="queryParams.rating" 
          placeholder="请选择评分"
          clearable
        >
          <el-option label="1星" value="1" />
          <el-option label="2星" value="2" />
          <el-option label="3星" value="3" />
          <el-option label="4星" value="4" />
          <el-option label="5星" value="5" />
        </el-select>
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
      <el-table-column prop="title" label="书籍标题" />
      <el-table-column prop="rating" label="评分">
        <template #default="{row}">
          <el-rate v-model="row.rating" disabled show-score />
        </template>
      </el-table-column>
      <el-table-column prop="content" label="评论内容" />
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
      :current-page="pageParams.pageNum"
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