import request from "../utils/request";
//获取所有评论
export const reviewlist = (params) => {
  return request.get(`/reviews`, { params });
};
//批量删除评论
export const deleteReviews = (ids) => {
  return request.delete(`/reviews/${ids}`);
};
//根据书本id获取评论
export const getReview = (bookId) => {
  return request.get(`/reviews/book/${bookId}`) // 匹配后端路径
}
//根据用户id根据书本id添加评论
export const addReviews = (bookId,data) => {
  return request.post(`/reviews/books/${bookId}`,data);
};
//根据jwt令牌的userId获取评论
export const getReviewByUserId = () => {
  return request.get(`/reviews/me`);
};