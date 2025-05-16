import request from "../utils/request";
//使用对象参数 + 默认值 + URLSearchParams
export const booklist = ({
  pageNum = 1,
  pageSize = 10,
  name = "",
  author = "",
  status = "",
  categoryName = ""
} = {}) => {
  const query = new URLSearchParams();
  query.append("pageNum", pageNum);
  query.append("pageSize", pageSize);
  if (name) query.append("name", name);
  if (author) query.append("author", author);
  if (status) query.append("status", status);
  if (categoryName) query.append("categoryName", categoryName);
 return request.get(`/books/all?${query.toString()}`);
};
// export const booklist = (params) => {
//   return request.get("/books/all", { params });
// };
 

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


