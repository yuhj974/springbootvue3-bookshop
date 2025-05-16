import request from "../utils/request";
//获取所有购物车
export const cartlist = (params) => {
  return request.get(`/carts`, { params });
};
//批量删除购物车
export const deleteCarts = (ids) => {
  return request.delete(`/carts/${ids}`);
};
//根据jwt令牌里的用户id获取购物车
export const getCartByUserId = () => {
  return request.get(`/carts/userid`);
};
//根据jwt令牌里的用户id再根据书本id添加购物车
export const addCart = (data) => {
  return request.post(`/carts/items`, data);
};
//添加购物车书本数量
export const updateCart = (id,quantity) => {
  return request.put(`/carts/items/${id}/${quantity}`);
};

