-- MySQL dump 10.13  Distrib 5.5.49, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: selling
-- ------------------------------------------------------
-- Server version	5.5.49-0+deb7u1

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
  `agent_coffer` double NOT NULL DEFAULT '0',
  `agent_refund` double NOT NULL DEFAULT '0',
  `agent_name` varchar(45) NOT NULL,
  `agent_gender` varchar(10) NOT NULL,
  `agent_phone` varchar(45) NOT NULL,
  `agent_address` varchar(100) NOT NULL,
  `agent_password` varchar(50) NOT NULL,
  `agent_wechat` varchar(45) DEFAULT NULL,
  `scale` int(11) NOT NULL DEFAULT '0',
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
INSERT INTO `agent` VALUES ('AGTeefoeo87',NULL,10000,0,'王旻','M','15869696969','46','f85e65970c62c8b667f315fdebee5505',NULL,6,NULL,1,0,'2016-06-23 17:00:42'),('AGTeoyiel50','AGTrlorvl21',100,0,'wangdi','F','18994050872','南京','430bad64fbd395fec55cfd428641c21e',NULL,7,NULL,1,0,'2016-06-21 13:07:40'),('AGTfeoeyl96',NULL,100,0,'点解点解','M','15805166899','守家','b5bf27b2555de44e3df2230080db5a1d',NULL,6,NULL,0,0,'2016-06-23 17:13:16'),('AGTfoolvy45',NULL,100,0,'翟','M','15634233636','是','67d77086409d29be26ffc46270dc142f',NULL,56,NULL,0,0,'2016-06-23 17:28:54'),('AGTfrrofe9',NULL,10000,0,'刘嘉','M','18662702209','江苏省南京市汉口路22号','e10adc3949ba59abbe56e057f20f883e',NULL,40,NULL,1,0,'2016-06-21 10:51:40'),('AGTllfefy86',NULL,100,0,'的话','M','15869652356','速速','d3cb757121f725fe825a1176031a1c14',NULL,6,NULL,0,0,'2016-06-23 17:07:32'),('AGTrewlfr7','AGTeoyiel50',49.99,0,'周文王','M','15850580905','南京大学','e10adc3949ba59abbe56e057f20f883e','ozqMbv2Mlx3VmPQMIFNJaNIlPOgk',5,NULL,1,0,'2016-06-19 15:39:30'),('AGTrlorvl21',NULL,10000,0,'王旻','M','15805166833','江苏省南京是汉口路22号','e10adc3949ba59abbe56e057f20f883e','ozqMbv01oBIlNd4g7YA94oh6n4Ys',50,NULL,1,0,'2016-06-19 15:39:37'),('AGTwolrye13',NULL,100,0,'SJ','M','15805166966','sjjs','77cbc257e66302866cf6191754c0c8e3',NULL,6,NULL,0,0,'2016-06-23 16:46:56'),('AGTwylxfz73','AGTrlorvl21',100,0,'王晓迪','M','15805167546','江苏南京鼓楼','e10adc3949ba59abbe56e057f20f883e',NULL,20,NULL,1,0,'2016-06-19 15:39:49'),('AGTxvviev79',NULL,100,0,'测试1','M','15805166834','不知道','700f6fa0edb608ee5cc3cfa63f1c94cc',NULL,6,NULL,0,0,'2016-06-23 17:21:25'),('AGTylfflz66','AGTrlorvl21',80,0,'章许帆','M','15895862305','江苏省南京市鼓楼区汉口路22号','ba0f58ea98e159854d3434ce2b6b04e1','ozqMbv3Q_cVoUmsxKsnutvDmHmfE',20,NULL,1,0,'2016-06-19 15:40:07');
/*!40000 ALTER TABLE `agent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `agent_gift`
--

DROP TABLE IF EXISTS `agent_gift`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agent_gift` (
  `agent_gift_id` varchar(20) NOT NULL,
  `agent_id` varchar(20) NOT NULL,
  `goods_id` varchar(20) NOT NULL,
  `available_amount` int(11) NOT NULL DEFAULT '0',
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  KEY `fk_agent_gift_agent1_idx` (`agent_id`),
  KEY `fk_agent_gift_goods1_idx` (`goods_id`),
  CONSTRAINT `fk_agent_gift_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_agent_gift_goods1` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`goods_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agent_gift`
--

LOCK TABLES `agent_gift` WRITE;
/*!40000 ALTER TABLE `agent_gift` DISABLE KEYS */;
INSERT INTO `agent_gift` VALUES ('AGGllelol43','AGTeefoeo87','COMyfxwez26',5,0,'2016-07-05 17:15:44'),('AGGvzwvel17','AGTfrrofe9','COMyfxwez26',5,0,'2016-07-05 17:15:55'),('AGGfrlxel95','AGTrlorvl21','COMyfxwez26',3,0,'2016-07-05 17:17:26');
/*!40000 ALTER TABLE `agent_gift` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `back_operation_log`
--

DROP TABLE IF EXISTS `back_operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `back_operation_log` (
  `log_id` varchar(20) NOT NULL,
  `admin_info` varchar(45) NOT NULL,
  `operation_event` varchar(45) NOT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `back_operation_log`
--

LOCK TABLES `back_operation_log` WRITE;
/*!40000 ALTER TABLE `back_operation_log` DISABLE KEYS */;
INSERT INTO `back_operation_log` VALUES ('BOLwewoyw32','admin','管理员admin将订单:ODRelfefv45设置为已付款',0,'2016-06-24 13:03:04');
/*!40000 ALTER TABLE `back_operation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank_card`
--

DROP TABLE IF EXISTS `bank_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bank_card` (
  `bank_card_id` varchar(20) NOT NULL,
  `bank_card_no` varchar(25) NOT NULL,
  `agent_id` varchar(20) NOT NULL,
  `preferred` tinyint(1) NOT NULL DEFAULT '0',
  `block_flag` tinyint(1) NOT NULL,
  `create_time` varchar(45) NOT NULL,
  PRIMARY KEY (`bank_card_id`),
  KEY `fk_bank_card_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_bank_card_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank_card`
--

LOCK TABLES `bank_card` WRITE;
/*!40000 ALTER TABLE `bank_card` DISABLE KEYS */;
/*!40000 ALTER TABLE `bank_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_info`
--

DROP TABLE IF EXISTS `credit_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `credit_info` (
  `credit_id` varchar(20) NOT NULL,
  `agent_id` varchar(20) NOT NULL,
  `credit_front` varchar(50) NOT NULL,
  `credit_back` varchar(50) NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`credit_id`),
  KEY `fk_credit_info_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_credit_info_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_info`
--

LOCK TABLES `credit_info` WRITE;
/*!40000 ALTER TABLE `credit_info` DISABLE KEYS */;
INSERT INTO `credit_info` VALUES ('CREeeefow32','AGTylfflz66','/material/upload/20160619/THfrzeoi41.jpeg','/material/upload/20160619/THryxlxy14.jpeg',0,'2016-06-19 15:40:07'),('CREeflxwe81','AGTfrrofe9','/material/upload/20160621/THolxfey96.jpeg','/material/upload/20160621/THwfllxy53.jpeg',0,'2016-06-21 10:51:40'),('CREelfeew36','AGTrlorvl21','/material/upload/20160619/THzyfyef61.png','/material/upload/20160619/THfylyry36.jpeg',0,'2016-06-19 15:39:37'),('CREeoeflf53','AGTxvviev79','/material/upload/20160623/THfzvioe46.jpg','/material/upload/20160623/THerlloo28.jpg',0,'2016-06-23 17:21:31'),('CREflooyx34','AGTrewlfr7','/material/upload/20160619/THoyxwle20.jpeg','/material/upload/20160619/THyffryv9.jpeg',0,'2016-06-19 15:39:30'),('CREflrxee30','AGTfoolvy45','/material/upload/20160623/THlzfleo24.jpg','/material/upload/20160623/THwofxie91.jpg',0,'2016-06-23 17:28:59'),('CREovfoeo41','AGTeoyiel50','/material/upload/20160621/THoeeelr76.jpg','/material/upload/20160621/THflxliz31.jpg',0,'2016-06-21 13:07:40'),('CREwlflly32','AGTwylxfz73','/material/upload/20160619/THollfyf24.jpg','/material/upload/20160619/THleyrlo8.jpg',0,'2016-06-19 15:39:49');
/*!40000 ALTER TABLE `credit_info` ENABLE KEYS */;
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
  `transformed` tinyint(1) NOT NULL DEFAULT '0',
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` varchar(45) NOT NULL,
  `wechat` varchar(45) DEFAULT NULL,
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
INSERT INTO `customer` VALUES ('CUSeeffle14','李克勤','AGTrlorvl21',0,0,'2016-06-22 13:38:25',NULL),('CUSezxzfy22','李彦宏','AGTrlorvl21',0,0,'2016-06-22 13:37:54',NULL),('CUSffoyrl45','马化腾','AGTrlorvl21',0,0,'2016-06-19 15:42:50',NULL),('CUSfreyel88','李四','AGTwylxfz73',0,0,'2016-06-19 15:54:19',NULL),('CUSfyrxel59','宝马','AGTrlorvl21',0,0,'2016-06-22 13:36:35',NULL),('CUSlilefv64','宋仲基','AGTrewlfr7',0,0,'2016-06-19 15:46:24',NULL),('CUSllwxez81','马英九','AGTrlorvl21',0,0,'2016-06-22 13:34:46',NULL),('CUSlxfilv35','马云','AGTrlorvl21',0,0,'2016-06-22 13:33:28',NULL),('CUSorzvvv30','李咏','AGTrlorvl21',0,0,'2016-06-22 14:02:05',NULL),('CUSrolyeo12','马尔代夫','AGTrlorvl21',0,0,'2016-06-22 13:35:09',NULL),('CUSwefevl98','李易峰','AGTrlorvl21',0,0,'2016-06-22 13:39:22',NULL),('CUSwviyvf61','张三','AGTwylxfz73',0,1,'2016-06-19 15:47:10',NULL),('CUSyfvfve28','刘嘉','AGTfrrofe9',0,0,'2016-06-21 11:05:00',NULL),('CUSyfvvwv93','章剑','AGTylfflz66',0,0,'2016-06-19 15:46:05',NULL),('CUSyrlliy60','马尔代夫群岛','AGTrlorvl21',0,0,'2016-06-22 13:35:40',NULL),('CUSzeiooo57','马尼拉','AGTrlorvl21',0,0,'2016-06-22 13:37:06',NULL);
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
INSERT INTO `customer_address` VALUES ('ADRerlvro34','CUSfreyel88','浙江',1,'2016-06-19 16:08:48'),('ADRerzyyy37','CUSlilefv64','南京大学金陵',1,'2016-06-19 15:58:41'),('ADRexwxfe30','CUSlilefv64','南京大学金陵晚报',1,'2016-06-19 16:19:23'),('ADRexyyvf81','CUSffoyrl45','中国深圳市某某某区',1,'2016-06-19 15:42:50'),('ADRfeeflz82','CUSrolyeo12','台北市中正区重庆南路一段122号',0,'2016-06-22 13:35:09'),('ADRfefyll49','CUSyfvvwv93','江苏省南京市鼓楼区汉口路22导人',0,'2016-06-19 16:14:00'),('ADRfeyoyl83','CUSeeffle14','香港',0,'2016-06-22 13:38:25'),('ADRflflxe91','CUSlilefv64','南京大学南园博',1,'2016-06-19 15:49:29'),('ADRfyfwfr15','CUSlilefv64','南京大学',1,'2016-06-19 15:46:24'),('ADRfyoyev62','CUSwefevl98','北京市朝阳区京润水上花园别墅',0,'2016-06-22 13:39:22'),('ADRfyxeyf10','CUSlilefv64','南京大学金陵',1,'2016-06-19 16:09:44'),('ADRivvefl9','CUSlilefv64','南京大学金陵石化填满',1,'2016-06-19 16:10:24'),('ADRixzlir0','CUSlilefv64','南京大学金陵石化',1,'2016-06-19 16:09:57'),('ADRiyyeyz7','CUSyfvfve28','南京市鼓楼区北京西路二号新村3-103',0,'2016-06-21 11:05:00'),('ADRleizof81','CUSlilefv64','南京大学金陵石化填满你说你的',1,'2016-06-19 16:29:58'),('ADRlffwrv50','CUSlilefv64','南京大学金陵',1,'2016-06-19 16:15:12'),('ADRlilyxy50','CUSyfvvwv93','江苏省南京市鼓楼区汉口路22方领导人',1,'2016-06-19 16:12:04'),('ADRlleyly23','CUSyrlliy60','台北市中正区重庆南路一段122号',0,'2016-06-22 13:35:40'),('ADRlryvfw45','CUSlilefv64','南京大学南园3舍',1,'2016-06-19 15:50:14'),('ADRlvflvl70','CUSfyrxel59','慕尼黑宝马总部宝马大厦',0,'2016-06-22 13:36:35'),('ADRlxefof63','CUSlxfilv35',' 阿里巴巴集团 香港办事处 香港湾仔分域街18号捷利中心25层',0,'2016-06-22 13:33:28'),('ADRlxyovi9','CUSfreyel88','江苏',1,'2016-06-19 15:55:38'),('ADRoeevfl55','CUSlilefv64','南京大学',0,'2016-06-20 09:47:48'),('ADRoeyfoe37','CUSzeiooo57','马尼拉',0,'2016-06-22 13:37:06'),('ADRowllvx71','CUSfreyel88','云南',1,'2016-06-19 15:55:49'),('ADRoxfwrw81','CUSlilefv64','南京大学南园',1,'2016-06-19 15:49:09'),('ADRryryyi79','CUSlilefv64','南京大学',1,'2016-06-19 16:19:48'),('ADRveflff0','CUSlilefv64','南京大学南园',1,'2016-06-19 15:49:38'),('ADRvefyif24','CUSwviyvf61','南京',0,'2016-06-19 15:47:10'),('ADRvfeolf25','CUSorzvvv30','北京',0,'2016-06-22 14:02:05'),('ADRvoovvo33','CUSyfvvwv93','江苏省南京市鼓楼区汉口路22方法',1,'2016-06-19 16:11:51'),('ADRvweero66','CUSllwxez81','台北市中正区重庆南路一段122号',0,'2016-06-22 13:34:46'),('ADRvylyel81','CUSfreyel88','浙江杭州',0,'2016-06-19 16:10:50'),('ADRvyxeff45','CUSlilefv64','南京大学南园3舍',1,'2016-06-19 15:48:50'),('ADRwfeoww58','CUSffoyrl45','中国深圳市某某某22',0,'2016-06-19 16:08:03'),('ADRwfiozr47','CUSlilefv64','南京大学金陵石化填满你说',1,'2016-06-19 16:10:07'),('ADRwiyzlo47','CUSlilefv64','南京大学金陵石化填满你说你的',1,'2016-06-19 16:10:19'),('ADRxffrve11','CUSlilefv64','南京大学金陵554',1,'2016-06-19 15:58:31'),('ADRyirfey55','CUSffoyrl45','中国深圳市某某某',1,'2016-06-19 16:07:40'),('ADRyyyfor35','CUSlilefv64','南京大学金陵晚报',1,'2016-06-19 16:09:29'),('ADRzeiyox47','CUSyfvvwv93','江苏省南京市鼓楼区汉口路22号',1,'2016-06-19 15:46:05'),('ADRzfolio59','CUSfreyel88','云南',1,'2016-06-19 15:54:19'),('ADRzoieee40','CUSffoyrl45','中国深圳市某某某区22',1,'2016-06-19 16:07:28'),('ADRzozixo72','CUSlilefv64','南京大学金陵',1,'2016-06-19 15:53:56'),('ADRzywevf19','CUSezxzfy22','北京市海淀区海淀大街38号银科大厦19层',0,'2016-06-22 13:37:54');
/*!40000 ALTER TABLE `customer_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_order`
--

DROP TABLE IF EXISTS `customer_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_order` (
  `order_id` varchar(20) NOT NULL,
  `goods_id` varchar(20) NOT NULL,
  `agent_id` varchar(20) DEFAULT NULL,
  `wechat` varchar(45) DEFAULT NULL,
  `quantity` int(11) NOT NULL DEFAULT '0',
  `receiver_name` varchar(45) NOT NULL,
  `receiver_phone` varchar(20) NOT NULL,
  `receiver_addr` varchar(100) NOT NULL,
  `total_price` double NOT NULL DEFAULT '0',
  `order_status` int(11) NOT NULL DEFAULT '0',
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `agent_id_idx` (`agent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_order`
--

LOCK TABLES `customer_order` WRITE;
/*!40000 ALTER TABLE `customer_order` DISABLE KEYS */;
INSERT INTO `customer_order` VALUES ('CUOelllre53','COMyfxwez26',NULL,NULL,1,'王旻','15805166833','甘肃兰州皋兰睡觉睡觉',0.02,1,0,'2016-06-19 12:57:31'),('CUOewolee35','COMyfxwez26','AGTrewlfr7',NULL,1,'我','15800000000','安徽合肥长丰你',0.01,1,0,'2016-06-20 12:56:10'),('CUOeyyfff90','COMyfxwez26','AGTrewlfr7',NULL,1,'章许帆','15895862305','北京昌平昌平还是说很多话',0.01,1,0,'2016-06-20 13:22:58'),('CUOeyzoef32','COMyfxwez26',NULL,NULL,2,'你的','15805685625','海外阿根廷阿根廷一个人',0.04,1,0,'2016-06-19 14:01:43'),('CUOfellye20','COMyfxwez26',NULL,NULL,1,'周文王','15850580905','澳门澳门澳门不知道',0.02,1,0,'2016-06-19 12:30:10'),('CUOfevexo48','COMyfxwez26','AGTefzioe24',NULL,1,'我','15000000000','澳门澳门澳门你',0.01,1,0,'2016-06-19 15:10:14'),('CUOflwlxz54','COMyfxwez26',NULL,NULL,1,'周文王','15850580905','澳门澳门澳门南京',0.02,1,0,'2016-06-19 13:41:08'),('CUOfvvoex92','COMyfxwez26','AGTrewlfr7',NULL,1,'你','15800000000','澳门澳门澳门在',0.01,1,0,'2016-06-20 13:21:18'),('CUOfvyvvx90','COMyfxwez26','AGTrewlfr7',NULL,1,'章许帆','15895862305','北京昌平昌平还是说很多话',0.01,1,0,'2016-06-20 13:22:26'),('CUOfwoifr76','COMyfxwez26',NULL,NULL,1,'王旻','15805166833','甘肃兰州皋兰睡觉睡觉',0.02,1,0,'2016-06-19 12:57:15'),('CUOieoffo61','COMyfxwez26','AGTrewlfr7',NULL,1,'我','15000000000','澳门澳门澳门南京',0.01,1,0,'2016-06-20 11:04:44'),('CUOieovfz69','COMyfxwez26','AGTefzioe24',NULL,1,'王旻','15805166833','北京昌平昌平一个人',0.01,1,0,'2016-06-19 14:19:24'),('CUOieyyel73','COMyfxwez26',NULL,NULL,1,'周文王','15850580905','澳门澳门澳门不知道',0.02,1,0,'2016-06-19 12:30:06'),('CUOlflvlo61','COMyfxwez26','AGTrewlfr7',NULL,1,'翟','15000000000','江苏南京高淳南京大学',0.01,1,0,'2016-06-20 11:01:57'),('CUOlfrvlv1','COMyfxwez26',NULL,NULL,2,'你的','15805685625','海外阿根廷阿根廷一个人',0.04,1,0,'2016-06-19 14:01:48'),('CUOllfzfo8','COMyfxwez26',NULL,NULL,1,'王旻','15866666666','内蒙古呼和浩特和林格尔不知道',0.02,1,0,'2016-06-19 12:35:12'),('CUOlllvoe29','COMyfxwez26',NULL,NULL,1,'王旻','15866666666','内蒙古呼和浩特和林格尔不知道',0.02,1,0,'2016-06-19 12:35:03'),('CUOllvvew24','COMyfxwez26','AGTefzioe24',NULL,1,'在','15866669999','安徽合肥长丰在一起',0.01,1,0,'2016-06-19 14:20:47'),('CUOllwyyl63','COMyfxwez26',NULL,NULL,2,'你的','15805685625','海外阿根廷阿根廷一个人',0.04,1,0,'2016-06-19 14:01:41'),('CUOlvolef47','COMyfxwez26',NULL,NULL,2,'你的','15805685625','海外阿根廷阿根廷一个人',0.04,1,0,'2016-06-19 14:00:51'),('CUOlyeyzy49','COMyfxwez26',NULL,NULL,1,'我啊','15805166833','澳门澳门澳门一个人',0.02,1,0,'2016-06-19 14:04:50'),('CUOoiolyl42','COMyfxwez26',NULL,NULL,1,'周文王','15850580905','澳门澳门澳门不知道',0.02,1,0,'2016-06-19 12:31:22'),('CUOoiyzwo15','COMyfxwez26',NULL,NULL,1,'周文王','15850580905','澳门澳门澳门不知道',0.02,1,0,'2016-06-19 12:29:56'),('CUOolylwf55','COMyfxwez26',NULL,NULL,1,'我的','15805166833','澳门澳门澳门双结局',0.02,1,0,'2016-06-19 14:11:25'),('CUOooxwyf50','COMyfxwez26',NULL,NULL,2,'你的','15805685625','海外阿根廷阿根廷一个人',0.04,1,0,'2016-06-19 14:01:47'),('CUOorioey95','COMyfxwez26',NULL,NULL,1,'你的','15805685625','海外阿根廷阿根廷一个人',0.02,1,0,'2016-06-19 14:00:16'),('CUOoxoefy43','COMyfxwez26',NULL,NULL,1,'王旻','15866666666','内蒙古呼和浩特和林格尔不知道',0.02,1,0,'2016-06-19 12:34:53'),('CUOoyefew17','COMyfxwez26','AGTefzioe24',NULL,1,'在','15866669999','安徽合肥长丰在一起',0.01,1,0,'2016-06-19 14:22:08'),('CUOrfvyzv11','COMyfxwez26','AGTefzioe24',NULL,1,'ud','15515556325','福建福州长乐在一起时',0.01,1,0,'2016-06-19 14:23:43'),('CUOrioxxy87','COMyfxwez26',NULL,NULL,2,'你的','15805685625','海外阿根廷阿根廷一个人',0.04,1,0,'2016-06-19 14:01:45'),('CUOroeeef90','COMyfxwez26','AGTrewlfr7',NULL,1,'我','15000000000','澳门澳门澳门南京',0.01,1,0,'2016-06-20 12:23:11'),('CUOveoyyy91','COMyfxwez26','AGTrewlfr7',NULL,1,'我','13000000000','安徽合肥长丰我',0.01,1,0,'2016-06-20 12:36:24'),('CUOvfwfef71','COMyfxwez26','AGTrewlfr7',NULL,1,'我','15000000000','澳门澳门澳门澳门特区',0.01,1,0,'2016-06-20 10:56:29'),('CUOvirfee32','COMyfxwez26',NULL,NULL,1,'你的','15805685625','海外阿根廷阿根廷一个人',0.02,1,0,'2016-06-19 14:00:04'),('CUOvizylf49','COMyfxwez26',NULL,NULL,1,'委屈难过','15805666666','澳门澳门澳门嘟嘟',0.02,1,0,'2016-06-19 13:24:58'),('CUOvlfvvv39','COMyfxwez26',NULL,NULL,1,'王旻','15866669999','海南海口海口不知道',0.02,1,0,'2016-06-19 12:42:38'),('CUOwlivfi74','COMyfxwez26','AGTrewlfr7',NULL,1,'你','15800000000','澳门澳门澳门在',0.01,1,0,'2016-06-20 13:21:29'),('CUOxrfvvw37','COMyfxwez26',NULL,NULL,1,'王旻','15805166833','甘肃兰州皋兰睡觉睡觉',0.02,1,0,'2016-06-19 12:57:25'),('CUOyerofe96','COMyfxwez26',NULL,NULL,1,'你的','15805685625','海外阿根廷阿根廷一个人',0.02,1,0,'2016-06-19 14:00:12'),('CUOyllrey77','COMyfxwez26',NULL,NULL,1,'周文王','15850580905','澳门澳门澳门不知道',0.02,1,0,'2016-06-19 12:30:53'),('CUOyoizve50','COMyfxwez26','AGTrewlfr7',NULL,1,'我','15800000000','澳门澳门澳门我',0.01,1,0,'2016-06-20 12:34:05'),('CUOyoozfy55','COMyfxwez26',NULL,NULL,1,'我的','15805166833','澳门澳门澳门双结局',0.02,1,0,'2016-06-19 14:10:59'),('CUOyvziff65','COMyfxwez26','AGTrewlfr7',NULL,1,'周文王','15850580905','安徽合肥长丰南京',0.01,1,0,'2016-06-20 10:53:33'),('CUOyxfxff72','COMyfxwez26','AGTrewlfr7',NULL,1,'我','18000000008','北京昌平昌平北京',0.01,1,0,'2016-06-20 12:25:06'),('CUOzellvw55','COMyfxwez26','AGTrewlfr7',NULL,1,'我','15600000000','北京昌平昌平吧',0.01,1,0,'2016-06-20 12:31:16');
/*!40000 ALTER TABLE `customer_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_order_bill`
--

DROP TABLE IF EXISTS `customer_order_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_order_bill` (
  `bill_id` varchar(20) NOT NULL,
  `order_id` varchar(20) NOT NULL,
  `channel_name` varchar(45) NOT NULL,
  `client_ip` varchar(45) NOT NULL,
  `bill_amount` double NOT NULL,
  `bill_status` int(11) NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`bill_id`),
  KEY `fk_customer_order_bill_customer_order1_idx` (`order_id`),
  CONSTRAINT `fk_customer_order_bill_customer_order1` FOREIGN KEY (`order_id`) REFERENCES `customer_order` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_order_bill`
