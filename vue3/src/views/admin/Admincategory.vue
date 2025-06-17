<template>
  <h1>分类管理</h1>
  <div class="container">
    <!-- 搜索栏 -->
    <el-form :inline="true" :model="searchForm" class="demo-form-inline">
      <el-form-item label="分类名称">
        <el-input v-model="searchForm.name" placeholder="请输入分类名称"  />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作栏 -->
    <div class="mb-4">
      <el-button type="primary" @click="handleAdd">新增分类</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      :data="categoryList"
      style="width: 100%"
      
    >
    <!-- @selection-change="handleSelectionChange" -->
    
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="分类名称" />
      <el-table-column prop="createTime" label="创建时间" width="180">
        <template #default="{ row }">
          {{ formatTime(row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="180">
        <template #default="{ row }">
          {{ formatTime(row.updateTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="pagination.current"
      v-model:page-size="pagination.size"
      :page-sizes="[10, 20, 50, 100]"
      :background="true"
      layout="total, sizes, prev, pager, next"
      :total="pagination.total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  categorylist,
  addCategory,
  updateCategory,
  deleteCategory
} from '@/api/category'
import dayjs from 'dayjs'


// 数据列表
const categoryList = ref([])

// 分页参数
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 搜索表单
const searchForm = reactive({})

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = reactive({
  id: null,
  name: ''
})

// 表单引用
const formRef = ref()

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '分类名称不能为空', trigger: 'blur' },
    { max: 20, message: '长度不能超过20个字符', trigger: 'blur' }
  ]
}

// 初始化加载数据
onMounted(() => {
  handleSearch()
})





// 分页事件
const handleSizeChange = (size) => {
  pagination.pageSize = size
  handleSearch()
}

const handleCurrentChange = (current) => {
  pagination.page = current
  handleSearch()
}

// 搜索（已修复）
const handleSearch =async () => {
  try {
    const params = {
      page: pagination.current,
      size: pagination.size,
      ...searchForm // 自动包含搜索条件
    }
    const res = await categorylist(params)
    categoryList.value = res.data.rows
    pagination.total = res.data.total
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}


// 重置搜索
const resetSearch = () => {
  searchForm.name = ''
  handleSearch()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增分类'
  form.id = null
  form.name = ''
  dialogVisible.value = true
}

// 编辑（已修复）
const handleEdit = (row) => {
  dialogTitle.value = '编辑分类'
  form.id = row.id // 添加ID设置
  form.name = row.name
  dialogVisible.value = true
}

// 删除（保持不变）
const handleDelete = async (id) => {
  
    await ElMessageBox.confirm('确认删除该分类吗？', '提示', {
      type: 'warning',
      confirmButtonText: '确认',
      cancelButtonText: '取消'
    })
     
      // 直接等待请求，成功会自动进入这里
      const res =  await deleteCategory(id)
      if (res.code ) {
        ElMessage.success('删除成功')
        handleSearch()
      }
      if (res.code === 0) {
        ElMessage.error(res.msg)
      }
     
  } 
    
  


// 提交表单（已添加验证）
const submitForm = async () => {
  try {
    // 进行表单验证
    await formRef.value.validate()
    
    if (form.id) {
      await updateCategory(form)
    } else {
      await addCategory(form)
    }
    ElMessage.success('操作成功')
    dialogVisible.value = false
    handleSearch() // 使用已定义的刷新方法
  } catch (error) {
    // 验证错误不提示
    if (error instanceof Error) return
    ElMessage.error('操作失败')
  }
}

// 时间格式化（保持不变）
const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
}
</script>

<style scoped>
.container {
  padding: 20px;
  background: #fff;
  border-radius: 4px;
}

.mb-4 {
  margin-bottom: 16px;
}

.el-pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>