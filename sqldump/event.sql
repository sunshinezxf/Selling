-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema event
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema event
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `event`
  DEFAULT CHARACTER SET utf8;
USE `event`;

-- -----------------------------------------------------
-- Table `event`.`article`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `event`.`article` (
  `article_id`          VARCHAR(20)  NOT NULL,
  `article_title`       VARCHAR(50)  NOT NULL,
  `article_description` VARCHAR(100) NOT NULL,
  `article_pic_url`     VARCHAR(100) NOT NULL,
  `block_flag`          TINYINT(1)   NOT NULL DEFAULT 0,
  `create_time`         DATETIME     NOT NULL,
  PRIMARY KEY (`article_id`)
)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `event`.`keyword`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `event`.`keyword` (
  `word_id`      VARCHAR(20) NOT NULL,
  `word_content` VARCHAR(45) NOT NULL,
  `article_id`   VARCHAR(20) NOT NULL,
  `block_flag`   TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`word_id`)
)
  ENGINE = InnoDB;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
