-- MySQL dump 10.13  Distrib 5.6.27, for osx10.11 (x86_64)
--
-- Host: localhost    Database: selling
-- ------------------------------------------------------
-- Server version	5.6.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Current Database: `selling`
--

CREATE DATABASE /*!32312 IF NOT EXISTS */ `selling` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `selling`;

--
-- Table structure for table `agent`
--

DROP TABLE IF EXISTS `agent`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agent` (
  `agent_id`       VARCHAR(20)  NOT NULL,
  `upper_agent_id` VARCHAR(20)           DEFAULT NULL,
  `agent_coffer`   DOUBLE       NOT NULL DEFAULT '0',
  `agent_refund`   DOUBLE       NOT NULL DEFAULT '0',
  `agent_name`     VARCHAR(45)  NOT NULL,
  `agent_gender`   VARCHAR(10)  NOT NULL,
  `agent_phone`    VARCHAR(45)  NOT NULL,
  `agent_address`  VARCHAR(100) NOT NULL,
  `agent_password` VARCHAR(50)  NOT NULL,
  `agent_wechat`   VARCHAR(45)           DEFAULT NULL,
  `scale`          INT(11)      NOT NULL DEFAULT '0',
  `agent_level`    INT(11)               DEFAULT NULL,
  `agent_granted`  TINYINT(1)   NOT NULL DEFAULT '0',
  `block_flag`     TINYINT(1)   NOT NULL DEFAULT '0',
  `create_time`    DATETIME     NOT NULL,
  PRIMARY KEY (`agent_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `agent_gift`
--

DROP TABLE IF EXISTS `agent_gift`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agent_gift` (
  `agent_gift_id`    VARCHAR(20) NOT NULL,
  `agent_id`         VARCHAR(20) NOT NULL,
  `goods_id`         VARCHAR(20) NOT NULL,
  `available_amount` INT(11)     NOT NULL DEFAULT '0',
  `block_flag`       TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time`      DATETIME    NOT NULL,
  KEY `fk_agent_gift_agent1_idx` (`agent_id`),
  KEY `fk_agent_gift_goods1_idx` (`goods_id`),
  CONSTRAINT `fk_agent_gift_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_agent_gift_goods1` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `back_operation_log`
--

