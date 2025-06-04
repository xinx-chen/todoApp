-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: todoappdb
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '#3498db',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (2,'测试分类 2025/5/18 00:27:46','#3a09a3','2025-05-17 16:27:46','testuser3'),(3,'测试分类 2025/5/18 00:27:53','#ccdfc0','2025-05-17 16:27:53','testuser3'),(4,'测试分类 2025/5/18 00:27:54','#108657','2025-05-17 16:27:54','testuser3'),(5,'测试分类 2025/5/18 00:27:54','#d17d99','2025-05-17 16:27:54','testuser3'),(6,'测试分类 2025/5/18 00:27:54','#acc8b2','2025-05-17 16:27:54','testuser3'),(7,'测试分类 2025/5/18 00:27:54','#a18071','2025-05-17 16:27:54','testuser3'),(8,'测试分类 2025/5/18 11:18:36','#4924a4','2025-05-18 03:18:36','zhangsan1'),(9,'测试分类 2025/5/18 11:18:37','#574c79','2025-05-18 03:18:37','zhangsan1'),(10,'测试分类 2025/5/18 11:51:29','#1065dd','2025-05-18 03:51:29','testuser'),(13,'不紧急','#3498db','2025-05-18 09:21:11','test6'),(14,'重要','#dd1d1d','2025-05-18 09:21:20','test6'),(15,'学习','#3498db','2025-06-03 14:34:38','testuser222'),(16,'工作','#1abc9c','2025-06-03 14:38:45','testuser222');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '#95a5a6',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES (2,'工作','#FF5733','testuser'),(3,'个人','#33FF57','testuser'),(4,'紧急','#5733FF','testuser'),(5,'重要','#33FFFF','testuser'),(6,'测试标签 2025/5/18 00:27:27','#7b4577','testuser3'),(7,'测试标签 2025/5/18 00:27:30','#90fc70','testuser3'),(8,'测试标签 2025/5/18 00:27:30','#10a19d','testuser3'),(9,'测试标签 2025/5/18 00:27:30','#30ee5b','testuser3'),(10,'测试标签 2025/5/18 11:18:40','#8f86c2','zhangsan1'),(11,'测试标签 2025/5/18 11:51:40','#90387c','testuser'),(13,'工作','#0dc6d3','test6'),(14,'代码','#679c54','test6');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo`
--

DROP TABLE IF EXISTS `todo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `todo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `done` tinyint(1) NOT NULL DEFAULT '0',
  `target_date` date DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `category_id` bigint DEFAULT NULL,
  `priority` int DEFAULT '0',
  `reminder_time` datetime DEFAULT NULL,
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL COMMENT '备注图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10043 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo`
--

