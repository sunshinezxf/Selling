-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: selling
-- ------------------------------------------------------
-- Server version	5.7.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `agent`
--

DROP TABLE IF EXISTS `agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agent` (
  `agent_id` varchar(20) NOT NULL,
  `upper_agent_id` varchar(20) DEFAULT NULL,
  `agent_coffer` double NOT NULL DEFAULT '0',
  `agent_name` varchar(45) NOT NULL,
  `agent_gender` varchar(10) NOT NULL,
  `agent_phone` varchar(45) NOT NULL,
  `agent_address` varchar(100) NOT NULL,
  `agent_password` varchar(50) NOT NULL,
  `agent_wechat` varchar(45) DEFAULT NULL,
  `agent_level` int(11) DEFAULT NULL,
  `agent_granted` tinyint(1) NOT NULL DEFAULT '0',
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`agent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agent`
--

LOCK TABLES `agent` WRITE;
/*!40000 ALTER TABLE `agent` DISABLE KEYS */;
INSERT INTO `agent` VALUES ('AGTvlorff50',NULL,0,'王旻','M','18000000000','江苏省南京市鼓楼区汉口路22号','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,1,0,'2016-05-09 16:13:10'),('AGTyoewlw97',NULL,0,'王晓迪','M','18100000000','江苏省南京市鼓楼区汉口路22号','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,0,0,'2016-05-09 16:14:15');
/*!40000 ALTER TABLE `agent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `customer_id` varchar(20) NOT NULL,
  `customer_name` varchar(45) NOT NULL,
  `agent_id` varchar(20) NOT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` varchar(45) NOT NULL,
  PRIMARY KEY (`customer_id`),
  KEY `fk_customer_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_customer_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES ('CUSfleill87','刘嘉','AGTvlorff50',0,'2016-05-13 12:50:50.28'),('CUSfofevo36','刘钦','AGTvlorff50',0,'2016-05-13 12:51:01.567'),('CUSwoffvx48','刘峰','AGTvlorff50',0,'2016-05-13 12:51:10.113');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_address`
--

DROP TABLE IF EXISTS `customer_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_address` (
  `customer_address_id` varchar(20) NOT NULL,
  `customer_id` varchar(20) NOT NULL,
  `address` varchar(100) NOT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`customer_address_id`),
  KEY `fk_customer_address_customer1_idx` (`customer_id`),
  CONSTRAINT `fk_customer_address_customer1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_address`
--

LOCK TABLES `customer_address` WRITE;
/*!40000 ALTER TABLE `customer_address` DISABLE KEYS */;
INSERT INTO `customer_address` VALUES ('ADDeveeil5','CUSwoffvx48','费彝民楼',0,'2016-05-13 12:51:10'),('ADDleeflw46','CUSfofevo36','费彝民楼',0,'2016-05-13 12:51:02'),('ADDvoxvle50','CUSfleill87','费彝民楼',0,'2016-05-13 12:50:50');
/*!40000 ALTER TABLE `customer_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_phone`
--

DROP TABLE IF EXISTS `customer_phone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_phone` (
  `customer_phone_id` varchar(20) NOT NULL,
  `customer_id` varchar(20) NOT NULL,
  `phone` varchar(45) NOT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`customer_phone_id`),
  KEY `fk_customer_phone_customer1_idx` (`customer_id`),
  CONSTRAINT `fk_customer_phone_customer1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_phone`
--

LOCK TABLES `customer_phone` WRITE;
/*!40000 ALTER TABLE `customer_phone` DISABLE KEYS */;
INSERT INTO `customer_phone` VALUES ('PNMiereyl26','CUSfofevo36','18911112222',0,'2016-05-13 12:51:02'),('PNMrovewl23','CUSwoffvx48','18911112233',0,'2016-05-13 12:51:10'),('PNMyxeffv65','CUSfleill87','12388889999',0,'2016-05-13 12:50:50');
/*!40000 ALTER TABLE `customer_phone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deposit_bill`
--

DROP TABLE IF EXISTS `deposit_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deposit_bill` (
  `bill_id` varchar(20) NOT NULL,
  `agent_id` varchar(20) NOT NULL,
  `channel_name` varchar(45) NOT NULL,
  `client_ip` varchar(45) NOT NULL,
  `bill_amount` double NOT NULL,
  `bill_status` tinyint(2) NOT NULL DEFAULT '0',
  `block_flag` tinyint(4) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`bill_id`),
  KEY `fk_deposit_bill_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_deposit_bill_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deposit_bill`
--

LOCK TABLES `deposit_bill` WRITE;
/*!40000 ALTER TABLE `deposit_bill` DISABLE KEYS */;
/*!40000 ALTER TABLE `deposit_bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods`
--

DROP TABLE IF EXISTS `goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goods` (
  `goods_id` varchar(20) NOT NULL,
  `goods_name` varchar(45) NOT NULL,
  `goods_price` double NOT NULL,
  `goods_description` varchar(45) DEFAULT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods`
--

LOCK TABLES `goods` WRITE;
/*!40000 ALTER TABLE `goods` DISABLE KEYS */;
INSERT INTO `goods` VALUES ('COMowxwol5','三七粉大号',398,'卖的就是大号',0,'2016-05-13 12:50:12'),('COMvlilfw91','三七粉',298,'卖的就是实惠',0,'2016-05-09 16:17:43');
/*!40000 ALTER TABLE `goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manager`
--

DROP TABLE IF EXISTS `manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manager` (
  `manager_id` varchar(20) NOT NULL,
  `manager_username` varchar(45) NOT NULL,
  `manager_password` varchar(45) NOT NULL,
  PRIMARY KEY (`manager_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manager`
--

LOCK TABLES `manager` WRITE;
/*!40000 ALTER TABLE `manager` DISABLE KEYS */;
INSERT INTO `manager` VALUES ('MNG00000001','admin','21232f297a57a5a743894a0e4a801fc3');
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order` (
  `order_id` varchar(20) NOT NULL,
  `agent_id` varchar(20) NOT NULL,
  `order_status` int(11) NOT NULL DEFAULT '0',
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `fk_order_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_order_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES ('ODRwxfwyf38','AGTvlorff50',1,0,'2016-05-13 12:51:46');
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_bill`
--

DROP TABLE IF EXISTS `order_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_bill` (
  `bill_id` varchar(20) NOT NULL,
  `agent_id` varchar(20) NOT NULL,
  `order_id` varchar(20) DEFAULT NULL,
  `channel_name` varchar(45) DEFAULT NULL,
  `client_ip` varchar(45) DEFAULT NULL,
  `bill_amount` double DEFAULT NULL,
  `bill_status` int(11) DEFAULT '0',
  `block_flag` tinyint(1) DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`bill_id`),
  UNIQUE KEY `order_id_UNIQUE` (`order_id`),
  UNIQUE KEY `create_time_UNIQUE` (`create_time`),
  UNIQUE KEY `bill_status_UNIQUE` (`bill_status`),
  UNIQUE KEY `block_flag_UNIQUE` (`block_flag`),
  UNIQUE KEY `bill_amount_UNIQUE` (`bill_amount`),
  UNIQUE KEY `client_ip_UNIQUE` (`client_ip`),
  UNIQUE KEY `channel_name_UNIQUE` (`channel_name`),
  KEY `fk_order_bill_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_order_bill_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_bill_order1` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_bill`
--

LOCK TABLES `order_bill` WRITE;
/*!40000 ALTER TABLE `order_bill` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_item` (
  `order_item_id` varchar(20) NOT NULL,
  `order_id` varchar(20) NOT NULL,
  `goods_id` varchar(20) NOT NULL,
  `order_item_status` int(11) DEFAULT NULL,
  `goods_quantity` int(11) NOT NULL DEFAULT '1',
  `order_item_price` double NOT NULL,
  `customer_id` varchar(20) NOT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`order_item_id`),
  KEY `fk_order_item_order_idx` (`order_id`),
  KEY `fk_order_item_goods1_idx` (`goods_id`),
  KEY `fk_order_item_customer1_idx` (`customer_id`),
  CONSTRAINT `fk_order_item_customer1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_item_goods1` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`goods_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES ('ODRfvyfvl7','ODRwxfwyf38','COMowxwol5',0,5,1990,'CUSfleill87',0,'2016-05-13 12:51:46'),('ODRzorvov40','ODRwxfwyf38','COMvlilfw91',0,2,596,'CUSfofevo36',0,'2016-05-13 12:51:46');
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `role_id` varchar(20) NOT NULL,
  `role_name` varchar(45) NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES ('ROL00000001','admin',0,'2016-05-03 11:00:00'),('ROL00000002','agent',0,'2016-05-03 11:00:10');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` varchar(20) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `role_id` varchar(20) NOT NULL,
  `manager_id` varchar(20) DEFAULT NULL,
  `agent_id` varchar(20) DEFAULT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `fk_user_role1_idx` (`role_id`),
  KEY `fk_user_agent1_idx` (`agent_id`),
  KEY `fk_user_manager1_idx` (`manager_id`),
  CONSTRAINT `fk_user_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_manager1` FOREIGN KEY (`manager_id`) REFERENCES `manager` (`manager_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_role1` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('USR00000001','admin','21232f297a57a5a743894a0e4a801fc3','ROL00000001','MNG00000001',NULL,0,'2016-05-03 11:00:20'),('USRflowfr67','18000000000','e10adc3949ba59abbe56e057f20f883e','ROL00000002',NULL,'AGTvlorff50',0,'2016-05-09 16:13:10'),('USRollvfy81','18100000000','e10adc3949ba59abbe56e057f20f883e','ROL00000002',NULL,'AGTyoewlw97',0,'2016-05-09 16:14:15');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-13 13:00:37
