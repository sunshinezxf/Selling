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

##2016年8月1日更新
ALTER TABLE `selling`.`order_item`
ADD COLUMN `order_item_description` VARCHAR(100) NULL
AFTER `order_item_price`;

##mysql version 5.7 use this
CREATE VIEW purchase_record_view AS
  SELECT *
  FROM (
         SELECT
           g.goods_id           AS goods_id,
           g.goods_name         AS goods_name,
           o.order_type         AS order_type,
           oi.order_item_status AS record_status,
           oi.goods_quantity    AS goods_quantity,
           oi.order_item_price  AS record_price,
           oi.create_time       AS create_time
         FROM order_item oi
           LEFT JOIN `order` o ON oi.order_id = o.order_id
           LEFT JOIN goods g ON oi.goods_id = g.goods_id
         WHERE oi.order_item_status IN (1, 2, 3)
         UNION ALL
         SELECT
           g.goods_id      AS goods_id,
           g.goods_name    AS goods_name,
           0               AS order_type,
           co.order_status AS record_status,
           co.quantity     AS goods_quantity,
           co.total_price  AS record_price,
           co.create_time  AS create_time
         FROM customer_order co
           LEFT JOIN goods g ON co.goods_id = g.goods_id
         WHERE co.order_status IN (1, 2, 3)) temp;

##mysql 5.6, 5.5 use the following
CREATE VIEW purchase_item AS
  SELECT
    g.goods_id           AS goods_id,
    g.goods_name         AS goods_name,
    o.order_type         AS order_type,
    oi.order_item_status AS record_status,
    oi.goods_quantity    AS goods_quantity,
    oi.order_item_price  AS record_price,
    oi.create_time       AS create_time
  FROM order_item oi
    LEFT JOIN `order` o ON oi.order_id = o.order_id
    LEFT JOIN goods g ON oi.goods_id = g.goods_id
  WHERE oi.order_item_status IN (1, 2, 3)
  UNION ALL
  SELECT
    g.goods_id      AS goods_id,
    g.goods_name    AS goods_name,
    0               AS order_type,
    co.order_status AS record_status,
    co.quantity     AS goods_quantity,
    co.total_price  AS record_price,
    co.create_time  AS create_time
  FROM customer_order co
    LEFT JOIN goods g ON co.goods_id = g.goods_id
  WHERE co.order_status IN (1, 2, 3);

CREATE VIEW purchase_record_view AS
  SELECT *
  FROM purchase_item;

##2016年8月3日更新
ALTER TABLE `selling`.`express_customer`
ADD COLUMN `description` VARCHAR(100) NULL
AFTER `goods_name`;

ALTER TABLE `selling`.`express_agent`
ADD COLUMN `description` VARCHAR(100) NULL
AFTER `goods_name`;

