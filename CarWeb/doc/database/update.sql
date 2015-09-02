/**
检查结果列表
 */
drop table if EXISTS conduct_item;
create table conduct_item(
  id INT PRIMARY KEY AUTO_INCREMENT,
  name varchar(64),
  parent_id int,
  unit varchar(32),
  code varchar(64),
  create_time DATETIME,
  stand_value varchar(128),
  error_range VARCHAR(128),
  stand_value_desp varchar(128) DEFAULT '标准值',
  error_range_desp varchar(128) DEFAULT '误差范围',
  current_value_desp varchar(128) DEFAULT '检测值',
  type int default 1,/*类型，1是文本，2是图片*/
  status int DEFAULT 1
);

/**
检测项目结果
 */
create table conduct_value(
  id int PRIMARY KEY AUTO_INCREMENT,
  item_id int not null,
  unit varchar(32),
  correct_value varchar(128),
  error_range VARCHAR(128),
  current_value varchar(128),
  create_time datetime,
  picture_url varchar(255),
  creator varchar(255),
  status int default 1
);

create table conduct(
    id int PRIMARY KEY AUTO_INCREMENT,
    car_id int not null,
    title varchar(255),
    create_time datetime,
    miles int default 0,/*检查时行驶里程*/
    status int default 1
);

CREATE TABLE car_friend
(
  USER_ID VARCHAR(128) PRIMARY KEY NOT NULL,
  NAME VARCHAR(256) NOT NULL,
  ORGANIZATION_ID INT NOT NULL,
  GENDER INT NOT NULL,
  PASSWORD VARCHAR(32) NOT NULL,
  TYPE_ID INT NOT NULL,
  CREATE_TIME DATETIME,
  LAST_MODIFIED DATETIME,
  LAST_LOGON DATETIME,
  LOGON_TIMES INT DEFAULT 0,
  STATUS INT,
  MAIL VARCHAR(128),
  PHONE VARCHAR(16),
  BIRTHDAY DATETIME,
  CITY VARCHAR(128),
  COMMITTEE VARCHAR(128),
  SECTION VARCHAR(128),
  UNIT VARCHAR(128)
);

alter table car add password varchar(64);
alter table car add phone VARCHAR(32);
