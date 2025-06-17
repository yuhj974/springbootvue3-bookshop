/*
 Navicat Premium Data Transfer

 Source Server         : yu
 Source Server Type    : MySQL
 Source Server Version : 80025 (8.0.25)
 Source Host           : localhost:3306
 Source Schema         : bookshop

 Target Server Type    : MySQL
 Target Server Version : 80025 (8.0.25)
 File Encoding         : 65001

 Date: 12/06/2025 00:37:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` int NOT NULL COMMENT '关联用户ID',
  `consignee` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细地址',
  `is_default` tinyint NULL DEFAULT 0 COMMENT '是否默认地址：1-是 0-否',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户收货地址' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of address
-- ----------------------------
INSERT INTO `address` VALUES (1, 1, '张三', '13812345678', '朝阳门大街100号', 0, '2025-03-08 17:01:35', '2025-06-11 22:54:48');
INSERT INTO `address` VALUES (6, 2, '李四', '13812345678', '下津路12号', 1, '2025-05-03 17:28:02', '2025-05-03 17:28:02');
INSERT INTO `address` VALUES (11, 1, '西北老汉', '14725836978', '机电', 0, '2025-05-11 22:37:02', '2025-06-11 22:54:48');
INSERT INTO `address` VALUES (12, 3, '李白', '19612345678', '四川绵州', 0, '2025-05-11 23:22:05', '2025-05-16 19:57:46');
INSERT INTO `address` VALUES (16, 3, '孟浩然', '18798547123', '襄州襄阳', 0, '2025-05-16 19:48:05', '2025-05-16 19:57:46');
INSERT INTO `address` VALUES (20, 3, '托比欧', '17845632104', '意大利撒丁岛', 1, '2025-05-16 19:57:46', '2025-05-16 19:57:46');
INSERT INTO `address` VALUES (23, 1, '岸边露伴', '14725414789', '杜王町', 1, '2025-06-11 22:54:48', '2025-06-11 22:54:48');
INSERT INTO `address` VALUES (25, 19, '花京院', '198453624145', '埃及', 1, '2025-06-12 00:18:41', '2025-06-12 00:18:41');
INSERT INTO `address` VALUES (26, 19, '刘裕', '18945647852', '江苏南京', 0, '2025-06-12 00:19:51', '2025-06-12 00:19:51');

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密后的密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ADMIN' COMMENT '角色默认ADMIN',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ADMIN' COMMENT '名字（默认ADMIN）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'yu', 'e10adc3949ba59abbe56e057f20f883e', 'http://localhost:8080/files/download/1748190799463_3.png', 'ADMIN', '有官我就录', '2025-03-08 16:50:00', '2025-05-26 22:35:02');
INSERT INTO `admin` VALUES (2, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'http://localhost:8080/files/download/1748712926605_1.png', 'ADMIN', '王嘉洛', '2025-03-08 17:01:35', '2025-06-01 01:35:27');
INSERT INTO `admin` VALUES (5, 'koll', 'e10adc3949ba59abbe56e057f20f883e', 'http://localhost:8080/files/download/1748705646630_2.jpg', 'ADMIN', 'ADMIN', '2025-05-03 16:38:55', '2025-05-31 23:34:08');
INSERT INTO `admin` VALUES (7, 'god', 'e10adc3949ba59abbe56e057f20f883e', 'http://localhost:8080/files/download/1749478937390_7.png', 'SUPERADMIN', '萝莉斯', '2025-05-31 22:21:22', '2025-06-09 22:22:19');

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '图书ID',
  `category_id` int NOT NULL COMMENT '所属分类ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图书名称',
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '作者',
  `price` decimal(10, 2) NOT NULL COMMENT '销售价格',
  `stock` int NULL DEFAULT 0 COMMENT '库存数量',
  `cover_image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '封面图片URL',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '图书详情描述',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1-上架 0-下架',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `is_recommended` tinyint NULL DEFAULT 0 COMMENT '\'是否为推荐书本：1-是，0-否\'',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图书信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (1, 3, '活着', '余华', 39.90, 77, 'http://localhost:8080/files/download/1748261640245_1.jpg', '余华代表作，讲述普通人在历史洪流中的苦难与坚韧', 1, '2023-03-08 17:01:35', '2025-06-11 17:54:19', 1);
INSERT INTO `book` VALUES (2, 5, 'Java编程思想', 'Bruce Eckel', 108.00, 41, 'http://localhost:8080/files/download/1748261690645_3.jpg', '深入理解Java编程语言与面向对象思想的权威指南', 1, '2023-03-08 17:01:35', '2025-06-12 00:22:08', 1);
INSERT INTO `book` VALUES (3, 4, '斗破苍穹', '天蚕土豆', 45.00, 77, 'http://localhost:8080/files/download/1748261752989_4.jpg', '网络文学经典之作，少年逆袭的热血玄幻故事', 1, '2023-03-08 17:01:35', '2025-06-07 19:34:41', 1);
INSERT INTO `book` VALUES (8, 1, '百年孤独', '加西亚·马尔克斯', 68.50, 95, 'http://localhost:8080/files/download/1748261794974_5.jpg', '魔幻现实主义文学巅峰，布恩迪亚家族七代人的传奇', 1, '2023-05-02 01:07:13', '2025-06-05 00:06:49', 0);
INSERT INTO `book` VALUES (9, 2, '人类简史', '尤瓦尔·赫拉利', 88.00, 37, 'http://localhost:8080/files/download/1748261835835_6.jpg', '从认知革命到科技时代，俯瞰人类发展全景', 1, '2023-05-02 01:13:46', '2025-06-11 22:55:08', 1);
INSERT INTO `book` VALUES (10, 6, '三体', '刘慈欣', 59.00, 7, 'http://localhost:8080/files/download/1748262098739_8.jpg', '中国科幻里程碑，描绘宇宙文明间的黑暗森林法则', 1, '2023-05-02 01:14:17', '2025-06-07 23:05:26', 1);
INSERT INTO `book` VALUES (11, 3, '经济学原理', '曼昆', 99.00, 18, 'http://localhost:8080/files/download/1748262197022_7.jpg', '全球畅销的经济学入门教材，系统解析微观与宏观经济学', 1, '2023-05-02 01:15:41', '2025-06-05 13:20:46', 0);
INSERT INTO `book` VALUES (13, 1, '追风筝的人', '卡勒德·胡赛尼', 42.00, 1, 'http://localhost:8080/files/download/1748262235966_9.jpg', '关于背叛与救赎的感人故事，跨越阿富汗数十年的变迁', 1, '2023-05-02 01:16:14', '2025-05-26 20:23:57', 0);
INSERT INTO `book` VALUES (14, 4, '雪中悍刀行', '烽火戏诸侯', 55.00, 43, 'http://localhost:8080/files/download/1748262283924_10.jpg', '武侠玄幻巨著，庙堂与江湖交织的权谋史诗', 1, '2023-05-02 01:16:48', '2025-06-11 22:53:18', 1);
INSERT INTO `book` VALUES (15, 3, 'Python编程：从入门到实践', 'Eric Matthes', 89.00, 30, 'http://localhost:8080/files/download/1748262329795_11.jpg', '零基础学习Python，通过项目实战掌握核心技能', 1, '2023-05-02 13:19:23', '2025-06-06 23:42:24', 1);
INSERT INTO `book` VALUES (23, 5, '算法导论', 'Thomas H. Cormen', 128.00, 19, 'http://localhost:8080/files/download/1748262386673_12.png', '计算机算法领域的权威教材，涵盖经典算法与数据结构', 1, '2023-05-02 15:47:15', '2025-06-07 23:05:26', 1);
INSERT INTO `book` VALUES (24, 2, '时间简史', '斯蒂芬·霍金', 75.00, 40, 'http://localhost:8080/files/download/1748262442787_13.jpg', '探索宇宙的起源与未来，解读时间与空间的奥秘', 1, '2023-05-02 19:35:17', '2025-06-06 23:43:45', 1);
INSERT INTO `book` VALUES (26, 9, '哈利·波特与魔法石', 'J.K.罗琳', 49.90, 34, 'http://localhost:8080/files/download/1748262496356_14.jpg', '魔法世界的开篇之作，少年哈利的奇幻冒险之旅', 0, '2023-05-02 23:30:02', '2025-06-06 23:43:48', 0);
INSERT INTO `book` VALUES (27, 9, '小王子', '安托万·德·圣-埃克苏佩里', 29.90, 93, 'http://localhost:8080/files/download/1748262544586_15.jpg', '治愈心灵的哲理童话，探索爱与责任的永恒主题', 1, '2023-05-07 23:41:53', '2025-06-11 22:53:18', 0);
INSERT INTO `book` VALUES (29, 1, '测试01', '测试', 45.00, 56, 'http://localhost:8080/files/download/1749635705183_80eaf8029d2a07c03fec79e7e011347a295072747.png', '测试', 1, '2025-06-11 17:55:09', '2025-06-11 17:55:09', 0);

-- ----------------------------
-- Table structure for carousel
-- ----------------------------
DROP TABLE IF EXISTS `carousel`;
CREATE TABLE `carousel`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片URL',
  `book_id` int NULL DEFAULT NULL COMMENT '关联图书ID（可选）',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '轮播图标题',
  `sort` int NULL DEFAULT 0 COMMENT '排序（数字越小越靠前）',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `book_id`(`book_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '轮播图表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of carousel
-- ----------------------------
INSERT INTO `carousel` VALUES (3, 'http://localhost:8080/files/download/1749374713269_1280.png', 1, '普通人在历史洪流中的苦难与坚韧', 1, 1, '2025-06-08 17:25:14', '2025-06-08 17:25:14');
INSERT INTO `carousel` VALUES (5, 'http://localhost:8080/files/download/1749317985349_3b0308ebb73389d0b1f050431e2f5ceef2108af6.jpg', 10, '中国科幻里程碑1', 2, 1, '2025-06-11 23:03:52', '2025-06-11 23:03:52');
INSERT INTO `carousel` VALUES (7, 'http://localhost:8080/files/download/1749652496029_图片尺寸修改 (1).png', 3, '少年逆袭的热血玄幻故事', 3, 1, '2025-06-11 22:34:57', '2025-06-11 22:34:57');

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '购物车项ID',
  `user_id` int NOT NULL COMMENT '关联用户ID',
  `book_id` int NOT NULL COMMENT '关联图书ID',
  `quantity` int NULL DEFAULT 1 COMMENT '购买数量',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_user_book`(`user_id` ASC, `book_id` ASC) USING BTREE,
  INDEX `book_id`(`book_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 151 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户购物车' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (149, 1, 9, 3, '2025-06-12 00:36:35', '2025-06-12 00:36:35');
INSERT INTO `cart` VALUES (150, 1, 23, 6, '2025-06-12 00:36:45', '2025-06-12 00:36:45');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图书分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '文学小说', '2025-03-08 17:01:35', '2025-05-02 22:32:20');
INSERT INTO `category` VALUES (2, '科学技术', '2025-03-08 17:01:35', '2025-03-08 17:01:35');
INSERT INTO `category` VALUES (3, '中国文学', '2025-03-08 17:01:35', '2025-03-08 17:01:35');
INSERT INTO `category` VALUES (4, '网络小说', '2025-03-08 17:01:35', '2025-03-08 17:01:35');
INSERT INTO `category` VALUES (5, '计算机', '2025-03-08 17:01:35', '2025-05-02 22:30:10');
INSERT INTO `category` VALUES (6, '历史', '2025-04-25 15:54:08', '2025-04-25 15:54:08');
INSERT INTO `category` VALUES (9, '军事', '2025-05-02 22:03:20', '2025-05-13 01:36:51');
INSERT INTO `category` VALUES (27, '温柔1', '2025-06-11 22:58:55', '2025-06-11 22:59:01');

-- ----------------------------
-- Table structure for operate_log
-- ----------------------------
DROP TABLE IF EXISTS `operate_log`;
CREATE TABLE `operate_log`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `operate_emp_id` int UNSIGNED NULL DEFAULT NULL COMMENT '操作人ID',
  `operate_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作的类名',
  `method_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作的方法名',
  `method_params` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法参数',
  `return_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '返回值, 存储json格式',
  `cost_time` int NULL DEFAULT NULL COMMENT '方法执行耗时, 单位:ms',
  `operation_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名字',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operate_log
-- ----------------------------
INSERT INTO `operate_log` VALUES (23, 7, '2025-06-05 13:42:59', 'com.bookserve.controller.CategoryController', 'addCategory', '[Category(id=null, name=111, createTime=2025-06-05T13:42:59.382976800, updateTime=2025-06-05T13:42:59.382976800)]', 'Result(code=1, msg=success, data=null)', 7, '新增分类');
INSERT INTO `operate_log` VALUES (24, 1, '2025-06-05 13:44:26', 'com.bookserve.controller.CategoryController', 'addCategory', '[Category(id=null, name=222, createTime=2025-06-05T13:44:25.638359500, updateTime=2025-06-05T13:44:25.638359500)]', 'Result(code=1, msg=success, data=null)', 6, '新增分类');
INSERT INTO `operate_log` VALUES (25, 5, '2025-06-11 17:55:09', 'com.bookserve.controller.BookController', 'addBook', '[Book(id=null, categoryId=null, title=测试01, author=测试, price=45, stock=56, coverImage=http://localhost:8080/files/download/1749635705183_80eaf8029d2a07c03fec79e7e011347a295072747.png, description=测试, status=1, createTime=2025-06-11T17:55:09.314800600, updateTime=2025-06-11T17:55:09.314800600, isRecommended=0, categoryName=文学小说)]', 'Result(code=1, msg=success, data=null)', 7, '新增书本');
INSERT INTO `operate_log` VALUES (26, 1, '2025-06-11 22:57:54', 'com.bookserve.controller.CategoryController', 'addCategory', '[Category(id=null, name=测试, createTime=2025-06-11T22:57:54.045037400, updateTime=2025-06-11T22:57:54.045037400)]', 'Result(code=1, msg=success, data=null)', 5, '新增分类');
INSERT INTO `operate_log` VALUES (27, 1, '2025-06-11 22:58:55', 'com.bookserve.controller.CategoryController', 'addCategory', '[Category(id=null, name=温柔, createTime=2025-06-11T22:58:54.956943200, updateTime=2025-06-11T22:58:54.956943200)]', 'Result(code=1, msg=success, data=null)', 6, '新增分类');

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '明细项ID',
  `order_id` int NOT NULL COMMENT '关联订单ID',
  `book_id` int NOT NULL COMMENT '图书ID',
  `price` decimal(10, 2) NOT NULL COMMENT '下单时单价（价格快照）',
  `quantity` int NOT NULL COMMENT '购买数量',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_id`(`order_id` ASC) USING BTREE,
  INDEX `book_id`(`book_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 70 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (58, 107, 10, 59.00, 2, '2025-06-05 13:20:46', '2025-06-05 13:20:46');
INSERT INTO `order_item` VALUES (59, 107, 11, 99.00, 1, '2025-06-05 13:20:46', '2025-06-05 13:20:46');
INSERT INTO `order_item` VALUES (60, 107, 14, 55.00, 1, '2025-06-05 13:20:46', '2025-06-05 13:20:46');
INSERT INTO `order_item` VALUES (61, 108, 3, 45.00, 1, '2025-06-07 00:27:24', '2025-06-07 00:27:24');
INSERT INTO `order_item` VALUES (62, 109, 23, 128.00, 1, '2025-06-07 18:13:02', '2025-06-07 18:13:02');
INSERT INTO `order_item` VALUES (63, 110, 10, 59.00, 1, '2025-06-07 23:05:26', '2025-06-07 23:05:26');
INSERT INTO `order_item` VALUES (64, 110, 23, 128.00, 2, '2025-06-07 23:05:26', '2025-06-07 23:05:26');
INSERT INTO `order_item` VALUES (65, 111, 14, 55.00, 3, '2025-06-11 22:52:26', '2025-06-11 22:52:26');
INSERT INTO `order_item` VALUES (66, 112, 14, 55.00, 1, '2025-06-11 22:53:18', '2025-06-11 22:53:18');
INSERT INTO `order_item` VALUES (67, 112, 27, 29.90, 5, '2025-06-11 22:53:18', '2025-06-11 22:53:18');
INSERT INTO `order_item` VALUES (68, 113, 9, 88.00, 1, '2025-06-11 22:55:09', '2025-06-11 22:55:09');
INSERT INTO `order_item` VALUES (69, 114, 2, 108.00, 3, '2025-06-12 00:22:09', '2025-06-12 00:22:09');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `user_id` int NOT NULL COMMENT '下单用户ID',
  `order_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号（业务生成）',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单状态：已付款/已发货/已完成',
  `address_id` int NOT NULL COMMENT '收货地址ID',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '确认收货时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_number`(`order_number` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `address_id`(`address_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 115 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (107, 1, 'ORDER17491008462592623', 272.00, '已完成', 1, '2025-06-05 13:20:46', '2025-06-05 13:21:03', NULL, '2025-06-05 13:20:46', '2025-06-05 14:05:27');
INSERT INTO `orders` VALUES (108, 1, 'ORDER17492272444516627', 45.00, '已完成', 11, '2025-06-07 00:27:24', '2025-06-11 22:57:29', NULL, '2025-06-07 00:27:24', '2025-06-12 00:35:22');
INSERT INTO `orders` VALUES (109, 1, 'ORDER17492911819328935', 128.00, '已发货', 11, '2025-06-07 18:13:02', '2025-06-11 22:57:31', NULL, '2025-06-07 18:13:02', '2025-06-11 22:57:30');
INSERT INTO `orders` VALUES (110, 3, 'ORDER17493087263545409', 315.00, '已完成', 20, '2025-06-07 23:05:26', '2025-06-07 23:06:58', NULL, '2025-06-07 23:05:26', '2025-06-07 23:10:48');
INSERT INTO `orders` VALUES (111, 1, 'ORDER17496535462928852', 165.00, '已发货', 11, '2025-06-11 22:52:26', '2025-06-11 22:57:34', NULL, '2025-06-11 22:52:26', '2025-06-11 22:57:33');
INSERT INTO `orders` VALUES (112, 1, 'ORDER17496535983367277', 204.50, '已付款', 1, '2025-06-11 22:53:18', NULL, NULL, '2025-06-11 22:53:18', '2025-06-11 22:53:18');
INSERT INTO `orders` VALUES (113, 1, 'ORDER17496537086073020', 88.00, '已发货', 23, '2025-06-11 22:55:09', '2025-06-11 22:57:37', NULL, '2025-06-11 22:55:09', '2025-06-11 22:57:37');
INSERT INTO `orders` VALUES (114, 19, 'ORDER17496589286911983', 324.00, '已付款', 25, '2025-06-12 00:22:09', NULL, NULL, '2025-06-12 00:22:09', '2025-06-12 00:22:09');

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `user_id` int NOT NULL COMMENT '评价用户ID',
  `book_id` int NOT NULL COMMENT '关联图书ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评价内容',
  `rating` tinyint NOT NULL COMMENT '评分（1-5星）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '评价时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `book_id`(`book_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图书评价表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of review
-- ----------------------------
INSERT INTO `review` VALUES (36, 2, 3, '爱吃', 5, '2025-06-01 01:36:31', '2025-06-01 01:36:31');
INSERT INTO `review` VALUES (37, 1, 10, '好', 5, '2025-06-05 14:05:42', '2025-06-05 14:05:42');
INSERT INTO `review` VALUES (38, 3, 23, '学完了，已经入职美团大厂了', 5, '2025-06-07 23:11:41', '2025-06-07 23:11:41');
INSERT INTO `review` VALUES (39, 1, 14, '666', 5, '2025-06-08 22:46:50', '2025-06-08 22:46:50');
INSERT INTO `review` VALUES (42, 1, 3, '斗破苍穹', 5, '2025-06-12 00:36:08', '2025-06-12 00:36:08');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密后的密码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话（加密存储）',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL地址',
  `status` tinyint NULL DEFAULT 1 COMMENT '用户状态：1-正常 0-禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'USER' COMMENT '角色默认USER',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'user' COMMENT '用户昵称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'aaa', 'e10adc3949ba59abbe56e057f20f883e', '12345678901@163.com', '13022200334', 'http://localhost:8080/files/download/1749658464368_aa6d-iskepxs7672084.jpg', 1, '2025-03-07 16:48:57', '2025-06-12 00:14:26', 'USER', '我是可口可乐');
INSERT INTO `user` VALUES (2, 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', 'zhangsan@bookstore.com', '13812345678', 'http://localhost:8080/files/2004.jpg', 0, '2025-03-08 17:01:35', '2025-06-06 13:31:05', 'USER', '妲莉丝');
INSERT INTO `user` VALUES (3, 'lisi', 'e10adc3949ba59abbe56e057f20f883e', 'lisi@bookstore.com', '13987654321', 'http://localhost:8080/files/download/1749305267124_R-C.jpg', 1, '2025-03-08 17:01:35', '2025-06-08 17:04:58', 'USER', '迪亚波罗');
INSERT INTO `user` VALUES (13, 'bnm', 'e10adc3949ba59abbe56e057f20f883e', '56@qq.com', '', 'http://localhost:8080/files/download/1749478969994_80eaf8029d2a07c03fec79e7e011347a295072747.png', 0, '2025-05-03 13:05:53', '2025-06-09 22:22:51', 'user', 'user');
INSERT INTO `user` VALUES (19, 'lop', 'e10adc3949ba59abbe56e057f20f883e', '1345234@qq.com', '18736925814', 'http://localhost:8080/files/download/1749478977625_7f4c072001090e713453d19cfe0ac3fd295072747.png', 1, '2025-05-06 20:35:20', '2025-06-09 22:22:58', 'user', '遇到纠纷');
INSERT INTO `user` VALUES (21, 'lopk', 'e10adc3949ba59abbe56e057f20f883e', '327628764@qq.com', '13022200666', 'http://localhost:8080/files/download/1749478983721_511ae5e30ba05d0b5005705809a2ccbb295072747.png', 1, '2025-05-08 13:32:41', '2025-06-09 22:23:04', 'user', '图集');

SET FOREIGN_KEY_CHECKS = 1;