-- -----------------------------------------------------
-- Table `selling`.`charge`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`charge` (
  `charge_id`   VARCHAR(45) NOT NULL,
  `order_no`    VARCHAR(20) NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`charge_id`),
  INDEX `fk_charge_customer_order_bill1_idx` (`order_no` ASC),
  CONSTRAINT `fk_charge_customer_order_bill1`
  FOREIGN KEY (`order_no`)
  REFERENCES `selling`.`customer_order_bill` (`bill_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

##2016年8月5日更新
CREATE OR REPLACE VIEW volume_item(agent_id, goods_id, quantity, create_time)
AS SELECT
     o.agent_id        AS agent_id,
     oi.goods_id       AS goods_id,
     oi.goods_quantity AS quantity,
     oi.create_time    AS create_time
   FROM order_item oi LEFT JOIN `order` o ON oi.order_id = o.order_id
   WHERE oi.order_item_status IN (1, 2, 3)
   GROUP BY o.agent_id, oi.goods_id
   UNION ALL
   SELECT
     co.agent_id    AS agent_id,
     co.goods_id    AS goods_id,
     co.quantity    AS quantity,
     co.create_time AS create_time
   FROM customer_order co
   WHERE co.order_status IN (1, 2, 3)
         AND co.agent_id IS NOT NULL
   GROUP BY co.agent_id, co.goods_id;

CREATE OR REPLACE VIEW last_volume_view(agent_id, goods_id, quantity)
AS
  SELECT
    agent_id,
    goods_id,
    sum(quantity) AS quantity
  FROM volume_item
  WHERE date_format(create_time, '%Y-%m') = date_format(date_sub(curdate(), INTERVAL 1 MONTH), '%Y-%m');

CREATE OR REPLACE VIEW total_volume_view(agent_id, goods_id, quantity)
AS
  SELECT
    agent_id,
    goods_id,
    sum(quantity) AS quantity
  FROM volume_item;

##2016年8月6日更新
CREATE TABLE IF NOT EXISTS `selling`.`gift_apply` (
  `gift_apply_id` VARCHAR(20) NOT NULL,
  `agent_id`      VARCHAR(20) NOT NULL,
  `goods_id`      VARCHAR(20) NOT NULL,
  `potential`     INT         NOT NULL,
  `apply_line`    INT         NOT NULL,
  `apply_status`  TINYINT(8)  NOT NULL DEFAULT 0,
  `block_flag`    TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`   DATETIME    NOT NULL,
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
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

##2016年8月9日更新
ALTER TABLE `selling`.`gift_apply`
ADD COLUMN `last_quantity` INT NOT NULL DEFAULT 0
AFTER `apply_line`,
ADD COLUMN `total_quantity` INT NOT NULL DEFAULT 0
AFTER `last_quantity`;

##2016年8月10日更新
ALTER TABLE `selling`.`refund_record`
ADD COLUMN `refund_level` TINYINT(2) NOT NULL DEFAULT 0
AFTER `refund_amount`;

UPDATE refund_record
SET refund_level = 1
WHERE refund_percent = 20;

UPDATE refund_record
SET refund_level = 2
WHERE refund_percent = 10;

CREATE OR REPLACE VIEW cashback_sum_view(agent_id, cash_back_month, quantity, amount, level)
AS (SELECT
      rr.agent_id,
      date_format(op.pool_date, '%Y-%m') AS cash_back_month,
      sum(op.quantity),
      sum(rr.refund_amount),
      rr.refund_level
    FROM refund_record rr LEFT JOIN order_pool op ON rr.order_pool_id = op.pool_id
    GROUP BY rr.agent_id, rr.refund_level, date_format(op.pool_date, '%Y-%m')
);


CREATE OR REPLACE VIEW cashback_month_view(agent_id, cash_back_month, quantity, amount, level)
AS
  SELECT
    agent_id,
    cash_back_month,
    quantity,
    amount,
    level
  FROM cashback_sum_view
  WHERE cash_back_month = date_format(date_sub(curdate(), INTERVAL 1 MONTH), '%Y-%m');

##2016年8月12日更新

CREATE OR REPLACE VIEW volume_item(agent_id, goods_id, quantity, price, create_time, order_type)
AS SELECT
     o.agent_id          AS agent_id,
     oi.goods_id         AS goods_id,
     oi.goods_quantity   AS quantity,
     oi.order_item_price AS price,
     oi.create_time      AS create_time,
     o.order_type        AS order_type
   FROM order_item oi LEFT JOIN `order` o ON oi.order_id = o.order_id
   WHERE oi.order_item_status IN (1, 2, 3)
   UNION ALL
   SELECT
     co.agent_id    AS agent_id,
     co.goods_id    AS goods_id,
     co.quantity    AS quantity,
     co.total_price AS price,
     co.create_time AS create_time,
     0              AS order_type
   FROM customer_order co
   WHERE co.order_status IN (1, 2, 3) AND co.agent_id IS NOT NULL;

##2016年8月24日
-- -----------------------------------------------------
-- Table `selling`.`event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`event` (
  `event_id`       VARCHAR(20) NOT NULL,
  `event_nickname` VARCHAR(45) NOT NULL,
  `event_start`    DATETIME    NOT NULL,
  `event_end`      DATETIME    NOT NULL,
  `block_flag`     TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`    DATETIME    NOT NULL,
  PRIMARY KEY (`event_id`)
)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `selling`.`event_application`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`event_application` (
  `application_id` VARCHAR(20) NOT NULL,
  `event_id`       VARCHAR(20) NOT NULL,
  `donor_name`     VARCHAR(45) NOT NULL,
  `donor_phone`    VARCHAR(45) NOT NULL,
  `donee_name`     VARCHAR(45) NOT NULL,
  `donee_gender`   VARCHAR(45) NOT NULL,
  `donee_phone`    VARCHAR(45) NOT NULL,
  `donee_address`  VARCHAR(45) NOT NULL,
  `relation`       VARCHAR(20) NOT NULL,
  `block_flag`     TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`    DATETIME    NOT NULL,
  PRIMARY KEY (`application_id`),
  INDEX `fk_event_application_event1_idx` (`event_id` ASC),
  CONSTRAINT `fk_event_application_event1`
  FOREIGN KEY (`event_id`)
  REFERENCES `selling`.`event` (`event_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `selling`.`event_question`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`event_question` (
  `question_id`      VARCHAR(20)  NOT NULL,
  `event_id`         VARCHAR(20)  NOT NULL,
  `question_content` VARCHAR(100) NOT NULL,
  `question_rank`    TINYINT(1)   NOT NULL,
  `block_flag`       TINYINT(1)   NOT NULL DEFAULT 0,
  `create_time`      DATETIME     NOT NULL,
  PRIMARY KEY (`question_id`),
  INDEX `fk_event_question_event1_idx` (`event_id` ASC),
  CONSTRAINT `fk_event_question_event1`
  FOREIGN KEY (`event_id`)
  REFERENCES `selling`.`event` (`event_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `selling`.`question_option`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`question_option` (
  `option_id`    VARCHAR(20) NOT NULL,
  `question_id`  VARCHAR(20) NOT NULL,
  `option_value` VARCHAR(45) NOT NULL,
  `block_flag`   TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`option_id`),
  INDEX `fk_question_option_event_question1_idx` (`question_id` ASC),
  CONSTRAINT `fk_question_option_event_question1`
  FOREIGN KEY (`question_id`)
  REFERENCES `selling`.`event_question` (`question_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `selling`.`question_answer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `selling`.`question_answer` (
  `answer_id`      VARCHAR(20) NOT NULL,
  `application_id` VARCHAR(20) NOT NULL,
  `option_id`      VARCHAR(20) NOT NULL,
  `block_flag`     TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`    DATETIME    NOT NULL,
  PRIMARY KEY (`answer_id`),
  INDEX `fk_question_answer_event_application1_idx` (`application_id` ASC),
  CONSTRAINT `fk_question_answer_event_application1`
  FOREIGN KEY (`application_id`)
  REFERENCES `selling`.`event_application` (`application_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

ALTER TABLE `selling`.`event`
ADD COLUMN `event_title` VARCHAR(45) NOT NULL
AFTER `event_id`;

ALTER TABLE `selling`.`question_answer`
DROP COLUMN `option_id`,
ADD COLUMN `question_content` VARCHAR(100) NOT NULL
AFTER `application_id`,
ADD COLUMN `question_option` VARCHAR(45) NOT NULL
AFTER `question_content`,
ADD COLUMN `question_rank` TINYINT(3) NOT NULL
AFTER `question_option`;

ALTER TABLE `selling`.`event_question`
CHANGE COLUMN `question_rank` `question_rank` TINYINT(3) NOT NULL;

ALTER TABLE `selling`.`event_application`
ADD COLUMN `donor_wechat` VARCHAR(45) NULL
AFTER `donor_phone`,
ADD COLUMN `wishes` VARCHAR(150) NULL
AFTER `relation`;

##2016年8月26日更新
ALTER TABLE `selling`.`event_question`
ADD COLUMN `choice_type` TINYINT(1) NOT NULL DEFAULT 0
AFTER `question_content`;

ALTER TABLE `selling`.`event_application`
ADD COLUMN `donee_age_range` VARCHAR(45) NOT NULL
AFTER `donee_address`;

CREATE TABLE IF NOT EXISTS `selling`.`event_order` (
  `event_order_id` VARCHAR(45) NOT NULL,
  `application_id` VARCHAR(45) NOT NULL,
  `event_id`       VARCHAR(45) NOT NULL,
  `goods_id`       VARCHAR(45) NOT NULL,
  `donee_name`     VARCHAR(45) NOT NULL,
  `donee_phone`    VARCHAR(45) NOT NULL,
  `donee_address`  VARCHAR(45) NOT NULL,
  `order_status`   TINYINT(3)  NOT NULL,
  `quantity`       TINYINT(1)  NOT NULL,
  `block_flag`     TINYINT(1)  NOT NULL,
  `create_time`    DATETIME    NOT NULL,
  PRIMARY KEY (`event_order_id`),
  INDEX `fk_event_order_event_application1_idx` (`application_id` ASC),
  INDEX `fk_event_order_event1_idx` (`event_id` ASC),
  INDEX `fk_event_order_goods1_idx` (`goods_id` ASC),
  CONSTRAINT `fk_event_order_event_application1`
  FOREIGN KEY (`application_id`)
  REFERENCES `selling`.`event_application` (`application_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_order_event1`
  FOREIGN KEY (`event_id`)
  REFERENCES `selling`.`event` (`event_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_order_goods1`
  FOREIGN KEY (`goods_id`)
  REFERENCES `selling`.`goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

##2016年8月29日更新
ALTER TABLE `selling`.`event_application`
ADD COLUMN `status` TINYINT(3) NOT NULL DEFAULT 0
AFTER `donee_age_range`;

##2016年8月30日更新
ALTER TABLE `selling`.`follower`
ADD COLUMN `follower_channel` VARCHAR(45) NOT NULL DEFAULT 'fuwu'
AFTER `follower_wechat_id`;

##2016年9月1日更新
CREATE TABLE IF NOT EXISTS `selling`.`express_application` (
  `express_id`       VARCHAR(20)  NOT NULL,
  `event_order_id`   VARCHAR(20)  NOT NULL,
  `express_no`       VARCHAR(20)  NOT NULL,
  `sender_name`      VARCHAR(45)  NOT NULL,
  `sender_phone`     VARCHAR(45)  NOT NULL,
  `sender_address`   VARCHAR(50)  NOT NULL,
  `receiver_name`    VARCHAR(45)  NOT NULL,
  `receiver_phone`   VARCHAR(45)  NOT NULL,
  `receiver_address` VARCHAR(50)  NOT NULL,
  `goods_name`       VARCHAR(45)  NOT NULL,
  `description`      VARCHAR(100) NULL,
  `block_flag`       TINYINT(1)   NOT NULL,
  `create_time`      DATETIME     NOT NULL,
  PRIMARY KEY (`express_id`),
  INDEX `fk_express_application_event_order1_idx` (`event_order_id` ASC),
  CONSTRAINT `fk_express_application_event_order1`
  FOREIGN KEY (`event_order_id`)
  REFERENCES `selling`.`event_order` (`event_order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;
  
##2016年9月14日更新
ALTER TABLE `selling`.`agent` 
ADD COLUMN `customer_service` TINYINT(1) NOT NULL DEFAULT '0' AFTER `create_time`;


##2016年9月13日更新
CREATE TABLE IF NOT EXISTS `selling`.`short_url` (
  `url_id`      VARCHAR(20)     NOT NULL,
  `long_url`    VARCHAR(2000)   NULL,
  `short_url`   VARCHAR(255)    NULL,
  `block_flag`  TINYINT(1)      NOT NULL,
  `create_time` DATETIME        NOT NULL,
  PRIMARY KEY (`url_id`)
)
  ENGINE = InnoDB;

##2016年9月23日更新
ALTER TABLE `selling`.`agent`
ADD COLUMN `agent_card` VARCHAR(50) DEFAULT NULL AFTER `agent_gender`;
