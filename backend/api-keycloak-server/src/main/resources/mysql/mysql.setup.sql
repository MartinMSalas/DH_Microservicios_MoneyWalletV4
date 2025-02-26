create database micro_mms_digital_money_wallet_user_service;
use micro_mms_digital_money_wallet_user_service;
CREATE TABLE `database_user` (
  `user_id` VARCHAR(36) NOT NULL,
  `image` LONGBLOB DEFAULT NULL,
  `phone_number` VARCHAR(20) DEFAULT NULL,
  `last_login` DATETIME DEFAULT NULL,
  `failed_attempts` INT DEFAULT 0,
  `create_dt` DATETIME NOT NULL,
  `update_dt` DATETIME NOT NULL,
  `version` INT DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;