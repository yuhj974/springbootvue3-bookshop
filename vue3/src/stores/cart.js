import { defineStore } from 'pinia'
import { getCartByUserId } from '@/api/cart'

export const useCartStore = defineStore('cart', {
  state: () => ({
    count: localStorage.getItem('cartCount') || 0 // 初始化时读取本地存储
  }),
  actions: {
    // 新增获取购物车数量的action
    async fetchCartCount() {
      try {
        const res = await getCartByUserId()
        const total = res.data.reduce((sum, item) => sum + item.quantity, 0)
        this.count = total
        localStorage.setItem('cartCount', total) // 存储到本地
      } catch (error) {
        console.error('获取购物车数量失败:', error)
        localStorage.removeItem('cartCount')
      }
    },
    // 更新数量的action
    updateCount(newCount) {
      this.count = newCount
      localStorage.setItem('cartCount', newCount)
    }
  }
})