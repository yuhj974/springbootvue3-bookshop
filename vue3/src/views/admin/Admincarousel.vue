<template>
  <h1>轮播图管理</h1>
  <div class="carousel-management">
    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="mb-4">
      <el-form-item label="标题">
        <el-input v-model="searchForm.title" placeholder="请输入标题" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="请选择">
          <el-option label="全部" :value="null" />
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">
          <el-icon><Search /></el-icon> 搜索
        </el-button>
        <el-button type="info" @click="clear">
          <el-icon><Refresh /></el-icon> 重置
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <div class="mb-4">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon> 添加轮播图
      </el-button>
      <el-button type="danger" @click="deleteHandler(selectedIds)" :disabled="selectedIds.length === 0">
        <el-icon><Delete /></el-icon> 批量删除
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      @selection-change="handleSelectionChange"
      stripe
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="轮播图" width="120">
        <template #default="{ row }">
          <el-image 
            :src="row.imageUrl" 
            fit="cover" 
            class="carousel-image"
            :preview-src-list="[row.imageUrl]"
          />
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" />
       <el-table-column prop="bookId" label="关联图书ID" />
      <el-table-column prop="booktitle" label="关联图书" />
      <el-table-column prop="sort" label="排序" width="100">
        <template #default="{row}">
          <el-tag>{{ row.sort }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{row}">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180">
        <template #default="{row}">
          {{ dayjs(row.createTime).format('YYYY-MM-DD HH:mm') }}
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="180">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">
            <el-icon><Edit /></el-icon> 编辑
          </el-button>
          <el-button 
            size="small" 
            type="danger" 
            @click="deleteHandler(scope.row.id)"
          >
            <el-icon><Delete /></el-icon> 删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      class="mt-4"
      background
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      :page-size="pageParams.pageSize"
      :current-page="pageParams.pageNum"
      @current-change="handlePageChange"
      @size-change="handleSizeChange"
    />

    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title">
      <el-form :model="formData" :rules="rules" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入轮播图标题" />
        </el-form-item>
        
        <el-form-item label="图片" prop="imageUrl">
          <el-upload
            class="avatar-uploader"
            action="/api/files/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <template v-if="formData.imageUrl">
              <img :src="formData.imageUrl" class="avatar" alt="轮播图" />
              <span class="avatar-edit-hint">点击修改</span>
            </template>
            <el-icon v-else class="avatar-uploader-icon">
              <i-ep-plus />
            </el-icon>
          </el-upload>
        </el-form-item>
        
        <el-form-item label="关联图书ID" prop="bookId">
          <el-input 
            v-model.number="formData.bookId" 
            type="number" 
            placeholder="请输入关联图书ID" 
          />
        </el-form-item>
        
        <el-form-item label="排序" prop="sort">
          <el-input-number 
            v-model="formData.sort" 
            :min="0" 
            :max="999" 
            controls-position="right" 
          />
          <span class="form-tip">数字越小越靠前</span>
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
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

<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import { 
  listCarousels, 
  addCarousel, 
  updateCarousel, 
  deleteCarousels 
} from '@/api/carousel'

// 头像上传逻辑
const beforeAvatarUpload = (rawFile) => {
  const isJPGorPNG = ['image/jpeg', 'image/png'].includes(rawFile.type)
  const isLt2M = rawFile.size / 1024 / 1024 < 2

  if (!isJPGorPNG) return ElMessage.error('轮播图只能是 JPG/PNG 格式!')
  if (!isLt2M) return ElMessage.error('轮播图大小不能超过 2MB!')
  return true
}

const handleAvatarSuccess = (res) => {
  if (res.code) {
    formData.value.imageUrl = res.data
    ElMessage.success('图片上传成功')
  }
}

// 表格数据
const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const selectedIds = ref([])

// 分页参数
const pageParams = ref({
  pageNum: 1,
  pageSize: 10
})

// 查询条件
const searchForm = ref({
  title: '',
  status: null
})

// 对话框相关
const dialog = ref({
  visible: false,
  title: '添加轮播图',
  isEdit: false
})

// 表单数据
const formData = ref({
  id: '',
  title: '',
  imageUrl: '',
  bookId: null,
  sort: 0,
  status: 1
})

// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  imageUrl: [
    { required: true, message: '请上传图片', trigger: 'change' }
  ],
  sort: [
    { required: true, message: '请设置排序值', trigger: 'blur' }
  ]
}

// 获取轮播图列表
const getList = async () => {
  loading.value = true
  const params = {
    pageNum: pageParams.value.pageNum,
    pageSize: pageParams.value.pageSize,
    ...searchForm.value
  }
  const res = await listCarousels(params)
  tableData.value = res.data.rows ||res.data
  total.value = res.data.total
  loading.value = false
}

// 搜索
const handleSearch = () => {
  pageParams.value.pageNum = 1
  getList()
}

// 清空搜索条件
const clear = () => {
  searchForm.value = { title: '', status: null }
  pageParams.value.pageNum = 1
  getList()
}

// 分页变化
const handlePageChange = (page) => {
  pageParams.value.pageNum = page
  getList()
}

// 页尺寸变化处理
const handleSizeChange = (size) => {
  pageParams.value.pageSize = size
  pageParams.value.pageNum = 1
  getList()
}

// 打开添加对话框
const handleAdd = () => {
  dialog.value = {
    visible: true,
    title: '添加轮播图',
    isEdit: false
  }
  formData.value = {
    id: '',
    title: '',
    imageUrl: '',
    bookId: null,
    sort: 0,
    status: 1
  }
}

// 打开编辑对话框
const handleEdit = (row) => {
  dialog.value = {
    visible: true,
    title: '编辑轮播图',
    isEdit: true
  }
  formData.value = { ...row }
}

// 提交表单
const submitForm = async () => {
  const postData = {
    ...formData.value,
    bookId: formData.value.bookId ? Number(formData.value.bookId) : null,
    status: Number(formData.value.status)
  }
  
  const result = dialog.value.isEdit 
    ? await updateCarousel(postData)
    : await addCarousel(postData)

  if (result.code === 1) {
    ElMessage.success(dialog.value.isEdit ? '修改成功' : '添加成功')
    dialog.value.visible = false
    getList()
  } else {
    ElMessage.error(result.msg || '操作失败')
  }
}

// 删除处理
const deleteHandler = async (ids) => {
  const idArray = Array.isArray(ids) ? ids : [ids]
  if (idArray.length === 0) return ElMessage.warning('请选择要删除的轮播图')
  
  await ElMessageBox.confirm(`确定要删除选中的 ${idArray.length} 个轮播图吗？`, '警告', { 
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  })
  
  const res = await deleteCarousels(idArray)
  if (res.code === 1) {
    ElMessage.success('删除成功')
    selectedIds.value = []
    getList()
  } else {
    ElMessage.error(res.msg || '删除失败')
  }
}

// 多选处理
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 初始化加载
getList()
</script>

<style scoped>
.carousel-management {
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
.carousel-image {
  width: 100px;
  height: 60px;
  border-radius: 4px;
  object-fit: cover;
}
.form-tip {
  margin-left: 10px;
  color: #999;
  font-size: 12px;
}
</style>