import request from "../utils/request";
// 获取所有地址
export const addresslist = (params) => {
  return request.get(`/addresses`, { params });
};
//批量删除地址
export const deleteAddress = (ids) => {
  return request.delete(`/addresses/${ids}`);
};
//根据jwt令牌获取当前用户的地址列表
export const getAddress = () => {
  return request.get(`/addresses/me`);
};
//根据jwt令牌用户id添加地址
export const addAddress = (data) => {
  return request.post(`/addresses`, data);
};
//修改地址
export const updateAddress = (data) => {
  return request.put(`/addresses`, data);
};
//根据id获取地址
export const getAddressById = (id) => {
  return request.get(`/addresses/${id}`);
};