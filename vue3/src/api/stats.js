import request from "../utils/request";
export const getStatsByDate = () => {
  return request.get(`/stats/general`);
};
//获取分类统计数据
export const getStatsByCategory = () => {
  return request.get(`/stats/category`);
};
