CREATE TABLE IF NOT EXISTS `building` (
  `account` varchar(10) NOT NULL,
  `meter_id` varchar(8) NOT NULL,
  `phase` varchar(1) NOT NULL,
  `description` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`account`),
  KEY `meter_phase` (`meter_id`,`phase`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP INDEX meter_phase ON building;
ALTER TABLE building ADD INDEX meter_phase(`meter_id`,`phase`);

DROP TABLE IF EXISTS `event_timestamp`;

CREATE TABLE IF NOT EXISTS `event_timestamp`(
    id int(11) NOT NULL,
    time_stamp bigint(20) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO `event_timestamp` (`id`,`time_stamp`) values(1,1572192000000);

DROP TABLE IF EXISTS  `event`;

CREATE TABLE IF NOT EXISTS `event`(
    account varchar(10) NOT NULL,
    alarm_type int(11) NOT NULL,
    device_status int(11) NOT NULL,
    device_type int(11) NOT NULL,
    power int(11) NOT NULL,
    time_stamp bigint(20) NOT NULL,
    create_time DATETIME  NOT NULL DEFAULT NOW()
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS  `device_auth`;

CREATE TABLE IF NOT EXISTS `device_auth`(
    account varchar(10) NOT NULL,
    device_auth_status int(11) NOT NULL,
    device_type int(11) NOT NULL,
    create_time DATETIME  NOT NULL DEFAULT NOW(),
    update_time DATETIME  NOT NULL DEFAULT NOW(),
    UNIQUE KEY `account_device` (`account`,`device_type`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS  `device_auth_record`;

CREATE TABLE IF NOT EXISTS `device_auth_record`(
    account varchar(10) NOT NULL,
    device_auth_status int(11) NOT NULL,
    device_type int(11) NOT NULL,
    auth_time bigint(11) NOT NULL,
    create_time DATETIME  NOT NULL DEFAULT NOW(),
    INDEX `account_device` (`account`,`device_type`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS users(
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    UNIQUE KEY ix_auth_username (username, authority)
);

INSERT IGNORE INTO users (username, password, enabled)
VALUES ('admin', '$2a$10$GVubHGULgsIo5gnvccfiXedWMweU.KszZ9I0JOpNzu85wpz7wAY4e', 1);
