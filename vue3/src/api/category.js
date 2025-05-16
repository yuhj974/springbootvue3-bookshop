import request from "../utils/request";

/* 获取分类列表 */
export const categorylist = (params) => {
  return request.get(`/categorys`, {
    params: { // 添加params参数传递
      ...params,
      // 适配常见后端参数命名
      page: params?.page,
      pageSize: params?.size,
      name: params?.name
    }
  })
}
/* 添加分类 */
export const addCategory = data => {
  return request.post(`/categorys`, data);
};
/* 删除分类 */
export const deleteCategory = id => {
  return request.delete(`/categorys/${id}`);
};
/* 根据id获取分类 */
export const getCategoryById = id => {
  return request.get(`/categorys/${id}`);
};
/* 修改分类 */
export const updateCategory = data => {
  return request.put(`/categorys`, data);
};

export const getCategory = () => {
  return request.get(`/categorys/all`);

};

