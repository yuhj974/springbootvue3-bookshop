import request from '../utils/request';

// 分页查询轮播图
export const listCarousels = (params) => {
  return request.get(`/carousel/list`, { params });
};

// 新增轮播图
export const addCarousel = (carousel) => {
  return request.post(`/carousel/add`, carousel);
};

// 更新轮播图
export const updateCarousel = (carousel) => {
  return request.put(`/carousel/update`, carousel);
};

// 删除/批量删除轮播图
export const deleteCarousels = (ids) => {
  return request.delete(`/carousel/delete`, { data: ids });
};