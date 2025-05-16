import request from "../utils/request";
//获取所有管理员
export const adminlist = (params) => {
  return request.get(`/admins`, { params });
};
//添加管理员
export const addAdmin = data => {
  return request.post(`/admins`, data);
};
//删除管理员
export const deleteAdmins = (id) => {
  return request.delete(`/admins/${id}`);
};
//根据id获取管理员
export const getAdminById = (id) => {
  return request.get(`/admins/${id}`);
};
//更新管理员
export const updateAdmin = (data) => {
  return request.put(`/admins`, data);
};
//根据jwt令牌的id查询管理员
export const getAdminByJwt = () => {
  return request.get(`/admins/me`);
};
