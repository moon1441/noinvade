alter table family add index meter_phase(`meter_id`,`phase`);

DROP TABLE `event_timestamp`;

CREATE TABLE IF NOT EXISTS `event_timestamp`(
    id int(11) NOT NULL,
    time_stamp bigint(20) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
INSERT INTO `event_timestamp` (`id`,`time_stamp`) values(1,1569575934);

DROP TABLE `event`;

CREATE TABLE IF NOT EXISTS `event`(
    account varchar(10) NOT NULL,
    alarm_type int(11) NOT NULL,
    device_status int(11) NOT NULL,
    device_type int(11) NOT NULL,
    power int(11) NOT NULL,
    time_stamp bigint(20) NOT NULL,
    create_time DATETIME  NOT NULL DEFAULT NOW(),
    PRIMARY KEY (`account`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE `device_auth`;

CREATE TABLE IF NOT EXISTS `device_auth`(
    account varchar(10) NOT NULL,
    device_auth_status int(11) NOT NULL,
    device_type int(11) NOT NULL,
    auth_time bigint(20) NOT NULL,
    create_time DATETIME  NOT NULL DEFAULT NOW(),
    PRIMARY KEY (`account`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;