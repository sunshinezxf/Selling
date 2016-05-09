-- MySQL dump 10.13  Distrib 5.6.27, for osx10.11 (x86_64)
--
-- Host: localhost    Database: selling
-- ------------------------------------------------------
-- Server version	5.6.27

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
-- Current Database: `selling`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `selling` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `selling`;

--
-- Table structure for table `agent`
--

DROP TABLE IF EXISTS `agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agent` (
  `agent_id` varchar(20) NOT NULL,
  `upper_agent_id` varchar(20) DEFAULT NULL,
  `agent_name` varchar(45) NOT NULL,
  `agent_gender` varchar(10) NOT NULL,
  `agent_phone` varchar(45) NOT NULL,
  `agent_address` varchar(100) NOT NULL,
  `agent_password` varchar(50) NOT NULL,
  `agent_wechat` varchar(45) DEFAULT NULL,
  `agent_level` int(11) DEFAULT NULL,
  `agent_paid` tinyint(1) NOT NULL DEFAULT '0',
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
INSERT INTO `agent` VALUES ('AGTexyvol85',NULL,'王旻','M','18000000000','江苏省南京市鼓楼区汉口路','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,0,0,0,'2016-05-09 15:25:50'),('AGTylefef7',NULL,'王晓迪','M','18100000000','江苏省南京市鼓楼区汉口路22号','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,0,0,0,'2016-05-09 15:28:25');
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
/*!40000 ALTER TABLE `customer_phone` ENABLE KEYS */;
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
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
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
  `goods_quantity` int(11) NOT NULL DEFAULT '1',
  `order_item_price` double NOT NULL,
  `customer_id` varchar(20) NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
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
INSERT INTO `user` VALUES ('USR00000001','admin','21232f297a57a5a743894a0e4a801fc3','ROL00000001','MNG00000001',NULL,0,'2016-05-03 11:00:20'),('USRewxool0','18000000000','e10adc3949ba59abbe56e057f20f883e','ROL00000002',NULL,'AGTexyvol85',0,'2016-05-09 15:25:51'),('USRvwfley24','18100000000','e10adc3949ba59abbe56e057f20f883e','ROL00000002',NULL,'AGTylefef7',0,'2016-05-09 15:28:26');
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

-- Dump completed on 2016-05-09 15:31:10
