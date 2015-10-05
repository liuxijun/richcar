DROP TABLE IF EXISTS repair;
CREATE TABLE repair(
        id int PRIMARY KEY AUTO_INCREMENT,
        repair_id int,
        file_id VARCHAR(128),

        create_time datetime,
        modify_time DATETIME,
        car_no varchar(32),
        fault_0 VARCHAR(255),
        fault_1 VARCHAR(255),
        fault_2 VARCHAR(255),
        fault_3 VARCHAR(255),
        fault_4 VARCHAR(255),
        fault_5 VARCHAR(255),
        fault_6 VARCHAR(255),
        fault_7 VARCHAR(255),
        fault_8 VARCHAR(255),
        fault_9 VARCHAR(255),
        fault_10 VARCHAR(255),
        fault_11 VARCHAR(255),
        item_0  VARCHAR(255),
        item_1  VARCHAR(255),
        item_2  VARCHAR(255),
        item_3  VARCHAR(255),
        item_4  VARCHAR(255),
        item_5  VARCHAR(255),
        item_6  VARCHAR(255),
        item_7  VARCHAR(255),
        item_8  VARCHAR(255),
        item_9  VARCHAR(255),
        item_10  VARCHAR(255),
        item_11  VARCHAR(255),
  in_time DATETIME,
  out_time DATETIME,
  recepton VARCHAR(64),
  workers VARCHAR(64),
  qc VARCHAR(64),
  status int
);
/**
   ³µÁ¾Åä¼þ
**/
DROP TABLE IF EXISTS parts;
CREATE TABLE parts(
  id int PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(64),
  homeland VARCHAR(64),
  level VARCHAR(32),
  price float,
  price_discount int,
  man_hour float,
  man_hour_discount int,
  status int
);