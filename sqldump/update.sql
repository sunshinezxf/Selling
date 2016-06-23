ALTER TABLE `selling`.`customer_order`
ADD INDEX `agent_id_idx` (`agent_id` ASC);
ALTER TABLE `selling`.`customer_order`
ADD CONSTRAINT `agent_id`
  FOREIGN KEY (`agent_id`)
  REFERENCES `selling`.`agent` (`agent_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE TABLE `selling`.`back_operation_log` (
  `log_id` VARCHAR(20) NOT NULL,
  `admin_info` VARCHAR(45) NOT NULL,
  `operation_event` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`log_id`));
