import request from "../utils/request";
/* 获取用户列表 */
export const userlist = (params) => {
  return request.get(`/users`, { params });
};
/* 添加用户 */
export const addUser = data =>{
  return  request.post("/users", data);
}
/* 根据id查询用户 */
export const getUserById = (id) =>{
  return  request.get(`/users/${id}`);
}
/* 修改用户 */
export const updateUser = (data) => {
  return  request.put(`/users`, data);
}
/* 批量删除用户 */
export const deleteUsers = (ids) => {
 return  request.delete(`/users/${ids}`);
}
//根据jwt令牌的用户id查询用户
export const getUserByJwt = () => {
  return request.get("/users/me");
}
//用户修改密码
export const updatePassword = (data) => {
  return request.put("/users/password", data);
}


