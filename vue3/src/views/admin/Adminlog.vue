<script setup>
import { ref, onMounted, computed } from 'vue'
import { getLog, deleteLog } from '@/api/login' 
import dayjs from 'dayjs'
import { jwtDecode } from 'jwt-decode' 
import { ElMessage, ElMessageBox } from 'element-plus'

// 响应式数据
const tableData = ref([])
const loading = ref(false)
const multipleSelection = ref([]) // 多选框选中的数据

// 当前用户信息
const currentAdminRole = ref('') // 初始为空

// 分页参数
const pageParams = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 查询参数
const queryParams = ref({
  operationName: ''
})

// 计算属性：判断当前用户是否为SUPERADMIN
const isSuperAdmin = computed(() => {
  return currentAdminRole.value === 'SUPERADMIN'
})

// 获取日志列表
const getList = async () => {
  try {
    loading.value = true
    
    const params = {
      pageNum: pageParams.value.currentPage,
      pageSize: pageParams.value.pageSize,
      operationName: queryParams.value.operationName
    }
    
    const res = await getLog(params)
    tableData.value = res.data.rows
    pageParams.value.total = res.data.total
  } finally {
    loading.value = false
  }
}

// 分页变化
const handleSizeChange = (val) => {
  pageParams.value.pageSize = val
  pageParams.value.currentPage = 1 // 重置为第一页
  getList()
}

const handleCurrentChange = (val) => {
  pageParams.value.currentPage = val
  getList()
}

// 查询日志
const searchLog = () => {
  pageParams.value.currentPage = 1
  getList()
}

// 重置查询
const resetQuery = () => {
  queryParams.value.operationName = ''
  searchLog()
}

// 多选框变化
const handleSelectionChange = (val) => {
  multipleSelection.value = val
}

// 删除单条日志
const handleDelete = async (id) => {
  if (!isSuperAdmin.value) {
    ElMessage.warning('您没有权限执行此操作')
    return
  }
  
  try {
    await ElMessageBox.confirm('确认删除该日志？', '警告', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteLog(id)
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    // 取消删除不处理
  }
}

// 批量删除日志
const batchDelete = async () => {
  if (!isSuperAdmin.value) {
    ElMessage.warning('您没有权限执行此操作')
    return
  }
  
  if (multipleSelection.value.length === 0) {
    ElMessage.warning('请选择要删除的日志')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确认删除选中的 ${multipleSelection.value.length} 条日志？`, '警告', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 处理单个或多个ID的情况
    const ids = multipleSelection.value.map(item => item.id)
    
    // 如果是单个ID，直接传递
    if (ids.length === 1) {
      await deleteLog(ids[0])
    } 
    // 如果是多个ID，以数组形式传递
    else {
      await deleteLog(ids)
    }
    
    ElMessage.success('批量删除成功')
    multipleSelection.value = [] // 清空选中数据
    getList()
  } catch (error) {
    // 取消删除不处理
  }
}

// 初始化加载
onMounted(() => {
  // 解析JWT token获取角色信息
  const token = localStorage.getItem('logintoken')
  if (token) {
    try {
      const decoded = jwtDecode(token)
      currentAdminRole.value = decoded.role // 假设JWT中包含role字段
    } catch (error) {
      console.error('JWT解析失败', error)
      currentAdminRole.value = 'ADMIN' // 默认设为普通管理员
    }
  }
  
  getList()
})
</script>

<template>
  <div class="app-container">
    <h1>操作日志管理</h1>
    
    <!-- 查询表单 -->
    <el-form :inline="true" class="search-form">
      <el-form-item label="操作名称">
        <el-input 
          v-model="queryParams.operationName" 
          placeholder="请输入操作名称"
          clearable
          @keyup.enter="searchLog"
        />
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" @click="searchLog">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
      
      <!-- 只有超级管理员能看到批量删除按钮 -->
      <el-form-item v-if="isSuperAdmin">
        <el-button 
          type="danger" 
          @click="batchDelete"
          :disabled="multipleSelection.length === 0"
        >
          批量删除
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      stripe
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <!-- 多选框，只有超级管理员能看到 -->
      <el-table-column type="selection" width="55" v-if="isSuperAdmin" />
      
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="operateEmpId" label="操作人ID" width="100" />
      <el-table-column prop="operateTime" label="操作时间" width="180">
        <template #default="{row}">
          {{ dayjs(row.operateTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
      </el-table-column>
      <el-table-column prop="operationName" label="操作名称" width="180" />
      <el-table-column prop="className" label="类名" min-width="150">
        <template #default="{row}">
          <el-tooltip effect="dark" :content="row.className" placement="top">
            <div class="text-ellipsis">
              {{ row.className }}
            </div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="methodName" label="方法名" min-width="150">
        <template #default="{row}">
          <el-tooltip effect="dark" :content="row.methodName" placement="top">
            <div class="text-ellipsis">
              {{ row.methodName }}
            </div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="methodParams" label="方法参数" min-width="200">
        <template #default="{row}">
          <el-tooltip effect="dark" :content="row.methodParams" placement="top" v-if="row.methodParams">
            <div class="text-ellipsis">
              {{ row.methodParams }}
            </div>
          </el-tooltip>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="returnValue" label="返回值" min-width="200">
        <template #default="{row}">
          <el-tooltip effect="dark" :content="row.returnValue" placement="top" v-if="row.returnValue">
            <div class="text-ellipsis">
              {{ row.returnValue }}
            </div>
          </el-tooltip>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="costTime" label="耗时(ms)" width="100">
        <template #default="{row}">
          <el-tag :type="row.costTime < 100 ? 'success' : row.costTime < 500 ? 'warning' : 'danger'">
            {{ row.costTime }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{row}">
          <el-button 
            type="danger" 
            size="small" 
            @click="handleDelete(row.id)"
            v-if="isSuperAdmin"
          >
            删除
          </el-button>
          <span v-else>-</span>
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
  margin-bottom: 20px;
}

.text-ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.el-pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>    