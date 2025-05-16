import request from "../utils/request";
//登录
export const login = (data) => {
  return request.post("/login", data);
};
//用户注册
export const register = (data) => {
  return request.post("/register", data);
};
