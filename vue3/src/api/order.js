import request from "../utils/request";
// 获取所有订单
export const orderlist = (params = {}) => {
  return request.get(`/orders`, { params });
};
//批量删除订单
export const deleteOrders = (ids) => {
  return request.delete(`/orders/${ids}`);
};
//根据订单id获取订单明细
export const getOrderById = (ItemId) => {
  return request.get(`/orderItems/order/${ItemId}`);
};
//改变订单状态为已发货
export const deliverOrder = (id) => {
  return request.put(`/orders/${id}/deliver`);
};
//根据jwt令牌的用户id获取订单
export const getOrderByUserId = () => {
  return request.get(`/orders/me`);
};
//改变订单状态为已完成
export const completeOrder = (id) => {
  return request.put(`/orders/${id}/receive`);
};
//根据jwt令牌里的用户id再根据书本id添加用户订单
export const addOrder = (data) => {
  return request.post(`/orders/me`,data);
};
//根据jwt令牌里的用户id再根据书本id添加用户订单
export const addOrder2 = (data) => {
  return request.post(`/orders/add`,data);
};
