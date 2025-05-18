/*
 Navicat Premium Data Transfer

 Source Server         : lesson
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : todoappdb

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 18/05/2025 11:55:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#3498db',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (2, '测试分类 2025/5/18 00:27:46', '#3a09a3', '2025-05-18 00:27:46', 'testuser3');
INSERT INTO `category` VALUES (3, '测试分类 2025/5/18 00:27:53', '#ccdfc0', '2025-05-18 00:27:53', 'testuser3');
INSERT INTO `category` VALUES (4, '测试分类 2025/5/18 00:27:54', '#108657', '2025-05-18 00:27:54', 'testuser3');
INSERT INTO `category` VALUES (5, '测试分类 2025/5/18 00:27:54', '#d17d99', '2025-05-18 00:27:54', 'testuser3');
INSERT INTO `category` VALUES (6, '测试分类 2025/5/18 00:27:54', '#acc8b2', '2025-05-18 00:27:54', 'testuser3');
INSERT INTO `category` VALUES (7, '测试分类 2025/5/18 00:27:54', '#a18071', '2025-05-18 00:27:54', 'testuser3');
INSERT INTO `category` VALUES (8, '测试分类 2025/5/18 11:18:36', '#4924a4', '2025-05-18 11:18:36', 'zhangsan1');
INSERT INTO `category` VALUES (9, '测试分类 2025/5/18 11:18:37', '#574c79', '2025-05-18 11:18:37', 'zhangsan1');
INSERT INTO `category` VALUES (10, '测试分类 2025/5/18 11:51:29', '#1065dd', '2025-05-18 11:51:29', 'testuser');

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#95a5a6',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES (2, '工作', '#FF5733', 'testuser');
INSERT INTO `tag` VALUES (3, '个人', '#33FF57', 'testuser');
INSERT INTO `tag` VALUES (4, '紧急', '#5733FF', 'testuser');
INSERT INTO `tag` VALUES (5, '重要', '#33FFFF', 'testuser');
INSERT INTO `tag` VALUES (6, '测试标签 2025/5/18 00:27:27', '#7b4577', 'testuser3');
INSERT INTO `tag` VALUES (7, '测试标签 2025/5/18 00:27:30', '#90fc70', 'testuser3');
INSERT INTO `tag` VALUES (8, '测试标签 2025/5/18 00:27:30', '#10a19d', 'testuser3');
INSERT INTO `tag` VALUES (9, '测试标签 2025/5/18 00:27:30', '#30ee5b', 'testuser3');
INSERT INTO `tag` VALUES (10, '测试标签 2025/5/18 11:18:40', '#8f86c2', 'zhangsan1');
INSERT INTO `tag` VALUES (11, '测试标签 2025/5/18 11:51:40', '#90387c', 'testuser');

-- ----------------------------
-- Table structure for todo
-- ----------------------------
DROP TABLE IF EXISTS `todo`;
CREATE TABLE `todo`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `done` tinyint(1) NOT NULL DEFAULT 0,
  `target_date` date NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `category_id` bigint NULL DEFAULT NULL,
  `priority` int NULL DEFAULT 0,
  `reminder_time` datetime NULL DEFAULT NULL,
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `created_at` datetime NULL DEFAULT NULL,
  `updated_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10025 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of todo
-- ----------------------------
INSERT INTO `todo` VALUES (10004, '学习Spring Boot', 0, '2025-05-20', 'admin', 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `todo` VALUES (10005, '这是一个测试添加', 0, '2025-05-17', 'admin', 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `todo` VALUES (10008, '测试待办事项 2025/5/18 00:26:41', 0, '2025-05-17', 'testuser3', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10009, '测试待办事项 2025/5/18 00:26:47', 0, '2025-05-17', 'testuser3', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10010, '测试待办事项 2025/5/18 00:27:16', 0, '2025-05-17', 'testuser3', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10011, '测试待办事项 2025/5/18 00:27:17', 0, '2025-05-17', 'testuser3', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10012, '测试待办事项 2025/5/18 00:27:17', 0, '2025-05-17', 'testuser3', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10013, '测试待办事项 2025/5/18 00:27:18', 0, '2025-05-17', 'testuser3', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10014, '测试待办事项 2025/5/18 00:27:18', 0, '2025-05-17', 'testuser3', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10015, '测试待办事项 2025/5/18 00:27:18', 0, '2025-05-17', 'testuser3', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10016, '测试待办事项 2025/5/18 00:27:19', 0, '2025-05-17', 'testuser3', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10017, '测试待办事项 2025/5/18 01:00:12', 0, '2025-05-17', 'lisi', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10018, '测试待办事项 2025/5/18 11:18:21', 0, '2025-05-18', 'zhangsan1', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10019, '测试待办事项 2025/5/18 11:18:23', 0, '2025-05-18', 'zhangsan1', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10020, '测试待办事项 2025/5/18 11:50:49', 0, '2025-05-18', 'testuser', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10021, '测试待办事项 2025/5/18 11:50:55', 0, '2025-05-18', 'testuser', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10022, '测试待办事项 2025/5/18 11:50:55', 0, '2025-05-18', 'testuser', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10023, '测试待办事项 2025/5/18 11:50:55', 0, '2025-05-18', 'testuser', NULL, 1, NULL, '', NULL, NULL);
INSERT INTO `todo` VALUES (10024, '测试待办事项 2025/5/18 11:51:15', 0, '2025-05-18', 'testuser', NULL, 1, NULL, '', NULL, NULL);

-- ----------------------------
-- Table structure for todo_tag
-- ----------------------------
DROP TABLE IF EXISTS `todo_tag`;
CREATE TABLE `todo_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `todo_id` bigint NOT NULL,
  `tag_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_todo_tag_todo`(`todo_id` ASC) USING BTREE,
  INDEX `fk_todo_tag_tag`(`tag_id` ASC) USING BTREE,
  CONSTRAINT `fk_todo_tag_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_todo_tag_todo` FOREIGN KEY (`todo_id`) REFERENCES `todo` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of todo_tag
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'testuser', '75K3eLr+dx6JJFuJ7LwIpEpOFmwGZZkRiB84PURz6U8=', 'test@example.com', '2025-05-17 21:27:38', '2025-05-17 21:27:38');
INSERT INTO `user` VALUES (2, 'testuser1', '8VNoPTsar63V7La/uW+g1lV93ch1KdHbXiBX2hc9B6Q=', 'test1@example.com', '2025-05-18 00:09:39', '2025-05-18 00:09:39');
INSERT INTO `user` VALUES (3, 'testuser2', 'koBzaePtIA+EhkcVXBa/cbCUH1tIJ+nG1VLpIrpRrN8=', 'test2@example.com', '2025-05-18 00:10:20', '2025-05-18 00:10:20');
INSERT INTO `user` VALUES (4, 'testuser3', 'E8CZYvMmWMYZDMu1AxNCn87q78roPA2v442d8eYMBOI=', 'test3@example.com', '2025-05-18 00:26:23', '2025-05-18 00:26:23');
INSERT INTO `user` VALUES (5, 'zhangsan', 'gnIdhEwE8iUCJEqHBhMT4I1JxujT++5H4SIBiApc5ss=', 'zhangsan@qq.com', '2025-05-18 00:29:21', '2025-05-18 00:29:21');
INSERT INTO `user` VALUES (6, 'lisi', 'Ra3lyYBv01haTOGZrb3wWDAaAeYlv1FCUpE+ZBGR7dk=', 'lisi@qq.com', '2025-05-18 00:39:24', '2025-05-18 00:39:24');
INSERT INTO `user` VALUES (7, 'test1', 'G08OmFGXGZjnMgeFRMlrNsPQHO33yqMyNZ1vHYNWcBQ=', 'test1@qq.com', '2025-05-18 10:39:00', '2025-05-18 10:39:00');
INSERT INTO `user` VALUES (8, 'test4', 'pOYk1obgPtJ2fAq9hcFEJrCxFX0s6B0nu0/k9vAdaIo=', 'test4@qq.com', '2025-05-18 11:06:48', '2025-05-18 11:06:48');
INSERT INTO `user` VALUES (9, 'zhangsan1', 'FaVr1N08ZUQHeg/fY5huM3WnWPwhBG1iOjFj/3Y28dk=', 'zhangsan1@example.com', '2025-05-18 11:18:09', '2025-05-18 11:18:09');
INSERT INTO `user` VALUES (10, 'test123', '7NcYcNGWMxapfjrDQIyYNa2M8PPBvHA1J8MCZVNPda4=', 'test123@qq.com', '2025-05-18 11:53:16', '2025-05-18 11:53:16');

SET FOREIGN_KEY_CHECKS = 1;
