import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useCartStore = defineStore('cart', () => {
  const items = ref({}) // 存储商品信息 { [bookId]: quantity }
  const count = ref(0)
  
  // 初始化购物车
  const initCart = () => {
    const savedCart = localStorage.getItem('cart')
    if (savedCart) {
      const parsed = JSON.parse(savedCart)
      items.value = parsed.items
      count.value = parsed.count
    }
  }

  // 添加商品
  const addItem = (bookId) => {
    if (items.value[bookId]) {
      return // 已存在的商品不能重复添加
    }
    
    items.value[bookId] = 1
    count.value++
    saveCart()
  }

  // 持久化存储
  const saveCart = () => {
    localStorage.setItem('cart', JSON.stringify({
      items: items.value,
      count: count.value
    }))
  }

  // 清空购物车
  const clearCart = () => {
    items.value = {}
    count.value = 0
    localStorage.removeItem('cart')
  }

  initCart() // 初始化时加载购物车数据

  return { items, count, addItem, clearCart }
})