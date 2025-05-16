<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import {
  userlist,
  addUser,
  updateUser,
  deleteUsers,
  getUserById
} from '@/api/user'

// 头像上传逻辑
const beforeAvatarUpload = (rawFile) => {
  const isJPGorPNG = ['image/jpeg', 'image/png'].includes(rawFile.type)
  const isLt2M = rawFile.size / 1024 / 1024 < 2

  if (!isJPGorPNG) {
    ElMessage.error('头像图片只能是 JPG/PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像图片大小不能超过 2MB!')
    return false
  }
  return true
}

const handleAvatarSuccess = (res) => {
  if (res.code) {
    formData.value.avatar = res.data
    ElMessage.success('头像上传成功')
  }
}

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 表格数据
const tableData = ref([])
const total = ref(0)
const loading = ref(false)

// 分页参数
const pageParams = ref({
  pageNum: 1,
  pageSize: 10
})

// 查询条件
const searchForm = ref({
  username: '',
  phone: '',
  status: ''
})

// 对话框相关
const dialog = ref({
  visible: false,
  title: '添加用户',
  isEdit: false
})

// 表单数据（角色固定为user）
const formData = ref({
  id: '',
  name: '',
  username: '',
  password: '',
  email: '',
  phone: '',
  avatar: '',
  status: '1',
  role: 'user' // 固定角色为普通用户
})

// 表单验证规则
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// 多选数组
const selectedIds = ref([])

// 获取用户列表
const getList = async () => {
  try {
    loading.value = true
    const params = {
      ...pageParams.value,
      ...searchForm.value
    }
    const res = await userlist(params)
    tableData.value = res.data.rows
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pageParams.value.page = 1
  getList()
}

// 清空搜索条件
const clear = () => {
  Object.assign(searchForm.value, {
    username: '',
    phone: '',
    status: ''
  })
  getList()
}

// 分页变化
const handlePageChange = (page) => {
  pageParams.value.pageNum = page  // 对应修改
  getList()
}

// 页尺寸变化处理
const handleSizeChange = (size) => {
  pageParams.value.pageSize = size  // 对应修改
  pageParams.value.pageNum = 1
  getList()
}

// 打开添加对话框
const handleAdd = () => {
  dialog.value.visible = true
  dialog.value.title = '添加用户'
  dialog.value.isEdit = false
  formData.value = {
    id: '',
    name: '',
    username: '',
    password: '',
    email: '',
    phone: '',
    avatar: '',
    status: '1',
    role: 'user' // 初始化时固定角色
  }
}

// 打开编辑对话框
const handleEdit = async (row) => {
  try {
    const res = await getUserById(row.id)
    if (res.code) {
      formData.value = { 
        ...res.data,
        status: res.data.status.toString(),
        role: 'user' // 强制覆盖为普通用户
      }
      dialog.value = {
        visible: true,
        title: '编辑用户',
        isEdit: true
      }
    }
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}

// 提交表单
const submitForm = async () => {
  try {
    let result
    const submitData = {
      ...formData.value,
      role: 'user' // 提交时强制设置为普通用户
    }
    
    if (dialog.value.isEdit) {
      result = await updateUser(submitData)
    } else {
      result = await addUser(submitData)
    }

    if (result.code) {
      ElMessage.success(dialog.value.isEdit ? '修改成功' : '添加成功')
      dialog.value.visible = false
      getList()
    }
    if (result.code === 0) {
      ElMessage.error(result.msg)
    }
  } catch (error) {
    ElMessage.error('操作失败: ' + (error.message || '未知错误'))
  }
}

// 删除处理
const deleteHandler = async (ids) => {
  try {
    await ElMessageBox.confirm('确定要删除选中的用户吗？', '警告', { 
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })
    
    const res = await deleteUsers(ids)
    
    if (res.code) {
      ElMessage.success('删除成功')
      selectedIds.value = []
      getList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败: ' + (error.message || '未知错误'))
    }
  }
}

// 初始化加载
onMounted(() => {
  getList()
})
</script>

<template>
  <h1>用户管理</h1>
  <div class="user-management">
    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm">
      <el-form-item label="用户名">
        <el-input v-model="searchForm.username" placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="searchForm.phone" placeholder="请输入手机号" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="请选择">
          <el-option label="全部" value="" />
          <el-option label="启用" value="1" />
          <el-option label="禁用" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button type="info" @click="clear">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <div class="mb-4">
      <el-button type="primary" @click="handleAdd">添加用户</el-button>
      <el-button type="danger" @click="deleteHandler(selectedIds)">批量删除</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="email" label="邮箱" width="180" />
      <el-table-column prop="phone" label="电话" width="130" />
      <el-table-column prop="role" label="角色" width="100">
        <template #default="{row}">
          {{ row.role === 'admin' ? '管理员' : '普通用户' }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{row}">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="头像" width="100">
        <template #default="{row}">
          <el-avatar :src="row.avatar" v-if="row.avatar" />
          <span v-else>无头像</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180">
        <template #default="{row}">
          {{ dayjs(row.create_time).format('YYYY-MM-DD HH:mm') }}
        </template>
      </el-table-column>
      <el-table-column label="更新时间" width="180">
        <template #default="{row}">
          {{ dayjs(row.update_time).format('YYYY-MM-DD HH:mm') }}
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="180">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button 
            size="small" 
            type="danger" 
            @click="deleteHandler(scope.row.id)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      class="mt-4"
      background
      layout="total, sizes, prev, pager, next"
      :total="total"
      :page-size="pageParams.size"
      :current-page="pageParams.page"
      @current-change="handlePageChange"
      @size-change="handleSizeChange"
    />

    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title">
      <el-form :model="formData" :rules="rules" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!dialog.isEdit">
          <el-input v-model="formData.password" show-password />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="formData.email" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="formData.phone" />
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            action="/api/files/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <template v-if="formData.avatar">
              <img :src="formData.avatar" class="avatar" alt="头像" />
              <span class="avatar-edit-hint">点击修改</span>
            </template>
            <el-icon v-else class="avatar-uploader-icon">
              <i-ep-plus />
            </el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status">
            <el-option label="启用" value="1" />
            <el-option label="禁用" value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.user-management {
  padding: 20px;
}
.mb-4 {
  margin-bottom: 1rem;
}
.mt-4 {
  margin-top: 1rem;
}
.avatar-uploader {
  position: relative;
  cursor: pointer;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border: 2px dashed var(--el-border-color);
  border-radius: 6px;
}
.avatar-uploader:hover {
  border-color: var(--el-color-primary);
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
  border-radius: 6px;
  object-fit: cover;
}
.avatar-edit-hint {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  text-align: center;
  padding: 4px;
  font-size: 12px;
  opacity: 0;
  transition: opacity 0.3s;
}
.avatar-uploader:hover .avatar-edit-hint {
  opacity: 1;
}
</style>