-- MySQL Script generated by MySQL Workbench
-- Mon Jan 17 15:39:31 2022
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema groute
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `groute` ;

-- -----------------------------------------------------
-- Schema groute
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `groute` DEFAULT CHARACTER SET utf8 ;
USE `groute` ;

-- -----------------------------------------------------
-- Table `groute`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `groute`.`user` ;

CREATE TABLE IF NOT EXISTS `groute`.`user` (
  `id` VARCHAR(100) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `nickname` VARCHAR(100) NULL,
  `phone` VARCHAR(13) NULL,
  `gender` VARCHAR(1) NULL,
  `birth` DATE NULL,
  `email` VARCHAR(100) NULL,
  `type` VARCHAR(10) NOT NULL,
  `token` VARCHAR(255) NULL,
  `img` VARCHAR(255) NULL,
  `create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `groute`.`theme`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `groute`.`theme` ;

CREATE TABLE IF NOT EXISTS `groute`.`theme` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `groute`.`area`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `groute`.`area` ;

CREATE TABLE IF NOT EXISTS `groute`.`area` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `img` VARCHAR(255) NULL,
  `lat` VARCHAR(20) NOT NULL,
  `lng` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `groute`.`place`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `groute`.`place` ;

CREATE TABLE IF NOT EXISTS `groute`.`place` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `type` VARCHAR(100) NOT NULL,
  `lat` VARCHAR(20) NOT NULL,
  `lng` VARCHAR(20) NOT NULL,
  `zipCode` VARCHAR(5) NOT NULL,
  `contact` VARCHAR(20) NULL,
  `address` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `theme_id` INT NOT NULL,
  `area_id` INT NOT NULL,
  `img` VARCHAR(255) NULL,
  `user_id` VARCHAR(100) NOT NULL COMMENT 'user_id에는 admin 또는 place를 등록한 user의 아이디\n동진님 - plan이 public이 될 때, 검토 후에  userplace가 place로 등록이 될 수 있도록(user_id -> admin)\n',
  PRIMARY KEY (`id`),
  INDEX `fk_theme_place_idx` (`theme_id` ASC) VISIBLE,
  INDEX `fk_area_place_idx` (`area_id` ASC) VISIBLE,
  INDEX `fk_user_place_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_theme_place`
    FOREIGN KEY (`theme_id`)
    REFERENCES `groute`.`theme` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_area_place`
    FOREIGN KEY (`area_id`)
    REFERENCES `groute`.`area` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_place`
    FOREIGN KEY (`user_id`)
    REFERENCES `groute`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `groute`.`userPlan`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `groute`.`userPlan` ;

CREATE TABLE IF NOT EXISTS `groute`.`userPlan` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(100) NOT NULL,
  `title` VARCHAR(100) NULL,
  `description` VARCHAR(255) NULL,
  `startDate` DATE NOT NULL,
  `endDate` DATE NOT NULL,
  `totalDate` VARCHAR(20) NOT NULL,
  `isPublic` VARCHAR(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_userPlan_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_userPlan`
    FOREIGN KEY (`user_id`)
    REFERENCES `groute`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `groute`.`planShareUser`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `groute`.`planShareUser` ;

CREATE TABLE IF NOT EXISTS `groute`.`planShareUser` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(100) NOT NULL,
  `plan_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_planShareUser_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_userPlan_planShareUser_idx` (`plan_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_planShareUser`
    FOREIGN KEY (`user_id`)
    REFERENCES `groute`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_userPlan_planShareUser`
    FOREIGN KEY (`plan_id`)
    REFERENCES `groute`.`userPlan` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `groute`.`route`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `groute`.`route` ;

CREATE TABLE IF NOT EXISTS `groute`.`route` (
  `id` INT NOT NULL,
  `name` VARCHAR(100) NULL,
  `day` INT NOT NULL COMMENT '일자별 경로\nday1, day2...',
  `memo` VARCHAR(255) NULL,
  `isCustom` VARCHAR(1) NOT NULL COMMENT '사용자가 커스텀한 route인지',
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `groute`.`routes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `groute`.`routes` ;

CREATE TABLE IF NOT EXISTS `groute`.`routes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `route_id` INT NOT NULL,
  `plan_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_idx` (`plan_id` ASC) VISIBLE,
  INDEX `fk_idx1` (`route_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_routes`
    FOREIGN KEY (`plan_id`)
    REFERENCES `groute`.`userPlan` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_route_routes`
    FOREIGN KEY (`route_id`)
    REFERENCES `groute`.`route` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `groute`.`account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `groute`.`account` ;

CREATE TABLE IF NOT EXISTS `groute`.`account` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `spentMoney` INT NOT NULL,
  `description` VARCHAR(100) NULL COMMENT '결제 내역',
  `category` VARCHAR(10) NULL COMMENT '숙소/항공/교통/관광/식비/쇼핑/기타',
  `routes_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_account_routes1_idx` (`routes_id` ASC) VISIBLE,
  CONSTRAINT `fk_account_routes1`
    FOREIGN KEY (`routes_id`)
    REFERENCES `groute`.`routes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `groute`.`routeDetail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `groute`.`routeDetail` ;

CREATE TABLE IF NOT EXISTS `groute`.`routeDetail` (
  `r_id` INT NOT NULL,
  `id` INT NOT NULL AUTO_INCREMENT,
  `place_id` INT NOT NULL,
  `priority` INT NOT NULL COMMENT '우선순위에 따라 장소 정렬',
  `memo` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_route_routeDetail_idx` (`r_id` ASC) VISIBLE,
  INDEX `fk_place_routeDetail_idx` (`place_id` ASC) VISIBLE,
  CONSTRAINT `fk_route_routeDetail`
    FOREIGN KEY (`r_id`)
    REFERENCES `groute`.`route` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_place_routeDetail`
    FOREIGN KEY (`place_id`)
    REFERENCES `groute`.`place` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
