<template>
  <h1>书籍管理</h1>
  <div class="container">
    <!-- 搜索表单 - 保留推荐状态筛选 -->
    <el-form :inline="true" :model="searchForm" class="demo-form-inline" style="margin: 15px 0;">
      <el-form-item label="书名">
        <el-input v-model="searchForm.name" placeholder="请输入书名"  />
      </el-form-item>
      <el-form-item label="作者">
        <el-input v-model="searchForm.author" placeholder="请输入作者"  />
      </el-form-item>
      <el-form-item label="分类">
        <el-select v-model="searchForm.categoryName" placeholder="请选择分类" clearable>
          <el-option
            v-for="item in categories"
            :key="item.id"
            :label="item.label"
            :value="item.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
          <el-option label="上架" value="1" />
          <el-option label="下架" value="0" />
        </el-select>
      </el-form-item>
      <!-- 保留推荐状态筛选 -->
      <el-form-item label="推荐状态">
        <el-select v-model="searchForm.isRecommended" placeholder="请选择推荐状态" clearable>
          <el-option label="推荐" value="1" />
          <el-option label="不推荐" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button type="info" @click="clear">重置</el-button>
      </el-form-item>
    </el-form>
    
    <!-- 操作按钮 - 移除批量推荐按钮 -->
    <div class="mb-4">
      <el-button type="primary" @click="addbooks">+新增</el-button>
      <el-button type="danger" @click="deleteHandler(selectedBooks)">批量删除</el-button>
    </div>
    
    <!-- 数据表格 - 保留推荐状态列 -->
    <el-table
      :data="bookList"
      style="width: 100%"
      @selection-change="handleSelectionChange"
      :row-key="row => row.id"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID"width="50" />
      <el-table-column label="封面">
        <template #default="scope">
          <el-image :src="scope.row.coverImage" style="width: 60px; height: 80px" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="title" label="书名" />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="categoryName" label="分类" />
      <el-table-column prop="price" label="价格" />
      <el-table-column prop="stock" label="库存" />
     <el-table-column prop="description" label="图书描述" width="200">
  <template #default="scope">
    <el-tooltip 
      :content="scope.row.description" 
      placement="top" 
      :disabled="!scope.row.description || scope.row.description.length < 50"
    >
      <div class="ellipsis-text">
        {{ scope.row.description }}
      </div>
    </el-tooltip>
  </template>
