import request from "../utils/request";
// export const booklist = ({
//   pageNum = 1,
//   pageSize = 100,
//   name = "",
//   author = "",
//   status = "",
//   categoryName = ""
// } = {}) => {
//   const query = new URLSearchParams();
//   query.append("pageNum", pageNum);
//   query.append("pageSize", pageSize);
//   if (name) query.append("name", name);
//   if (author) query.append("author", author);
//   if (status) query.append("status", status);
//   if (categoryName) query.append("categoryName", categoryName);
//  return request.get(`/books/all?${query.toString()}`);
// };
export const booklist = (params) => {
  return request.get("/books/all", {params });
};
export const getbooklist = (params) => {
  return request.get("/books/full", {params});
};
 

// 管理员添加图书
export const addBook = data => {
  return request.post("/books", data);
};

// 批量删除图书
export const deleteBooks = (ids) => {
  return request.delete(`/books/${ids}`);
};
//根据id查询图书
export const getBookById = (id) => {
  return request.get(`/books/${id}`);
};
// 修改图书
export const updateBook = (data) => {
  return request.put(`/books`, data);
};

// 设置单本书籍推荐状态
export const setBookRecommend = (id, recommendStatus) => {
  return request.put(`/books/recommend/${id}`, null, {
    params: { recommendStatus }
  });
};


//获取推荐书本列表
export const getRecommendBooks = () => {
  return request.get(`/books/recommended`);
};