DROP TABLE IF EXISTS `back_operation_log`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `back_operation_log` (
  `log_id`          VARCHAR(20)  NOT NULL,
  `admin_info`      VARCHAR(45)  NOT NULL,
  `operation_event` VARCHAR(200) NOT NULL,
  `ip`              VARCHAR(45)           DEFAULT NULL,
  `block_flag`      TINYINT(1)   NOT NULL DEFAULT '0',
  `create_time`     DATETIME     NOT NULL,
  PRIMARY KEY (`log_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bank_card`
--

DROP TABLE IF EXISTS `bank_card`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bank_card` (
  `bank_card_id` VARCHAR(20) NOT NULL,
  `bank_card_no` VARCHAR(25) NOT NULL,
  `agent_id`     VARCHAR(20) NOT NULL,
  `preferred`    TINYINT(1)  NOT NULL DEFAULT '0',
  `block_flag`   TINYINT(1)  NOT NULL,
  `create_time`  VARCHAR(45) NOT NULL,
  PRIMARY KEY (`bank_card_id`),
  KEY `fk_bank_card_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_bank_card_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `credit_info`
--

DROP TABLE IF EXISTS `credit_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `credit_info` (
  `credit_id`    VARCHAR(20) NOT NULL,
  `agent_id`     VARCHAR(20) NOT NULL,
  `credit_front` VARCHAR(50) NOT NULL,
  `credit_back`  VARCHAR(50) NOT NULL,
  `block_flag`   TINYINT(1)  NOT NULL,
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`credit_id`),
  KEY `fk_credit_info_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_credit_info_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `customer_id`   VARCHAR(20) NOT NULL,
  `customer_name` VARCHAR(45) NOT NULL,
  `agent_id`      VARCHAR(20) NOT NULL,
  `transformed`   TINYINT(1)  NOT NULL DEFAULT '0',
  `block_flag`    TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time`   VARCHAR(45) NOT NULL,
  `wechat`        VARCHAR(45)          DEFAULT NULL,
  PRIMARY KEY (`customer_id`),
  KEY `fk_customer_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_customer_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_address`
--

DROP TABLE IF EXISTS `customer_address`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_address` (
  `customer_address_id` VARCHAR(20)  NOT NULL,
  `customer_id`         VARCHAR(20)  NOT NULL,
  `address`             VARCHAR(100) NOT NULL,
  `block_flag`          TINYINT(1)   NOT NULL DEFAULT '0',
  `create_time`         DATETIME     NOT NULL,
  PRIMARY KEY (`customer_address_id`),
  KEY `fk_customer_address_customer1_idx` (`customer_id`),
  CONSTRAINT `fk_customer_address_customer1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_order`
--

DROP TABLE IF EXISTS `customer_order`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_order` (
  `order_id`       VARCHAR(20)  NOT NULL,
  `goods_id`       VARCHAR(20)  NOT NULL,
  `agent_id`       VARCHAR(20)           DEFAULT NULL,
  `wechat`         VARCHAR(45)           DEFAULT NULL,
  `quantity`       INT(11)      NOT NULL DEFAULT '0',
  `receiver_name`  VARCHAR(45)  NOT NULL,
  `receiver_phone` VARCHAR(20)  NOT NULL,
  `receiver_addr`  VARCHAR(100) NOT NULL,
  `total_price`    DOUBLE       NOT NULL DEFAULT '0',
  `order_status`   INT(11)      NOT NULL DEFAULT '0',
  `block_flag`     TINYINT(1)   NOT NULL DEFAULT '0',
  `create_time`    DATETIME     NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `agent_id_idx` (`agent_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_order_bill`
--

DROP TABLE IF EXISTS `customer_order_bill`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_order_bill` (
  `bill_id`      VARCHAR(20) NOT NULL,
  `order_id`     VARCHAR(20) NOT NULL,
  `channel_name` VARCHAR(45) NOT NULL,
  `client_ip`    VARCHAR(45) NOT NULL,
  `bill_amount`  DOUBLE      NOT NULL,
  `bill_status`  INT(11)     NOT NULL,
  `block_flag`   TINYINT(1)  NOT NULL,
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`bill_id`),
  KEY `fk_customer_order_bill_customer_order1_idx` (`order_id`),
  CONSTRAINT `fk_customer_order_bill_customer_order1` FOREIGN KEY (`order_id`) REFERENCES `customer_order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_phone`
--

DROP TABLE IF EXISTS `customer_phone`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_phone` (
  `customer_phone_id` VARCHAR(20) NOT NULL,
  `customer_id`       VARCHAR(20) NOT NULL,
  `phone`             VARCHAR(45) NOT NULL,
  `block_flag`        TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time`       DATETIME    NOT NULL,
  PRIMARY KEY (`customer_phone_id`),
  KEY `fk_customer_phone_customer1_idx` (`customer_id`),
  CONSTRAINT `fk_customer_phone_customer1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `deposit_bill`
--

DROP TABLE IF EXISTS `deposit_bill`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deposit_bill` (
  `bill_id`      VARCHAR(20) NOT NULL,
  `agent_id`     VARCHAR(20) NOT NULL,
  `channel_name` VARCHAR(45) NOT NULL,
  `client_ip`    VARCHAR(45) NOT NULL,
  `bill_amount`  DOUBLE      NOT NULL,
  `bill_status`  TINYINT(2)  NOT NULL DEFAULT '0',
  `block_flag`   TINYINT(4)  NOT NULL DEFAULT '0',
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`bill_id`),
  KEY `fk_deposit_bill_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_deposit_bill_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `distribute_code`
--

DROP TABLE IF EXISTS `distribute_code`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `distribute_code` (
  `distribute_code_id` VARCHAR(20) NOT NULL,
  `agent_id`           VARCHAR(20) NOT NULL,
  `code_value`         VARCHAR(45) NOT NULL,
  `block_flag`         TINYINT(1)  NOT NULL,
  `create_time`        DATETIME    NOT NULL,
  PRIMARY KEY (`distribute_code_id`),
  KEY `fk_distribute_code_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_distribute_code_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `express`
--

DROP TABLE IF EXISTS `express`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `express` (
  `express_id`       VARCHAR(20) NOT NULL,
  `order_item_id`    VARCHAR(20)          DEFAULT NULL,
  `express_no`       VARCHAR(45) NOT NULL,
  `sender_name`      VARCHAR(45) NOT NULL,
  `sender_phone`     VARCHAR(45) NOT NULL,
  `sender_address`   VARCHAR(50) NOT NULL,
  `receiver_name`    VARCHAR(45) NOT NULL,
  `receiver_phone`   VARCHAR(45) NOT NULL,
  `receiver_address` VARCHAR(50) NOT NULL,
  `goods_name`       VARCHAR(45)          DEFAULT NULL,
  `blockFlag`        TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time`      DATETIME             DEFAULT NULL,
  PRIMARY KEY (`express_id`),
  KEY `fk_express_order_item1_idx` (`order_item_id`),
  CONSTRAINT `fk_express_order_item1` FOREIGN KEY (`order_item_id`) REFERENCES `order_item` (`order_item_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `express_agent`
--

DROP TABLE IF EXISTS `express_agent`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `express_agent` (
  `express_id`       VARCHAR(20) NOT NULL,
  `order_item_id`    VARCHAR(20)          DEFAULT NULL,
  `express_no`       VARCHAR(45) NOT NULL,
  `sender_name`      VARCHAR(45) NOT NULL,
  `sender_phone`     VARCHAR(45) NOT NULL,
  `sender_address`   VARCHAR(50) NOT NULL,
  `receiver_name`    VARCHAR(45) NOT NULL,
  `receiver_phone`   VARCHAR(45) NOT NULL,
  `receiver_address` VARCHAR(50) NOT NULL,
  `goods_name`       VARCHAR(45)          DEFAULT NULL,
  `description`      VARCHAR(100)         DEFAULT NULL,
  `block_flag`       TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time`      DATETIME             DEFAULT NULL,
  PRIMARY KEY (`express_id`),
  KEY `fk_express_order_item1_idx` (`order_item_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `express_customer`
--

DROP TABLE IF EXISTS `express_customer`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `express_customer` (
  `express_id`        VARCHAR(20) NOT NULL,
  `customer_order_id` VARCHAR(20)          DEFAULT NULL,
  `express_no`        VARCHAR(20)          DEFAULT NULL,
  `sender_name`       VARCHAR(45) NOT NULL,
  `sender_phone`      VARCHAR(45) NOT NULL,
  `sender_address`    VARCHAR(50) NOT NULL,
  `receiver_name`     VARCHAR(45) NOT NULL,
  `receiver_phone`    VARCHAR(45) NOT NULL,
  `receiver_address`  VARCHAR(50) NOT NULL,
  `goods_name`        VARCHAR(45) NOT NULL,
  `description`       VARCHAR(100)         DEFAULT NULL,
  `block_flag`        TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time`       DATETIME    NOT NULL,
  PRIMARY KEY (`express_id`),
  KEY `fk_express_customer_customer_order1_idx` (`customer_order_id`),
  CONSTRAINT `fk_express_customer_customer_order1` FOREIGN KEY (`customer_order_id`) REFERENCES `customer_order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `follower`
--

DROP TABLE IF EXISTS `follower`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `follower` (
  `follower_wechat_id` VARCHAR(30) NOT NULL,
  `follower_nickname`  VARCHAR(45)          DEFAULT NULL,
  `follower_gender`    TINYINT(1)  NOT NULL DEFAULT '0',
  `follower_city`      VARCHAR(45)          DEFAULT NULL,
  `follower_province`  VARCHAR(45)          DEFAULT NULL,
  `block_flag`         TINYINT(4)  NOT NULL DEFAULT '0',
  `create_time`        VARCHAR(45) NOT NULL,
  PRIMARY KEY (`follower_wechat_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goods`
--

DROP TABLE IF EXISTS `goods`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goods` (
  `goods_id`          VARCHAR(20) NOT NULL,
  `goods_name`        VARCHAR(45) NOT NULL,
  `prime_price`       DOUBLE      NOT NULL,
  `agent_price`       DOUBLE      NOT NULL,
  `goods_description` VARCHAR(45)          DEFAULT NULL,
  `block_flag`        TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time`       DATETIME    NOT NULL,
  PRIMARY KEY (`goods_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goods_thumbnail`
--

DROP TABLE IF EXISTS `goods_thumbnail`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goods_thumbnail` (
  `thumbnail_id`   VARCHAR(20)  NOT NULL,
  `goods_id`       VARCHAR(20)           DEFAULT NULL,
  `thumbnail_path` VARCHAR(100) NOT NULL,
  `block_flag`     TINYINT(1)   NOT NULL DEFAULT '0',
  `create_time`    VARCHAR(45)  NOT NULL,
  PRIMARY KEY (`thumbnail_id`),
  KEY `fk_goods_thumbnail_goods1_idx` (`goods_id`),
  CONSTRAINT `fk_goods_thumbnail_goods1` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `manager`
--

DROP TABLE IF EXISTS `manager`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manager` (
  `manager_id`       VARCHAR(20) NOT NULL,
  `manager_username` VARCHAR(45) NOT NULL,
  `manager_password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`manager_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order` (
  `order_id`     VARCHAR(20) NOT NULL,
  `agent_id`     VARCHAR(20) NOT NULL,
  `order_status` INT(11)     NOT NULL DEFAULT '0',
  `order_price`  DOUBLE      NOT NULL DEFAULT '0',
  `order_type`   INT(11)     NOT NULL DEFAULT '0',
  `block_flag`   TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `fk_order_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_order_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_bill`
--

DROP TABLE IF EXISTS `order_bill`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_bill` (
  `bill_id`      VARCHAR(20) NOT NULL,
  `agent_id`     VARCHAR(20) NOT NULL,
  `order_id`     VARCHAR(20) NOT NULL,
  `channel_name` VARCHAR(45) NOT NULL,
  `client_ip`    VARCHAR(45) NOT NULL,
  `bill_amount`  DOUBLE      NOT NULL,
  `bill_status`  INT(11)     NOT NULL DEFAULT '0',
  `block_flag`   TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`bill_id`),
  KEY `fk_order_bill_agent1_idx` (`agent_id`),
  KEY `fk_order_bill_order1` (`order_id`),
  CONSTRAINT `fk_order_bill_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_bill_order1` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_item` (
  `order_item_id`          VARCHAR(20)  NOT NULL,
  `order_id`               VARCHAR(20)  NOT NULL,
  `goods_id`               VARCHAR(20)  NOT NULL,
  `order_item_status`      INT(11)               DEFAULT NULL,
  `receive_address`        VARCHAR(100) NOT NULL,
  `goods_quantity`         INT(11)      NOT NULL DEFAULT '1',
  `order_item_price`       DOUBLE       NOT NULL,
  `order_item_description` VARCHAR(100)          DEFAULT NULL,
  `customer_id`            VARCHAR(20)  NOT NULL,
  `block_flag`             TINYINT(1)   NOT NULL DEFAULT '0',
  `create_time`            DATETIME     NOT NULL,
  PRIMARY KEY (`order_item_id`),
  KEY `fk_order_item_order_idx` (`order_id`),
  KEY `fk_order_item_goods1_idx` (`goods_id`),
  KEY `fk_order_item_customer1_idx` (`customer_id`),
  CONSTRAINT `fk_order_item_customer1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_item_goods1` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_pool`
--

DROP TABLE IF EXISTS `order_pool`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_pool` (
  `pool_id`          VARCHAR(20) NOT NULL,
  `agent_id`         VARCHAR(20) NOT NULL,
  `goods_id`         VARCHAR(20) NOT NULL,
  `refund_config_id` VARCHAR(20) NOT NULL,
  `quantity`         INT(11)     NOT NULL DEFAULT '0',
  `price`            DOUBLE      NOT NULL DEFAULT '0',
  `refund_amount`    DOUBLE      NOT NULL DEFAULT '0',
  `pool_date`        DATE        NOT NULL,
  `block_flag`       TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time`      DATETIME    NOT NULL,
  PRIMARY KEY (`pool_id`),
  KEY `fk_order_pool_agent1_idx` (`agent_id`),
  KEY `fk_order_pool_goods1_idx` (`goods_id`),
  KEY `fk_order_pool_refund_config1_idx` (`refund_config_id`),
  CONSTRAINT `fk_order_pool_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_pool_goods1` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_pool_refund_config1` FOREIGN KEY (`refund_config_id`) REFERENCES `refund_config` (`refund_config_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `purchase_item`
--

DROP TABLE IF EXISTS `purchase_item`;
/*!50001 DROP VIEW IF EXISTS `purchase_item`*/;
SET @saved_cs_client = @@character_set_client;
SET character_set_client = utf8;
  /*!50001 CREATE VIEW `purchase_item` AS
  SELECT
    1 AS `goods_id`,
    1 AS `goods_name`,
    1 AS `order_type`,
    1 AS `record_status`,
    1 AS `goods_quantity`,
    1 AS `record_price`,
    1 AS `create_time`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `purchase_record_view`
--

DROP TABLE IF EXISTS `purchase_record_view`;
/*!50001 DROP VIEW IF EXISTS `purchase_record_view`*/;
SET @saved_cs_client = @@character_set_client;
SET character_set_client = utf8;
  /*!50001 CREATE VIEW `purchase_record_view` AS
  SELECT
    1 AS `goods_id`,
    1 AS `goods_name`,
    1 AS `order_type`,
    1 AS `record_status`,
    1 AS `goods_quantity`,
    1 AS `record_price`,
    1 AS `create_time`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `refund`
--

DROP TABLE IF EXISTS `refund`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `refund` (
  `refund_id`      VARCHAR(20) NOT NULL,
  `refund_name`    VARCHAR(45) NOT NULL,
  `redund_percent` VARCHAR(45) NOT NULL,
  `block_flag`     TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time`    DATETIME    NOT NULL,
  PRIMARY KEY (`refund_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `refund_config`
--

DROP TABLE IF EXISTS `refund_config`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `refund_config` (
  `refund_config_id`      VARCHAR(20) NOT NULL,
  `goods_id`              VARCHAR(20)          DEFAULT NULL,
  `refund_trigger_amount` INT(11)     NOT NULL,
  `month_config`          INT(11)     NOT NULL DEFAULT '1',
  `level_1_percent`       DOUBLE               DEFAULT '0',
  `level_2_percent`       DOUBLE               DEFAULT '0',
  `level_3_percent`       DOUBLE               DEFAULT '0',
  `block_flag`            TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time`           DATETIME    NOT NULL,
  PRIMARY KEY (`refund_config_id`),
  KEY `fk_refund_config_goods1_idx` (`goods_id`),
  CONSTRAINT `fk_refund_config_goods1` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `refund_record`
--

DROP TABLE IF EXISTS `refund_record`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `refund_record` (
  `refund_id`          VARCHAR(20)  NOT NULL,
  `agent_id`           VARCHAR(20)  NOT NULL,
  `order_pool_id`      VARCHAR(45)           DEFAULT NULL,
  `refund_name`        VARCHAR(45)  NOT NULL,
  `refund_description` VARCHAR(100) NOT NULL,
  `refund_percent`     DOUBLE       NOT NULL,
  `refund_amount`      DOUBLE       NOT NULL,
  `block_flag`         TINYINT(1)   NOT NULL DEFAULT '0',
  `create_time`        DATETIME     NOT NULL,
  PRIMARY KEY (`refund_id`),
  KEY `fk_refund_record_order_pool1_idx` (`order_pool_id`),
  KEY `fk_refund_record_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_refund_record_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_refund_record_order_pool1` FOREIGN KEY (`order_pool_id`) REFERENCES `order_pool` (`pool_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `role_id`          VARCHAR(20) NOT NULL,
  `role_name`        VARCHAR(45) NOT NULL,
  `block_flag`       TINYINT(1)  NOT NULL,
  `create_time`      DATETIME    NOT NULL,
  `role_description` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`role_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ship_config`
--

DROP TABLE IF EXISTS `ship_config`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ship_config` (
  `ship_config_id`   VARCHAR(20) NOT NULL,
  `ship_config_date` INT(11)     NOT NULL,
  `block_flag`       TINYINT(1)  NOT NULL,
  `create_time`      DATETIME    NOT NULL,
  PRIMARY KEY (`ship_config_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id`     VARCHAR(20) NOT NULL,
  `username`    VARCHAR(45) NOT NULL,
  `password`    VARCHAR(45) NOT NULL,
  `role_id`     VARCHAR(20) NOT NULL,
  `manager_id`  VARCHAR(20)          DEFAULT NULL,
  `agent_id`    VARCHAR(20)          DEFAULT NULL,
  `block_flag`  TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `fk_user_role1_idx` (`role_id`),
  KEY `fk_user_agent1_idx` (`agent_id`),
  KEY `fk_user_manager1_idx` (`manager_id`),
  CONSTRAINT `fk_user_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_manager1` FOREIGN KEY (`manager_id`) REFERENCES `manager` (`manager_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_role1` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `withdraw_record`
--

DROP TABLE IF EXISTS `withdraw_record`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `withdraw_record` (
  `withdraw_id`     VARCHAR(20) NOT NULL,
  `agent_id`        VARCHAR(20)          DEFAULT NULL,
  `current_coffer`  DOUBLE      NOT NULL DEFAULT '0',
  `wechat`          VARCHAR(45) NOT NULL,
  `withdraw_amount` DOUBLE      NOT NULL DEFAULT '0',
  `withdraw_status` TINYINT(1)  NOT NULL DEFAULT '0',
  `block_flag`      TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time`     DATETIME    NOT NULL,
  PRIMARY KEY (`withdraw_id`),
  KEY `fk_withdraw_record_agent1_idx` (`agent_id`),
  CONSTRAINT `fk_withdraw_record_agent1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO selling.manager (manager_id, manager_username, manager_password)
VALUES ('MNG00000001', 'admin', '21232f297a57a5a743894a0e4a801fc3');

INSERT INTO selling.role (role_id, role_name, block_flag, create_time, role_description) VALUES
  ("ROL00000001", "admin", 0, "2016-05-03 11:00:00", "\u7ba1\u7406\u5458"),
  ("ROL00000002", "agent", 0, "2016-05-03 11:00:10", "\u4ee3\u7406\u5546"),
  ("ROL00000003", "auditor", 0, "2016-05-03 11:00:20", "\u4ee3\u7406\u5ba1\u6838\u5458"),
  ("ROL00000004", "express", 0, "2016-05-03 11:00:30", "\u53d1\u8d27\u5458"),
  ("ROL00000005", "finance", 0, "2016-05-03 11:00:30", "\u8d22\u52a1\u4eba\u5458");


INSERT INTO selling.user (user_id, username, password, role_id, manager_id, agent_id, block_flag, create_time) VALUES
  ('USR00000001', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'ROL00000001', 'MNG00000001', NULL, '0',
   '2016-05-03 11:00:20');

--
-- Current Database: `selling`
--

USE `selling`;

--
-- Final view structure for view `purchase_item`
--

/*!50001 DROP VIEW IF EXISTS `purchase_item`*/;
/*!50001 SET @saved_cs_client = @@character_set_client */;
/*!50001 SET @saved_cs_results = @@character_set_results */;
/*!50001 SET @saved_col_connection = @@collation_connection */;
/*!50001 SET character_set_client = utf8 */;
/*!50001 SET character_set_results = utf8 */;
/*!50001 SET collation_connection = utf8_general_ci */;
  /*!50001 CREATE ALGORITHM = UNDEFINED */
  /*!50013 DEFINER =`root`@`localhost`
  SQL SECURITY DEFINER */
  /*!50001 VIEW `purchase_item` AS SELECT
                                     `g`.`goods_id`           AS `goods_id`,
                                     `g`.`goods_name`         AS `goods_name`,
                                     `o`.`order_type`         AS `order_type`,
                                     `oi`.`order_item_status` AS `record_status`,
                                     `oi`.`goods_quantity`    AS `goods_quantity`,
                                     `oi`.`order_item_price`  AS `record_price`,
                                     `oi`.`create_time`       AS `create_time`
                                   FROM ((`order_item` `oi` LEFT JOIN `order` `o`
                                       ON ((`oi`.`order_id` = `o`.`order_id`))) LEFT JOIN `goods` `g`
                                       ON ((`oi`.`goods_id` = `g`.`goods_id`)))
                                   WHERE (`oi`.`order_item_status` >= 1)
                                   UNION ALL SELECT
                                               `g`.`goods_id`      AS `goods_id`,
                                               `g`.`goods_name`    AS `goods_name`,
                                               0                   AS `order_type`,
                                               `co`.`order_status` AS `record_status`,
                                               `co`.`quantity`     AS `goods_quantity`,
                                               `co`.`total_price`  AS `record_price`,
                                               `co`.`create_time`  AS `create_time`
                                             FROM (`customer_order` `co` LEFT JOIN `goods` `g`
                                                 ON ((`co`.`goods_id` = `g`.`goods_id`)))
                                             WHERE (`co`.`order_status` >= 1) */;
/*!50001 SET character_set_client = @saved_cs_client */;
/*!50001 SET character_set_results = @saved_cs_results */;
/*!50001 SET collation_connection = @saved_col_connection */;

--
-- Final view structure for view `purchase_record_view`
--

/*!50001 DROP VIEW IF EXISTS `purchase_record_view`*/;
/*!50001 SET @saved_cs_client = @@character_set_client */;
/*!50001 SET @saved_cs_results = @@character_set_results */;
/*!50001 SET @saved_col_connection = @@collation_connection */;
/*!50001 SET character_set_client = utf8 */;
/*!50001 SET character_set_results = utf8 */;
/*!50001 SET collation_connection = utf8_general_ci */;
  /*!50001 CREATE ALGORITHM = UNDEFINED */
  /*!50013 DEFINER =`root`@`localhost`
  SQL SECURITY DEFINER */
  /*!50001 VIEW `purchase_record_view` AS
  SELECT
    `purchase_item`.`goods_id`       AS `goods_id`,
    `purchase_item`.`goods_name`     AS `goods_name`,
    `purchase_item`.`order_type`     AS `order_type`,
    `purchase_item`.`record_status`  AS `record_status`,
    `purchase_item`.`goods_quantity` AS `goods_quantity`,
    `purchase_item`.`record_price`   AS `record_price`,
    `purchase_item`.`create_time`    AS `create_time`
  FROM `purchase_item` */;
/*!50001 SET character_set_client = @saved_cs_client */;
/*!50001 SET character_set_results = @saved_cs_results */;
/*!50001 SET collation_connection = @saved_col_connection */;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2016-08-03 13:16:32