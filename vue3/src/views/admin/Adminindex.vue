<template>
  <div class="stats-container">
    <!-- 顶部统计卡片 -->
    <div class="stats-cards">
      <div 
        v-for="(stat, key) in formattedGeneralStats" 
        :key="key" 
        class="stat-card"
        :class="{ 'negative-stat': stat.isNegative }"
      >
        <div class="stat-title">{{ stat.label }}</div>
        <div class="stat-value">
          <span v-if="stat.prefix">{{ stat.prefix }}</span>
          {{ stat.formattedValue || stat.value }}
          <span v-if="stat.suffix" class="stat-suffix">{{ stat.suffix }}</span>
        </div>
      </div>
    </div>

    <!-- 分类统计图表 -->
    <div class="chart-container">
      <template v-if="isLoadingCategoryStats">
        <div class="loading-spinner">加载中...</div>
      </template>
      <template v-else>
        <v-chart class="chart" :option="categoryChartOption" autoresize />
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useToast } from 'vue-toastification';
import { getStatsByDate, getStatsByCategory } from '@/api/stats';

const toast = useToast();
const generalStats = ref({});
const categoryChartOption = ref({});
const isLoadingGeneralStats = ref(true);
const isLoadingCategoryStats = ref(true);

// 格式化综合统计数据
const formattedGeneralStats = computed(() => {
  const stats = generalStats.value || {};
  return {
    totalOrders: { 
      label: '总订单数', 
      value: stats.totalOrders || 0 
    },
    todayOrders: { 
      label: '今日订单', 
      value: stats.todayOrders || 0
    },
    totalUsers: { 
      label: '总用户数', 
      value: stats.totalUsers || 0 
    },
    todayUsers: { 
      label: '今日新增用户', 
      value: stats.todayUsers || 0
    },
    totalBooks: { 
      label: '总书本数', 
      value: stats.totalBooks || 0 
    },
    todayBooks: { 
      label: '今日新增书本', 
      value: stats.todayBooks || 0
    },
    totalStock: { 
      label: '总库存量', 
      value: stats.totalStock || 0,
      formattedValue: stats.totalStock ? `${stats.totalStock}` : '0',
      suffix: '本'
    },
    todayAddedStock: { 
      label: '今日新增库存',
      value: stats.todayAddedStock || 0,
      prefix: '+'
    },
    // yesterdayDecreasedStock: { 
    //   label: '昨日减少库存',
    //   value: Math.abs(stats.yesterdayDecreasedStock) || 0,
    //   isNegative: true,
    //   prefix: '-'
    // }
  };
});

// 获取综合统计数据
const fetchGeneralStats = async () => {
  try {
    const response = await getStatsByDate();
    if (response) {
      generalStats.value = response;
    } else {
      toast.error('获取综合统计数据时，接口返回的数据格式不正确');
    }
  } catch (error) {
    toast.error('获取统计数据失败: ' + error.message);
  } finally {
    isLoadingGeneralStats.value = false;
  }
};

// 获取分类统计数据并渲染图表
const fetchCategoryStats = async () => {
  try {
    const response = await getStatsByCategory();
    if (response) {
      const data = response.map(item => ({
        name: item.categoryName || '未分类',
        value: item.bookCount || 0
      }));

      categoryChartOption.value = {
        title: { 
          text: '图书分类分布',
          left: 'center',
          textStyle: { fontSize: 16 }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}本 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: data.map(item => item.name)
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          label: {
            show: true,
            formatter: '{b|{b}}\n{c}本',
            rich: { b: { fontWeight: 'bold' } }
          },
          data: data
        }]
      };
    }
  } catch (error) {
    toast.error('获取分类统计失败: ' + error.message);
  } finally {
    isLoadingCategoryStats.value = false;
  }
};

onMounted(() => {
  fetchGeneralStats();
  fetchCategoryStats();
});
</script>

<style scoped>
.stats-container {
  padding: 20px;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
  position: relative;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-title {
  color: #666;
  font-size: 14px;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 26px;
  font-weight: 600;
  color: #333;
  display: flex;
  align-items: baseline;
}

.stat-suffix {
  font-size: 14px;
  color: #666;
  margin-left: 4px;
  font-weight: normal;
}

.negative-stat .stat-value {
  color: #ef4444;
}

.chart-container {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: relative;
}

.chart {
  height: 500px;
  width: 100%;
}

.loading-spinner {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 16px;
  color: #888;
}
</style>