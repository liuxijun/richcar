create database richcar default charset utf8;
grant all privileges on richcar.* to 'richcar'@'localhost' identified by 'richcar123' with grant option;
use richcar;
create table car(
  id int PRIMARY KEY AUTO_INCREMENT,
  create_time datetime,
  creator int,
  user_id varchar(255),
  sn varchar(255),
  car_no varchar(255),/*牌照*/
  product varchar(255),/*品牌*/
  product_type varchar(255),/*型号*/
  product_hometown varchar(255),/*产地*/
  sales_company varchar(255),/*销售单位*/
  vin_code varchar(128),
  engine_code varchar(128),
  enine_type varchar(128),
  gearbox varchar(255),/*变速箱*/
  length_width_height varchar(128),/*长宽高*/
  car_color varchar(64),
  inner_color varchar(64),
  gas_type varchar(64),
  emission_type varchar(32),/*排放标准*/
  emission float,/*排量*/
  tyre_type varchar(64),/*轮胎规格*/
  car_type varchar(64),/*车辆用途*/
  mileage int,/*行驶里程*/
  maintain_times int, /*保养次数*/
  MOT_stime date,
  MOT_etime date,
  production_date date,
  insure_stime datetime,
  insure_etime datetime,
  insure_company varchar(128),
  car_picture_top varchar(255),
  car_picture_left varchar(255),
  car_picture_front varchar(255),
  car_picture_bottom varchar(255),
  car_picture_right varchar(255),
  car_picture_back varchar(255)
);
drop table IF EXISTS dictionary;
create TABLE dictionary(
  code VARCHAR(32) NOT NULL PRIMARY KEY ,
  name VARCHAR(32),
  desp VARCHAR(255),
  parent_code VARCHAR(32)
);