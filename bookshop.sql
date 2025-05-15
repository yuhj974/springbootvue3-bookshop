/*
 Navicat Premium Data Transfer

 Source Server         : ji
 Source Server Type    : MySQL
 Source Server Version : 80025 (8.0.25)
 Source Host           : localhost:3306
 Source Schema         : bookshop

 Target Server Type    : MySQL
 Target Server Version : 80025 (8.0.25)
 File Encoding         : 65001

 Date: 15/05/2025 19:19:24
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
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户收货地址' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of address
-- ----------------------------
INSERT INTO `address` VALUES (1, 1, '张三6', '13812345678', '朝阳门大街100号', 0, '2025-03-08 17:01:35', '2025-05-15 01:24:58');
INSERT INTO `address` VALUES (6, 2, '李四', '13812345678', '热特如果', 0, '2025-05-03 17:28:02', '2025-05-03 17:28:02');
INSERT INTO `address` VALUES (11, 1, 'jojo', '14725836978', '机电', 1, '2025-05-11 22:37:02', '2025-05-15 01:24:58');
INSERT INTO `address` VALUES (12, 3, '打火机', '19612345678', '软件2302', 1, '2025-05-11 23:22:05', '2025-05-11 23:22:05');
INSERT INTO `address` VALUES (13, 19, '文档', '18936985474', '1111', 1, '2025-05-13 23:56:20', '2025-05-13 23:56:20');

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'yu', 'e10adc3949ba59abbe56e057f20f883e', 'http://localhost:8080/files/download/1747048203897_1.png', 'ADMIN', '团购会', '2025-03-08 16:50:00', '2025-05-15 19:19:10');
INSERT INTO `admin` VALUES (2, 'admin', 'e10adc3949ba59abbe56e057f20f883e', NULL, 'ADMIN', 'ADMIN', '2025-03-08 17:01:35', '2025-03-08 17:01:35');
INSERT INTO `admin` VALUES (5, 'koll', 'e10adc3949ba59abbe56e057f20f883e', 'http://localhost:8080/files/download/1746261533621_554704010110247455752f63d655f6384cbb2f2f9873dd52.jpg', 'ADMIN', 'ADMIN', '2025-05-03 16:38:55', '2025-05-08 13:04:21');
INSERT INTO `admin` VALUES (6, 'nm', 'e10adc3949ba59abbe56e057f20f883e', 'http://localhost:8080/files/download/1746635812894_554704010110247455752f63d655f6384cbb2f2f9873dd52.jpg', 'ADMIN', '啊大大', '2025-05-08 00:36:54', '2025-05-08 13:04:24');

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
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图书信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (1, 3, '活着', '余华', 39.90, 83, 'http://localhost:8080/files/2004.jpg', '经典现实主义文学作品', 1, '2025-03-08 17:01:35', '2025-05-15 01:25:54');
INSERT INTO `book` VALUES (2, 5, 'Java编程思想', 'Bruce Eckel', 108.00, 48, 'http://localhost:8080/files/2004.jpg', 'Java学习必读经典', 1, '2025-03-08 17:01:35', '2025-05-15 01:25:54');
INSERT INTO `book` VALUES (3, 4, '斗破苍穹', '天蚕土豆', 45.00, 99, 'http://localhost:8080/files/download/1747199764045_2.jpg', '经典玄幻小说', 1, '2025-03-08 17:01:35', '2025-05-14 13:16:05');
INSERT INTO `book` VALUES (8, 1, '大师傅似的', '若是', 20.30, 100, 'http://localhost:8080/files/download/1746188367219_bf9d98b49a7d911a3618f85d29d66c471254371692.jpg', '大师傅士大夫', 1, '2025-05-02 01:07:13', '2025-05-02 20:19:29');
INSERT INTO `book` VALUES (9, 1, '让他地方', '二手房是', 9.20, 40, 'http://localhost:8080/files/download/1746193700492_bf9d98b49a7d911a3618f85d29d66c471254371692.jpg', '阿迪斯', 0, '2025-05-02 01:13:46', '2025-05-02 21:48:23');
INSERT INTO `book` VALUES (10, 1, '的观点', '的法国队', 71.00, 10, 'http://localhost:8080/files/download/1746199879290_bf9d98b49a7d911a3618f85d29d66c471254371692.jpg', '梵蒂冈的', 0, '2025-05-02 01:14:17', '2025-05-02 23:31:20');
INSERT INTO `book` VALUES (11, 6, '龙山', '第三方', 12.00, 0, 'http://localhost:8080/files/download/1746440886287_554704010110247455752f63d655f6384cbb2f2f9873dd52.jpg', '发帖和用法', 1, '2025-05-02 01:15:41', '2025-05-14 02:13:40');
INSERT INTO `book` VALUES (13, 2, '书店', '方式', 80.00, 1, NULL, '二十多', 1, '2025-05-02 01:16:14', '2025-05-02 21:49:19');
INSERT INTO `book` VALUES (14, 4, '双方都是', '第三方是', 45.00, 49, NULL, '的方式', 1, '2025-05-02 01:16:48', '2025-05-14 22:44:38');
INSERT INTO `book` VALUES (15, 1, '书店二五', '士大夫', 12.00, 0, '', '的说法是', 1, '2025-05-02 13:19:23', '2025-05-14 00:48:37');
INSERT INTO `book` VALUES (23, 5, '而是', '223', 22.00, 22, 'http://localhost:8080/files/download/1746172026529_01.png', '222', 1, '2025-05-02 15:47:15', '2025-05-02 15:47:15');
INSERT INTO `book` VALUES (24, 1, '让他地方', '二手房是', 9.20, 40, 'http://localhost:8080/files/download/1746185714317_bf9d98b49a7d911a3618f85d29d66c471254371692.jpg', '阿迪斯', 0, '2025-05-02 19:35:17', '2025-05-02 19:35:17');
INSERT INTO `book` VALUES (25, 3, 'C语言', '玉宏基1', 36.00, 56, NULL, '666', 0, '2025-05-02 19:37:39', '2025-05-02 19:37:39');
INSERT INTO `book` VALUES (26, 9, '567', '人太多', 12.00, 34, 'http://localhost:8080/files/download/1746199800654_bf9d98b49a7d911a3618f85d29d66c471254371692.jpg', '儿童', 0, '2025-05-02 23:30:02', '2025-05-02 23:30:02');
INSERT INTO `book` VALUES (27, 9, '如图', '如图', 34.00, 100, 'http://localhost:8080/files/download/1746632509769_554704010110247455752f63d655f6384cbb2f2f9873dd52.jpg', '豆腐干豆腐干', 1, '2025-05-07 23:41:53', '2025-05-07 23:41:53');

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
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户购物车' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (27, 3, 3, 1, '2025-05-14 23:16:59', '2025-05-14 23:16:59');

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
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图书分类表' ROW_FORMAT = DYNAMIC;

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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operate_log
-- ----------------------------
INSERT INTO `operate_log` VALUES (1, 1, '2025-05-07 22:56:01', 'com.bookserve.controller.CategoryController', 'addCategory', '[Category(id=null, name=第三方, createTime=2025-05-07T22:56:01.431134300, updateTime=2025-05-07T22:56:01.431134300)]', 'Result(code=1, msg=success, data=null)', 6);
INSERT INTO `operate_log` VALUES (2, 1, '2025-05-13 23:57:37', 'com.bookserve.controller.OrderController', 'deleteOrder', '[[63]]', 'Result(code=1, msg=success, data=null)', 17);
INSERT INTO `operate_log` VALUES (3, 1, '2025-05-13 23:57:42', 'com.bookserve.controller.OrderController', 'deleteOrder', '[[71]]', 'Result(code=1, msg=success, data=null)', 7);
INSERT INTO `operate_log` VALUES (4, 1, '2025-05-13 23:57:45', 'com.bookserve.controller.OrderController', 'deleteOrder', '[[70]]', 'Result(code=1, msg=success, data=null)', 8);
INSERT INTO `operate_log` VALUES (5, 1, '2025-05-13 23:57:47', 'com.bookserve.controller.OrderController', 'deleteOrder', '[[69]]', 'Result(code=1, msg=success, data=null)', 5);
INSERT INTO `operate_log` VALUES (6, 1, '2025-05-13 23:57:49', 'com.bookserve.controller.OrderController', 'deleteOrder', '[[68]]', 'Result(code=1, msg=success, data=null)', 5);
INSERT INTO `operate_log` VALUES (7, 1, '2025-05-13 23:57:51', 'com.bookserve.controller.OrderController', 'deleteOrder', '[[67]]', 'Result(code=1, msg=success, data=null)', 5);
INSERT INTO `operate_log` VALUES (8, 1, '2025-05-13 23:57:53', 'com.bookserve.controller.OrderController', 'deleteOrder', '[[66]]', 'Result(code=1, msg=success, data=null)', 8);
INSERT INTO `operate_log` VALUES (9, 1, '2025-05-13 23:58:05', 'com.bookserve.controller.OrderController', 'deleteOrder', '[[65, 64, 62, 61, 59, 51, 60]]', 'Result(code=1, msg=success, data=null)', 7);
INSERT INTO `operate_log` VALUES (10, 1, '2025-05-14 00:59:06', 'com.bookserve.controller.OrderController', 'deleteOrder', '[[77, 76, 75, 74, 73, 72, 22]]', 'Result(code=1, msg=success, data=null)', 18);

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
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (1, 1, 2, 108.00, 1, '2025-03-08 17:01:35', '2025-03-08 17:01:35');
INSERT INTO `order_item` VALUES (2, 1, 3, 45.00, 2, '2025-03-08 17:01:35', '2025-03-08 17:01:35');
INSERT INTO `order_item` VALUES (3, 2, 3, 45.00, 2, '2025-03-08 17:01:35', '2025-03-08 17:01:35');
INSERT INTO `order_item` VALUES (7, 8, 3, 45.00, 2, '2025-05-08 13:07:00', '2025-05-08 13:07:00');
INSERT INTO `order_item` VALUES (8, 9, 1, 39.90, 3, '2025-05-08 13:08:47', '2025-05-08 13:08:47');
INSERT INTO `order_item` VALUES (9, 21, 25, 36.00, 2, '2025-05-13 19:30:32', '2025-05-13 19:30:32');
INSERT INTO `order_item` VALUES (10, 21, 1, 39.90, 1, '2025-05-13 19:30:32', '2025-05-13 19:30:32');
INSERT INTO `order_item` VALUES (21, 78, 1, 39.90, 1, '2025-05-14 01:00:34', '2025-05-14 01:00:34');
INSERT INTO `order_item` VALUES (22, 79, 1, 39.90, 5, '2025-05-14 01:00:51', '2025-05-14 01:00:51');
INSERT INTO `order_item` VALUES (23, 80, 1, 39.90, 5, '2025-05-14 01:11:21', '2025-05-14 01:11:21');
INSERT INTO `order_item` VALUES (24, 81, 3, 45.00, 1, '2025-05-14 01:44:47', '2025-05-14 01:44:47');
INSERT INTO `order_item` VALUES (25, 82, 11, 12.00, 1, '2025-05-14 02:11:07', '2025-05-14 02:11:07');
INSERT INTO `order_item` VALUES (26, 83, 11, 12.00, 1, '2025-05-14 02:12:09', '2025-05-14 02:12:09');
INSERT INTO `order_item` VALUES (27, 84, 11, 12.00, 3, '2025-05-14 02:13:41', '2025-05-14 02:13:41');
INSERT INTO `order_item` VALUES (28, 85, 1, 39.90, 6, '2025-05-14 14:08:18', '2025-05-14 14:08:18');
INSERT INTO `order_item` VALUES (29, 86, 14, 45.00, 1, '2025-05-14 22:44:38', '2025-05-14 22:44:38');
INSERT INTO `order_item` VALUES (30, 87, 1, 39.90, 1, '2025-05-15 01:25:54', '2025-05-15 01:25:54');
INSERT INTO `order_item` VALUES (31, 87, 2, 108.00, 2, '2025-05-15 01:25:54', '2025-05-15 01:25:54');

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
) ENGINE = InnoDB AUTO_INCREMENT = 88 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 1, 'ORDER20230901123456', 201.90, '已完成', 1, NULL, '2025-05-03 22:22:26', NULL, '2025-03-08 17:01:35', '2025-05-11 22:41:23');
INSERT INTO `orders` VALUES (2, 1, 'ORDER20230901987654', 90.00, '已完成', 6, NULL, NULL, NULL, '2025-03-08 17:01:35', '2025-05-03 21:32:32');
INSERT INTO `orders` VALUES (8, 2, 'ORDER20230901985587', 90.00, '已付款', 6, '2025-05-08 13:06:33', NULL, NULL, '2025-05-08 13:06:33', '2025-05-08 13:06:33');
INSERT INTO `orders` VALUES (9, 3, 'ORDER20230901983690', 119.70, '已付款', 6, '2025-05-08 13:08:21', NULL, NULL, '2025-05-08 13:08:21', '2025-05-08 13:08:21');
INSERT INTO `orders` VALUES (21, 1, 'ORDER17471358320227519', 159.97, '已付款', 1, '2025-05-13 19:30:32', NULL, NULL, '2025-05-13 19:30:32', '2025-05-13 19:30:32');
INSERT INTO `orders` VALUES (78, 3, 'ORDER17471556335623503', 39.90, '已完成', 12, '2025-05-14 01:00:34', '2025-05-14 23:58:16', NULL, '2025-05-14 01:00:34', '2025-05-14 23:59:05');
INSERT INTO `orders` VALUES (79, 3, 'ORDER17471556505021442', 199.50, '已完成', 12, '2025-05-14 01:00:51', '2025-05-14 23:58:18', NULL, '2025-05-14 01:00:51', '2025-05-15 00:07:28');
INSERT INTO `orders` VALUES (80, 3, 'ORDER17471562812679520', 199.50, '已发货', 12, '2025-05-14 01:11:21', '2025-05-14 23:58:20', NULL, '2025-05-14 01:11:21', '2025-05-14 23:58:20');
INSERT INTO `orders` VALUES (81, 3, 'ORDER17471582867301014', 45.00, '已发货', 12, '2025-05-14 01:44:47', '2025-05-14 23:58:22', NULL, '2025-05-14 01:44:47', '2025-05-14 23:58:22');
INSERT INTO `orders` VALUES (82, 19, 'ORDER17471598665489206', 12.00, '已发货', 13, '2025-05-14 02:11:07', '2025-05-14 22:46:02', NULL, '2025-05-14 02:11:07', '2025-05-14 22:46:01');
INSERT INTO `orders` VALUES (83, 19, 'ORDER17471599287889679', 12.00, '已发货', 13, '2025-05-14 02:12:09', '2025-05-14 22:45:59', NULL, '2025-05-14 02:12:09', '2025-05-14 22:45:59');
INSERT INTO `orders` VALUES (84, 19, 'ORDER17471600206824499', 36.00, '已发货', 13, '2025-05-14 02:13:41', '2025-05-14 22:45:57', NULL, '2025-05-14 02:13:41', '2025-05-14 22:45:57');
INSERT INTO `orders` VALUES (85, 1, 'ORDER17472028977847144', 239.40, '已完成', 1, '2025-05-14 14:08:18', '2025-05-14 22:45:43', NULL, '2025-05-14 14:08:18', '2025-05-15 01:24:27');
INSERT INTO `orders` VALUES (86, 1, 'ORDER17472338782999303', 45.00, '已完成', 11, '2025-05-14 22:44:38', '2025-05-14 22:45:40', NULL, '2025-05-14 22:44:38', '2025-05-14 22:46:23');
INSERT INTO `orders` VALUES (87, 1, 'ORDER17472435540073069', 255.90, '已付款', 11, '2025-05-15 01:25:54', NULL, NULL, '2025-05-15 01:25:54', '2025-05-15 01:25:54');

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
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图书评价表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of review
-- ----------------------------
INSERT INTO `review` VALUES (29, 1, 3, '6', 4, '2025-05-14 13:29:25', '2025-05-14 13:29:25');
INSERT INTO `review` VALUES (30, 1, 2, '9', 5, '2025-05-14 13:29:55', '2025-05-14 13:29:55');
INSERT INTO `review` VALUES (31, 1, 14, '7', 1, '2025-05-14 22:44:40', '2025-05-14 22:44:40');
INSERT INTO `review` VALUES (32, 3, 1, '6', 5, '2025-05-14 23:59:12', '2025-05-14 23:59:12');
INSERT INTO `review` VALUES (33, 3, 1, '7', 4, '2025-05-15 00:19:09', '2025-05-15 00:19:09');
INSERT INTO `review` VALUES (34, 1, 14, '1', 5, '2025-05-15 01:24:10', '2025-05-15 01:24:10');
INSERT INTO `review` VALUES (35, 1, 1, 'p', 4, '2025-05-15 01:25:25', '2025-05-15 01:25:25');

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
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'aaa', 'e10adc3949ba59abbe56e057f20f883e', '12345678901@163.com', '13022200334', 'http://localhost:8080/files/download/1747159644967_2.jpg', 1, '2025-03-07 16:48:57', '2025-05-15 01:25:12', 'USER', 'jojo2');
INSERT INTO `user` VALUES (2, 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', 'zhangsan@bookstore.com', '13812345678', 'http://localhost:8080/files/2004.jpg', 1, '2025-03-08 17:01:35', '2025-05-03 00:28:06', 'USER', 'user');
INSERT INTO `user` VALUES (3, 'lisi', 'e10adc3949ba59abbe56e057f20f883e', 'lisi@bookstore.com', '13987654321', 'http://localhost:8080/files/download/1747046838657_1.png', 1, '2025-03-08 17:01:35', '2025-05-15 13:14:26', 'USER', '榆虹姬');
INSERT INTO `user` VALUES (13, 'bnm', 'e10adc3949ba59abbe56e057f20f883e', '56@qq.com', '', 'http://localhost:8080/files/download/1746256864624_554704010110247455752f63d655f6384cbb2f2f9873dd52.jpg', 0, '2025-05-03 13:05:53', '2025-05-08 19:50:17', 'USER', 'user');
INSERT INTO `user` VALUES (15, 'aqw', 'e10adc3949ba59abbe56e057f20f883e', '', '13612312457', 'http://localhost:8080/files/download/1746358352922_bf9d98b49a7d911a3618f85d29d66c471254371692.jpg', 0, '2025-05-04 19:32:50', '2025-05-08 19:50:18', 'USER', 'user276374');
INSERT INTO `user` VALUES (16, 'zxc', 'e10adc3949ba59abbe56e057f20f883e', '123456789@qq.com', '17312345678', NULL, 1, '2025-05-04 23:51:53', '2025-05-08 19:50:20', 'USER', 'user548844');
INSERT INTO `user` VALUES (17, 'qweer', 'e10adc3949ba59abbe56e057f20f883e', '32594654@qq.com', '17812345679', NULL, 1, '2025-05-05 17:30:17', '2025-05-08 19:50:22', 'USER', 'user829913');
INSERT INTO `user` VALUES (18, 'laji', 'e10adc3949ba59abbe56e057f20f883e', '1472580@163.com', '17777678741', NULL, 1, '2025-05-05 17:31:43', '2025-05-08 19:50:23', 'USER', '无敌');
INSERT INTO `user` VALUES (19, 'lop', 'e10adc3949ba59abbe56e057f20f883e', '1345234@qq.com', '18736925814', 'http://localhost:8080/files/download/1746534917779_554704010110247455752f63d655f6384cbb2f2f9873dd52.jpg', 1, '2025-05-06 20:35:20', '2025-05-08 19:50:25', 'USER', '遇到纠纷');
INSERT INTO `user` VALUES (20, 'dffg', 'e10adc3949ba59abbe56e057f20f883e', '233345@qq.com', '19312345678', NULL, 1, '2025-05-08 13:03:30', '2025-05-08 13:03:30', 'USER', 'user216874');
INSERT INTO `user` VALUES (21, 'lopk', 'e10adc3949ba59abbe56e057f20f883e', '327628764@qq.com', '13022200666', 'http://localhost:8080/files/download/1746682358975_554704010110247455752f63d655f6384cbb2f2f9873dd52.jpg', 1, '2025-05-08 13:32:41', '2025-05-08 13:32:41', 'USER', '图集');
INSERT INTO `user` VALUES (22, 'dfgdf', 'e10adc3949ba59abbe56e057f20f883e', '6254@qq.com', '18714702589', NULL, 1, '2025-05-08 13:40:05', '2025-05-08 13:40:05', 'USER', 'user449195');
INSERT INTO `user` VALUES (23, 'cvbn', 'e10adc3949ba59abbe56e057f20f883e', 'sdf@163.com', '14578945632', NULL, 1, '2025-05-08 13:49:26', '2025-05-08 13:49:26', 'USER', 'ccc');

SET FOREIGN_KEY_CHECKS = 1;
