ALTER TABLE `selling`.`customer_order`
ADD INDEX `agent_id_idx` (`agent_id` ASC);
ALTER TABLE `selling`.`customer_order`
ADD CONSTRAINT `agent_id`
FOREIGN KEY (`agent_id`)
REFERENCES `selling`.`agent` (`agent_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE TABLE `selling`.`back_operation_log` (
  `log_id`          VARCHAR(20) NOT NULL,
  `admin_info`      VARCHAR(45) NOT NULL,
  `operation_event` VARCHAR(45) NOT NULL,
  `block_flag`      TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`     DATETIME    NOT NULL,
  PRIMARY KEY (`log_id`)
);

ALTER TABLE `selling`.`express`
CHANGE COLUMN `blockFlag` `block_flag` TINYINT(1) NOT NULL DEFAULT '0', RENAME TO `selling`.`express_agent`;

DROP TABLE IF EXISTS `selling`.`express_customer`;

CREATE TABLE IF NOT EXISTS `selling`.`express_customer` (
  `express_id`        VARCHAR(20) NOT NULL,
  `customer_order_id` VARCHAR(20) NULL,
  `express_no`        VARCHAR(20) NULL,
  `sender_name`       VARCHAR(45) NOT NULL,
  `sender_phone`      VARCHAR(45) NOT NULL,
  `sender_address`    VARCHAR(50) NOT NULL,
  `receiver_name`     VARCHAR(45) NOT NULL,
  `receiver_phone`    VARCHAR(45) NOT NULL,
  `receiver_address`  VARCHAR(50) NOT NULL,
  `goods_name`        VARCHAR(45) NOT NULL,
  `block_flag`        TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`       DATETIME    NOT NULL,
  PRIMARY KEY (`express_id`),
  INDEX `fk_express_customer_customer_order1_idx` (`customer_order_id` ASC),
  CONSTRAINT `fk_express_customer_customer_order1`
  FOREIGN KEY (`customer_order_id`)
  REFERENCES `selling`.`customer_order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

DROP TABLE IF EXISTS `selling`.`agent_gift`;

CREATE TABLE IF NOT EXISTS `selling`.`agent_gift` (
  `agent_gift_id`    VARCHAR(20) NOT NULL,
  `agent_id`         VARCHAR(20) NOT NULL,
  `goods_id`         VARCHAR(20) NOT NULL,
  `available_amount` INT         NOT NULL DEFAULT 0,
  `block_flag`       TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`      DATETIME    NOT NULL,
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
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

ALTER TABLE `selling`.`order`
ADD COLUMN `order_type` INT NOT NULL DEFAULT 0
AFTER `order_price`;


##2016年7月6日更新
ALTER TABLE `selling`.`role`
ADD COLUMN `role_description` VARCHAR(45) NOT NULL;

INSERT INTO `selling`.`role` (`role_id`, `role_name`, `role_description`, `block_flag`, `create_time`)
VALUES ('ROL00000003', 'auditor', '代理审核员', '0', '2016-05-03 11:00:20');
INSERT INTO `selling`.`role` (`role_id`, `role_name`, `role_description`, `block_flag`, `create_time`)
VALUES ('ROL00000004', 'express', '发货员', '0', '2016-05-03 11:00:30');
INSERT INTO `selling`.`role` (`role_id`, `role_name`, `role_description`, `block_flag`, `create_time`)
VALUES ('ROL00000005', 'finance', '财务人员', '0', '2016-05-03 11:00:30');

UPDATE role
SET role_description = '管理员'
WHERE role_name = 'admin';

UPDATE role
SET role_description = '代理商'
WHERE role_name = 'agent';


##2016年7月13日更新
ALTER TABLE `selling`.`back_operation_log`
ADD COLUMN `ip` VARCHAR(45) NULL
AFTER `operation_event`;


##2016年7月14日更新
ALTER TABLE `selling`.`refund_config`
ADD COLUMN `month_config` INT NOT NULL DEFAULT 1
AFTER `refund_trigger_amount`;

##2016年7月20日更新
ALTER TABLE `back_operation_log` CHANGE COLUMN `operation_event` `operation_event` VARCHAR(255);



