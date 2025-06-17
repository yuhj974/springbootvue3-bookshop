import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getUserByJwt } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref({
    name: '未登录',
    avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
  })

  // 带缓存的用户信息加载
  const loadUserInfo = async () => {
    try {
      const res = await getUserByJwt()
      userInfo.value = {
        name: res.data.name || '未登录',
        avatar: `${res.data.avatar}?t=${Date.now()}` // 防止缓存
      }
    } catch (error) {
      console.error('用户信息加载失败:', error)
    }
  }

  return { 
    userInfo,
    loadUserInfo
  }
})