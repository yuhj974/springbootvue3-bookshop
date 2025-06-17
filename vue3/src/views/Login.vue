<template>
  <div class="login-container">
    <div class="login-box">
      <div class="form-wrapper">
        <div class="form-header">
          {{ isLogin ? '欢迎登录' : '用户注册' }}
        </div>
        
        <!-- 登录表单 -->
        <el-form 
          v-if="isLogin"
          ref="loginFormRef"
          :rules="loginRules"
          :model="loginForm"
          class="form-content"
        >
          <el-form-item prop="username">
            <el-input 
              size="large"
              v-model="loginForm.username"
              placeholder="请输入用户名"
              :prefix-icon="User"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              size="large"
              v-model="loginForm.password"
              type="password"
              show-password
              placeholder="请输入密码"
              :prefix-icon="Lock"
            />
          </el-form-item>
          <!-- 新增角色选择 -->
          <el-form-item prop="role">
            <el-select
              v-model="loginForm.role"
              placeholder="请选择角色类型"
              size="large"
              style="width: 100%"
            >
              <el-option label="管理员" value="ADMIN" />
             <!--<el-option label="超级管理员" value="SUPERADMIN" />-->
              <el-option label="普通用户" value="USER" />
            </el-select>
          </el-form-item>
          <el-button 
            type="primary" 
            size="large"
            @click="handleLogin"
            class="submit-btn"
            :loading="loginLoading"
          >
            登 录
          </el-button>
          <div class="form-footer">
            还没有账号？
            <a class="switch-link" @click="switchForm(false)">立即注册</a>
          </div>
        </el-form>

        <!-- 注册表单 -->
        <el-form
          v-else
          ref="registerFormRef"
          :rules="registerRules"
          :model="registerForm"
          class="form-content"
        >
          <el-form-item prop="username">
            <el-input
              size="large"
              v-model="registerForm.username"
              placeholder="请输入用户名"
              :prefix-icon="User"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              size="large"
              v-model="registerForm.password"
              type="password"
              show-password
              placeholder="请输入密码"
              :prefix-icon="Lock"
            />
          </el-form-item>
          <el-form-item prop="email">
            <el-input
              size="large"
              v-model="registerForm.email"
              placeholder="请输入邮箱"
              :prefix-icon="Message"
            />
          </el-form-item>
          <el-form-item prop="phone">
            <el-input
              size="large"
              v-model="registerForm.phone"
              placeholder="请输入手机号"
              :prefix-icon="Phone"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              size="large"
              v-model="registerForm.name"
              placeholder="昵称（选填）"
              :prefix-icon="User"
            />
          </el-form-item>
          <el-button 
            type="primary" 
            size="large"
            @click="handleRegister"
            class="submit-btn"
            :loading="registerLoading"
          >
            注 册
          </el-button>
          <div class="form-footer">
            已有账号？
            <a class="switch-link" @click="switchForm(true)">立即登录</a>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Message, Phone } from '@element-plus/icons-vue'
import { jwtDecode } from 'jwt-decode'
import { login, register } from '@/api/login'

const router = useRouter()

// 表单状态
const isLogin = ref(true)
const loginLoading = ref(false)
const registerLoading = ref(false)

// 登录表单
const loginForm = ref({
  username: '',
  password: '',
  role: 'USER' // 默认选择普通用户
})
const loginFormRef = ref()
const loginRules = ref({
  username: [{ 
    required: true, 
    message: '请输入用户名', 
    trigger: 'blur' 
  }],
  password: [
    { 
      required: true, 
      message: '请输入密码', 
      trigger: 'blur' 
    },
    { 
      min: 1, 
      max: 18, 
      message: '密码长度1-18位', 
      trigger: 'blur' 
    }
  ],
  role: [
    { 
      required: true, 
      message: '请选择登录角色', 
      trigger: 'change' 
    }
  ]
})