LOCK TABLES `todo` WRITE;
/*!40000 ALTER TABLE `todo` DISABLE KEYS */;
INSERT INTO `todo` VALUES (10004,'学习Spring Boot',0,'2025-05-20','admin',1,NULL,NULL,NULL,NULL,NULL,NULL),(10005,'这是一个测试添加',0,'2025-05-17','admin',1,NULL,NULL,NULL,NULL,NULL,NULL),(10008,'测试待办事项 2025/5/18 00:26:41',0,'2025-05-17','testuser3',NULL,1,NULL,'',NULL,NULL,NULL),(10009,'测试待办事项 2025/5/18 00:26:47',0,'2025-05-17','testuser3',NULL,1,NULL,'',NULL,NULL,NULL),(10010,'测试待办事项 2025/5/18 00:27:16',0,'2025-05-17','testuser3',NULL,1,NULL,'',NULL,NULL,NULL),(10011,'测试待办事项 2025/5/18 00:27:17',0,'2025-05-17','testuser3',NULL,1,NULL,'',NULL,NULL,NULL),(10012,'测试待办事项 2025/5/18 00:27:17',0,'2025-05-17','testuser3',NULL,1,NULL,'',NULL,NULL,NULL),(10013,'测试待办事项 2025/5/18 00:27:18',0,'2025-05-17','testuser3',NULL,1,NULL,'',NULL,NULL,NULL),(10014,'测试待办事项 2025/5/18 00:27:18',0,'2025-05-17','testuser3',NULL,1,NULL,'',NULL,NULL,NULL),(10015,'测试待办事项 2025/5/18 00:27:18',0,'2025-05-17','testuser3',NULL,1,NULL,'',NULL,NULL,NULL),(10016,'测试待办事项 2025/5/18 00:27:19',0,'2025-05-17','testuser3',NULL,1,NULL,'',NULL,NULL,NULL),(10017,'测试待办事项 2025/5/18 01:00:12',0,'2025-05-17','lisi',NULL,1,NULL,'',NULL,NULL,NULL),(10018,'测试待办事项 2025/5/18 11:18:21',0,'2025-05-18','zhangsan1',NULL,1,NULL,'',NULL,NULL,NULL),(10019,'测试待办事项 2025/5/18 11:18:23',0,'2025-05-18','zhangsan1',NULL,1,NULL,'',NULL,NULL,NULL),(10020,'测试待办事项 2025/5/18 11:50:49',0,'2025-05-18','testuser',NULL,1,NULL,'',NULL,NULL,NULL),(10021,'测试待办事项 2025/5/18 11:50:55',0,'2025-05-18','testuser',NULL,1,NULL,'',NULL,NULL,NULL),(10022,'测试待办事项 2025/5/18 11:50:55',0,'2025-05-18','testuser',NULL,1,NULL,'',NULL,NULL,NULL),(10023,'测试待办事项 2025/5/18 11:50:55',0,'2025-05-18','testuser',NULL,1,NULL,'',NULL,NULL,NULL),(10024,'测试待办事项 2025/5/18 11:51:15',0,'2025-05-18','testuser',NULL,1,NULL,'',NULL,NULL,NULL),(10025,'test3的一个代办事项测试',1,'2025-05-18','test3',NULL,1,'2025-05-18 16:53:00','这是个测试',NULL,NULL,NULL),(10026,'test3的一个代办事项测试2',0,'2025-05-20','test3',NULL,2,'2025-05-18 16:53:00','这是个测试2',NULL,NULL,NULL),(10027,'这又又又是个测试啊',1,'2025-05-18','test6',13,0,'2025-05-19 17:32:00','测试',NULL,NULL,NULL),(10028,'这又又又是个测试啊2',0,'2025-05-18','test6',13,2,'2025-05-19 17:32:00','测试',NULL,NULL,NULL),(10029,'test',0,'2025-05-28','123',NULL,0,'2025-05-28 12:30:00','',NULL,NULL,NULL),(10030,'test2',0,'2025-05-28','123',NULL,2,'2025-05-28 08:30:00','',NULL,NULL,NULL),(10031,'222222',0,'2025-05-27','123',NULL,1,NULL,'',NULL,NULL,NULL),(10033,'444',0,'2025-05-27','123',NULL,0,NULL,'',NULL,NULL,NULL),(10034,'123',0,'2025-05-27','123',NULL,1,'2025-05-28 21:07:00','',NULL,NULL,NULL),(10035,'测试',0,'2025-06-03','testuser222',NULL,0,'2025-06-03 19:40:00','',NULL,NULL,NULL),(10036,'测试',0,'2025-06-04','testuser222',15,0,'2025-06-03 19:40:00','',NULL,NULL,NULL),(10037,'测试',0,'2025-06-06','testuser222',16,1,'2025-06-03 19:59:00','',NULL,NULL,NULL),(10038,'陈述事实',0,'2025-06-02','testuser222',NULL,0,NULL,'',NULL,NULL,NULL),(10039,'123',0,'2025-06-03','testuser222',NULL,2,NULL,'',NULL,NULL,NULL),(10040,'1233',0,'2025-06-03','testuser222',NULL,3,NULL,'',NULL,NULL,NULL),(10041,'123456',0,'2025-06-03','testuser222',NULL,0,NULL,'',NULL,NULL,NULL),(10042,'1234567888',0,'2025-06-03','testuser222',NULL,0,NULL,'',NULL,NULL,'https://i.ibb.co/KzpGvr0j/5.jpg');
/*!40000 ALTER TABLE `todo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo_tag`
--

DROP TABLE IF EXISTS `todo_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `todo_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `todo_id` bigint NOT NULL,
  `tag_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_todo_tag_todo` (`todo_id`) USING BTREE,
  KEY `fk_todo_tag_tag` (`tag_id`) USING BTREE,
  CONSTRAINT `fk_todo_tag_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_todo_tag_todo` FOREIGN KEY (`todo_id`) REFERENCES `todo` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo_tag`
--

LOCK TABLES `todo_tag` WRITE;
/*!40000 ALTER TABLE `todo_tag` DISABLE KEYS */;
INSERT INTO `todo_tag` VALUES (4,10028,14),(5,10027,13);
/*!40000 ALTER TABLE `todo_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `email` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'testuser','75K3eLr+dx6JJFuJ7LwIpEpOFmwGZZkRiB84PURz6U8=','test@example.com','2025-05-17 13:27:38','2025-05-17 13:27:38'),(2,'testuser1','8VNoPTsar63V7La/uW+g1lV93ch1KdHbXiBX2hc9B6Q=','test1@example.com','2025-05-17 16:09:39','2025-05-17 16:09:39'),(3,'testuser2','koBzaePtIA+EhkcVXBa/cbCUH1tIJ+nG1VLpIrpRrN8=','test2@example.com','2025-05-17 16:10:20','2025-05-17 16:10:20'),(4,'testuser3','E8CZYvMmWMYZDMu1AxNCn87q78roPA2v442d8eYMBOI=','test3@example.com','2025-05-17 16:26:23','2025-05-17 16:26:23'),(5,'zhangsan','gnIdhEwE8iUCJEqHBhMT4I1JxujT++5H4SIBiApc5ss=','zhangsan@qq.com','2025-05-17 16:29:21','2025-05-17 16:29:21'),(6,'lisi','Ra3lyYBv01haTOGZrb3wWDAaAeYlv1FCUpE+ZBGR7dk=','lisi@qq.com','2025-05-17 16:39:24','2025-05-17 16:39:24'),(7,'test1','G08OmFGXGZjnMgeFRMlrNsPQHO33yqMyNZ1vHYNWcBQ=','test1@qq.com','2025-05-18 02:39:00','2025-05-18 02:39:00'),(8,'test4','pOYk1obgPtJ2fAq9hcFEJrCxFX0s6B0nu0/k9vAdaIo=','test4@qq.com','2025-05-18 03:06:48','2025-05-18 03:06:48'),(9,'zhangsan1','FaVr1N08ZUQHeg/fY5huM3WnWPwhBG1iOjFj/3Y28dk=','zhangsan1@example.com','2025-05-18 03:18:09','2025-05-18 03:18:09'),(10,'test123','7NcYcNGWMxapfjrDQIyYNa2M8PPBvHA1J8MCZVNPda4=','test123@qq.com','2025-05-18 03:53:16','2025-05-18 03:53:16'),(11,'test2','YDA64iuZiGG847KPM+7BvnWKITyGyTwHbb6fVYwRx1I=','test2@qq.com','2025-05-18 08:18:17','2025-05-18 08:18:17'),(12,'test3','/WGgOvT3fYcPwh4F5+gGeAlcktgIz7O1wnnuBMdKyhM=','test3@qq.com','2025-05-18 08:52:34','2025-05-18 08:52:34'),(13,'test6','7Qy5C9+k+TmBp9A8/5khOoaqlqbLz4nsXoiJhx8Ihyc=','test6@qq.com','2025-05-18 09:19:23','2025-05-18 09:19:23'),(14,'test7','vXyREmSq4Vtm1CkbaFCCmqlphrHT6tNNH9v+8nBWwRI=','test7@qq.com','2025-05-18 09:39:53','2025-05-18 09:39:53'),(15,'testuser222','pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=','test@example1.com','2025-06-02 07:24:12','2025-06-02 07:24:12');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'todoappdb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-03 23:21:09
