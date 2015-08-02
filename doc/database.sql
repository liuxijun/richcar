create database richcar default charset utf8;
grant all privileges on richcar.* to 'richcar'@'localhost' identified by 'richcar123' with grant option;
use richcar;
create table car(
  id int PRIMARY KEY AUTO_INCREMENT,
  create_time datetime,
  creator int,
  user_id varchar(255),
  sn varchar(255),
  car_no varchar(255),/*����*/
  product varchar(255),/*Ʒ��*/
  product_type varchar(255),/*�ͺ�*/
  product_hometown varchar(255),/*����*/
  sales_company varchar(255),/*���۵�λ*/
  vin_code varchar(128),
  engine_code varchar(128),
  enine_type varchar(128),
  gearbox varchar(255),/*������*/
  length_width_height varchar(128),/*�����*/
  car_color varchar(64),
  inner_color varchar(64),
  gas_type varchar(64),
  emission_type varchar(32),/*�ŷű�׼*/
  emission float,/*����*/
  tyre_type varchar(64),/*��̥���*/
  car_type varchar(64),/*������;*/
  mileage int,/*��ʻ���*/
  maintain_times int, /*��������*/
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