// 注册表单
const registerForm = ref({
  username: '',
  password: '',
  email: '',
  phone: '',
  name: ''
})
const registerFormRef = ref()
const registerRules = ref({
  username: [{ 
    required: true, 
    message: '请输入用户名', 
    trigger: 'blur' 
  }],
  password: [
    { 
      required: true, 
      message: '请输入密码', 
      trigger: 'blur' 
    },
    { 
      min: 6, 
      max: 18, 
      message: '密码长度6-18位', 
      trigger: 'blur' 
    }
  ],
  email: [
    { 
      required: true, 
      message: '请输入邮箱', 
      trigger: 'blur' 
    },
    { 
      type: 'email', 
      message: '请输入正确的邮箱格式', 
      trigger: ['blur', 'change'] 
    }
  ],
  phone: [
    { 
      required: true, 
      message: '请输入手机号', 
      trigger: 'blur' 
    },
    { 
      pattern: /^1[3-9]\d{9}$/, 
      message: '请输入正确的手机号码', 
      trigger: 'blur' 
    }
  ]
})

// 处理登录
const handleLogin = async () => {
  try {
    loginLoading.value = true
    await loginFormRef.value.validate()
    const res = await login(loginForm.value)
    
    if (res.code === 1) {
      localStorage.setItem('logintoken', JSON.stringify( {token: res.data }))
      const { role } = jwtDecode(res.data)
      console.log(role)
      const {name} = jwtDecode(res.data)
      console.log(name)
      const  {avatar} = jwtDecode(res.data)
      console.log(avatar)
      
      ElMessage.success('登录成功')
      
      // 根据角色跳转不同页面
      if (res.code === 1) {
      // 根据角色跳转不同页面
      if (role === 'ADMIN'|| role === 'SUPERADMIN') {
        router.push('/admin/adminindex')
      } else {
        router.push('/user/home')
      }
    }
    } else {
      ElMessage.error(res.msg || '登录失败')
    }
  } catch (error) {
    if (error.response) {
      ElMessage.error(`登录失败: ${error.response.data.msg}`)
    } else {
      ElMessage.error('用户名或密码错误')
    }
  } finally {
    loginLoading.value = false
  }
}

// 处理注册
const handleRegister = async () => {
  try {
    registerLoading.value = true
    await registerFormRef.value.validate()
    const res = await register(registerForm.value)
    
    if (res.code === 1) {
      ElMessage.success('注册成功')
      switchForm(true)
      // 清空注册表单
      Object.keys(registerForm.value).forEach(key => {
        registerForm.value[key] = ''
      })
    } else {
      ElMessage.error(res.msg || '注册失败')
    }
  } catch (error) {
    if (error.response) {
      ElMessage.error(`注册失败: ${error.response.data.msg}`)
    } else {
      ElMessage.error('网络请求异常，请稍后重试')
    }
  } finally {
    registerLoading.value = false
  }
}

// 切换表单
const switchForm = (status) => {
  isLogin.value = status
  // 清空验证错误
  if (status) {
    loginFormRef.value?.clearValidate()
  } else {
    registerFormRef.value?.clearValidate()
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: flex-end;
  background-image: url("@/assets/4.png");
  background-size: cover;
  background-position: center;
}

.login-box {
  width: 50%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
}

.form-wrapper {
  width: 400px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(5px);
}

.form-header {
  font-size: 26px;
  color: #0742b1;
  text-align: center;
  margin-bottom: 35px;
  font-weight: 600;
  letter-spacing: 1px;
}

.form-content {
  margin-top: 25px;
}

.submit-btn {
  width: 100%;
  margin-top: 25px;
  height: 45px;
  font-size: 16px;
}

.form-footer {
  margin-top: 25px;
  text-align: center;
  color: #666;
  font-size: 14px;
}

.switch-link {
  color: #0742b1;
  cursor: pointer;
  margin-left: 6px;
  font-weight: 500;
  transition: all 0.3s;
}

.switch-link:hover {
  text-decoration: underline;
  opacity: 0.9;
}

/* 下拉菜单样式优化 */
.el-select {
  width: 100%;
}

.el-select :deep(.el-input__inner) {
  height: 45px;
  line-height: 45px;
}

.el-select :deep(.el-input__icon) {
  height: 100%;
  display: flex;
  align-items: center;
}
</style>