</el-table-column>

      <el-table-column  label="书本状态">
        <template #default="scope">
          <el-tag :type="Number(scope.row.status) === 1 ? 'success' : 'danger'">
            {{ Number(scope.row.status) === 1 ? '已上架' : '未上架' }}
          </el-tag>
        </template>
      </el-table-column>
      <!-- 保留推荐状态列 -->
      <el-table-column label="站长推荐" width="80">
        <template #default="scope">
          <el-tag :type="Number(scope.row.isRecommended) === 1 ? 'success' : 'info'">
            {{ Number(scope.row.isRecommended) === 1 ? '推荐' : '-' }}
          </el-tag>
        </template>
      </el-table-column>
     <el-table-column label="创建时间" width="180">
      <template #default="scope">
        {{ dayjs(scope.row.createTime).format('YYYY-MM-DD HH:mm') }}
      </template>
    </el-table-column>
    <el-table-column label="更新时间" width="180">
      <template #default="scope">
        {{ dayjs(scope.row.updateTime).format('YYYY-MM-DD HH:mm') }}
      </template>
    </el-table-column>
      
      <el-table-column label="操作" width="250">
        <template #default="scope">
          <el-button  @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-button type="danger" @click="deleteHandler(scope.row.id)">删除</el-button>
          <!-- 保留推荐状态切换按钮 -->
          <el-button 
            size="small" 
            :type="Number(scope.row.isRecommended) === 1 ? 'warning' : 'success'"
            @click="toggleRecommend(scope.row)"
          >
            {{ Number(scope.row.isRecommended) === 1 ? '取消推荐' : '设为推荐' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="pageNum"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 30, 50]"
      :background="background"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    
    <!-- 新增/编辑对话框 - 保留推荐状态选项 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑书籍' : '新增书籍'"
      width="600px"
    >
      <el-form :model="form" label-width="80px" :rules="rules" ref="formRef">
        <el-form-item label="书名" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model="form.price" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input v-model="form.stock" />
        </el-form-item>
        <el-form-item label="图书描述" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea"
            :autosize="{ minRows: 3, maxRows: 6 }" 
            placeholder="请输入图书描述"
          />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryName" placeholder="请选择分类">
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.label"
              :value="item.label"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" >
          <el-radio-group v-model="form.status">
            <el-radio label="1">已上架</el-radio>
            <el-radio label="0">未上架</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 修改对话框中的上传组件 -->
        <el-form-item label="封面">
          <el-upload
            action="/api/files/upload"
            :on-success="handleUploadSuccess"  
          >
            <el-button type="primary">点击上传</el-button>
          </el-upload>
          <el-image
            v-if="form.coverImage"
            :src="form.coverImage"
            style="width: 100px; height: 140px; margin-top: 10px"
          />
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
import { ref, onMounted } from 'vue'
import { 
  booklist, 
  addBook, 
  deleteBooks, 
  getBookById, 
  updateBook, 
  setBookRecommend  // 保留单本设置推荐状态的API
} from '@/api/book'
import { categorylist, getCategory } from '@/api/category'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs';
const bookList = ref([])
const categories = ref([])
const form = ref({
  title: '',
  author: '',
  price: '',
  stock: '',
  description: '',
  categoryName: '',
  status: '1',
  isRecommended: 0, // 保留推荐状态，默认为0（不推荐）
  coverImage: ''
});

/* ====================================== */
// 添加选中书籍的响应式变量
const selectedBooks = ref([])

// 表格选择处理
const handleSelectionChange = (selection) => {
  selectedBooks.value = selection.map(item => item.id)
}

const deleteHandler = async (ids) => {
  try {
    const message = Array.isArray(ids) 
      ? `确认要删除选中的${ids.length}本书籍吗？`
      : '确认要删除该书籍吗？'

    await ElMessageBox.confirm(message, '警告', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const res = await deleteBooks(ids)
    if (res.code) {
      ElMessage.success(Array.isArray(ids) ? '批量删除成功' : '删除成功')
      handleSearch()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

/* ========================================== */

/* 分页 */
const pageNum = ref(1)
const pageSize = ref(10)
const background = ref(true)
const total = ref(0)
const handleSizeChange = (val) => {
  pageSize.value = val
  handleSearch()
}
const handleCurrentChange = (val) => {
  pageNum.value = val
  handleSearch()
}

/* ============================================= */
const dialogVisible = ref(false); // 控制对话框显示
const isEdit = ref(false); // 标记是否是编辑模式
const formRef = ref(); // 表单引用

// 表单验证规则
const rules = {
  title: [{ required: true, message: '书名不能为空', trigger: 'blur' }],
  author: [{ required: true, message: '作者不能为空', trigger: 'blur' }],
  price: [
    { required: true, message: '价格不能为空', trigger: 'blur' },
    { pattern: /^\d+(\.\d{1,2})?$/, message: '价格格式不正确' }
  ],
  stock: [
    { required: true, message: '库存不能为空', trigger: 'blur' },
    { pattern: /^[0-9]\d*$/, message: '库存必须为整数' }
  ],
  categoryName: [{ required: true, message: '请选择分类', trigger: 'change' }]
};

// 修改 addbooks 方法
const addbooks = () => {
  dialogVisible.value = true;
  isEdit.value = false;
  form.value = { // 重置表单数据
    title: '',
    author: '',
    price: '',
    stock: '',
    description: '',
    categoryName: '',
    status: '1',
    isRecommended: 0, // 默认不推荐
    coverImage: ''
  };
};

///////////////////////////////////////////////////////////
// 图片上传成功处理
const handleUploadSuccess = (response) => {
  console.log(response);
  
  if (response.code) { 
    form.value.coverImage = response.data;
    ElMessage.success('上传成功');
  } else {
    ElMessage.error(response.message || '上传失败');
  }
};

// 修改submitForm方法，添加更新逻辑
const submitForm = async () => {
  await formRef.value.validate();

  try {
    // 转换数值类型字段（保留ID）
    const postData = {
      ...form.value,
      id: form.value.id, // 确保包含ID
      price: Number(form.value.price),
      stock: Number(form.value.stock),
      status: String(form.value.status),
      isRecommended: Number(form.value.isRecommended) // 确保推荐状态为数字
    };

    let res;
    if (isEdit.value) {
      // 编辑模式调用更新接口
      res = await updateBook(postData);
    } else {
      // 新增模式调用添加接口
      res = await addBook(postData);
    }

    if (res.code) {
      dialogVisible.value = false;
      ElMessage.success(isEdit.value ? '修改成功' : '新增成功');
      handleSearch(); // 刷新列表
    }
  } catch (error) {
    ElMessage.error(error.message || '操作失败');
  }
};
/* ================================================= */

// 搜索表单
const searchForm = ref({
  name: '',
  author: '',
  categoryName: '',
  status: '',
  isRecommended: '' // 保留推荐状态搜索条件
})

// ===========================================
const handleEdit = async (id) => {
  try {
    const res = await getBookById(id)
    if (res.code) {
      dialogVisible.value = true
      isEdit.value = true
      
      // 转换数据类型
      form.value = {
        ...res.data,
        status: String(res.data.status),
        isRecommended: Number(res.data.isRecommended) // 确保推荐状态为数字
      }
    }
  } catch (error) {
    ElMessage.error('获取图书信息失败')
  }
}

// ============================================

// 保留切换推荐状态的方法
const toggleRecommend = async (book) => {
  try {
    const newStatus = book.isRecommended === 1 ? 0 : 1;
    const action = newStatus === 1 ? '推荐' : '取消推荐';
    
    await ElMessageBox.confirm(`确认要${action}《${book.title}》吗？`, '提示', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    });
    
    const res = await setBookRecommend(book.id, newStatus);
    if (res.code) {
      ElMessage.success(`${action}成功`);
      // 更新当前行状态，避免刷新整个列表
      book.isRecommended = newStatus;
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败');
    }
  }
};

// 移除批量设置推荐状态的方法

//  搜索
const handleSearch = async () => {
  const params = {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    name: searchForm.value.name,
    author: searchForm.value.author,
    status: searchForm.value.status,
    categoryName: searchForm.value.categoryName,
    isRecommended: searchForm.value.isRecommended // 保留推荐状态搜索参数
  };

  const result = await booklist(params);
  if (result.code) {
    bookList.value = result.data.rows;
    total.value = result.data.total;
  }
};

onMounted(async () => {
  await handleSearch();    // 加载书籍列表
  await fetchCategories(); // 加载分类数据
})

const fetchCategories = async () => {
  try {
    const res = await getCategory()
    if (res.code) {
      categories.value = res.data.map(item => ({
        value: item.id,
        label: item.name
      }))
    }
  } catch (error) {
    console.error('获取分类数据失败:', error)
  }
}

// 清空搜索条件
const clear = () => {
  searchForm.value = {
    name: '',
    author: '',
    categoryName: '',
    status: '',
    isRecommended: '' // 重置推荐状态搜索条件
  }
  handleSearch()
}
</script>

<style scoped>
.el-tag {
  margin: 2px;
}
.ellipsis-text {
  display: -webkit-box;
  display: -moz-box; /* Firefox 支持 */
  display: box;
  -webkit-line-clamp: 2;
  line-clamp: 2; /* 添加标准属性 */
  -webkit-box-orient: vertical;
  box-orient: vertical; /* 标准化属性 */
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.4;
}
</style>    