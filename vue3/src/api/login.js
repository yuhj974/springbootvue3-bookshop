import request from "../utils/request";
//登录
export const login = (data) => {
  return request.post("/login", data);
};
//用户注册
export const register = (data) => {
  return request.post("/register", data);
};
//获取操作日志信息
export const getLog = (params ) => {
  return request.get("/operateLog",{ params });
};
//批量删除操作日志
export const deleteLog = (ids) => {
  return request.delete(`/operateLog/${ids}`);
};
