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
DROP TABLE IF EXISTS `selling`.`agent` ;

CREATE TABLE IF NOT EXISTS `selling`.`agent` (
  `agent_id` VARCHAR(20) NOT NULL,
  `upper_agent_id` VARCHAR(20) NULL DEFAULT NULL,
  `agent_coffer` DOUBLE NOT NULL DEFAULT '0',
  `agent_refund` DOUBLE NOT NULL DEFAULT '0',
  `agent_name` VARCHAR(45) NOT NULL,
  `agent_gender` VARCHAR(10) NOT NULL,
  `agent_phone` VARCHAR(45) NOT NULL,
  `agent_address` VARCHAR(100) NOT NULL,
  `agent_password` VARCHAR(50) NOT NULL,
  `agent_wechat` VARCHAR(45) NULL DEFAULT NULL,
  `scale` INT(11) NOT NULL DEFAULT '0',
  `agent_level` INT(11) NULL DEFAULT NULL,
  `agent_granted` TINYINT(1) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`agent_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`bank_card`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`bank_card` ;

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
-- Table `selling`.`credit_info`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`credit_info` ;

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
DROP TABLE IF EXISTS `selling`.`customer` ;

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
DROP TABLE IF EXISTS `selling`.`customer_address` ;

CREATE TABLE IF NOT EXISTS `selling`.`customer_address` (
  `customer_address_id` VARCHAR(20) NOT NULL,
  `customer_id` VARCHAR(20) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
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
-- Table `selling`.`customer_order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`customer_order` ;

CREATE TABLE IF NOT EXISTS `selling`.`customer_order` (
  `order_id` VARCHAR(20) NOT NULL,
  `goods_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NULL DEFAULT NULL,
  `wechat` VARCHAR(45) NULL DEFAULT NULL,
  `quantity` INT(11) NOT NULL DEFAULT '0',
  `receiver_name` VARCHAR(45) NOT NULL,
  `receiver_addr` VARCHAR(100) NOT NULL,
  `total_price` DOUBLE NOT NULL DEFAULT '0',
  `order_status` INT(11) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`order_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`customer_phone`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`customer_phone` ;

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
DROP TABLE IF EXISTS `selling`.`deposit_bill` ;

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
DROP TABLE IF EXISTS `selling`.`distribute_code` ;

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
-- Table `selling`.`goods`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`goods` ;

CREATE TABLE IF NOT EXISTS `selling`.`goods` (
  `goods_id` VARCHAR(20) NOT NULL,
  `goods_name` VARCHAR(45) NOT NULL,
  `primary_price` DOUBLE NOT NULL,
  `agent_price` DOUBLE NOT NULL,
  `goods_description` VARCHAR(45) NULL DEFAULT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`goods_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`order` ;

CREATE TABLE IF NOT EXISTS `selling`.`order` (
  `order_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NOT NULL,
  `order_status` INT(11) NOT NULL DEFAULT '0',
  `order_price` DOUBLE NOT NULL DEFAULT '0',
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
DROP TABLE IF EXISTS `selling`.`order_item` ;

CREATE TABLE IF NOT EXISTS `selling`.`order_item` (
  `order_item_id` VARCHAR(20) NOT NULL,
  `order_id` VARCHAR(20) NOT NULL,
  `goods_id` VARCHAR(20) NOT NULL,
  `order_item_status` INT(11) NULL DEFAULT NULL,
  `receive_address` VARCHAR(100) NOT NULL,
  `goods_quantity` INT(11) NOT NULL DEFAULT '1',
  `order_item_price` DOUBLE NOT NULL,
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
DROP TABLE IF EXISTS `selling`.`express` ;

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
-- Table `selling`.`follower`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`follower` ;

CREATE TABLE IF NOT EXISTS `selling`.`follower` (
  `follower_wechat_id` VARCHAR(30) NOT NULL,
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
-- Table `selling`.`manager`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`manager` ;

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
DROP TABLE IF EXISTS `selling`.`order_bill` ;

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
DROP TABLE IF EXISTS `selling`.`refund_config` ;

CREATE TABLE IF NOT EXISTS `selling`.`refund_config` (
  `refund_config_id` VARCHAR(20) NOT NULL,
  `goods_id` VARCHAR(20) NULL DEFAULT NULL,
  `refund_trigger_amount` INT(11) NOT NULL,
  `level_1_percent` DOUBLE NULL DEFAULT '0',
  `level_2_percent` DOUBLE NULL DEFAULT '0',
  `level_3_percent` DOUBLE NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
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
DROP TABLE IF EXISTS `selling`.`order_pool` ;

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
-- Table `selling`.`refund`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`refund` ;

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
DROP TABLE IF EXISTS `selling`.`refund_record` ;

CREATE TABLE IF NOT EXISTS `selling`.`refund_record` (
  `refund_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NOT NULL,
  `order_pool_id` VARCHAR(45) NULL DEFAULT NULL,
  `refund_name` VARCHAR(45) NOT NULL,
  `refund_description` VARCHAR(100) NOT NULL,
  `refund_percent` DOUBLE NOT NULL,
  `refund_amount` DOUBLE NOT NULL,
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
DROP TABLE IF EXISTS `selling`.`role` ;

CREATE TABLE IF NOT EXISTS `selling`.`role` (
  `role_id` VARCHAR(20) NOT NULL,
  `role_name` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`role_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`ship_config`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`ship_config` ;

CREATE TABLE IF NOT EXISTS `selling`.`ship_config` (
  `ship_config_id` VARCHAR(20) NOT NULL,
  `ship_config_date` INT(11) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`ship_config_id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `selling`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`user` ;

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
DROP TABLE IF EXISTS `selling`.`withdraw_record` ;

CREATE TABLE IF NOT EXISTS `selling`.`withdraw_record` (
  `withdraw_id` VARCHAR(20) NOT NULL,
  `agent_id` VARCHAR(20) NULL DEFAULT NULL,
  `current_coffer` DOUBLE NOT NULL DEFAULT '0',
  `bank_no` VARCHAR(25) NOT NULL,
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
-- Table `selling`.`customer_order_bill`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`customer_order_bill` ;

CREATE TABLE IF NOT EXISTS `selling`.`customer_order_bill` (
  `bill_id` VARCHAR(20) NOT NULL,
  `order_id` VARCHAR(20) NOT NULL,
  `channel_name` VARCHAR(45) NOT NULL,
  `client_ip` VARCHAR(45) NOT NULL,
  `bill_amount` DOUBLE NOT NULL,
  `bill_status` INT NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`bill_id`),
  INDEX `fk_customer_order_bill_customer_order1_idx` (`order_id` ASC),
  CONSTRAINT `fk_customer_order_bill_customer_order1`
  FOREIGN KEY (`order_id`)
  REFERENCES `selling`.`customer_order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `selling`.`goods_thumbnail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `selling`.`goods_thumbnail` ;

CREATE TABLE IF NOT EXISTS `selling`.`goods_thumbnail` (
  `thumbnail_id` VARCHAR(20) NOT NULL,
  `goods_id` VARCHAR(20) NOT NULL,
  `thumbnail_path` VARCHAR(100) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` VARCHAR(45) NULL,
  PRIMARY KEY (`thumbnail_id`),
  INDEX `fk_goods_thumbnail_goods1_idx` (`goods_id` ASC),
  CONSTRAINT `fk_goods_thumbnail_goods1`
  FOREIGN KEY (`goods_id`)
  REFERENCES `selling`.`goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `selling`.`agent`
-- -----------------------------------------------------
START TRANSACTION;
USE `selling`;
INSERT INTO `selling`.`agent` (`agent_id`, `upper_agent_id`, `agent_coffer`, `agent_refund`, `agent_name`, `agent_gender`, `agent_phone`, `agent_address`, `agent_password`, `agent_wechat`, `scale`, `agent_level`, `agent_granted`, `block_flag`, `create_time`) VALUES ('AGTvlorff50', NULL, 0, 0, '王旻', 'M', '18000000000', '江苏省南京市鼓楼区汉口路22号', 'e10adc3949ba59abbe56e057f20f883e', NULL, 20, 0, 1, 0, '2016-05-05 15:57:56');

COMMIT;


-- -----------------------------------------------------
-- Data for table `selling`.`manager`
-- -----------------------------------------------------
START TRANSACTION;
USE `selling`;
INSERT INTO `selling`.`manager` (`manager_id`, `manager_username`, `manager_password`) VALUES ('MNG00000001', 'admin', '21232f297a57a5a743894a0e4a801fc3');

COMMIT;


-- -----------------------------------------------------
-- Data for table `selling`.`role`
-- -----------------------------------------------------
START TRANSACTION;
USE `selling`;
INSERT INTO `selling`.`role` (`role_id`, `role_name`, `block_flag`, `create_time`) VALUES ('ROL00000001', 'admin', 0, '2016-5-3 11:00:00');
INSERT INTO `selling`.`role` (`role_id`, `role_name`, `block_flag`, `create_time`) VALUES ('ROL00000002', 'agent', 0, '2016-5-3 11:00:10');

COMMIT;


-- -----------------------------------------------------
-- Data for table `selling`.`user`
-- -----------------------------------------------------
START TRANSACTION;
USE `selling`;
INSERT INTO `selling`.`user` (`user_id`, `username`, `password`, `role_id`, `manager_id`, `agent_id`, `block_flag`, `create_time`) VALUES ('USR00000001', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'ROL00000001', 'MNG00000001', NULL, 0, '2016-05-03 11:00:20');
INSERT INTO `selling`.`user` (`user_id`, `username`, `password`, `role_id`, `manager_id`, `agent_id`, `block_flag`, `create_time`) VALUES ('USRflowfr67', '18000000000', 'e10adc3949ba59abbe56e057f20f883e', 'ROL00000002', NULL, 'AGTvlorff50', 0, '2016-05-09 16:13:10');

COMMIT;

