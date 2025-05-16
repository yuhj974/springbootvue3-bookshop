<template>
  <h1>书籍管理</h1>
  <div class="container">
    <!-- 搜索表单 -->
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
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button type="info" @click="clear">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 操作按钮 -->
    <div class="mb-4">
      <el-button type="primary" @click="addbooks">+新增</el-button>
      <el-button type="danger" @click="deleteHandler(selectedBooks)">批量删除</el-button>

    </div>
    <!-- 数据表格 -->
    <el-table
  :data="bookList"
  style="width: 100%"
  @selection-change="handleSelectionChange"
  :row-key="row => row.id"
>
      
      <el-table-column type="selection" width="55" />
      <!-- <el-table-column type="index" label="序号" /> -->
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
      <el-table-column prop="description" label="图书描述" />
      <el-table-column  label="状态">
        <template #default="scope">
         <!-- 修正比较逻辑 -->
          <el-tag :type="Number(scope.row.status) === 1 ? 'success' : 'danger'">
            {{ Number(scope.row.status) === 1 ? '已上架' : '未上架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column prop="updateTime" label="更新时间"  width="180"/>
      
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-button  type="danger" @click="deleteHandler(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 30, 50]"
      :background="background"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <!-- 新增/编辑对话框 -->
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
          <el-input v-model="form.description" />
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
              action="api/files/upload"
              :on-success="handleUploadSuccess"
                :headers="uploadHeaders"
            >
              <el-button type="primary">点击上传</el-button>
              <!-- <template #tip>
                <div class="el-upload__tip">支持JPG/PNG格式，大小不超过2MB</div>
              </template> -->
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
import { booklist,addBook,deleteBooks ,getBookById,updateBook} from '@/api/book'
import { categorylist,getCategory } from '@/api/category'
import { ElMessage,ElMessageBox } from 'element-plus'



const form = ref({
  title: '',
  author: '',
  price: '',
  stock: '',
  description: '',
  categoryName: '',
  status: '',
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
const currentPage = ref(1)
const pageSize = ref(10)
const background = ref(true)
const total = ref(0)
const handleSizeChange = (val) => {
  handleSearch()
}
const handleCurrentChange = (val) => {
  handleSearch()
}

/* ============================================= */
// 在 script setup 中添加以下代码（保留原有其他代码）
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
    { pattern: /^[1-9]\d*$/, message: '库存必须为正整数' }
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
    coverImage: ''
  };
};
///////////////////////////////////////////////////////////
// 图片上传成功处理
const handleUploadSuccess = (response) => {
  console.log(response);
  
  if (response.code) { // 根据实际接口 code
    form.value.coverImage = response.data; // 根据实际字段
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
      status: String(form.value.status)
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


const searchForm = ref({
  name: '',
  author: '',
  categoryName: '',
  status: ''
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
      }
    }
  } catch (error) {
    ElMessage.error('获取图书信息失败')
  }
 

}
// ============================================

//  搜索
// 组件 script 部分
const handleSearch = async () => {
  const params = {
    pageNum: currentPage.value,
    pageSize: pageSize.value,
    name: searchForm.value.name,
    author: searchForm.value.author,
    status: searchForm.value.status,
    categoryName: searchForm.value.categoryName
  };

  const result = await booklist(params); // 传对象参数
  if (result.code) { // 根据实际接口返回的 code 判断
    bookList.value = result.data.rows;
    total.value = result.data.total;
  }
};

onMounted(async () => {
 
  await handleSearch();    // 再加载书籍列表
  await fetchCategories(); // 先加载分类数据
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
    status: ''
  }
  handleSearch()
}

const bookList = ref([])
const categories = ref([])
</script>

<style scoped>

</style>