--

LOCK TABLES `customer_order_bill` WRITE;
/*!40000 ALTER TABLE `customer_order_bill` DISABLE KEYS */;
INSERT INTO `customer_order_bill` VALUES ('COBeefvri18','CUOfwoifr76','wx_pub','117.136.45.101',0.02,0,0,'2016-06-19 12:57:16'),('COBefeyly17','CUOlvolef47','wx_pub','218.104.116.54',0.04,0,0,'2016-06-19 14:00:53'),('COBeleffi57','CUOoyefew17','wx_pub','218.104.116.54',0.01,1,0,'2016-06-19 14:22:09'),('COBeozfov81','CUOoiyzwo15','wx_pub','218.104.116.54',0.02,0,0,'2016-06-19 12:29:57'),('COBevfeee88','CUOfvyvvx90','wx_pub','218.104.116.54',0.01,0,0,'2016-06-20 13:22:29'),('COBexeeox76','CUOvirfee32','wx_pub','218.104.116.54',0.02,0,0,'2016-06-19 14:00:06'),('COBeyevzf32','CUOfvvoex92','wx_pub','218.104.116.54',0.01,0,0,'2016-06-20 13:21:20'),('COBfevxle29','CUOlflvlo61','wx_pub','218.104.116.54',0.01,1,0,'2016-06-20 11:01:59'),('COBfllefy23','CUOyoizve50','wx_pub','218.104.116.54',0.01,1,0,'2016-06-20 12:34:06'),('COBflwelr61','CUOwlivfi74','wx_pub','218.104.116.54',0.01,1,0,'2016-06-20 13:21:30'),('COBfolfoz88','CUOyoozfy55','wx_pub','218.104.116.54',0.02,0,0,'2016-06-19 14:11:01'),('COBfvivll12','CUOllvvew24','wx_pub','218.104.116.54',0.01,0,0,'2016-06-19 14:20:49'),('COBfzllvo41','CUOieyyel73','wx_pub','218.104.116.54',0.02,0,0,'2016-06-19 12:30:07'),('COBieveif27','CUOorioey95','wx_pub','218.104.116.54',0.02,0,0,'2016-06-19 14:00:17'),('COBiylfll61','CUOflwlxz54','wx_pub','218.104.116.54',0.02,1,0,'2016-06-19 13:41:10'),('COBlfflxe64','CUOelllre53','wx_pub','117.136.45.101',0.02,0,0,'2016-06-19 12:57:32'),('COBlvflrw45','CUOlllvoe29','wx_pub','218.104.116.54',0.02,0,0,'2016-06-19 12:35:04'),('COBlvlrel68','CUOyxfxff72','wx_pub','218.104.116.54',0.01,1,0,'2016-06-20 12:25:07'),('COBoezelo70','CUOolylwf55','wx_pub','218.104.116.54',0.02,0,0,'2016-06-19 14:11:26'),('COBolvoxl92','CUOieoffo61','wx_pub','218.104.116.54',0.01,1,0,'2016-06-20 11:04:45'),('COBooeeev16','CUOfellye20','wx_pub','218.104.116.54',0.02,0,0,'2016-06-19 12:30:11'),('COBovolyy73','CUOvizylf49','wx_pub','117.136.66.143',0.02,0,0,'2016-06-19 13:24:59'),('COBovvlwy37','CUOrfvyzv11','wx_pub','218.104.116.54',0.01,1,0,'2016-06-19 14:23:45'),('COBrevyyf33','CUOlyeyzy49','wx_pub','218.104.116.54',0.02,0,0,'2016-06-19 14:04:52'),('COBrflolx91','CUOfevexo48','wx_pub','218.104.116.54',0.01,1,0,'2016-06-19 15:10:15'),('COBryxvel50','CUOyvziff65','wx_pub','218.104.116.54',0.01,1,0,'2016-06-20 10:53:34'),('COBrzzoei51','CUOroeeef90','wx_pub','218.104.116.54',0.01,1,0,'2016-06-20 12:23:12'),('COBvilywv82','CUOzellvw55','wx_pub','218.104.116.54',0.01,1,0,'2016-06-20 12:31:19'),('COBvvoeee35','CUOvfwfef71','wx_pub','218.104.116.54',0.01,1,0,'2016-06-20 10:56:31'),('COBweefef65','CUOewolee35','wx_pub','218.104.116.54',0.01,1,0,'2016-06-20 12:56:11'),('COBxfleel20','CUOlfrvlv1','wx_pub','218.104.116.54',0.04,0,0,'2016-06-19 14:01:50'),('COBxixlef23','CUOoiolyl42','wx_pub','218.104.116.54',0.02,0,0,'2016-06-19 12:31:23'),('COByefzee28','CUOoxoefy43','wx_pub','218.104.116.54',0.02,0,0,'2016-06-19 12:34:55'),('COByoyvoy8','CUOveoyyy91','wx_pub','218.104.116.54',0.01,1,0,'2016-06-20 12:36:25'),('COByvffwe27','CUOxrfvvw37','wx_pub','117.136.45.101',0.02,0,0,'2016-06-19 12:57:26'),('COBzivvfe57','CUOieovfz69','wx_pub','218.104.116.54',0.01,1,0,'2016-06-19 14:19:25'),('COBziyoey47','CUOvlfvvv39','wx_pub','218.104.116.54',0.02,0,0,'2016-06-19 12:42:39');
/*!40000 ALTER TABLE `customer_order_bill` ENABLE KEYS */;
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
INSERT INTO `customer_phone` VALUES ('PNMexvfrf98','CUSlilefv64','15000000000',0,'2016-06-19 15:46:24'),('PNMeyzrrf51','CUSrolyeo12','18849758373',0,'2016-06-22 13:35:09'),('PNMffefel22','CUSezxzfy22','17388333333',0,'2016-06-22 13:37:54'),('PNMfylvve91','CUSfyrxel59','15968489999',0,'2016-06-22 13:36:35'),('PNMfzilee23','CUSllwxez81','13458484848',0,'2016-06-22 13:34:46'),('PNMilzvvl8','CUSeeffle14','18494944333',0,'2016-06-22 13:38:25'),('PNMiylofx86','CUSorzvvv30','18343736134',0,'2016-06-22 14:02:05'),('PNMleewyf38','CUSyfvfve28','18662702209',0,'2016-06-21 11:05:00'),('PNMlolxxx14','CUSfreyel88','15764568594',0,'2016-06-19 15:54:19'),('PNMofilyw75','CUSlxfilv35','18566667777',0,'2016-06-22 13:33:28'),('PNMrfeeyf85','CUSffoyrl45','15986569998',0,'2016-06-19 15:42:50'),('PNMviezoi1','CUSyfvvwv93','13044630938',0,'2016-06-19 15:46:05'),('PNMvolyyi44','CUSwefevl98','18822229999',0,'2016-06-22 13:39:22'),('PNMwfrliv37','CUSwviyvf61','15764568594',0,'2016-06-19 15:47:10'),('PNMwoeyew68','CUSyrlliy60','15933332222',0,'2016-06-22 13:35:40'),('PNMyvolwl52','CUSzeiooo57','18494943333',0,'2016-06-22 13:37:06');
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
INSERT INTO `deposit_bill` VALUES ('DPBelilll87','AGTwylxfz73','wx_pub','218.104.116.54',0.01,1,0,'2016-06-19 15:46:12'),('DPBelvfzi67','AGTrlorvl21','wx_pub','117.136.45.101',0.01,1,0,'2016-06-21 11:08:47'),('DPBleovvx75','AGTrlorvl21','wx_pub','45.62.96.197',0.01,0,0,'2016-06-19 16:46:12'),('DPBlrovyi2','AGTylfflz66','wx_pub','211.162.81.115',1,0,0,'2016-06-21 10:57:41'),('DPBoixlee6','AGTylfflz66','wx_pub','211.162.81.115',0.01,0,0,'2016-06-21 10:44:45'),('DPBzyrrze50','AGTrlorvl21','wx_pub','45.62.96.197',0.01,1,0,'2016-06-19 16:46:23');
/*!40000 ALTER TABLE `deposit_bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `distribute_code`
--

DROP TABLE IF EXISTS `distribute_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `distribute_code` (
  `distribute_code_id` varchar(20) NOT NULL,
  `agent_id` varchar(20) NOT NULL,
  `code_value` varchar(45) NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`distribute_code_id`),
  KEY `fk_distribute_code_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_distribute_code_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `distribute_code`
--

LOCK TABLES `distribute_code` WRITE;
/*!40000 ALTER TABLE `distribute_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `distribute_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `express`
--

DROP TABLE IF EXISTS `express`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `express` (
  `express_id` varchar(20) NOT NULL,
  `order_item_id` varchar(20) DEFAULT NULL,
  `express_no` varchar(45) NOT NULL,
  `sender_name` varchar(45) NOT NULL,
  `sender_phone` varchar(45) NOT NULL,
  `sender_address` varchar(50) NOT NULL,
  `receiver_name` varchar(45) NOT NULL,
  `receiver_phone` varchar(45) NOT NULL,
  `receiver_address` varchar(50) NOT NULL,
  `goods_name` varchar(45) DEFAULT NULL,
  `blockFlag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`express_id`),
  KEY `fk_express_order_item1_idx` (`order_item_id`),
  CONSTRAINT `fk_express_order_item1` FOREIGN KEY (`order_item_id`) REFERENCES `order_item` (`order_item_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `express`
--

LOCK TABLES `express` WRITE;
/*!40000 ALTER TABLE `express` DISABLE KEYS */;
/*!40000 ALTER TABLE `express` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `express_agent`
--

DROP TABLE IF EXISTS `express_agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `express_agent` (
  `express_id` varchar(20) NOT NULL,
  `order_item_id` varchar(20) DEFAULT NULL,
  `express_no` varchar(45) NOT NULL,
  `sender_name` varchar(45) NOT NULL,
  `sender_phone` varchar(45) NOT NULL,
  `sender_address` varchar(50) NOT NULL,
  `receiver_name` varchar(45) NOT NULL,
  `receiver_phone` varchar(45) NOT NULL,
  `receiver_address` varchar(50) NOT NULL,
  `goods_name` varchar(45) DEFAULT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`express_id`),
  KEY `fk_express_order_item1_idx` (`order_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `express_agent`
--

LOCK TABLES `express_agent` WRITE;
/*!40000 ALTER TABLE `express_agent` DISABLE KEYS */;
INSERT INTO `express_agent` VALUES ('EPAevveix51','ORIxyyrfr92','900887','云南云草纲目生物科技有限公司','1589582305','云南省昆明市盘龙区东风东路36号建工大楼14层','马化腾','15986569998','中国深圳市某某某22','三七粉',0,'2016-06-30 14:27:13');
/*!40000 ALTER TABLE `express_agent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `express_customer`
--

DROP TABLE IF EXISTS `express_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `express_customer` (
  `express_id` varchar(20) NOT NULL,
  `customer_order_id` varchar(20) DEFAULT NULL,
  `express_no` varchar(20) DEFAULT NULL,
  `sender_name` varchar(45) NOT NULL,
  `sender_phone` varchar(45) NOT NULL,
  `sender_address` varchar(50) NOT NULL,
  `receiver_name` varchar(45) NOT NULL,
  `receiver_phone` varchar(45) NOT NULL,
  `receiver_address` varchar(50) NOT NULL,
  `goods_name` varchar(45) NOT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`express_id`),
  KEY `fk_express_customer_customer_order1_idx` (`customer_order_id`),
  CONSTRAINT `fk_express_customer_customer_order1` FOREIGN KEY (`customer_order_id`) REFERENCES `customer_order` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `express_customer`
--

LOCK TABLES `express_customer` WRITE;
/*!40000 ALTER TABLE `express_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `express_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follower`
--

DROP TABLE IF EXISTS `follower`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `follower` (
  `follower_wechat_id` varchar(30) NOT NULL,
  `follower_nickname` varchar(45) DEFAULT NULL,
  `follower_gender` tinyint(1) NOT NULL DEFAULT '0',
  `follower_city` varchar(45) DEFAULT NULL,
  `follower_province` varchar(45) DEFAULT NULL,
  `block_flag` tinyint(4) NOT NULL DEFAULT '0',
  `create_time` varchar(45) NOT NULL,
  PRIMARY KEY (`follower_wechat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follower`
--

LOCK TABLES `follower` WRITE;
/*!40000 ALTER TABLE `follower` DISABLE KEYS */;
INSERT INTO `follower` VALUES ('ozqMbv01oBIlNd4g7YA94oh6n4Ys','王旻',1,'南京','江苏',0,'2016-06-19 15:37:05'),('ozqMbv3Q_cVoUmsxKsnutvDmHmfE','章许帆',1,'南京','江苏',0,'2016-06-19 15:52:47'),('ozqMbv4C-tpz8yXQ5x_5Sb5Scz9o','今天',1,'南京','江苏',0,'2016-06-20 11:24:21'),('ozqMbvwv6XTlyc8n5Ju963mQDYXw','王晓迪',1,'南京','江苏',0,'2016-06-19 15:37:37'),('ozqMbvxvlX0O8wBeRbwbpLDNo5pA','雯雯',2,'南京','江苏',0,'2016-06-22 14:24:29');
/*!40000 ALTER TABLE `follower` ENABLE KEYS */;
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
  `prime_price` double NOT NULL,
  `agent_price` double NOT NULL,
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
INSERT INTO `goods` VALUES ('COMyfxwez26','三七粉',398,298,'三七粉，岂止于养生',0,'2016-07-05 17:15:01');
/*!40000 ALTER TABLE `goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods_thumbnail`
--

DROP TABLE IF EXISTS `goods_thumbnail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goods_thumbnail` (
  `thumbnail_id` varchar(20) NOT NULL,
  `goods_id` varchar(20) DEFAULT NULL,
  `thumbnail_path` varchar(100) NOT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` varchar(45) NOT NULL,
  PRIMARY KEY (`thumbnail_id`),
  KEY `fk_goods_thumbnail_goods1_idx` (`goods_id`),
  CONSTRAINT `fk_goods_thumbnail_goods1` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`goods_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_thumbnail`
--

LOCK TABLES `goods_thumbnail` WRITE;
/*!40000 ALTER TABLE `goods_thumbnail` DISABLE KEYS */;
/*!40000 ALTER TABLE `goods_thumbnail` ENABLE KEYS */;
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
  `order_price` double NOT NULL DEFAULT '0',
  `order_type` int(11) NOT NULL DEFAULT '0',
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
INSERT INTO `order` VALUES ('ODRxflewf14','AGTrlorvl21',2,298,1,0,'2016-07-05 17:18:41');
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
  `order_id` varchar(20) NOT NULL,
  `channel_name` varchar(45) NOT NULL,
  `client_ip` varchar(45) NOT NULL,
  `bill_amount` double NOT NULL,
  `bill_status` int(11) NOT NULL DEFAULT '0',
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`bill_id`),
  KEY `fk_order_bill_agent1_idx` (`agent_id`),
  KEY `fk_order_bill_order1` (`order_id`),
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
  `receive_address` varchar(100) NOT NULL,
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
INSERT INTO `order_item` VALUES ('ORIielvoy1','ODRxflewf14','COMyfxwez26',1,'慕尼黑宝马总部宝马大厦',1,298,'CUSfyrxel59',0,'2016-07-05 17:18:41');
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_pool`
--

DROP TABLE IF EXISTS `order_pool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_pool` (
  `pool_id` varchar(20) NOT NULL,
  `agent_id` varchar(20) NOT NULL,
  `goods_id` varchar(20) NOT NULL,
  `refund_config_id` varchar(20) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT '0',
  `price` double NOT NULL DEFAULT '0',
  `refund_amount` double NOT NULL DEFAULT '0',
  `pool_date` date NOT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`pool_id`),
  KEY `fk_order_pool_agent1_idx` (`agent_id`),
  KEY `fk_order_pool_goods1_idx` (`goods_id`),
  KEY `fk_order_pool_refund_config1_idx` (`refund_config_id`),
  CONSTRAINT `fk_order_pool_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_pool_goods1` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`goods_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_pool_refund_config1` FOREIGN KEY (`refund_config_id`) REFERENCES `refund_config` (`refund_config_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_pool`
--

LOCK TABLES `order_pool` WRITE;
/*!40000 ALTER TABLE `order_pool` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_pool` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refund`
--

DROP TABLE IF EXISTS `refund`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `refund` (
  `refund_id` varchar(20) NOT NULL,
  `refund_name` varchar(45) NOT NULL,
  `redund_percent` varchar(45) NOT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`refund_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refund`
--

LOCK TABLES `refund` WRITE;
/*!40000 ALTER TABLE `refund` DISABLE KEYS */;
/*!40000 ALTER TABLE `refund` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refund_config`
--

