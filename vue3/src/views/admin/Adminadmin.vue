<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { adminlist, addAdmin, deleteAdmins, getAdminById, updateAdmin } from '@/api/admin'
import dayjs from 'dayjs'
import { jwtDecode } from 'jwt-decode' // 导入jwt解码库

// 当前登录管理员信息
const currentAdminId = ref(localStorage.getItem('currentAdminId'))
const currentAdminRole = ref('') // 初始为空

// 表格数据
const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const { proxy } = getCurrentInstance()
const eventBus = proxy.$eventBus

// 查询参数
const queryParams = ref({
  page: 1,
  size: 10,
  username: ''
})

// 表单相关
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const formData = ref({
  id: '',
  username: '',
  password: '',
  name: 'ADMIN',
  avatar: '',
  role: 'ADMIN'
})

// 头像上传
const uploadHeaders = ref({
  Authorization: `Bearer ${localStorage.getItem('token')}`
})
const imageUrl = ref('')

// 表单规则
const rules = ref({
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在2到20个字符', trigger: 'blur' }
  ],
})

// 获取数据
const fetchData = async () => {
  try {
    loading.value = true
    const res = await adminlist(queryParams.value)
    tableData.value = res.data.rows
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

// 头像上传处理
const handleAvatarSuccess = (res) => {
  if (res.code === 1) {
    formData.value.avatar = res.data
    imageUrl.value = res.data
  }
}

// 上传前校验
const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过2MB!')
    return false
  }
  return true
}

// 搜索
const handleSearch = () => {
  queryParams.value.page = 1
  fetchData()
}

// 重置
const handleReset = () => {
  queryParams.value = {
    page: 1,
    size: 10,
    username: ''
  }
  fetchData()
}

// 分页变化
const handleSizeChange = (size) => {
  queryParams.value.size = size
  fetchData()
}

const handleCurrentChange = (page) => {
  queryParams.value.page = page
  fetchData()
}

// 打开新增对话框
const openCreate = () => {
  isEdit.value = false
  formData.value = {
    username: '',
    password: '',
    name: 'ADMIN',
    avatar: '',
    role: 'ADMIN'
  }
  imageUrl.value = ''
  dialogVisible.value = true
}

// 打开编辑对话框
const openEdit = async (row) => {
  try {
    isEdit.value = true
    const res = await getAdminById(row.id)
    formData.value = {
      ...res.data,
      password: '' // 清空密码字段
    }
    imageUrl.value = res.data.avatar || ''
    dialogVisible.value = true
  } catch (error) {
    console.error(error)
  }
}

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate()
    const submitData = { ...formData.value }

    if (isEdit.value) {
      // 移除空密码字段
      if (submitData.password === '') {
        delete submitData.password
      }
      await updateAdmin(submitData)
      ElMessage.success('更新成功')
      
      // 如果修改的是当前用户，触发更新
      if (submitData.id.toString() === currentAdminId.value) {
        eventBus.emit('admin-updated')
      }
    } else {
      const result = await addAdmin(submitData)
      if (result.code === 0) {
        ElMessage.error(result.msg)
      } else {
        ElMessage.success('新增成功')
      }
    }
    
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error(error)
  }
}

// 删除
const handleDelete = async (id) => {
  try {
    
    
    await ElMessageBox.confirm('确认删除该管理员？', '警告', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteAdmins(id)
    ElMessage.success('删除成功')
    
    // 如果删除的是当前用户
    if (id.toString() === currentAdminId.value) {
      eventBus.emit('logout')
    }
    
    fetchData()
  } catch (error) {
    // 取消删除不处理
  }
}

// 时间格式化
const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
}

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
  
  fetchData()
})
</script>

<template>
  <h1>管理员管理</h1>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true">
      <el-form-item label="账号">
        <el-input 
          v-model="queryParams.username" 
          placeholder="请输入账号"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-button 
      type="primary" 
      @click="openCreate"
      v-if="currentAdminRole === 'SUPERADMIN'"
    >
      新增管理员
    </el-button>

    <!-- 数据表格 -->
    <el-table 
      :data="tableData" 
      v-loading="loading"
      style="margin-top: 20px"
    >
      <el-table-column prop="username" label="账号" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column label="头像">
        <template #default="{row}">
          <el-avatar :size="50" :src="row.avatar">
            <span v-if="!row.avatar">{{ row.username?.charAt(0) }}</span>
          </el-avatar>
        </template>
      </el-table-column>
      <el-table-column label="角色">
        <template #default="{row}">
          <el-tag>{{ row.role }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间">
        <template #default="{row}">{{ formatTime(row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="更新时间">
        <template #default="{row}">{{ formatTime(row.updateTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{row}">
          <!-- 自己这一行的操作 -->
          <template v-if="row.id.toString() === currentAdminId">
            <el-button type="primary" size="small" @click="openEdit(row)">编辑</el-button>
            <!-- <el-tag type="danger" size="small" disabled>不能删除自己</el-tag> -->
          </template>
          
          <!-- 对其他管理员的操作 -->
          <template v-else>
            <!-- SUPERADMIN可以编辑和删除其他管理员 -->
            <template v-if="currentAdminRole === 'SUPERADMIN'">
              <el-button type="primary" size="small" @click="openEdit(row)">编辑</el-button>
              <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
            </template>
            
            <!-- 普通管理员对其他管理员无操作权限 -->
            <span v-else class="text-muted">-</span>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      style="margin-top: 20px"
      v-model:current-page="queryParams.page"
      v-model:page-size="queryParams.size"
      :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="isEdit ? '编辑管理员' : '新增管理员'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="账号" prop="username">
          <el-input v-model="formData.username" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="formData.password" 
            show-password 
            :placeholder="isEdit ? '留空则不修改密码' : '请输入密码'"
          />
        </el-form-item>

        <el-form-item label="姓名">
          <el-input v-model="formData.name" />
        </el-form-item>

        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            action="/api/files/upload"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="imageUrl" :src="imageUrl" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="角色">
          <el-tag>ADMIN</el-tag>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.app-container {
  padding: 20px;
}

:deep(.avatar-uploader) .avatar {
  width: 120px;
  height: 120px;
  display: block;
  object-fit: contain;
}

:deep(.avatar-uploader) .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

:deep(.avatar-uploader) .el-upload:hover {
  border-color: var(--el-color-primary);
}

:deep(.avatar-uploader-icon) {
  width: 120px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #8c939d;
  font-size: 28px;
  background-color: #f5f7fa;
}

.text-muted {
  color: #999;
  font-size: 12px;
}
</style>