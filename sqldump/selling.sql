-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema selling
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema selling
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `selling` DEFAULT CHARACTER SET utf8 ;
USE `selling` ;

-- -----------------------------------------------------
-- Table `selling`.`agent`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`agent` (
  `agent_id` VARCHAR(20) NOT NULL,
  `upper_agent_id` VARCHAR(20) NULL DEFAULT NULL,
  `agent_coffer` DOUBLE NOT NULL DEFAULT '0',
  `agent_refund` DOUBLE NOT NULL DEFAULT '0',
  `agent_name` VARCHAR(45) NOT NULL,
  `agent_gender` VARCHAR(10) NOT NULL,
  `agent_card` VARCHAR(50) NULL DEFAULT NULL,
  `agent_phone` VARCHAR(45) NOT NULL,
  `agent_address` VARCHAR(100) NOT NULL,
  `agent_password` VARCHAR(50) NOT NULL,
  `agent_wechat` VARCHAR(45) NULL DEFAULT NULL,
  `scale` INT(11) NOT NULL DEFAULT '0',
  `agent_level` INT(11) NULL DEFAULT NULL,
  `agent_granted` TINYINT(1) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  `customer_service` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`agent_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`goods`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`goods` (
  `goods_id` VARCHAR(20) NOT NULL,
  `goods_name` VARCHAR(45) NOT NULL,
  `prime_price` DOUBLE NOT NULL,
  `agent_price` DOUBLE NOT NULL,
  `standard` VARCHAR(50) NULL DEFAULT NULL,
  `goods_description` VARCHAR(45) NULL DEFAULT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`goods_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`agent_gift`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`agent_gift` (
  `agent_gift_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NOT NULL,
  `goods_id` VARCHAR(20) NOT NULL,
  `available_amount` INT(11) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  INDEX `fk_agent_gift_agent1_idx` (`agent_id` ASC),
  INDEX `fk_agent_gift_goods1_idx` (`goods_id` ASC),
  CONSTRAINT `fk_agent_gift_agent1`
  FOREIGN KEY (`agent_id`)
  REFERENCES `selling`.`agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_agent_gift_goods1`
  FOREIGN KEY (`goods_id`)
  REFERENCES `selling`.`goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`agent_kpi`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`agent_kpi` (
  `kpi_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NOT NULL,
  `agent_name` VARCHAR(45) NOT NULL,
  `customer_quantity` INT(11) NOT NULL,
  `direct_agent_quantity` INT(11) NOT NULL,
  `agent_contribution` INT(11) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`kpi_id`),
  INDEX `fk_agent_kpi_agent1_idx` (`agent_id` ASC),
  CONSTRAINT `fk_agent_kpi_agent1`
  FOREIGN KEY (`agent_id`)
  REFERENCES `selling`.`agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`agent_vitality`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`agent_vitality` (
  `agentVitality_id` VARCHAR(20) NOT NULL,
  `vitality_quantity` INT(11) NOT NULL DEFAULT '0',
  `vitality_price` VARCHAR(45) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`agentVitality_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`back_operation_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`back_operation_log` (
  `log_id` VARCHAR(20) NOT NULL,
  `admin_info` VARCHAR(45) NOT NULL,
  `operation_event` VARCHAR(255) NULL DEFAULT NULL,
  `ip` VARCHAR(45) NULL DEFAULT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`log_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`bank_card`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`bank_card` (
  `bank_card_id` VARCHAR(20) NOT NULL,
  `bank_card_no` VARCHAR(25) NOT NULL,
  `agent_id` VARCHAR(20) NOT NULL,
  `preferred` TINYINT(1) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`bank_card_id`),
  INDEX `fk_bank_card_agent1_idx` (`agent_id` ASC),
  CONSTRAINT `fk_bank_card_agent1`
  FOREIGN KEY (`agent_id`)
  REFERENCES `selling`.`agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`customer_order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`customer_order` (
  `order_id` VARCHAR(20) NOT NULL,
  `goods_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NULL DEFAULT NULL,
  `wechat` VARCHAR(45) NULL DEFAULT NULL,
  `quantity` INT(11) NOT NULL DEFAULT '0',
  `receiver_name` VARCHAR(45) NOT NULL,
  `receiver_phone` VARCHAR(20) NOT NULL,
  `receiver_addr` VARCHAR(100) NOT NULL,
  `total_price` DOUBLE NOT NULL DEFAULT '0',
  `order_status` INT(11) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`order_id`),
  INDEX `agent_id_idx` (`agent_id` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`customer_order_bill`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`customer_order_bill` (
  `bill_id` VARCHAR(20) NOT NULL,
  `order_id` VARCHAR(20) NOT NULL,
  `channel_name` VARCHAR(45) NOT NULL,
  `client_ip` VARCHAR(45) NOT NULL,
  `bill_amount` DOUBLE NOT NULL,
  `bill_status` INT(11) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`bill_id`),
  INDEX `fk_customer_order_bill_customer_order1_idx` (`order_id` ASC),
  CONSTRAINT `fk_customer_order_bill_customer_order1`
  FOREIGN KEY (`order_id`)
  REFERENCES `selling`.`customer_order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`charge`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`charge` (
  `charge_id` VARCHAR(45) NOT NULL,
  `order_no` VARCHAR(20) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`charge_id`),
  INDEX `fk_charge_customer_order_bill1_idx` (`order_no` ASC),
  CONSTRAINT `fk_charge_customer_order_bill1`
  FOREIGN KEY (`order_no`)
  REFERENCES `selling`.`customer_order_bill` (`bill_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`credit_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`credit_info` (
  `credit_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NOT NULL,
  `credit_front` VARCHAR(50) NOT NULL,
  `credit_back` VARCHAR(50) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`credit_id`),
  INDEX `fk_credit_info_agent1_idx` (`agent_id` ASC),
  CONSTRAINT `fk_credit_info_agent1`
  FOREIGN KEY (`agent_id`)
  REFERENCES `selling`.`agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`customer` (
  `customer_id` VARCHAR(20) NOT NULL,
  `customer_name` VARCHAR(45) NOT NULL,
  `agent_id` VARCHAR(20) NOT NULL,
  `transformed` TINYINT(1) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` VARCHAR(45) NOT NULL,
  `wechat` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`customer_id`),
  INDEX `fk_customer_agent1_idx` (`agent_id` ASC),
  CONSTRAINT `fk_customer_agent1`
  FOREIGN KEY (`agent_id`)
  REFERENCES `selling`.`agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`customer_address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`customer_address` (
  `customer_address_id` VARCHAR(20) NOT NULL,
  `customer_id` VARCHAR(20) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `province` VARCHAR(20) NULL DEFAULT NULL,
  `city` VARCHAR(45) NULL DEFAULT NULL,
  `district` VARCHAR(45) NULL DEFAULT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`customer_address_id`),
  INDEX `fk_customer_address_customer1_idx` (`customer_id` ASC),
  CONSTRAINT `fk_customer_address_customer1`
  FOREIGN KEY (`customer_id`)
  REFERENCES `selling`.`customer` (`customer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`customer_phone`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`customer_phone` (
  `customer_phone_id` VARCHAR(20) NOT NULL,
  `customer_id` VARCHAR(20) NOT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`customer_phone_id`),
  INDEX `fk_customer_phone_customer1_idx` (`customer_id` ASC),
  CONSTRAINT `fk_customer_phone_customer1`
  FOREIGN KEY (`customer_id`)
  REFERENCES `selling`.`customer` (`customer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`deposit_bill`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`deposit_bill` (
  `bill_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NOT NULL,
  `channel_name` VARCHAR(45) NOT NULL,
  `client_ip` VARCHAR(45) NOT NULL,
  `bill_amount` DOUBLE NOT NULL,
  `bill_status` TINYINT(2) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(4) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`bill_id`),
  INDEX `fk_deposit_bill_agent1_idx` (`agent_id` ASC),
  CONSTRAINT `fk_deposit_bill_agent1`
  FOREIGN KEY (`agent_id`)
  REFERENCES `selling`.`agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`distribute_code`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`distribute_code` (
  `distribute_code_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NOT NULL,
  `code_value` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`distribute_code_id`),
  INDEX `fk_distribute_code_agent1_idx` (`agent_id` ASC),
  CONSTRAINT `fk_distribute_code_agent1`
  FOREIGN KEY (`agent_id`)
  REFERENCES `selling`.`agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`event` (
  `event_id` VARCHAR(20) NOT NULL,
  `event_title` VARCHAR(45) NOT NULL,
  `event_nickname` VARCHAR(45) NOT NULL,
  `event_start` DATETIME NOT NULL,
  `event_end` DATETIME NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`event_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`event_application`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`event_application` (
  `application_id` VARCHAR(20) NOT NULL,
  `event_id` VARCHAR(20) NOT NULL,
  `donor_name` VARCHAR(45) NOT NULL,
  `donor_phone` VARCHAR(45) NOT NULL,
  `donor_wechat` VARCHAR(45) NULL DEFAULT NULL,
  `donee_name` VARCHAR(45) NOT NULL,
  `donee_gender` VARCHAR(45) NOT NULL,
  `donee_phone` VARCHAR(45) NOT NULL,
  `donee_address` VARCHAR(45) NOT NULL,
  `donee_age_range` VARCHAR(45) NOT NULL,
  `status` TINYINT(3) NOT NULL DEFAULT '0',
  `relation` VARCHAR(20) NOT NULL,
  `wishes` VARCHAR(150) NULL DEFAULT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`application_id`),
  INDEX `fk_event_application_event1_idx` (`event_id` ASC),
  CONSTRAINT `fk_event_application_event1`
  FOREIGN KEY (`event_id`)
  REFERENCES `selling`.`event` (`event_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`event_order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`event_order` (
  `event_order_id` VARCHAR(45) NOT NULL,
  `application_id` VARCHAR(45) NOT NULL,
  `event_id` VARCHAR(45) NOT NULL,
  `goods_id` VARCHAR(45) NOT NULL,
  `donee_name` VARCHAR(45) NOT NULL,
  `donee_phone` VARCHAR(45) NOT NULL,
  `donee_address` VARCHAR(45) NOT NULL,
  `order_status` TINYINT(3) NOT NULL,
  `quantity` TINYINT(1) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`event_order_id`),
  INDEX `fk_event_order_event_application1_idx` (`application_id` ASC),
  INDEX `fk_event_order_event1_idx` (`event_id` ASC),
  INDEX `fk_event_order_goods1_idx` (`goods_id` ASC),
  CONSTRAINT `fk_event_order_event1`
  FOREIGN KEY (`event_id`)
  REFERENCES `selling`.`event` (`event_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_order_event_application1`
  FOREIGN KEY (`application_id`)
  REFERENCES `selling`.`event_application` (`application_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_order_goods1`
  FOREIGN KEY (`goods_id`)
  REFERENCES `selling`.`goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`event_question`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`event_question` (
  `question_id` VARCHAR(20) NOT NULL,
  `event_id` VARCHAR(20) NOT NULL,
  `question_content` VARCHAR(100) NOT NULL,
  `choice_type` TINYINT(1) NOT NULL DEFAULT '0',
  `question_rank` TINYINT(3) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`question_id`),
  INDEX `fk_event_question_event1_idx` (`event_id` ASC),
  CONSTRAINT `fk_event_question_event1`
  FOREIGN KEY (`event_id`)
  REFERENCES `selling`.`event` (`event_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`order` (
  `order_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NOT NULL,
  `order_status` INT(11) NOT NULL DEFAULT '0',
  `order_price` DOUBLE NOT NULL DEFAULT '0',
  `order_type` INT(11) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`order_id`),
  INDEX `fk_order_agent1_idx` (`agent_id` ASC),
  CONSTRAINT `fk_order_agent1`
  FOREIGN KEY (`agent_id`)
  REFERENCES `selling`.`agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`order_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`order_item` (
  `order_item_id` VARCHAR(20) NOT NULL,
  `order_id` VARCHAR(20) NOT NULL,
  `goods_id` VARCHAR(20) NOT NULL,
  `order_item_status` INT(11) NULL DEFAULT NULL,
  `receive_address` VARCHAR(100) NOT NULL,
  `goods_quantity` INT(11) NOT NULL DEFAULT '1',
  `order_item_price` DOUBLE NOT NULL,
  `order_item_description` VARCHAR(100) NULL DEFAULT NULL,
  `customer_id` VARCHAR(20) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`order_item_id`),
  INDEX `fk_order_item_order_idx` (`order_id` ASC),
  INDEX `fk_order_item_goods1_idx` (`goods_id` ASC),
  INDEX `fk_order_item_customer1_idx` (`customer_id` ASC),
  CONSTRAINT `fk_order_item_customer1`
  FOREIGN KEY (`customer_id`)
  REFERENCES `selling`.`customer` (`customer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_item_goods1`
  FOREIGN KEY (`goods_id`)
  REFERENCES `selling`.`goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_item_order`
  FOREIGN KEY (`order_id`)
  REFERENCES `selling`.`order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`express`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`express` (
  `express_id` VARCHAR(20) NOT NULL,
  `order_item_id` VARCHAR(20) NULL DEFAULT NULL,
  `express_no` VARCHAR(45) NOT NULL,
  `sender_name` VARCHAR(45) NOT NULL,
  `sender_phone` VARCHAR(45) NOT NULL,
  `sender_address` VARCHAR(50) NOT NULL,
  `receiver_name` VARCHAR(45) NOT NULL,
  `receiver_phone` VARCHAR(45) NOT NULL,
  `receiver_address` VARCHAR(50) NOT NULL,
  `goods_name` VARCHAR(45) NULL DEFAULT NULL,
  `blockFlag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`express_id`),
  INDEX `fk_express_order_item1_idx` (`order_item_id` ASC),
  CONSTRAINT `fk_express_order_item1`
  FOREIGN KEY (`order_item_id`)
  REFERENCES `selling`.`order_item` (`order_item_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`express_agent`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`express_agent` (
  `express_id` VARCHAR(20) NOT NULL,
  `order_item_id` VARCHAR(20) NULL DEFAULT NULL,
  `express_no` VARCHAR(45) NOT NULL,
  `sender_name` VARCHAR(45) NOT NULL,
  `sender_phone` VARCHAR(45) NOT NULL,
  `sender_address` VARCHAR(50) NOT NULL,
  `receiver_name` VARCHAR(45) NOT NULL,
  `receiver_phone` VARCHAR(45) NOT NULL,
  `receiver_address` VARCHAR(50) NOT NULL,
  `goods_name` VARCHAR(45) NULL DEFAULT NULL,
  `description` VARCHAR(100) NULL DEFAULT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`express_id`),
  INDEX `fk_express_order_item1_idx` (`order_item_id` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`express_application`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`express_application` (
  `express_id` VARCHAR(20) NOT NULL,
  `event_order_id` VARCHAR(20) NOT NULL,
  `express_no` VARCHAR(20) NOT NULL,
  `sender_name` VARCHAR(45) NOT NULL,
  `sender_phone` VARCHAR(45) NOT NULL,
  `sender_address` VARCHAR(50) NOT NULL,
  `receiver_name` VARCHAR(45) NOT NULL,
  `receiver_phone` VARCHAR(45) NOT NULL,
  `receiver_address` VARCHAR(50) NOT NULL,
  `goods_name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(100) NULL DEFAULT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`express_id`),
  INDEX `fk_express_application_event_order1_idx` (`event_order_id` ASC),
  CONSTRAINT `fk_express_application_event_order1`
  FOREIGN KEY (`event_order_id`)
  REFERENCES `selling`.`event_order` (`event_order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`express_customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`express_customer` (
  `express_id` VARCHAR(20) NOT NULL,
  `customer_order_id` VARCHAR(20) NULL DEFAULT NULL,
  `express_no` VARCHAR(20) NULL DEFAULT NULL,
  `sender_name` VARCHAR(45) NOT NULL,
  `sender_phone` VARCHAR(45) NOT NULL,
  `sender_address` VARCHAR(50) NOT NULL,
  `receiver_name` VARCHAR(45) NOT NULL,
  `receiver_phone` VARCHAR(45) NOT NULL,
  `receiver_address` VARCHAR(50) NOT NULL,
  `goods_name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(100) NULL DEFAULT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`express_id`),
  INDEX `fk_express_customer_customer_order1_idx` (`customer_order_id` ASC),
  CONSTRAINT `fk_express_customer_customer_order1`
  FOREIGN KEY (`customer_order_id`)
  REFERENCES `selling`.`customer_order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`follower`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`follower` (
  `follower_wechat_id` VARCHAR(30) NOT NULL,
  `follower_channel` VARCHAR(45) NOT NULL DEFAULT 'fuwu',
  `follower_nickname` VARCHAR(45) NULL DEFAULT NULL,
  `follower_gender` TINYINT(1) NOT NULL DEFAULT '0',
  `follower_city` VARCHAR(45) NULL DEFAULT NULL,
  `follower_province` VARCHAR(45) NULL DEFAULT NULL,
  `block_flag` TINYINT(4) NOT NULL DEFAULT '0',
  `create_time` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`follower_wechat_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`gift_apply`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`gift_apply` (
  `gift_apply_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NOT NULL,
  `goods_id` VARCHAR(20) NOT NULL,
  `potential` INT(11) NOT NULL,
  `apply_line` INT(11) NOT NULL,
  `last_quantity` INT(11) NOT NULL DEFAULT '0',
  `total_quantity` INT(11) NOT NULL DEFAULT '0',
  `apply_status` TINYINT(8) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`gift_apply_id`),
  INDEX `fk_gift_apply_agent1_idx` (`agent_id` ASC),
  INDEX `fk_gift_apply_goods1_idx` (`goods_id` ASC),
  CONSTRAINT `fk_gift_apply_agent1`
  FOREIGN KEY (`agent_id`)
  REFERENCES `selling`.`agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_gift_apply_goods1`
  FOREIGN KEY (`goods_id`)
  REFERENCES `selling`.`goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`goods_thumbnail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`goods_thumbnail` (
  `thumbnail_id` VARCHAR(20) NOT NULL,
  `goods_id` VARCHAR(20) NULL DEFAULT NULL,
  `type` VARCHAR(50) NULL DEFAULT NULL,
  `thumbnail_path` VARCHAR(100) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`thumbnail_id`),
  INDEX `fk_goods_thumbnail_goods1_idx` (`goods_id` ASC),
  CONSTRAINT `fk_goods_thumbnail_goods1`
  FOREIGN KEY (`goods_id`)
  REFERENCES `selling`.`goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`manager`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`manager` (
  `manager_id` VARCHAR(20) NOT NULL,
  `manager_username` VARCHAR(45) NOT NULL,
  `manager_password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`manager_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`order_bill`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`order_bill` (
  `bill_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NOT NULL,
  `order_id` VARCHAR(20) NOT NULL,
  `channel_name` VARCHAR(45) NOT NULL,
  `client_ip` VARCHAR(45) NOT NULL,
  `bill_amount` DOUBLE NOT NULL,
  `bill_status` INT(11) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`bill_id`),
  INDEX `fk_order_bill_agent1_idx` (`agent_id` ASC),
  INDEX `fk_order_bill_order1` (`order_id` ASC),
  CONSTRAINT `fk_order_bill_agent1`
  FOREIGN KEY (`agent_id`)
  REFERENCES `selling`.`agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_bill_order1`
  FOREIGN KEY (`order_id`)
  REFERENCES `selling`.`order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`refund_config`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`refund_config` (
  `refund_config_id` VARCHAR(20) NOT NULL,
  `goods_id` VARCHAR(20) NULL DEFAULT NULL,
  `refund_trigger_amount` INT(11) NOT NULL,
  `month_config` INT(11) NOT NULL DEFAULT '1',
  `level_1_percent` DOUBLE NULL DEFAULT '0',
  `level_2_percent` DOUBLE NULL DEFAULT '0',
  `level_3_percent` DOUBLE NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  `universal` TINYINT(1) NOT NULL DEFAULT '1',
  `universal_month` INT(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`refund_config_id`),
  INDEX `fk_refund_config_goods1_idx` (`goods_id` ASC),
  CONSTRAINT `fk_refund_config_goods1`
  FOREIGN KEY (`goods_id`)
  REFERENCES `selling`.`goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`order_pool`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`order_pool` (
  `pool_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NOT NULL,
  `goods_id` VARCHAR(20) NOT NULL,
  `refund_config_id` VARCHAR(20) NOT NULL,
  `quantity` INT(11) NOT NULL DEFAULT '0',
  `price` DOUBLE NOT NULL DEFAULT '0',
  `refund_amount` DOUBLE NOT NULL DEFAULT '0',
  `pool_date` DATE NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`pool_id`),
  INDEX `fk_order_pool_agent1_idx` (`agent_id` ASC),
  INDEX `fk_order_pool_goods1_idx` (`goods_id` ASC),
  INDEX `fk_order_pool_refund_config1_idx` (`refund_config_id` ASC),
  CONSTRAINT `fk_order_pool_agent1`
  FOREIGN KEY (`agent_id`)
  REFERENCES `selling`.`agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_pool_goods1`
  FOREIGN KEY (`goods_id`)
  REFERENCES `selling`.`goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_pool_refund_config1`
  FOREIGN KEY (`refund_config_id`)
  REFERENCES `selling`.`refund_config` (`refund_config_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`question_answer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`question_answer` (
  `answer_id` VARCHAR(20) NOT NULL,
  `application_id` VARCHAR(20) NOT NULL,
  `question_content` VARCHAR(100) NOT NULL,
  `question_option` VARCHAR(45) NOT NULL,
  `question_rank` TINYINT(3) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`answer_id`),
  INDEX `fk_question_answer_event_application1_idx` (`application_id` ASC),
  CONSTRAINT `fk_question_answer_event_application1`
  FOREIGN KEY (`application_id`)
  REFERENCES `selling`.`event_application` (`application_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`question_option`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`question_option` (
  `option_id` VARCHAR(20) NOT NULL,
  `question_id` VARCHAR(20) NOT NULL,
  `option_value` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`option_id`),
  INDEX `fk_question_option_event_question1_idx` (`question_id` ASC),
  CONSTRAINT `fk_question_option_event_question1`
  FOREIGN KEY (`question_id`)
  REFERENCES `selling`.`event_question` (`question_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`refund`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`refund` (
  `refund_id` VARCHAR(20) NOT NULL,
  `refund_name` VARCHAR(45) NOT NULL,
  `redund_percent` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`refund_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`refund_record`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`refund_record` (
  `refund_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NOT NULL,
  `order_pool_id` VARCHAR(45) NULL DEFAULT NULL,
  `refund_name` VARCHAR(45) NOT NULL,
  `refund_description` VARCHAR(100) NOT NULL,
  `refund_percent` DOUBLE NOT NULL,
  `refund_amount` DOUBLE NOT NULL,
  `refund_level` TINYINT(2) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`refund_id`),
  INDEX `fk_refund_record_order_pool1_idx` (`order_pool_id` ASC),
  INDEX `fk_refund_record_agent1_idx` (`agent_id` ASC),
  CONSTRAINT `fk_refund_record_agent1`
  FOREIGN KEY (`agent_id`)
  REFERENCES `selling`.`agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_refund_record_order_pool1`
  FOREIGN KEY (`order_pool_id`)
  REFERENCES `selling`.`order_pool` (`pool_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`role` (
  `role_id` VARCHAR(20) NOT NULL,
  `role_name` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  `role_description` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`role_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`ship_config`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`ship_config` (
  `ship_config_id` VARCHAR(20) NOT NULL,
  `ship_config_date` INT(11) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`ship_config_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`short_url`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`short_url` (
  `url_id` VARCHAR(20) NOT NULL,
  `long_url` VARCHAR(2000) NULL DEFAULT NULL,
  `short_url` VARCHAR(255) NULL DEFAULT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`url_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`user` (
  `user_id` VARCHAR(20) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `role_id` VARCHAR(20) NOT NULL,
  `manager_id` VARCHAR(20) NULL DEFAULT NULL,
  `agent_id` VARCHAR(20) NULL DEFAULT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`user_id`),
  INDEX `fk_user_role1_idx` (`role_id` ASC),
  INDEX `fk_user_agent1_idx` (`agent_id` ASC),
  INDEX `fk_user_manager1_idx` (`manager_id` ASC),
  CONSTRAINT `fk_user_agent1`
  FOREIGN KEY (`agent_id`)
  REFERENCES `selling`.`agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_manager1`
  FOREIGN KEY (`manager_id`)
  REFERENCES `selling`.`manager` (`manager_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_role1`
  FOREIGN KEY (`role_id`)
  REFERENCES `selling`.`role` (`role_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`withdraw_record`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`withdraw_record` (
  `withdraw_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NULL DEFAULT NULL,
  `current_coffer` DOUBLE NOT NULL DEFAULT '0',
  `wechat` VARCHAR(45) NOT NULL,
  `withdraw_amount` DOUBLE NOT NULL DEFAULT '0',
  `withdraw_status` TINYINT(1) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`withdraw_id`),
  INDEX `fk_withdraw_record_agent1_idx` (`agent_id` ASC),
  CONSTRAINT `fk_withdraw_record_agent1`
  FOREIGN KEY (`agent_id`)
  REFERENCES `selling`.`agent` (`agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`contribution_factor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`contribution_factor` (
  `factor_id` VARCHAR(20) NOT NULL,
  `factor_name` VARCHAR(45) NOT NULL,
  `factor_weight` DOUBLE NOT NULL DEFAULT 0,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`factor_id`))
  ENGINE = InnoDB;

USE `selling` ;

-- -----------------------------------------------------
-- Placeholder table for view `selling`.`cashback_month_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`cashback_month_view` (`agent_id` INT, `cash_back_month` INT, `quantity` INT, `amount` INT, `level` INT);

-- -----------------------------------------------------
-- Placeholder table for view `selling`.`cashback_sum_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`cashback_sum_view` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `selling`.`last_volume_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`last_volume_view` (`agent_id` INT, `goods_id` INT, `sum(quantity)` INT);

-- -----------------------------------------------------
-- Placeholder table for view `selling`.`order_item_sum`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`order_item_sum` (`agent_id` INT, `order_type` INT, `order_id` INT, `goods_id` INT, `order_item_status` INT, `goods_quantity` INT, `order_item_price` INT, `customer_name` INT, `block_flag` INT, `create_time` INT);

-- -----------------------------------------------------
-- Placeholder table for view `selling`.`purchase_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`purchase_item` (`goods_id` INT, `goods_name` INT, `order_type` INT, `record_status` INT, `goods_quantity` INT, `record_price` INT, `create_time` INT);

-- -----------------------------------------------------
-- Placeholder table for view `selling`.`purchase_record_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`purchase_record_view` (`goods_id` INT, `goods_name` INT, `order_type` INT, `record_status` INT, `goods_quantity` INT, `record_price` INT, `create_time` INT);

-- -----------------------------------------------------
-- Placeholder table for view `selling`.`total_volume_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`total_volume_view` (`agent_id` INT, `goods_id` INT, `sum(quantity)` INT);

-- -----------------------------------------------------
-- Placeholder table for view `selling`.`volume_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`volume_item` (`agent_id` INT, `goods_id` INT, `quantity` INT, `price` INT, `create_time` INT, `order_type` INT);

-- -----------------------------------------------------
-- View `selling`.`cashback_month_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`cashback_month_view`;
USE `selling`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `selling`.`cashback_month_view` AS select `cashback_sum_view`.`agent_id` AS `agent_id`,`cashback_sum_view`.`cash_back_month` AS `cash_back_month`,`cashback_sum_view`.`quantity` AS `quantity`,`cashback_sum_view`.`amount` AS `amount`,`cashback_sum_view`.`level` AS `level` from `selling`.`cashback_sum_view` where (`cashback_sum_view`.`cash_back_month` = date_format((curdate() - interval 1 month),'%Y-%m'));

-- -----------------------------------------------------
-- View `selling`.`cashback_sum_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`cashback_sum_view`;
USE `selling`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `selling`.`cashback_sum_view` AS (select `rr`.`agent_id` AS `agent_id`,date_format(`op`.`pool_date`,'%Y-%m') AS `cash_back_month`,sum(`op`.`quantity`) AS `quantity`,sum(`rr`.`refund_amount`) AS `amount`,`rr`.`refund_level` AS `level` from (`selling`.`refund_record` `rr` left join `selling`.`order_pool` `op` on((`rr`.`order_pool_id` = `op`.`pool_id`))) group by `rr`.`agent_id`,`rr`.`refund_level`,date_format(`op`.`pool_date`,'%Y-%m'));

-- -----------------------------------------------------
-- View `selling`.`last_volume_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`last_volume_view`;
USE `selling`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `selling`.`last_volume_view` AS select `volume_item`.`agent_id` AS `agent_id`,`volume_item`.`goods_id` AS `goods_id`,sum(`volume_item`.`quantity`) AS `sum(quantity)` from `selling`.`volume_item` where (date_format(`volume_item`.`create_time`,'%Y-%m') = date_format((curdate() - interval 1 month),'%Y-%m'));

-- -----------------------------------------------------
-- View `selling`.`order_item_sum`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`order_item_sum`;
USE `selling`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `selling`.`order_item_sum` AS select `o`.`agent_id` AS `agent_id`,`o`.`order_type` AS `order_type`,`oi`.`order_item_id` AS `order_id`,`oi`.`goods_id` AS `goods_id`,`oi`.`order_item_status` AS `order_item_status`,`oi`.`goods_quantity` AS `goods_quantity`,`oi`.`order_item_price` AS `order_item_price`,`c`.`customer_name` AS `customer_name`,`oi`.`block_flag` AS `block_flag`,`oi`.`create_time` AS `create_time` from ((`selling`.`order_item` `oi` left join `selling`.`order` `o` on((`oi`.`order_id` = `o`.`order_id`))) left join `selling`.`customer` `c` on((`oi`.`customer_id` = `c`.`customer_id`))) union all select `co`.`agent_id` AS `agent_id`,2 AS `order_type`,`co`.`order_id` AS `order_id`,`co`.`goods_id` AS `goods_id`,`co`.`order_status` AS `order_item_status`,`co`.`quantity` AS `goods_quantity`,`co`.`total_price` AS `order_item_price`,`co`.`receiver_name` AS `customer_name`,`co`.`block_flag` AS `block_flag`,`co`.`create_time` AS `create_time` from `selling`.`customer_order` `co`;

-- -----------------------------------------------------
-- View `selling`.`purchase_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`purchase_item`;
USE `selling`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `selling`.`purchase_item` AS select `g`.`goods_id` AS `goods_id`,`g`.`goods_name` AS `goods_name`,`o`.`order_type` AS `order_type`,`oi`.`order_item_status` AS `record_status`,`oi`.`goods_quantity` AS `goods_quantity`,`oi`.`order_item_price` AS `record_price`,`oi`.`create_time` AS `create_time` from ((`selling`.`order_item` `oi` left join `selling`.`order` `o` on((`oi`.`order_id` = `o`.`order_id`))) left join `selling`.`goods` `g` on((`oi`.`goods_id` = `g`.`goods_id`))) where (`oi`.`order_item_status` in (1,2,3)) union all select `g`.`goods_id` AS `goods_id`,`g`.`goods_name` AS `goods_name`,0 AS `order_type`,`co`.`order_status` AS `record_status`,`co`.`quantity` AS `goods_quantity`,`co`.`total_price` AS `record_price`,`co`.`create_time` AS `create_time` from (`selling`.`customer_order` `co` left join `selling`.`goods` `g` on((`co`.`goods_id` = `g`.`goods_id`))) where (`co`.`order_status` in (1,2,3));

-- -----------------------------------------------------
-- View `selling`.`purchase_record_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`purchase_record_view`;
USE `selling`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `selling`.`purchase_record_view` AS select `purchase_item`.`goods_id` AS `goods_id`,`purchase_item`.`goods_name` AS `goods_name`,`purchase_item`.`order_type` AS `order_type`,`purchase_item`.`record_status` AS `record_status`,`purchase_item`.`goods_quantity` AS `goods_quantity`,`purchase_item`.`record_price` AS `record_price`,`purchase_item`.`create_time` AS `create_time` from `selling`.`purchase_item`;

-- -----------------------------------------------------
-- View `selling`.`total_volume_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`total_volume_view`;
USE `selling`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `selling`.`total_volume_view` AS select `volume_item`.`agent_id` AS `agent_id`,`volume_item`.`goods_id` AS `goods_id`,sum(`volume_item`.`quantity`) AS `sum(quantity)` from `selling`.`volume_item`;

-- -----------------------------------------------------
-- View `selling`.`volume_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`volume_item`;
USE `selling`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `selling`.`volume_item` AS select `o`.`agent_id` AS `agent_id`,`oi`.`goods_id` AS `goods_id`,`oi`.`goods_quantity` AS `quantity`,`oi`.`order_item_price` AS `price`,`oi`.`create_time` AS `create_time`,`o`.`order_type` AS `order_type` from (`selling`.`order_item` `oi` left join `selling`.`order` `o` on((`oi`.`order_id` = `o`.`order_id`))) where (`oi`.`order_item_status` in (1,2,3)) union all select `co`.`agent_id` AS `agent_id`,`co`.`goods_id` AS `goods_id`,`co`.`quantity` AS `quantity`,`co`.`total_price` AS `price`,`co`.`create_time` AS `create_time`,0 AS `order_type` from `selling`.`customer_order` `co` where ((`co`.`order_status` in (1,2,3)) and (`co`.`agent_id` is not null));

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