DROP TABLE IF EXISTS `refund_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `refund_config` (
  `refund_config_id` varchar(20) NOT NULL,
  `goods_id` varchar(20) DEFAULT NULL,
  `refund_trigger_amount` int(11) NOT NULL,
  `level_1_percent` double DEFAULT '0',
  `level_2_percent` double DEFAULT '0',
  `level_3_percent` double DEFAULT '0',
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`refund_config_id`),
  KEY `fk_refund_config_goods1_idx` (`goods_id`),
  CONSTRAINT `fk_refund_config_goods1` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`goods_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refund_config`
--

LOCK TABLES `refund_config` WRITE;
/*!40000 ALTER TABLE `refund_config` DISABLE KEYS */;
INSERT INTO `refund_config` VALUES ('RCGlvlfvf37','COMyfxwez26',15,85,20,10,0,'2016-07-05 17:15:25');
/*!40000 ALTER TABLE `refund_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refund_record`
--

DROP TABLE IF EXISTS `refund_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `refund_record` (
  `refund_id` varchar(20) NOT NULL,
  `agent_id` varchar(20) NOT NULL,
  `order_pool_id` varchar(45) DEFAULT NULL,
  `refund_name` varchar(45) NOT NULL,
  `refund_description` varchar(100) NOT NULL,
  `refund_percent` double NOT NULL,
  `refund_amount` double NOT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`refund_id`),
  KEY `fk_refund_record_order_pool1_idx` (`order_pool_id`),
  KEY `fk_refund_record_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_refund_record_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_refund_record_order_pool1` FOREIGN KEY (`order_pool_id`) REFERENCES `order_pool` (`pool_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refund_record`
--

LOCK TABLES `refund_record` WRITE;
/*!40000 ALTER TABLE `refund_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `refund_record` ENABLE KEYS */;
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
  `role_description` varchar(45) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES ('ROL00000001','admin',0,'2016-05-03 11:00:00','管理员'),('ROL00000002','agent',0,'2016-05-03 11:00:10','代理商'),('ROL00000003','auditor',0,'2016-05-03 11:00:20','代理审核员'),('ROL00000004','express',0,'2016-05-03 11:00:30','发货员'),('ROL00000005','finance',0,'2016-05-03 11:00:30','财务人员');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ship_config`
--

DROP TABLE IF EXISTS `ship_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ship_config` (
  `ship_config_id` varchar(20) NOT NULL,
  `ship_config_date` int(11) NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`ship_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ship_config`
--

LOCK TABLES `ship_config` WRITE;
/*!40000 ALTER TABLE `ship_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `ship_config` ENABLE KEYS */;
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
INSERT INTO `user` VALUES ('USR00000001','admin','21232f297a57a5a743894a0e4a801fc3','ROL00000001','MNG00000001',NULL,0,'2016-05-03 11:00:20'),('USReyofez14','15850580905','e10adc3949ba59abbe56e057f20f883e','ROL00000002',NULL,'AGTrewlfr7',0,'2016-06-19 15:39:30'),('USRfyvrwo26','15869696969','f85e65970c62c8b667f315fdebee5505','ROL00000002',NULL,'AGTeefoeo87',0,'2016-06-23 17:00:42'),('USRixwfyr89','15805166899','b5bf27b2555de44e3df2230080db5a1d','ROL00000002',NULL,'AGTfeoeyl96',0,'2016-06-23 17:13:16'),('USRlrvyoy13','18662702209','e10adc3949ba59abbe56e057f20f883e','ROL00000002',NULL,'AGTfrrofe9',0,'2016-06-21 10:51:40'),('USRlxoyve63','15805166966','77cbc257e66302866cf6191754c0c8e3','ROL00000002',NULL,'AGTwolrye13',0,'2016-06-23 16:46:56'),('USRollwyx66','15869652356','d3cb757121f725fe825a1176031a1c14','ROL00000002',NULL,'AGTllfefy86',0,'2016-06-23 17:07:32'),('USRroifrl26','15805166833','e10adc3949ba59abbe56e057f20f883e','ROL00000002',NULL,'AGTrlorvl21',0,'2016-06-19 15:39:37'),('USRwofoif51','15805166834','700f6fa0edb608ee5cc3cfa63f1c94cc','ROL00000002',NULL,'AGTxvviev79',0,'2016-06-23 17:21:25'),('USRyffrfe70','15634233636','67d77086409d29be26ffc46270dc142f','ROL00000002',NULL,'AGTfoolvy45',0,'2016-06-23 17:28:54'),('USRyveovw36','18994050872','430bad64fbd395fec55cfd428641c21e','ROL00000002',NULL,'AGTeoyiel50',0,'2016-06-21 13:07:40'),('USRzoelwx79','15805167546','e10adc3949ba59abbe56e057f20f883e','ROL00000002',NULL,'AGTwylxfz73',0,'2016-06-19 15:39:49'),('USRzzffvl96','15895862305','ba0f58ea98e159854d3434ce2b6b04e1','ROL00000002',NULL,'AGTylfflz66',0,'2016-06-19 15:40:07');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `withdraw_record`
--

DROP TABLE IF EXISTS `withdraw_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `withdraw_record` (
  `withdraw_id` varchar(20) NOT NULL,
  `agent_id` varchar(20) DEFAULT NULL,
  `current_coffer` double NOT NULL DEFAULT '0',
  `wechat` varchar(45) NOT NULL,
  `withdraw_amount` double NOT NULL DEFAULT '0',
  `withdraw_status` tinyint(1) NOT NULL DEFAULT '0',
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`withdraw_id`),
  KEY `fk_withdraw_record_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_withdraw_record_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `withdraw_record`
--

LOCK TABLES `withdraw_record` WRITE;
/*!40000 ALTER TABLE `withdraw_record` DISABLE KEYS */;
INSERT INTO `withdraw_record` VALUES ('WIDffyifl25','AGTwylxfz73',1,'ozqMbvwv6XTlyc8n5Ju963mQDYXw',1,0,0,'2016-06-19 15:57:12'),('WIDixxoxf58','AGTrlorvl21',95,'ozqMbv01oBIlNd4g7YA94oh6n4Ys',5,0,1,'2016-07-04 17:14:25'),('WIDwefvlf0','AGTylfflz66',100,'ozqMbv3Q_cVoUmsxKsnutvDmHmfE',20,0,0,'2016-07-04 16:29:37'),('WIDwlxffy17','AGTrewlfr7',100,'ozqMbv2Mlx3VmPQMIFNJaNIlPOgk',50,0,0,'2016-07-04 16:29:44'),('WIDzleeli49','AGTrlorvl21',100,'ozqMbv01oBIlNd4g7YA94oh6n4Ys',5,0,0,'2016-07-04 16:29:47');
/*!40000 ALTER TABLE `withdraw_record` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-07-06 13:22:29
