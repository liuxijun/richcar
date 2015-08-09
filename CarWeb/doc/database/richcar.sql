-- MySQL dump 10.13  Distrib 5.6.26, for Linux (x86_64)
--
-- Host: localhost    Database: richcar
-- ------------------------------------------------------
-- Server version	5.6.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table ad
--

DROP TABLE IF EXISTS ad;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ad (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(30) DEFAULT NULL,
  DESP varchar(200) DEFAULT NULL,
  START_TIME datetime DEFAULT NULL,
  END_TIME datetime DEFAULT NULL,
  AD_LENGTH int(16) DEFAULT NULL,
  AD_PLAY_TYPE int(16) DEFAULT NULL,
  PLAY_TIME int(16) DEFAULT NULL,
  IS_FORCE int(16) DEFAULT NULL,
  url varchar(1024) DEFAULT NULL,
  DEVICE_ID mediumtext,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ad
--

LOCK TABLES ad WRITE;
/*!40000 ALTER TABLE ad DISABLE KEYS */;
INSERT INTO ad VALUES (1,'益达口香糖','益达口香糖','2014-06-11 00:00:00','2014-07-30 23:59:59',15,1,0,0,'/ad/yida.mp4','2'),(2,'赶集网广告','','2014-07-01 00:00:00','2014-07-31 23:59:59',15,1,0,0,'/ad/ganjiwang.mp4','2'),(3,'yaha咖啡','','2014-07-01 00:00:00','2014-07-31 23:59:59',30,1,0,0,'/ad/yahaCoffee.mp4','6'),(4,'瑞士swatch','','2014-07-01 00:00:00','2014-07-31 23:59:59',15,1,0,0,'/ad/swatch.mp4','2');
/*!40000 ALTER TABLE ad ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table ad_log
--

DROP TABLE IF EXISTS ad_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ad_log (
  ID int(11) NOT NULL AUTO_INCREMENT,
  AD_ID varchar(30) DEFAULT NULL,
  AD_URL_ID varchar(200) DEFAULT NULL,
  VISIT_TIME datetime DEFAULT NULL,
  SP_ID int(11) DEFAULT NULL,
  AD_RANGE_ID int(11) DEFAULT NULL,
  CHANNEL_ID int(11) DEFAULT NULL,
  CONTENT_ID int(11) DEFAULT NULL,
  TRAIN_ID int(11) DEFAULT '0',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1005 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ad_log
--

LOCK TABLES ad_log WRITE;
/*!40000 ALTER TABLE ad_log DISABLE KEYS */;
/*!40000 ALTER TABLE ad_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table ad_range
--

DROP TABLE IF EXISTS ad_range;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ad_range (
  ID int(16) NOT NULL AUTO_INCREMENT,
  AD_ID int(16) DEFAULT NULL,
  TYPE int(16) DEFAULT NULL,
  CID int(16) DEFAULT NULL,
  POS int(11) DEFAULT '1',
  AD_NAME varchar(64) DEFAULT NULL,
  DESERT_NAME varchar(64) DEFAULT NULL,
  LINE_NAME varchar(64) DEFAULT NULL,
  LINE_ID int(11) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ad_range
--

LOCK TABLES ad_range WRITE;
/*!40000 ALTER TABLE ad_range DISABLE KEYS */;
INSERT INTO ad_range VALUES (1,1,1,15884430,1,'益达口香糖','栏目：　体育','广武高铁',2),(2,3,1,15884424,1,'yaha咖啡','栏目：　电影','沈大高铁',11),(3,1,2,21,1,'益达口香糖','媒体：大寒桃花开：杨幂的未整容时代','',0),(4,2,1,15884440,1,'赶集网广告','栏目：　风尚','',0);
/*!40000 ALTER TABLE ad_range ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table ad_url
--

DROP TABLE IF EXISTS ad_url;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ad_url (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(30) DEFAULT NULL,
  DESP varchar(200) DEFAULT NULL,
  TYPE int(16) DEFAULT NULL,
  URL varchar(200) DEFAULT NULL,
  PLAY_PERCENT int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ad_url
--

LOCK TABLES ad_url WRITE;
/*!40000 ALTER TABLE ad_url DISABLE KEYS */;
/*!40000 ALTER TABLE ad_url ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table admin
--

DROP TABLE IF EXISTS admin;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE admin (
  ID int(16) NOT NULL AUTO_INCREMENT,
  LOGIN varchar(50) NOT NULL,
  PASSWORD varchar(50) NOT NULL,
  REALNAME varchar(50) DEFAULT NULL,
  TELEPHONE varchar(20) DEFAULT NULL,
  EMAIL varchar(20) DEFAULT NULL,
  ADDRESS varchar(200) DEFAULT NULL,
  STATUS int(11) DEFAULT NULL,
  OLDPASSWORDLOG varchar(100) DEFAULT NULL,
  MODIFYDATE datetime DEFAULT NULL,
  LASTLOGINTIME datetime DEFAULT NULL,
  IS_ROOT int(11) DEFAULT NULL,
  IS_SYSTEM int(11) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table admin
--

LOCK TABLES admin WRITE;
/*!40000 ALTER TABLE admin DISABLE KEYS */;
INSERT INTO admin VALUES (1,'root','f2112ab2e0030251a91903dfa1b796db','超级用户','','','',1,'0e63264b3d762097f09c68b18706d2af|||39b553bc26fc96094b35239cb328d61a','2014-11-26 14:22:42','2015-08-08 18:09:47',1,1),(2,'admin','f2112ab2e0030251a91903dfa1b796db','管理员',NULL,NULL,NULL,1,NULL,'2015-08-08 16:59:00','2015-08-08 16:59:24',0,1);
/*!40000 ALTER TABLE admin ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table admin_channel
--

DROP TABLE IF EXISTS admin_channel;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE admin_channel (
  ID int(11) NOT NULL AUTO_INCREMENT,
  ADMIN_ID int(11) NOT NULL,
  CHANNEL_ID int(11) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1454 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table admin_channel
--

LOCK TABLES admin_channel WRITE;
/*!40000 ALTER TABLE admin_channel DISABLE KEYS */;
/*!40000 ALTER TABLE admin_channel ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table admin_csp
--

DROP TABLE IF EXISTS admin_csp;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE admin_csp (
  ID int(16) NOT NULL AUTO_INCREMENT,
  ADMIN_ID int(16) DEFAULT NULL,
  CSP_ID int(16) DEFAULT NULL,
  IS_DEFAULT_CSP int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=30575921 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table admin_csp
--

LOCK TABLES admin_csp WRITE;
/*!40000 ALTER TABLE admin_csp DISABLE KEYS */;
INSERT INTO admin_csp VALUES (30575918,3,2,1),(30575919,4,2,1),(30575920,1,2,1);
/*!40000 ALTER TABLE admin_csp ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table admin_role
--

DROP TABLE IF EXISTS admin_role;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE admin_role (
  ID int(16) NOT NULL AUTO_INCREMENT,
  ADMIN_ID int(16) DEFAULT NULL,
  CSP_ID int(16) DEFAULT NULL,
  ROLE_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table admin_role
--

LOCK TABLES admin_role WRITE;
/*!40000 ALTER TABLE admin_role DISABLE KEYS */;
INSERT INTO admin_role VALUES (1,1,2,1),(2,1,2,2),(3,1,2,3),(4,1,2,4),(6,2,1,3),(7,2,1,4);
/*!40000 ALTER TABLE admin_role ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table area
--

DROP TABLE IF EXISTS area;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE area (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(30) DEFAULT NULL,
  DESP varchar(100) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table area
--

LOCK TABLES area WRITE;
/*!40000 ALTER TABLE area DISABLE KEYS */;
INSERT INTO area VALUES (1,'外省',''),(310,'邯郸',''),(311,'石家庄',''),(312,'保定',''),(313,'张家口',''),(314,'承德',''),(315,'唐山',''),(316,'廊坊',''),(317,'沧州',''),(318,'衡水',''),(319,'邢台',''),(355,'秦皇岛',''),(1001,'银河',''),(1002,'省网','');
/*!40000 ALTER TABLE area ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table area_demand_log
--

DROP TABLE IF EXISTS area_demand_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE area_demand_log (
  ID int(11) NOT NULL AUTO_INCREMENT,
  DATE_STATICS datetime DEFAULT NULL,
  AREA_ID int(11) DEFAULT NULL,
  COUNT int(11) DEFAULT NULL,
  TYPE int(11) DEFAULT NULL,
  LENGTH int(11) DEFAULT NULL,
  CREATETIME datetime DEFAULT NULL,
  GRADE int(11) DEFAULT NULL,
  PADCOUNT int(11) DEFAULT NULL,
  PADLENGTH int(11) DEFAULT NULL,
  PHONECOUNT int(11) DEFAULT NULL,
  PHONELENGTH int(11) DEFAULT NULL,
  MOBILE_BYTES_SEND mediumtext,
  ELSE_BYTES_SEND mediumtext,
  BYTES_SEND mediumtext,
  BYTES_SEND_PAD mediumtext,
  BYTES_SEND_PHONE mediumtext,
  mobile_count mediumtext,
  else_count mediumtext,
  mobile_length mediumtext,
  else_length mediumtext,
  user_onLine_count mediumtext,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table area_demand_log
--

LOCK TABLES area_demand_log WRITE;
/*!40000 ALTER TABLE area_demand_log DISABLE KEYS */;
/*!40000 ALTER TABLE area_demand_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table area_ip_range
--

DROP TABLE IF EXISTS area_ip_range;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE area_ip_range (
  ID int(16) NOT NULL AUTO_INCREMENT,
  AREA_ID int(16) DEFAULT NULL,
  IP_RANGE_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table area_ip_range
--

LOCK TABLES area_ip_range WRITE;
/*!40000 ALTER TABLE area_ip_range DISABLE KEYS */;
/*!40000 ALTER TABLE area_ip_range ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table car
--

DROP TABLE IF EXISTS car;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE car (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_time datetime DEFAULT NULL,
  creator int(11) DEFAULT NULL,
  user_id varchar(255) DEFAULT NULL,
  sn varchar(255) DEFAULT NULL,
  car_no varchar(255) DEFAULT NULL,
  product varchar(255) DEFAULT NULL,
  product_type varchar(255) DEFAULT NULL,
  product_hometown varchar(255) DEFAULT NULL,
  sales_company varchar(255) DEFAULT NULL,
  vin_code varchar(128) DEFAULT NULL,
  engine_code varchar(128) DEFAULT NULL,
  enine_type varchar(128) DEFAULT NULL,
  gearbox varchar(255) DEFAULT NULL,
  length_width_height varchar(128) DEFAULT NULL,
  car_color varchar(64) DEFAULT NULL,
  inner_color varchar(64) DEFAULT NULL,
  gas_type varchar(64) DEFAULT NULL,
  emission_type varchar(32) DEFAULT NULL,
  emission float DEFAULT NULL,
  tyre_type varchar(64) DEFAULT NULL,
  car_type varchar(64) DEFAULT NULL,
  mileage int(11) DEFAULT NULL,
  maintain_times int(11) DEFAULT NULL,
  MOT_stime date DEFAULT NULL,
  MOT_etime date DEFAULT NULL,
  production_date date DEFAULT NULL,
  insure_stime datetime DEFAULT NULL,
  insure_etime datetime DEFAULT NULL,
  insure_company varchar(128) DEFAULT NULL,
  car_picture_top varchar(255) DEFAULT NULL,
  car_picture_left varchar(255) DEFAULT NULL,
  car_picture_front varchar(255) DEFAULT NULL,
  car_picture_bottom varchar(255) DEFAULT NULL,
  car_picture_right varchar(255) DEFAULT NULL,
  car_picture_back varchar(255) DEFAULT NULL,
  insure_type varchar(64) DEFAULT NULL,
  status int(11) DEFAULT '1',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table car
--

LOCK TABLES car WRITE;
/*!40000 ALTER TABLE car DISABLE KEYS */;
INSERT INTO car VALUES (1,'2015-08-08 16:08:08',0,'林国义','20150804085801','吉J8825','cherry','A5','productHometownCN','salesCompanySenBen','','','enineType4Lv','gearboxAuto','','carColorWhite','innerColorGray','92','cnL0',NULL,'tyreTypeThin','carTypeForHome',88888,2,NULL,NULL,NULL,NULL,NULL,'insureCompanyPingAn','/upload/2015/08/04/683805_top.jpg','/upload/car/left.jpg','/upload/car/front.jpg','/upload/car/bottom.jpg','/upload/car/right.jpg','/upload/car/back.jpg','insureTypeSZX',1),(2,'2015-08-08 16:08:08',0,'奥巴马','20150804085801','沪F8825','cherry',NULL,'productHometownCN','salesCompanyJinHong','','','enineType4Lv','gearboxAuto','','carColorBlue','innerColorFeather','01','cnL0',0.8,'tyreTypeThin','carTypeForCommerial',0,0,NULL,NULL,NULL,NULL,NULL,'insureCompanyDaDi','/upload/2015/08/04/162943_top.jpg','/upload/car/left.jpg','/upload/car/front.jpg','/upload/2015/08/04/558840_bottom.jpg','/upload/car/right.jpg','/upload/car/back.jpg','insureTypeDQX',1);
/*!40000 ALTER TABLE car ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table channel
--

DROP TABLE IF EXISTS channel;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE channel (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(200) DEFAULT NULL,
  DESCRIPTION varchar(200) DEFAULT NULL,
  TYPE int(16) DEFAULT NULL,
  CSP_ID int(16) DEFAULT NULL,
  POSTER1 varchar(200) DEFAULT NULL,
  POSTER2 varchar(200) DEFAULT NULL,
  PARENT_ID int(16) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  CODE varchar(255) DEFAULT NULL,
  GRADE int(16) DEFAULT NULL,
  AUDIT_FLAG int(11) DEFAULT '1',
  b_color varchar(64) DEFAULT '0',
  layout varchar(64) DEFAULT '1',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=474431747 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table channel
--

LOCK TABLES channel WRITE;
/*!40000 ALTER TABLE channel DISABLE KEYS */;
/*!40000 ALTER TABLE channel ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table channel_demand_log
--

DROP TABLE IF EXISTS channel_demand_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE channel_demand_log (
  ID int(11) NOT NULL AUTO_INCREMENT,
  DATE_STATICS datetime DEFAULT NULL,
  CHANNEL_ID mediumtext,
  COUNT int(11) DEFAULT NULL,
  LENGTH mediumtext,
  CREATETIME datetime DEFAULT NULL,
  TYPE int(11) DEFAULT NULL,
  BYTES_SEND mediumtext,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table channel_demand_log
--

LOCK TABLES channel_demand_log WRITE;
/*!40000 ALTER TABLE channel_demand_log DISABLE KEYS */;
/*!40000 ALTER TABLE channel_demand_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table channel_template
--

DROP TABLE IF EXISTS channel_template;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE channel_template (
  CHANNEL_ID int(16) NOT NULL AUTO_INCREMENT,
  LIST_TEMPLATE int(16) DEFAULT NULL,
  DETAIL_TEMPLATE int(16) DEFAULT NULL,
  CREATEOR varchar(256) DEFAULT NULL,
  INDEX_TEMPLATE int(16) DEFAULT NULL,
  PRIMARY KEY (CHANNEL_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table channel_template
--

LOCK TABLES channel_template WRITE;
/*!40000 ALTER TABLE channel_template DISABLE KEYS */;
/*!40000 ALTER TABLE channel_template ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table client_log
--

DROP TABLE IF EXISTS client_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE client_log (
  ID int(16) NOT NULL AUTO_INCREMENT,
  TIME datetime DEFAULT NULL,
  USER_AGENT varchar(1000) DEFAULT NULL,
  PHONE_CODE varchar(100) DEFAULT NULL,
  CLIENT_VERSION varchar(100) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  TYPE int(11) DEFAULT '1',
  AREA_ID int(16) DEFAULT '311',
  UUID varchar(128) DEFAULT NULL,
  IP varchar(32) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table client_log
--

LOCK TABLES client_log WRITE;
/*!40000 ALTER TABLE client_log DISABLE KEYS */;
/*!40000 ALTER TABLE client_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table config
--

DROP TABLE IF EXISTS config;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE config (
  name varchar(128) NOT NULL,
  value varchar(2048) DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  PRIMARY KEY (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table config
--

LOCK TABLES config WRITE;
/*!40000 ALTER TABLE config DISABLE KEYS */;
INSERT INTO config VALUES ('account.cardSelfURL','http://race.kdsj2.sx.cn/race/cardSelf.jsp','account.cardSelfURL'),('account.ICPURL','http://218.26.171.233/user/account/login.jsp','account.ICPURL'),('account.ispAuthenticationURL','http://race.kdsj2.sx.cn:8087/race/SsoLogin?icpId=12548','account.ispAuthenticationURL'),('account.ispLogoutURL','http://race.kdsj2.sx.cn:8087/race/logout','account.ispLogoutURL'),('account.ISPURL','http://www.kdsj2.sx.cn/100/index.html?icpId=12548','account.ISPURL'),('account.logoutURL','http://218.26.171.233/user/account/logout.jsp','account.logoutURL'),('account.merchantNumber','12548','account.merchantNumber'),('account.resultChecker','http://218.26.171.233/user/account/resultChecker.jsp','account.resultChecker'),('account.showResultURL','http://218.26.171.233/user/account/excuteResult.jsp?errorCode=','account.showResultURL'),('cache.timeToIdle','300','cache.timeToIdle'),('cache.timeToLive','300','cache.timeToLive'),('city.recommend.alias','sh','city.recommend.alias'),('client.refreshInterval','60','client.refreshInterval'),('default.serverUrls','file:///c:/movie;file:///e:/movie;file:///E:/QingPu/qingpu;rtsp://192.168.1.128/news;rtsp://58.246.106.18/news','default.serverUrls'),('defaultLogin','root','defaultLogin'),('defaultPwd','123456','defaultPwd'),('defaultVerifyCode','fortuneDebugTimeAutoInput','defaultVerifyCode'),('doshow.seedDir','E:\\doshow\\seeder\\seed\\','doshow.seedDir'),('encoder.useVideoMatrix','false','encoder.useVideoMatrix'),('export.snap.PosterHeightMEDIA_PIC_RECOM2','300','export.snap.PosterHeightMEDIA_PIC_RECOM2'),('export.snap.PosterHeightPC_MEDIA_POSTER_BIG','225','export.snap.PosterHeightPC_MEDIA_POSTER_BIG'),('export.snap.PosterHeightPC_MEDIA_POSTER_HORIZONTAL_BIG','480','export.snap.PosterHeightPC_MEDIA_POSTER_HORIZONTAL_BIG'),('export.snap.posterStartSecondsMEDIA_PIC_RECOM2','500','export.snap.posterStartSecondsMEDIA_PIC_RECOM2'),('export.snap.posterStartSecondsPC_MEDIA_POSTER_BIG','360','export.snap.posterStartSecondsPC_MEDIA_POSTER_BIG'),('export.snap.posterStartSecondsPC_MEDIA_POSTER_HORIZONTAL_BIG','500','export.snap.posterStartSecondsPC_MEDIA_POSTER_HORIZONTAL_BIG'),('export.snap.PosterWidthMEDIA_PIC_RECOM2','620','export.snap.PosterWidthMEDIA_PIC_RECOM2'),('export.snap.PosterWidthPC_MEDIA_POSTER_BIG','185','export.snap.PosterWidthPC_MEDIA_POSTER_BIG'),('export.snap.PosterWidthPC_MEDIA_POSTER_HORIZONTAL_BIG','640','export.snap.PosterWidthPC_MEDIA_POSTER_HORIZONTAL_BIG'),('file.copy.srcDir','E:/local/','file.copy.srcDir'),('file.copy.targetDir','E:/storage/','file.copy.targetDir'),('ftpUtils.ControlEncoding','UTF-8','ftpUtils.ControlEncoding'),('ftpUtils.PathNameEncoding','ISO-8859-1','ftpUtils.PathNameEncoding'),('hbMobile.notNeedVerifyCode','false','hbMobile.notNeedVerifyCode'),('import.baseFileDir','c:/logs/','import.baseFileDir'),('import.useSystemDir','true','import.useSystemDir'),('importor.desertConnStr','jdbc:oracle:thin:@192.168.1.190:1521:orcl','importor.desertConnStr'),('importor.desertPwd','rms_bj','importor.desertPwd'),('importor.desertUser','rms_bj','importor.desertUser'),('importor.sourceConnStr','jdbc:oracle:thin:@192.168.1.190:1521:orcl','importor.sourceConnStr'),('importor.sourcePwd','mediastack35','importor.sourcePwd'),('importor.sourceUser','mediastack35','importor.sourceUser'),('importXml.autoEncode','false','importXml.autoEncode'),('importXml.createSelectValueIfNotExists','false','importXml.createSelectValueIfNotExists'),('importXml.default.checkMediaFile','false','importXml.default.checkMediaFile'),('importXml.default.copyPoster','true','importXml.default.copyPoster'),('importXml.default.deviceId','11054546','importXml.default.deviceId'),('importXml.default.fileEncoding','UTF-8','importXml.default.fileEncoding'),('importXml.default.ignoreCopyRight','true','importXml.default.ignoreCopyRight'),('importXml.default.moduleId','1','importXml.default.moduleId'),('importXml.default.syncPoster','false','importXml.default.syncPoster'),('importXml.picPath.downloadIfNotExists','true','importXml.picPath.downloadIfNotExists'),('importXml.picPath.relateFromXmlPath','false','importXml.picPath.relateFromXmlPath'),('importXml.useSystemDir','false','importXml.useSystemDir'),('menuNeedCheckPermission','true','menuNeedCheckPermission'),('needCheckLogin','false','needCheckLogin'),('needCheckPermission','false','needCheckPermission'),('needCheckVerifyCode','true','needCheckVerifyCode'),('productIdBaiS','8018000500','productIdBaiS'),('productIdHuShu','8018000402','productIdHuShu'),('productIdHuShu10','8018600305','productIdHuShu10'),('productIdYouP','8018000200','productIdYouP'),('publish.default.simplePropertyIds','name,id,intro,poster,posterBig,date','publish.default.simplePropertyIds'),('QUERY_USER_INFO_NATIVE','false','QUERY_USER_INFO_NATIVE'),('QUERY_USER_INFO_SERVICE_ID','18000012','QUERY_USER_INFO_SERVICE_ID'),('QUERY_USER_INFO_SERVICE_URL','http://61.55.156.205:8090/webservices/services/QueryUserInfoServiceApplyHttpPort','QUERY_USER_INFO_SERVICE_URL'),('QUERY_USER_INFO_URL','http://61.55.144.86/vac/queryUser.jsp?command=ip2phone','QUERY_USER_INFO_URL'),('redex.pro.upload.path','/home/fortune/movie','redex.pro.upload.path'),('redex.stat.count','10','redex.stat.count'),('redex.top.stat.days','30','redex.top.stat.days'),('redex.transcenter.address','http://127.0.0.1/transcenter/','redex.transcenter.address'),('rms.list.pageSize','30','rms.list.pageSize'),('SGIP_CLIENT_TIMEOUT','30','SGIP_CLIENT_TIMEOUT'),('SGIP_CORP_ID','14294','SGIP_CORP_ID'),('SGIP_EXPIRED_MINUTES','0','SGIP_EXPIRED_MINUTES'),('SGIP_IP','127.0.0.1','SGIP_IP'),('SGIP_MAX_FEEVALUE','9999','SGIP_MAX_FEEVALUE'),('SGIP_MAX_LINK','2','SGIP_MAX_LINK'),('SGIP_MIN_FEEVALUE','0','SGIP_MIN_FEEVALUE'),('SGIP_NODE_ID','30','SGIP_NODE_ID'),('SGIP_PASSWORD','123456','SGIP_PASSWORD'),('SGIP_PORT','9001','SGIP_PORT'),('SGIP_QUEUE_NUM','10000','SGIP_QUEUE_NUM'),('SGIP_SEND_NATIVE','false','SGIP_SEND_NATIVE'),('SGIP_SERVER_BIND_IP','192.168.1.28','SGIP_SERVER_BIND_IP'),('SGIP_SERVER_BIND_PORT','6888','SGIP_SERVER_BIND_PORT'),('SGIP_SERVER_PWD','123456','SGIP_SERVER_PWD'),('SGIP_SERVER_TIMEOUT','30','SGIP_SERVER_TIMEOUT'),('SGIP_SERVER_USER','14294','SGIP_SERVER_USER'),('SGIP_SMGW_ENCODING','utf-8','SGIP_SMGW_ENCODING'),('SGIP_SMGW_URL','http://61.55.144.87/smgw/?resultXmlFormat=true&command=send','SGIP_SMGW_URL'),('SGIP_SP_NUMBER','10655910010','SGIP_SP_NUMBER'),('SGIP_TIME_EXT','032+','SGIP_TIME_EXT'),('SGIP_USER','14294','SGIP_USER'),('snap.Dir','/home/fortune/snap','snap.Dir'),('ssoKey.effectTime','20110526','ssoKey.effectTime'),('ssoKey.key','000000000000000000000000','ssoKey.key'),('system.buy.whiteList','15613199253;18631118565;18631110681;18631110330;18603211395;18603210858;18632921367;18631110202;18603213821;','system.buy.whiteList'),('system.compactMode','true','system.compactMode'),('system.compactMode.defaultLiveChannelId','474431603','system.compactMode.defaultLiveChannelId'),('system.content.startEncodeTaskWhenSaveContent','true','system.content.startEncodeTaskWhenSaveContent'),('system.copyRight','&copy 2014 Î«²ñ¶¯Á¦°æÈ¨ËùÓÐ','system.copyRight'),('system.default.webHost','serverIP','system.default.webHost'),('system.defaultM3U8ExtName','.m3u8','system.defaultM3U8ExtName'),('system.encode.autoDistributeWhenFinished','true','system.encode.autoDistributeWhenFinished'),('system.encode.workerMaxCount','1','system.encode.workerMaxCount'),('system.encoder.browseServerType','2','system.encoder.browseServerType'),('system.encoder.masterUrl','http://127.0.0.1/interface/encoderSession.jsp','system.encoder.masterUrl'),('system.encoder.maxWaitingSecondsWhenTaskNull','30','system.encoder.maxWaitingSecondsWhenTaskNull'),('system.encoder.skipIfExists','true','system.encoder.skipIfExists'),('system.encoder.startEncoder','true','system.encoder.startEncoder'),('system.gslb.policy','120.27.29.191->default;10.0.66.11->119.191.61.30,123.133.64.102,123.168.22.6,219.146.118.238,172.17.,10.0.','system.gslb.policy'),('system.hls.proxy.maxBufferLength','4096000','system.hls.proxy.maxBufferLength'),('system.hls.proxy.sourceServer','http://60.12.204.22/','system.hls.proxy.sourceServer'),('system.hls.proxy.useCache','true','system.hls.proxy.useCache'),('system.hls.proxy.webServerThreadCount','128','system.hls.proxy.webServerThreadCount'),('system.movie.fileInterfaceUrl','http://127.0.0.1/interface/files.jsp','system.movie.fileInterfaceUrl'),('system.movie.listRemoteMode','true','system.movie.listRemoteMode'),('system.movie.pathName','/home/fortune/movie','system.movie.pathName'),('system.playMediaUrlSource','false','system.playMediaUrlSource'),('system.scan.sqlResultDir','D:/scanResult/','system.scan.sqlResultDir'),('system.showSelectModule','true','system.showSelectModule'),('system.showSnapButton','true','system.showSnapButton'),('system.tools.cmdOfGetFileInfo','/usr/local/bin/ffprobe -v quiet -show_format -show_streams \"%sourceFile%\"','system.tools.cmdOfGetFileInfo'),('system.tools.cmdOfGetFileInfoXmlFormat','/usr/local/bin/ffprobe -v quiet -show_format -show_streams -print_format xml -i \"%sourceFile%\"','system.tools.cmdOfGetFileInfoXmlFormat'),('system.tools.encode.cmdLine','/usr/local/bin/ffmpeg -i \"%sourceFile%\" -ss %startTime% -t %duration% -vcodec %videoCodec% -profile %h264VideoProfile% -level %h264ProfileLevel% -vb %videoBandwith%k -r %fps%  -s %videoWidth%x%videoHeight% -acodec %audioCodec% -strict -2 -ab %audioBandwith%k -ac %audioChannelCount% -ar %audioSampleHz% -flags -global_header -segment_format mpegts -map 0 -map -0:d -map -0:s -f segment -segment_list \"%saveFile%.m3u8\" -segment_time %segmentDuration% -segment_list_flags +live -segment_list_entry_prefix \"%prefix%/\" -bsf h264_mp4toannexb  \"%savePath%/%prefix%/%04d.ts\"','system.tools.encode.cmdLine'),('system.tools.encode.cmdLine.extParameter','-map 0 -map -0:d -map -0:s \"%saveFile%\"','system.tools.encode.cmdLine.extParameter'),('system.tools.encode.cmdLine.m3u8ExtParameter','-flags -global_header -segment_format mpegts -map 0 -map -0:d -map -0:s -f segment -segment_list \"%saveFile%.m3u8\" -segment_time %segmentDuration% -segment_list_flags +live -segment_list_entry_prefix \"%prefix%/\" -bsf h264_mp4toannexb  \"%savePath%/%prefix%/%04d.ts\"','system.tools.encode.cmdLine.m3u8ExtParameter'),('system.tools.encode.cmdLineDOS','cmd.exe /c \"C:\\FFMpeg\\bin\\ffmpeg.exe\" -i \"%sourceFile%\" -ss %startTime% -pix_fmt yuv420p -profile:v %h264VideoProfile% -level %h264ProfileLevel% -vcodec %videoCodec% -profile %h264VideoProfile% -level %h264ProfileLevel% -vb %videoBandwith%k -r %fps%  -s %videoWidth%x%videoHeight% -acodec %audioCodec% -strict -2 -ab %audioBandwith%k -ac %audioChannelCount% -ar %audioSampleHz%','system.tools.encode.cmdLineDOS'),('system.tools.encode.defaultAACEncoder','aac','system.tools.encode.defaultAACEncoder'),('system.tools.encode.defaultH264Encoder','libx264','system.tools.encode.defaultH264Encoder'),('system.tools.encode.tempDir','/home/fortune/temp/','system.tools.encode.tempDir'),('system.tools.preview.audioBandwidth','64','system.tools.preview.audioBandwidth'),('system.tools.preview.cmdLine','/usr/local/bin/ffmpeg -ss %startSeconds% -i \"%sourceFileName%\" -copyts -vcodec libx264 -profile:v main -level 30 -vb %videoBandwidth%k -s %videoWidth%x%videoHeight% -acodec aac -strict -2 -ab %audioBandwidth%k -ac 2 -ar 48000 -flags -global_header -segment_format mpegts -map 0 -map -0:d -map -0:s -f segment -segment_list \"%m3u8FileName%\" -segment_time %segmentDuration% -segment_list_flags +live -segment_list_entry_prefix \"%prefix%\" -bsf h264_mp4toannexb \"%tsPathName%\"','system.tools.preview.cmdLine'),('system.tools.preview.targetDuration','5','system.tools.preview.targetDuration'),('system.tools.preview.videoBandwidth','300','system.tools.preview.videoBandwidth'),('system.tools.preview.videoHeight','240','system.tools.preview.videoHeight'),('system.tools.preview.videoWidth','320','system.tools.preview.videoWidth'),('system.tools.snap.cmdLine','/usr/local/bin/ffmpeg -ss %startTime% -i \"%sourceFile%\" -f image2 \"%resultFile%\"','system.tools.snap.cmdLine'),('system.virtualPath.path0','in','system.virtualPath.path0'),('system.virtualPath.path0To','E:/movie','system.virtualPath.path0To'),('system.virtualPath.path1','sport','system.virtualPath.path1'),('system.virtualPath.path1To','E:/pl','system.virtualPath.path1To'),('system.virtualPath.root','E:/movie','system.virtualPath.root'),('system.visitLog.path','E:/logs/10.0.66.11/','system.visitLog.path'),('system.visitLog.serverUrl','http://localhost:8080/interface/saveLog.jsp?logFormat=apache','system.visitLog.serverUrl'),('system.welcomeMessage','»¶Ó­¹âÁÙÎ«²ñ¶¯Á¦ÍøÂçµçÊÓÌ¨¹ÜÀíÏµÍ³','system.welcomeMessage'),('system.xmlBaseDir','/baseXmlDir/','system.xmlBaseDir'),('systme.visitLog.encoding','GBK','systme.visitLog.encoding'),('userCached','false','userCached'),('vac.default.destinationDeviceID','001801','vac.default.destinationDeviceID'),('vac.default.destinationDeviceType','0','vac.default.destinationDeviceType'),('vac.default.pwd','123456','vac.default.pwd'),('vac.default.sourceDeviceID','421802','vac.default.sourceDeviceID'),('vac.default.sourceDeviceType','42','vac.default.sourceDeviceType'),('vac.hostIp','127.0.0.1','vac.hostIp'),('vac.hostPort','9999','vac.hostPort'),('vac.native.call','false','vac.native.call'),('vac.needCheckPrice','true','vac.needCheckPrice'),('vac.visit.url','http://61.55.144.86/vac/?command=checkPriceInterface','vac.visit.url');
/*!40000 ALTER TABLE config ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table content
--

DROP TABLE IF EXISTS content;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE content (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(100) DEFAULT NULL,
  ACTORS varchar(255) DEFAULT NULL,
  DIRECTORS varchar(127) DEFAULT NULL,
  CREATOR_ADMIN_ID int(16) DEFAULT NULL,
  CREATE_TIME datetime DEFAULT NULL,
  CSP_ID int(16) DEFAULT NULL,
  MODULE_ID int(16) DEFAULT NULL,
  DEVICE_ID int(16) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  STATUS_TIME datetime DEFAULT NULL,
  CONTENT_AUDIT_ID int(16) DEFAULT NULL,
  DIGI_RIGHT_URL varchar(200) DEFAULT NULL,
  VALID_START_TIME datetime DEFAULT NULL,
  VALID_END_TIME datetime DEFAULT NULL,
  ALL_VISIT_COUNT int(16) DEFAULT NULL,
  MONTH_VISIT_COUNT int(16) DEFAULT NULL,
  WEEK_VISIT_COUNT int(16) DEFAULT NULL,
  INTRO varchar(4000) DEFAULT NULL,
  POST1_URL varchar(200) DEFAULT NULL,
  POST2_URL varchar(200) DEFAULT NULL,
  PROPERTY1 int(16) DEFAULT NULL,
  PROPERTY2 int(16) DEFAULT NULL,
  PROPERTY3 varchar(100) DEFAULT NULL,
  PROPERTY4 varchar(100) DEFAULT NULL,
  PROPERTY5 varchar(100) DEFAULT NULL,
  PROPERTY6 varchar(100) DEFAULT NULL,
  PROPERTY7 varchar(1000) DEFAULT NULL,
  PROPERTY8 varchar(1000) DEFAULT NULL,
  SCORE float DEFAULT NULL,
  SCORE_COUNT int(16) DEFAULT NULL,
  CONTENT_ID varchar(100) DEFAULT NULL,
  VISIT_COUNT_STATUS int(16) DEFAULT NULL,
  SCORE_STATUS int(16) DEFAULT NULL,
  IS_SPECIAL int(16) DEFAULT '0',
  user_types varchar(255) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table content
--

LOCK TABLES content WRITE;
/*!40000 ALTER TABLE content DISABLE KEYS */;
/*!40000 ALTER TABLE content ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table content_audit
--

DROP TABLE IF EXISTS content_audit;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE content_audit (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CONTENT_ID int(16) DEFAULT NULL,
  TYPE int(16) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  RESULT int(16) DEFAULT NULL,
  RESULT_MSG varchar(100) DEFAULT NULL,
  DESP varchar(200) DEFAULT NULL,
  APPLY_TIME datetime DEFAULT NULL,
  AUDIT_ADMIN_ID int(16) DEFAULT NULL,
  AUDIT_TIME datetime DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table content_audit
--

LOCK TABLES content_audit WRITE;
/*!40000 ALTER TABLE content_audit DISABLE KEYS */;
/*!40000 ALTER TABLE content_audit ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table content_channel
--

DROP TABLE IF EXISTS content_channel;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE content_channel (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CONTENT_ID int(16) DEFAULT NULL,
  CHANNEL_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table content_channel
--

LOCK TABLES content_channel WRITE;
/*!40000 ALTER TABLE content_channel DISABLE KEYS */;
/*!40000 ALTER TABLE content_channel ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table content_csp
--

DROP TABLE IF EXISTS content_csp;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE content_csp (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CONTENT_ID int(16) DEFAULT NULL,
  CSP_ID int(16) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  STATUS_TIME datetime DEFAULT NULL,
  CONTENT_AUDIT_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table content_csp
--

LOCK TABLES content_csp WRITE;
/*!40000 ALTER TABLE content_csp DISABLE KEYS */;
INSERT INTO content_csp VALUES (1,1,2,0,'2015-06-04 19:30:01',0),(2,2,2,1,'2015-06-05 08:30:00',0),(3,3,2,2,'2015-07-14 14:13:28',0),(4,4,2,1,'2015-06-05 13:18:20',0),(5,5,2,1,'2015-06-06 17:54:35',0),(6,6,2,0,'2015-06-06 17:58:03',0),(7,7,2,1,'2015-06-06 18:18:22',0),(8,8,2,0,'2015-06-06 18:20:23',0),(9,9,2,1,'2015-06-08 10:33:07',0),(10,10,2,0,'2015-06-08 10:33:07',0),(11,11,2,4,'2015-06-08 17:54:56',0),(12,12,2,1,'2015-06-08 17:26:01',0),(13,13,2,0,'2015-06-08 17:26:43',0),(14,14,2,2,'2015-07-14 14:16:13',0),(15,15,2,1,'2015-06-08 18:08:34',0),(16,16,2,0,'2015-06-08 18:08:34',0),(17,17,2,1,'2015-06-08 18:13:03',0),(18,18,2,0,'2015-06-08 18:13:03',0),(19,19,2,0,'2015-06-09 14:22:20',-1),(20,20,2,2,'2015-07-14 14:44:27',0),(21,21,2,1,'2015-06-16 12:45:39',0),(22,22,2,0,'2015-06-16 13:02:54',0),(23,23,2,2,'2015-07-14 14:17:42',0),(24,24,2,0,'2015-06-16 19:30:01',-1),(25,25,2,0,'2015-06-17 19:30:00',-1),(26,26,2,0,'2015-06-18 16:22:08',0),(27,27,2,0,'2015-06-18 19:30:00',-1),(28,28,2,0,'2015-06-19 19:30:01',-1),(29,29,2,0,'2015-06-20 19:30:00',-1),(30,30,2,0,'2015-06-21 19:30:01',-1),(31,31,2,0,'2015-06-22 19:30:00',-1),(32,32,2,0,'2015-06-23 19:30:01',-1),(33,33,2,0,'2015-06-24 19:30:01',-1),(34,34,2,0,'2015-06-25 19:30:01',-1),(35,35,2,0,'2015-06-26 10:26:01',-1),(36,36,2,1,'2015-07-06 18:04:18',-1),(37,37,2,1,'2015-06-26 11:00:00',0),(38,38,2,2,'2015-07-06 17:45:57',-1),(39,39,2,0,'2015-06-26 19:30:00',-1),(40,40,2,0,'2015-06-27 19:30:00',-1),(41,41,2,2,'2015-07-14 14:23:44',0),(42,42,2,0,'2015-07-07 09:13:52',0),(43,43,2,2,'2015-07-14 14:25:25',0),(44,44,2,2,'2015-07-14 14:27:26',0),(45,45,2,2,'2015-07-14 14:44:30',0),(46,46,2,2,'2015-07-14 13:57:25',-1),(47,47,2,2,'2015-07-14 14:46:40',0),(48,48,2,2,'2015-07-14 14:47:32',0),(49,49,2,2,'2015-07-14 13:59:56',0),(50,50,2,2,'2015-07-14 14:00:02',0),(51,51,2,2,'2015-07-14 13:58:39',0),(52,52,2,2,'2015-07-14 14:00:07',0),(53,53,2,2,'2015-07-14 13:57:20',-1),(54,54,2,2,'2015-07-14 17:37:46',-1),(55,55,2,2,'2015-07-14 18:13:16',-1),(56,56,2,2,'2015-07-14 18:13:17',-1),(57,57,2,2,'2015-07-14 18:13:18',-1),(58,58,2,2,'2015-07-15 10:43:09',-1),(59,59,2,2,'2015-07-15 14:32:31',-1),(60,60,2,1,'2015-07-15 15:58:09',0),(61,61,2,0,'2015-07-15 15:55:00',-1),(62,62,2,1,'2015-07-15 16:01:05',0),(63,63,2,2,'2015-07-23 07:11:14',0),(64,64,2,2,'2015-07-16 08:59:29',0),(65,65,2,0,'2015-07-16 09:46:08',-1),(66,66,2,0,'2015-07-16 09:48:00',-1),(67,67,2,0,'2015-07-16 11:50:00',-1),(68,68,2,0,'2015-07-16 15:55:01',-1),(69,69,2,0,'2015-07-17 11:50:00',-1),(70,70,2,0,'2015-07-17 15:55:00',-1),(71,71,2,0,'2015-07-18 11:50:00',-1),(72,72,2,0,'2015-07-18 15:55:00',-1),(73,73,2,0,'2015-07-19 11:50:00',-1),(74,74,2,0,'2015-07-19 15:55:00',-1),(75,75,2,0,'2015-07-20 11:50:01',-1),(76,76,2,0,'2015-07-20 15:55:00',-1),(77,77,2,0,'2015-07-21 11:50:01',-1),(78,78,2,0,'2015-07-21 15:55:00',-1),(79,79,2,2,'2015-07-21 16:42:39',0),(80,80,2,0,'2015-07-21 16:41:04',-1),(81,81,2,0,'2015-07-21 16:46:14',-1),(82,82,2,2,'2015-07-21 17:53:38',0),(83,83,2,0,'2015-07-21 17:45:00',-1),(84,84,2,0,'2015-07-22 11:11:54',-1),(85,85,2,0,'2015-07-22 11:20:00',-1),(86,86,2,0,'2015-07-22 11:41:19',-1),(87,87,2,0,'2015-07-22 11:50:00',-1),(88,88,2,0,'2015-07-22 11:50:00',-1),(89,89,2,2,'2015-07-22 14:13:28',-1),(90,90,2,2,'2015-07-22 14:20:52',0),(91,91,2,0,'2015-07-22 16:25:39',0),(92,92,2,0,'2015-07-22 16:47:12',-1),(93,93,2,0,'2015-07-22 17:25:39',-1),(94,94,2,0,'2015-07-23 11:50:01',-1),(95,95,2,0,'2015-07-23 15:55:00',-1);
/*!40000 ALTER TABLE content_csp ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table content_demand_log
--

DROP TABLE IF EXISTS content_demand_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE content_demand_log (
  ID int(11) NOT NULL AUTO_INCREMENT,
  DATE_STATICS datetime DEFAULT NULL,
  CONTENT_ID mediumtext,
  COUNT mediumtext,
  LENGTH mediumtext,
  SP_ID mediumtext,
  CREATETIME datetime DEFAULT NULL,
  TYPE mediumtext,
  CHANNEL_ID mediumtext,
  PADCOUNT mediumtext,
  PADLENGTH mediumtext,
  PHONECOUNT mediumtext,
  PHONELENGTH mediumtext,
  BYTES_SEND_PHONE mediumtext,
  BYTES_SEND_PAD mediumtext,
  BYTES_SEND mediumtext,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table content_demand_log
--

LOCK TABLES content_demand_log WRITE;
/*!40000 ALTER TABLE content_demand_log DISABLE KEYS */;
/*!40000 ALTER TABLE content_demand_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table content_notice
--

DROP TABLE IF EXISTS content_notice;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE content_notice (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CONTENT varchar(100) DEFAULT NULL,
  STATUS varchar(20) DEFAULT NULL,
  CREATE_TIME datetime DEFAULT NULL,
  ONLINE_TIME datetime DEFAULT NULL,
  OFFLINE_TIME datetime DEFAULT NULL,
  CSP_ID int(16) DEFAULT NULL,
  ADMIN_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table content_notice
--

LOCK TABLES content_notice WRITE;
/*!40000 ALTER TABLE content_notice DISABLE KEYS */;
INSERT INTO content_notice VALUES (1,'\"name\":\"版本名\",\"version\":\"版本号\",\"link\":\"下载地址\",\"description\":\"描述\",\"deforceUpdate\":\"0\"','2','2014-09-01 00:00:00',NULL,NULL,NULL,1),(2,'description\":\"aboutUs\"','1','2014-09-01 00:00:00',NULL,NULL,NULL,1),(3,'\"description\":\"简介\"','1','2014-09-01 00:00:00',NULL,NULL,NULL,1),(4,'name\":\"IOS版本名\",\"version\":\"IOS版本号\",\"link\":\"下载地址\",\"description\":\"描述\",\"deforceUpdate\":\"0','1','2014-09-01 00:00:00',NULL,NULL,NULL,1);
/*!40000 ALTER TABLE content_notice ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table content_property
--

DROP TABLE IF EXISTS content_property;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE content_property (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CONTENT_ID int(16) DEFAULT NULL,
  PROPERTY_ID int(16) DEFAULT NULL,
  INT_VALUE int(16) DEFAULT NULL,
  STRING_VALUE varchar(1024) DEFAULT NULL,
  DESP varchar(1024) DEFAULT NULL,
  EXTRA_DATA varchar(4000) DEFAULT NULL,
  SUBCONTENT_ID varchar(100) DEFAULT NULL,
  NAME varchar(100) DEFAULT NULL,
  THUMB_PIC varchar(2014) DEFAULT NULL,
  LENGTH int(16) DEFAULT NULL,
  EXTRA_INT int(16) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY CP_CONTENT_ID_INDEX (CONTENT_ID),
  KEY CP_PROPERTY_ID_INDEX (PROPERTY_ID)
) ENGINE=InnoDB AUTO_INCREMENT=214 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table content_property
--

LOCK TABLES content_property WRITE;
/*!40000 ALTER TABLE content_property DISABLE KEYS */;
/*!40000 ALTER TABLE content_property ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table content_recommend
--

DROP TABLE IF EXISTS content_recommend;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE content_recommend (
  ID int(16) NOT NULL AUTO_INCREMENT,
  RECOMMEND_ID int(16) DEFAULT NULL,
  CONTENT_ID int(16) DEFAULT NULL,
  DISPLAY_ORDER int(16) DEFAULT NULL,
  CHANNEL_ID int(16) DEFAULT NULL,
  CSP_ID int(16) DEFAULT '0',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=554 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table content_recommend
--

LOCK TABLES content_recommend WRITE;
/*!40000 ALTER TABLE content_recommend DISABLE KEYS */;
INSERT INTO content_recommend VALUES (175,433979349,661,2,NULL,2),(179,433979349,688,3,NULL,2),(180,433979349,658,5,NULL,2),(181,433979349,685,4,NULL,2),(264,10,911,3,NULL,2),(422,433979348,1338,5,NULL,2),(441,433979348,1383,4,NULL,2),(451,433979345,1406,6,NULL,2),(458,10,1098,1,NULL,2),(459,10,1406,2,NULL,2),(460,433979345,1423,5,NULL,2),(461,433979345,1424,4,NULL,2),(482,433979348,1487,2,NULL,2),(483,433979348,1461,3,NULL,2),(491,433979345,1512,3,NULL,2),(496,433979345,1514,2,NULL,2),(506,10,1514,4,NULL,2),(507,10,1423,5,NULL,2),(513,433979348,1551,1,NULL,2),(517,433979349,1538,1,NULL,2),(518,433979345,1564,1,NULL,2),(550,433979346,1669,0,NULL,2),(551,433979346,1660,1,NULL,2),(552,433979346,1662,2,NULL,2),(553,433979346,1667,3,NULL,2);
/*!40000 ALTER TABLE content_recommend ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table content_related
--

DROP TABLE IF EXISTS content_related;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE content_related (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CONTENT_ID int(16) DEFAULT NULL,
  RELATED_ID int(16) DEFAULT NULL,
  RELATED_CONTENT_ID int(16) DEFAULT NULL,
  DISPLAY_ORDER int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=826373 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table content_related
--

LOCK TABLES content_related WRITE;
/*!40000 ALTER TABLE content_related DISABLE KEYS */;
/*!40000 ALTER TABLE content_related ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table content_service_product
--

DROP TABLE IF EXISTS content_service_product;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE content_service_product (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CONTENT_ID int(16) DEFAULT NULL,
  SERVICE_PRODUCT_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table content_service_product
--

LOCK TABLES content_service_product WRITE;
/*!40000 ALTER TABLE content_service_product DISABLE KEYS */;
/*!40000 ALTER TABLE content_service_product ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table content_zt_log
--

DROP TABLE IF EXISTS content_zt_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE content_zt_log (
  ID int(16) NOT NULL AUTO_INCREMENT,
  TYPE int(16) DEFAULT NULL,
  CONTENT_ID int(16) DEFAULT NULL,
  IS_SPECIAL int(16) DEFAULT NULL,
  CREATETIME datetime DEFAULT NULL,
  CSP_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table content_zt_log
--

LOCK TABLES content_zt_log WRITE;
/*!40000 ALTER TABLE content_zt_log DISABLE KEYS */;
/*!40000 ALTER TABLE content_zt_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table csp
--

DROP TABLE IF EXISTS csp;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE csp (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(100) DEFAULT NULL,
  ADDRESS varchar(100) DEFAULT NULL,
  PHONE varchar(30) DEFAULT NULL,
  EMAIL varchar(60) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  IS_CP int(16) DEFAULT NULL,
  IS_SP int(16) DEFAULT NULL,
  IS_CP_ONLINE_AUDIT int(16) DEFAULT NULL,
  IS_CP_OFFLINE_AUDIT int(16) DEFAULT NULL,
  IS_SP_ONLINE_AUDIT int(16) DEFAULT NULL,
  IS_SP_OFFLINE_AUDIT int(16) DEFAULT NULL,
  ALIAS varchar(255) DEFAULT NULL,
  SP_ID varchar(255) DEFAULT NULL,
  TYPE int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table csp
--

LOCK TABLES csp WRITE;
/*!40000 ALTER TABLE csp DISABLE KEYS */;
INSERT INTO csp VALUES (2,'潍柴总部','','','',1,1,1,0,0,0,0,'WP','2014',1);
/*!40000 ALTER TABLE csp ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table csp_auditor
--

DROP TABLE IF EXISTS csp_auditor;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE csp_auditor (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CSP_ID int(16) DEFAULT NULL,
  ADMIN_ID int(16) DEFAULT NULL,
  CP_ONLINE int(16) DEFAULT NULL,
  CP_OFFLINE int(16) DEFAULT NULL,
  SP_ONLINE int(16) DEFAULT NULL,
  SP_OFFLINE int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table csp_auditor
--

LOCK TABLES csp_auditor WRITE;
/*!40000 ALTER TABLE csp_auditor DISABLE KEYS */;
INSERT INTO csp_auditor VALUES (3,2,4,1,1,1,1);
/*!40000 ALTER TABLE csp_auditor ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table csp_channel
--

DROP TABLE IF EXISTS csp_channel;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE csp_channel (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CSP_ID int(16) NOT NULL,
  CHANNEL_ID int(16) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table csp_channel
--

LOCK TABLES csp_channel WRITE;
/*!40000 ALTER TABLE csp_channel DISABLE KEYS */;
INSERT INTO csp_channel VALUES (4,2,474431589);
/*!40000 ALTER TABLE csp_channel ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table csp_csp
--

DROP TABLE IF EXISTS csp_csp;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE csp_csp (
  ID int(16) NOT NULL AUTO_INCREMENT,
  MASTER_CSP_ID int(16) DEFAULT NULL,
  CSP_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11882264 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table csp_csp
--

LOCK TABLES csp_csp WRITE;
/*!40000 ALTER TABLE csp_csp DISABLE KEYS */;
INSERT INTO csp_csp VALUES (24302,24298,24297),(10944317,51,50),(10944318,11,50),(11882263,2,2);
/*!40000 ALTER TABLE csp_csp ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table csp_device
--

DROP TABLE IF EXISTS csp_device;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE csp_device (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CSP_ID int(16) DEFAULT NULL,
  DEVICE_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=32027421 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table csp_device
--

LOCK TABLES csp_device WRITE;
/*!40000 ALTER TABLE csp_device DISABLE KEYS */;
INSERT INTO csp_device VALUES (32027416,2,1),(32027418,2,12),(32027419,2,10),(32027420,2,7);
/*!40000 ALTER TABLE csp_device ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table csp_module
--

DROP TABLE IF EXISTS csp_module;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE csp_module (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CSP_ID int(16) DEFAULT NULL,
  MODULE_ID int(16) DEFAULT NULL,
  IS_DEFAULT int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=28331922 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table csp_module
--

LOCK TABLES csp_module WRITE;
/*!40000 ALTER TABLE csp_module DISABLE KEYS */;
INSERT INTO csp_module VALUES (28331919,2,10000,1),(28331920,2,503754003,NULL),(28331921,2,503754004,NULL);
/*!40000 ALTER TABLE csp_module ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table csp_product
--

DROP TABLE IF EXISTS csp_product;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE csp_product (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CSP_ID int(16) DEFAULT NULL,
  PRODUCT_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table csp_product
--

LOCK TABLES csp_product WRITE;
/*!40000 ALTER TABLE csp_product DISABLE KEYS */;
/*!40000 ALTER TABLE csp_product ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table csp_template
--

DROP TABLE IF EXISTS csp_template;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE csp_template (
  CSP_ID int(16) NOT NULL AUTO_INCREMENT,
  CREATEOR varchar(256) DEFAULT NULL,
  TEMPLATE_ID int(16) DEFAULT NULL,
  PRIMARY KEY (CSP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table csp_template
--

LOCK TABLES csp_template WRITE;
/*!40000 ALTER TABLE csp_template DISABLE KEYS */;
/*!40000 ALTER TABLE csp_template ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table dailystatics_log
--

DROP TABLE IF EXISTS dailystatics_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE dailystatics_log (
  ID int(16) NOT NULL AUTO_INCREMENT,
  DATE_STATICS datetime DEFAULT NULL,
  ALL_NETFLOW int(16) DEFAULT NULL,
  ALLLIVE_NETFLOW int(16) DEFAULT NULL,
  ALLCONTENT_NETFLOW int(16) DEFAULT NULL,
  WASU_NETFLOW int(16) DEFAULT NULL,
  VOOLE_NETFLOW int(16) DEFAULT NULL,
  BESTV_NETFLOW int(16) DEFAULT NULL,
  ONLINE_USER int(16) DEFAULT NULL,
  ONLINEUSER_NETFLOW int(16) DEFAULT NULL,
  QUARTER_BINGFA varchar(2000) DEFAULT NULL,
  MAX_BINGFA varchar(2000) DEFAULT NULL,
  CREATETIME datetime DEFAULT NULL,
  WASULADONG_NETFLOW int(16) DEFAULT NULL,
  VOOLELADONG_NETFLOW int(16) DEFAULT NULL,
  BESTVLADONG_NETFLOW int(16) DEFAULT NULL,
  MOBILE_NETFLOW int(16) DEFAULT NULL,
  ELSE_NETFLOW int(16) DEFAULT NULL,
  ALL_NETFLOW_PAD int(16) DEFAULT NULL,
  ALL_NETFLOW_PHONE int(16) DEFAULT NULL,
  ALLLIVE_NETFLOW_PAD int(16) DEFAULT NULL,
  ALLLIVE_NETFLOW_PHONE int(16) DEFAULT NULL,
  ALLCONTENT_NETFLOW_PHONE int(16) DEFAULT NULL,
  ALLCONTENT_NETFLOW_PAD int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table dailystatics_log
--

LOCK TABLES dailystatics_log WRITE;
/*!40000 ALTER TABLE dailystatics_log DISABLE KEYS */;
/*!40000 ALTER TABLE dailystatics_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table device
--

DROP TABLE IF EXISTS device;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE device (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(100) DEFAULT NULL,
  TYPE int(16) DEFAULT NULL,
  IP varchar(20) DEFAULT NULL,
  URL varchar(200) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  FTP_PORT int(16) DEFAULT NULL,
  FTP_USER varchar(100) DEFAULT NULL,
  FTP_PWD varchar(60) DEFAULT NULL,
  FTP_PATH varchar(100) DEFAULT NULL,
  EXPORT_PATH varchar(255) DEFAULT NULL,
  LOCAL_PATH varchar(102) DEFAULT NULL,
  MAX_TASK int(16) DEFAULT NULL,
  MONIT_PORT int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table device
--

LOCK TABLES device WRITE;
/*!40000 ALTER TABLE device DISABLE KEYS */;
INSERT INTO device VALUES (1,'中心流媒体服务器',2,'127.0.0.1','http://127.0.0.1/',1,21,'shisu','shisu2015',NULL,'F:/movie/','F:/movie/',500,80),(7,'中心业务服务器',3,'127.0.0.1','http://127.0.0.1/',1,21,'','',NULL,'/home/fortune/movie','/home/fortune/movie',1000,80),(10,'中心转码服务器',4,'192.168.1.29','http://192.168.1.29/',1,21,'fortune','fortune!@#456',NULL,'/home/fortune/movie/','/home/fortune/movie/',10,80),(12,'中心直播服务器',6,'192.168.1.30','http://10.2.9.70:8080/live/',1,21,'user','user',NULL,'/home/fortune/movie/','/home/fortune/movie/',500,2050);
/*!40000 ALTER TABLE device ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table dictionary
--

DROP TABLE IF EXISTS dictionary;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE dictionary (
  code varchar(32) NOT NULL,
  name varchar(32) DEFAULT NULL,
  desp varchar(255) DEFAULT NULL,
  parent_code varchar(32) DEFAULT NULL,
  PRIMARY KEY (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table dictionary
--

LOCK TABLES dictionary WRITE;
/*!40000 ALTER TABLE dictionary DISABLE KEYS */;
INSERT INTO dictionary VALUES ('0.8','0.8升','','emission'),('01','0号柴油','','gasType'),('1.0','1.0升','','emission'),('1.2','1.2升','','emission'),('1.5','1.5升','','emission'),('1.6','1.6升','','emission'),('2.0','2.0升','','emission'),('2.8','2.8升','','emission'),('3.0','2.8升以上','','emission'),('90','90号','','gasType'),('92','92号','','gasType'),('93','93号','','gasType'),('95','95号','','gasType'),('97','97号','','gasType'),('A3','A3','','cherry'),('A5','A5','','cherry'),('audi','奥迪','','product'),('bmw','宝马','','product'),('buick','别克','','product'),('carColor','车身颜色','车身颜色','systemRoot'),('carColorBlue','蓝色','','carColor'),('carColorGray','银灰色','','carColor'),('carColorWhite','白色','','carColor'),('carType','车辆用途','车辆用途','systemRoot'),('carTypeForCommerial','商用','车辆用途-商用','carType'),('carTypeForHome','个人','车辆用途-个人','carType'),('cherry','奇瑞','','product'),('cnL0','未达标','','emissionType'),('cnL3','国3','','emissionType'),('cnL4','国4','','emissionType'),('cnL5','国5','','emissionType'),('dazhong','大众','','product'),('emission','排 量','排 量','systemRoot'),('emissionType','排放标准','排放标准','systemRoot'),('enineType','发动机型号','发动机型号','systemRoot'),('enineType4Lv','4缸全铝','','enineType'),('enineType8Lv','8缸全铝','','enineType'),('gasType','燃油种类','燃油种类','systemRoot'),('gearbox','变速箱类型','变速箱类型','systemRoot'),('gearboxAuto','自动档','','gearbox'),('gearboxManual','手动档','','gearbox'),('gearboxManualWithAuto','手自一体','','gearbox'),('innerColor','内饰颜色','内饰颜色','systemRoot'),('innerColorFeather','全皮','','innerColor'),('innerColorGray','布艺全灰','','innerColor'),('innerColorWhite','布艺全白','','innerColor'),('insureCompany','保险公司','保险公司','systemRoot'),('insureCompanyDaDi','大地车险','','insureCompany'),('insureCompanyPingAn','平安车险','','insureCompany'),('insureCompanyTaiPingYang','太平洋保险','','insureCompany'),('insureCompanyTianPing','天平车险','','insureCompany'),('insureCompanyYangGuang','阳光车险','','insureCompany'),('insureType','险种','险种','systemRoot'),('insureTypeDQX','盗抢险','','insureType'),('insureTypeHuaHen','划痕险','','insureType'),('insureTypeJQX','交强险','','insureType'),('insureTypeSZX','第三者责任险','','insureType'),('porche','保时捷','','product'),('product','汽车厂商','','systemRoot'),('productHometown','汽车产地','汽车产地','systemRoot'),('productHometownCN','中国大陆','','productHometown'),('productHometownEurop','欧洲','','productHometown'),('productHometownJP','日本','','productHometown'),('productHometownUSA','美国','','productHometown'),('QQ','QQ','','cherry'),('salesCompany','销售单位','销售单位','systemRoot'),('salesCompanyJinHong','锦鸿','','salesCompany'),('salesCompanySenBen','森本','','salesCompany'),('salesCompanySiXing','四星','','salesCompany'),('tiger','瑞虎','','cherry'),('tyreType','轮胎类型','轮胎类型','systemRoot'),('tyreTypeThin','窄胎','','tyreType'),('tyreTypeWide','宽胎','','tyreType');
/*!40000 ALTER TABLE dictionary ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table encoder_task
--

DROP TABLE IF EXISTS encoder_task;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE encoder_task (
  ID int(11) NOT NULL AUTO_INCREMENT,
  ENCODER_ID int(11) NOT NULL,
  CLIP_ID int(11) NOT NULL,
  START_TIME datetime DEFAULT NULL,
  STOP_TIME datetime DEFAULT NULL,
  STATUS int(11) DEFAULT '0',
  PROCESS int(11) DEFAULT NULL,
  TEMPLATE_ID int(11) DEFAULT NULL,
  DESERT_FILE_NAME varchar(1024) DEFAULT NULL,
  SOURCE_FILE_NAME varchar(1024) DEFAULT NULL,
  STREAM_SERVER_ID int(11) DEFAULT NULL,
  NAME varchar(255) DEFAULT NULL,
  ENCODE_LOG varchar(4000) DEFAULT NULL,
  FILE_SIZE bigint(20) DEFAULT NULL,
  FILE_DATE datetime DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY ET_CLIP_ID_INDEX (CLIP_ID),
  KEY ET_START_TIME_INDEX (START_TIME),
  KEY ET_STATUS_ENCODER_ID_INDEX (STATUS,ENCODER_ID),
  KEY ET_STATUS_INDEX (STATUS),
  KEY IDX_ENCODERTASK_ENCODERID (ENCODER_ID),
  KEY IDX_ET_CID_TID (CLIP_ID,TEMPLATE_ID)
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table encoder_task
--

LOCK TABLES encoder_task WRITE;
/*!40000 ALTER TABLE encoder_task DISABLE KEYS */;
/*!40000 ALTER TABLE encoder_task ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table encoder_template
--

DROP TABLE IF EXISTS encoder_template;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE encoder_template (
  ID int(16) NOT NULL AUTO_INCREMENT,
  TEMPLATE_NAME varchar(128) NOT NULL,
  TEMPLATE_CODE varchar(64) DEFAULT NULL,
  V_ENCODER_TYPE varchar(32) DEFAULT NULL,
  V_BITRATE int(16) DEFAULT NULL,
  V_FRAME_RATE int(16) DEFAULT NULL,
  V_KEYFRAME_INTERVAL int(16) DEFAULT NULL,
  V_WIDTH int(16) DEFAULT NULL,
  V_HEIGHT int(16) DEFAULT NULL,
  V_FIXED_QP int(16) DEFAULT NULL,
  V_MAX_QP int(16) DEFAULT NULL,
  A_CODEC varchar(128) NOT NULL,
  A_CHANNEL int(16) NOT NULL,
  A_SAMPLE_RATE int(16) DEFAULT NULL,
  A_TYPE int(16) DEFAULT NULL,
  A_BIRATE int(16) DEFAULT NULL,
  FILE_FORMAT varchar(32) NOT NULL,
  PROPERTY_ID int(11) DEFAULT NULL,
  DISTRIBUTE int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=27773072 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table encoder_template
--

LOCK TABLES encoder_template WRITE;
/*!40000 ALTER TABLE encoder_template DISABLE KEYS */;
INSERT INTO encoder_template VALUES (27773070,'1Mbps','1M','x264',1024,25,2,720,480,28,30,'faac',2,48000,0,96,'m3u8',676496266,0),(27773071,'512Kbps','512K_640x480','x264',374,25,1,640,360,15,15,'faac',2,48000,0,64,'m3u8',676496294,1);
/*!40000 ALTER TABLE encoder_template ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table epg
--

DROP TABLE IF EXISTS epg;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE epg (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(128) DEFAULT NULL,
  THUMB_PIC varchar(128) DEFAULT NULL,
  LIVE_ID int(11) DEFAULT NULL,
  CONTENT_ID int(11) DEFAULT NULL,
  STATUS int(11) DEFAULT NULL,
  BEGIN_TIME datetime DEFAULT NULL,
  END_TIME datetime DEFAULT NULL,
  PRIMARY KEY (ID),
  UNIQUE KEY IDX_EPG_BEGINTIME (BEGIN_TIME),
  UNIQUE KEY IDX_EPG_CONTENT (CONTENT_ID),
  UNIQUE KEY IDX_EPG_ENDTIME (END_TIME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table epg
--

LOCK TABLES epg WRITE;
/*!40000 ALTER TABLE epg DISABLE KEYS */;
/*!40000 ALTER TABLE epg ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table frd_bookmark
--

DROP TABLE IF EXISTS frd_bookmark;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE frd_bookmark (
  ID int(16) NOT NULL AUTO_INCREMENT,
  USER_ID varchar(30) DEFAULT NULL,
  USER_TYPE int(16) DEFAULT NULL,
  CONTENT_ID varchar(30) DEFAULT NULL,
  SUBCONTENT_ID varchar(30) DEFAULT NULL,
  SUBCONTENT_NAME varchar(30) DEFAULT NULL,
  SUBCONTENT_TYPE int(16) DEFAULT NULL,
  SERVICE_TYPE int(16) DEFAULT NULL,
  BOOKMARK_ID varchar(30) DEFAULT NULL,
  BOOKMARK_TYPE int(16) DEFAULT NULL,
  BOOKMARK_VALUE varchar(30) DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=32503 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table frd_bookmark
--

LOCK TABLES frd_bookmark WRITE;
/*!40000 ALTER TABLE frd_bookmark DISABLE KEYS */;
INSERT INTO frd_bookmark VALUES (32483,'xjliu',NULL,'45','1',NULL,NULL,NULL,NULL,NULL,'142','2015-07-14 15:29:19'),(32484,'xjliu',NULL,'43','1',NULL,NULL,NULL,NULL,NULL,'20','2015-07-14 09:53:48'),(32485,'xjliu',NULL,'20','1',NULL,NULL,NULL,NULL,NULL,'4930','2015-07-14 15:42:50'),(32486,'xjliu',NULL,'14','1',NULL,NULL,NULL,NULL,NULL,'1','2015-07-14 10:42:51'),(32487,'xjliu',NULL,'51','1',NULL,NULL,NULL,NULL,NULL,'395','2015-07-14 11:30:12'),(32488,'xjliu',NULL,'52','1',NULL,NULL,NULL,NULL,NULL,'0','2015-07-14 15:39:28'),(32489,'xjliu',NULL,'44','1',NULL,NULL,NULL,NULL,NULL,'446','2015-07-14 11:26:42'),(32490,'xjliu',NULL,'38','1',NULL,NULL,NULL,NULL,NULL,'18','2015-07-14 13:48:16'),(32491,'xjliu',NULL,'23','1',NULL,NULL,NULL,NULL,NULL,'0','2015-07-14 14:45:33'),(32492,'xjliu',NULL,'48','1',NULL,NULL,NULL,NULL,NULL,'NaN','2015-07-14 14:57:33'),(32493,'xjliu',NULL,'53','1',NULL,NULL,NULL,NULL,NULL,'0','2015-07-14 15:26:52'),(32494,'xjliu',NULL,'46','1',NULL,NULL,NULL,NULL,NULL,'40','2015-07-14 14:11:23'),(32495,'xjliu',NULL,'3','1',NULL,NULL,NULL,NULL,NULL,'0','2015-07-14 14:45:42'),(32496,'xjliu',NULL,'54','1',NULL,NULL,NULL,NULL,NULL,'704','2015-07-14 17:50:01'),(32497,'xjliu',NULL,'55','1',NULL,NULL,NULL,NULL,NULL,'28','2015-07-14 18:14:07'),(32498,'xjliu',NULL,'59','1',NULL,NULL,NULL,NULL,NULL,'53','2015-07-15 14:33:41'),(32499,'xjliu',NULL,'63','1',NULL,NULL,NULL,NULL,NULL,'0','2015-07-16 08:31:01'),(32500,'xjliu',NULL,'89','1',NULL,NULL,NULL,NULL,NULL,'6','2015-07-22 14:13:50'),(32501,'xjliu',NULL,'82','1',NULL,NULL,NULL,NULL,NULL,'61','2015-07-22 14:14:59'),(32502,'xjliu',NULL,'90','1',NULL,NULL,NULL,NULL,NULL,'6','2015-07-22 14:23:28');
/*!40000 ALTER TABLE frd_bookmark ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table frd_recommend
--

DROP TABLE IF EXISTS frd_recommend;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE frd_recommend (
  ID int(16) NOT NULL AUTO_INCREMENT,
  USER_ID varchar(30) DEFAULT NULL,
  USER_TYPE int(16) DEFAULT NULL,
  CONTENT_ID varchar(30) DEFAULT NULL,
  SUBCONTENT_ID varchar(30) DEFAULT NULL,
  SUBCONTENT_NAME varchar(30) DEFAULT NULL,
  SUBCONTENT_TYPE int(16) DEFAULT NULL,
  SERVICE_TYPE int(16) DEFAULT NULL,
  MODE1 int(16) DEFAULT NULL,
  FRIEND_ID_LIST varchar(300) DEFAULT NULL,
  INFO varchar(300) DEFAULT NULL,
  TIME_STAMP varchar(300) DEFAULT NULL,
  RECOMMENDED_ID varchar(30) DEFAULT NULL,
  RECOMMENDER varchar(300) DEFAULT NULL,
  RECOMMENDER_NAME varchar(300) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table frd_recommend
--

LOCK TABLES frd_recommend WRITE;
/*!40000 ALTER TABLE frd_recommend DISABLE KEYS */;
/*!40000 ALTER TABLE frd_recommend ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table frd_recommend_friend
--

DROP TABLE IF EXISTS frd_recommend_friend;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE frd_recommend_friend (
  ID int(16) NOT NULL AUTO_INCREMENT,
  RECOMMEND_ID varchar(30) DEFAULT NULL,
  FRIEND_ID varchar(30) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table frd_recommend_friend
--

LOCK TABLES frd_recommend_friend WRITE;
/*!40000 ALTER TABLE frd_recommend_friend DISABLE KEYS */;
/*!40000 ALTER TABLE frd_recommend_friend ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table frd_user_friend
--

DROP TABLE IF EXISTS frd_user_friend;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE frd_user_friend (
  ID int(16) NOT NULL AUTO_INCREMENT,
  FRIEND_ID varchar(30) DEFAULT NULL,
  FRIEND_NAME varchar(30) DEFAULT NULL,
  USER_ID varchar(30) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table frd_user_friend
--

LOCK TABLES frd_user_friend WRITE;
/*!40000 ALTER TABLE frd_user_friend DISABLE KEYS */;
/*!40000 ALTER TABLE frd_user_friend ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table front_user
--

DROP TABLE IF EXISTS front_user;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE front_user (
  USER_ID varchar(128) NOT NULL,
  NAME varchar(256) NOT NULL,
  ORGANIZATION_ID int(11) NOT NULL,
  GENDER int(11) NOT NULL,
  PASSWORD varchar(32) NOT NULL,
  TYPE_ID int(11) NOT NULL,
  CREATE_TIME datetime DEFAULT NULL,
  LAST_MODIFIED datetime DEFAULT NULL,
  LAST_LOGON datetime DEFAULT NULL,
  LOGON_TIMES int(11) DEFAULT '0',
  STATUS int(11) DEFAULT NULL,
  MAIL varchar(128) DEFAULT NULL,
  PHONE varchar(16) DEFAULT NULL,
  BIRTHDAY datetime DEFAULT NULL,
  CITY varchar(128) DEFAULT NULL,
  COMMITTEE varchar(128) DEFAULT NULL,
  SECTION varchar(128) DEFAULT NULL,
  UNIT varchar(128) DEFAULT NULL,
  PRIMARY KEY (USER_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table front_user
--

LOCK TABLES front_user WRITE;
/*!40000 ALTER TABLE front_user DISABLE KEYS */;
INSERT INTO front_user VALUES ('xjliu','刘喜军',1,1,'675b6078ead20bddbec07023b9bd276e',1,NULL,'2015-06-05 18:17:35',NULL,0,1,'','',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE front_user ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table front_user_type
--

DROP TABLE IF EXISTS front_user_type;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE front_user_type (
  TYPE_ID int(11) NOT NULL AUTO_INCREMENT,
  TYPE_NAME varchar(128) NOT NULL,
  PRIMARY KEY (TYPE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table front_user_type
--

LOCK TABLES front_user_type WRITE;
/*!40000 ALTER TABLE front_user_type DISABLE KEYS */;
/*!40000 ALTER TABLE front_user_type ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table individual
--

DROP TABLE IF EXISTS individual;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE individual (
  ID int(11) NOT NULL AUTO_INCREMENT,
  LOGO_PATH varchar(256) DEFAULT '/page/redex/assets/images/logo.png',
  NAME varchar(64) DEFAULT 'Redex',
  MOBILE_LOGO_PATH varchar(256) DEFAULT '/page/redex/assets/images/logo2.png',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table individual
--

LOCK TABLES individual WRITE;
/*!40000 ALTER TABLE individual DISABLE KEYS */;
INSERT INTO individual VALUES (1,'/images/individual/1.png','虞友管理系统','/images/individual/2.png');
/*!40000 ALTER TABLE individual ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table investigation
--

DROP TABLE IF EXISTS investigation;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE investigation (
  ID int(11) NOT NULL AUTO_INCREMENT,
  TITLE varchar(512) NOT NULL,
  CREATE_TIME date NOT NULL,
  START_TIME datetime DEFAULT NULL,
  END_TIME datetime DEFAULT NULL,
  STATUS int(11) NOT NULL,
  LAST_MODIFIED datetime DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table investigation
--

LOCK TABLES investigation WRITE;
/*!40000 ALTER TABLE investigation DISABLE KEYS */;
/*!40000 ALTER TABLE investigation ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table investigation_question
--

DROP TABLE IF EXISTS investigation_question;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE investigation_question (
  ID int(11) NOT NULL AUTO_INCREMENT,
  INVESTIGATION_ID int(11) NOT NULL,
  QUESTION_ID int(11) NOT NULL,
  SEQUENCE int(11) NOT NULL,
  PRIMARY KEY (ID),
  KEY INDEX_INVESTIGATION_ID (INVESTIGATION_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table investigation_question
--

LOCK TABLES investigation_question WRITE;
/*!40000 ALTER TABLE investigation_question DISABLE KEYS */;
/*!40000 ALTER TABLE investigation_question ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table investigation_result
--

DROP TABLE IF EXISTS investigation_result;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE investigation_result (
  ID int(11) NOT NULL AUTO_INCREMENT,
  INVESTIGAT_TIME datetime NOT NULL,
  DURATION int(11) NOT NULL,
  INVESTIGATION_ID int(11) NOT NULL,
  USER_ID varchar(64) NOT NULL,
  PRIMARY KEY (ID),
  KEY INDEX_INVESTIGATION_ID (INVESTIGATION_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table investigation_result
--

LOCK TABLES investigation_result WRITE;
/*!40000 ALTER TABLE investigation_result DISABLE KEYS */;
/*!40000 ALTER TABLE investigation_result ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table ip2phone_log
--

DROP TABLE IF EXISTS ip2phone_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ip2phone_log (
  ID int(16) NOT NULL AUTO_INCREMENT,
  IP varchar(64) DEFAULT NULL,
  TOKEN varchar(255) DEFAULT NULL,
  UNI_KEY varchar(255) DEFAULT NULL,
  PHONE varchar(32) DEFAULT NULL,
  RESULT_CODE int(11) DEFAULT NULL,
  LOGS varchar(1024) DEFAULT NULL,
  START_TIME datetime DEFAULT NULL,
  USER_AGENT varchar(255) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY IDX_ON_IP2PHONE (PHONE),
  KEY IDX_ON_IP2PHONE_START_TIME (START_TIME)
) ENGINE=InnoDB AUTO_INCREMENT=288 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ip2phone_log
--

LOCK TABLES ip2phone_log WRITE;
/*!40000 ALTER TABLE ip2phone_log DISABLE KEYS */;
/*!40000 ALTER TABLE ip2phone_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table ip_range
--

DROP TABLE IF EXISTS ip_range;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ip_range (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(30) DEFAULT NULL,
  DESP varchar(100) DEFAULT NULL,
  IP_FROM int(16) DEFAULT NULL,
  IP_TO int(16) DEFAULT NULL,
  AREA_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ip_range
--

LOCK TABLES ip_range WRITE;
/*!40000 ALTER TABLE ip_range DISABLE KEYS */;
/*!40000 ALTER TABLE ip_range ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table live
--

DROP TABLE IF EXISTS live;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE live (
  ID int(11) NOT NULL AUTO_INCREMENT,
  TITLE varchar(512) NOT NULL,
  TASK_ID int(11) NOT NULL,
  CREATE_TIME datetime NOT NULL,
  STATUS int(11) NOT NULL,
  AUTO_CONTROL int(11) NOT NULL,
  SUFFIX varchar(64) DEFAULT NULL,
  ACTOR varchar(128) DEFAULT NULL,
  INTRO varchar(1024) DEFAULT NULL,
  POSTER varchar(512) DEFAULT NULL,
  FORESHOW int(11) DEFAULT '0',
  SERVER_ID int(11) DEFAULT NULL,
  TYPE int(11) NOT NULL,
  START_TIME time DEFAULT NULL,
  END_TIME time DEFAULT NULL,
  START_DATE date DEFAULT NULL,
  END_DATE date DEFAULT NULL,
  WEEK_DAY varchar(64) DEFAULT NULL,
  USER_TYPES varchar(256) DEFAULT NULL,
  NEED_RECORD int(11) NOT NULL,
  RECORD_CHANNELS varchar(1024) DEFAULT NULL,
  CHANNEL varchar(64) DEFAULT NULL,
  CREATOR int(11) DEFAULT NULL,
  CSP_ID int(11) DEFAULT NULL,
  IS_LIVE int(11) DEFAULT '1',
  MODULE_ID int(11) DEFAULT '10000',
  PRIMARY KEY (ID),
  KEY INDEX_LIVE_TASK (TASK_ID)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table live
--

LOCK TABLES live WRITE;
/*!40000 ALTER TABLE live DISABLE KEYS */;
/*!40000 ALTER TABLE live ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table live_channel
--

DROP TABLE IF EXISTS live_channel;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE live_channel (
  ID int(11) NOT NULL AUTO_INCREMENT,
  LIVE_ID int(11) NOT NULL,
  CHANNEL_ID int(11) NOT NULL,
  PRIMARY KEY (ID),
  KEY INDEX_LC_LIVE (LIVE_ID),
  KEY INDEX_LC_CHANNEL (CHANNEL_ID)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table live_channel
--

LOCK TABLES live_channel WRITE;
/*!40000 ALTER TABLE live_channel DISABLE KEYS */;
INSERT INTO live_channel VALUES (8,6,474431631),(11,12,474431602),(12,12,474431664),(13,10,474431595);
/*!40000 ALTER TABLE live_channel ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table live_log
--

DROP TABLE IF EXISTS live_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE live_log (
  ID int(11) NOT NULL AUTO_INCREMENT,
  LIVE_ID int(11) NOT NULL,
  START_TIME datetime DEFAULT NULL,
  END_TIME datetime DEFAULT NULL,
  RECORDED int(11) DEFAULT NULL,
  PATH varchar(512) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY INDEX_LL_LIVE (LIVE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table live_log
--

LOCK TABLES live_log WRITE;
/*!40000 ALTER TABLE live_log DISABLE KEYS */;
/*!40000 ALTER TABLE live_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table menu
--

DROP TABLE IF EXISTS menu;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE menu (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(50) NOT NULL,
  URL varchar(50) NOT NULL,
  PERMISSION_ID int(11) DEFAULT NULL,
  PERMISSION_STR varchar(2000) DEFAULT NULL,
  PARENT_ID int(11) DEFAULT NULL,
  STATUS int(11) DEFAULT '1',
  STYLE varchar(255) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table menu
--

LOCK TABLES menu WRITE;
/*!40000 ALTER TABLE menu DISABLE KEYS */;
INSERT INTO menu VALUES (1,'车辆管理','/cars/',-1,'',0,1,'car'),(2,'车友信息','/cars/cars.jsp',-1,'/cars/cars.jsp;/cars/carView.jsp',1,1,''),(4,'内容一览','/pub/pubList.jsp',-1,'/content/normalContent.action;/publish/channel!channelTree.action;/pub/pubList.jsp;/user/userType!list.action;/module/module!list.action;/man.jsp;/content/modify.action;/content/getContent.action;/content/content!cpChangeStatus.action',1,10,''),(11,'系统管理','/sys/',-1,'',0,1,'cog|background-color:#feb448;'),(12,'字典管理','/sys/dict.jsp',-1,'/sys/dict.jsp',11,1,''),(17,'管理员管理','/sys/admin.jsp',-1,'/security/admin!saveAdmin.action;/security/admin!lock.action;/security/admin!searchAdmin.action;/sys/admin.jsp;/man.jsp;/security/admin!removeAdmin.action;/security/role!list.action',11,1,''),(19,'系统个性化','#',-1,'',11,10,''),(20,'超户专属','/su/',-1,'',0,1,'user|background-color:#9564e2;'),(21,'安全日志','/su/logs.jsp',-1,'/su/logs.jsp;/system/systemLog!list.action;/man.jsp',20,1,''),(22,'角色管理','/su/roles.jsp',-1,'/security/role!listRoles.action;/security/role!delete.action;/su/roles.jsp;/security/role!save.action;/security/role!view.action',20,1,''),(23,'功能管理','/su/menus.jsp',-1,'/security/menu!list.action;/security/menu!delete.action;/security/menu!view.action;/security/menu!save.action;/security/menu!listFolderMenus.action;/su/menus.jsp;/man.jsp',20,1,''),(24,'前台登录日志','/su/userLoginLogs.jsp',-1,'/su/userLoginLogs.jsp;/user/userlogin!list.action',20,10,'');
/*!40000 ALTER TABLE menu ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table module
--

DROP TABLE IF EXISTS module;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE module (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(40) DEFAULT NULL,
  CODE varchar(40) DEFAULT NULL,
  DESP varchar(200) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  TYPE int(11) DEFAULT '1',
  COLUMNS int(11) DEFAULT '2',
  CHANNEL_ID int(11) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=503754005 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table module
--

LOCK TABLES module WRITE;
/*!40000 ALTER TABLE module DISABLE KEYS */;
/*!40000 ALTER TABLE module ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table module_property
--

DROP TABLE IF EXISTS module_property;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE module_property (
  ID int(11) NOT NULL AUTO_INCREMENT,
  MODULE_ID int(11) DEFAULT NULL,
  PROPERTY_ID int(11) DEFAULT NULL,
  DISPLAY_ORDER int(11) DEFAULT NULL,
  STATUS int(11) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table module_property
--

LOCK TABLES module_property WRITE;
/*!40000 ALTER TABLE module_property DISABLE KEYS */;
/*!40000 ALTER TABLE module_property ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table option_result
--

DROP TABLE IF EXISTS option_result;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE option_result (
  ID int(11) NOT NULL AUTO_INCREMENT,
  RESULT_ID int(11) NOT NULL,
  OPTION_ID int(11) NOT NULL,
  TYPE int(11) DEFAULT '1',
  PRIMARY KEY (ID),
  KEY INDEX_OPTION_ID (OPTION_ID),
  KEY INDEX_RESULT_ID (RESULT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table option_result
--

LOCK TABLES option_result WRITE;
/*!40000 ALTER TABLE option_result DISABLE KEYS */;
/*!40000 ALTER TABLE option_result ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table organization
--

DROP TABLE IF EXISTS organization;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE organization (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(256) NOT NULL,
  SEQUENCE int(11) DEFAULT NULL,
  PARENT_ID int(11) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table organization
--

LOCK TABLES organization WRITE;
/*!40000 ALTER TABLE organization DISABLE KEYS */;
/*!40000 ALTER TABLE organization ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table organization_channel
--

DROP TABLE IF EXISTS organization_channel;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE organization_channel (
  ID int(11) NOT NULL AUTO_INCREMENT,
  ORGANIZATION_ID int(11) NOT NULL,
  CHANNEL_ID int(11) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=7928 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table organization_channel
--

LOCK TABLES organization_channel WRITE;
/*!40000 ALTER TABLE organization_channel DISABLE KEYS */;
/*!40000 ALTER TABLE organization_channel ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table permission
--

DROP TABLE IF EXISTS permission;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE permission (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(128) NOT NULL,
  TARGET varchar(128) NOT NULL,
  CLASSNAME varchar(255) NOT NULL,
  METHODNAME varchar(255) NOT NULL,
  PERMISSIONDESC varchar(1024) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=544377644 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table permission
--

LOCK TABLES permission WRITE;
/*!40000 ALTER TABLE permission DISABLE KEYS */;
/*!40000 ALTER TABLE permission ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table phone_range
--

DROP TABLE IF EXISTS phone_range;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE phone_range (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(30) DEFAULT NULL,
  DESP varchar(100) DEFAULT NULL,
  PHONE_FROM mediumtext,
  PHONE_TO mediumtext,
  AREA_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=118696587 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table phone_range
--

LOCK TABLES phone_range WRITE;
/*!40000 ALTER TABLE phone_range DISABLE KEYS */;
/*!40000 ALTER TABLE phone_range ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table product
--

DROP TABLE IF EXISTS product;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE product (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(100) DEFAULT NULL,
  PAY_PRODUCT_NO varchar(100) DEFAULT NULL,
  PRICE float DEFAULT NULL,
  TYPE int(16) DEFAULT NULL,
  VALID_LENGTH int(16) DEFAULT NULL,
  LENGTH_UNIT int(16) DEFAULT NULL,
  AUTO_PAY int(16) DEFAULT NULL,
  SEARCH_EXTRA int(16) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  MOBILE_PRODUCT int(11) DEFAULT NULL,
  DESCRIPTION varchar(1024) DEFAULT NULL,
  SEQUENCE int(16) DEFAULT NULL,
  COST_TYPE int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table product
--

LOCK TABLES product WRITE;
/*!40000 ALTER TABLE product DISABLE KEYS */;
/*!40000 ALTER TABLE product ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table property
--

DROP TABLE IF EXISTS property;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE property (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CODE varchar(200) DEFAULT NULL,
  NAME varchar(200) DEFAULT NULL,
  DATA_TYPE int(11) DEFAULT NULL,
  IS_MULTI_LINE int(11) DEFAULT NULL,
  IS_MERGE int(11) DEFAULT NULL,
  MAX_SIZE int(16) DEFAULT NULL,
  IS_NULL int(11) DEFAULT NULL,
  IS_MAIN int(11) DEFAULT NULL,
  COLUMN_NAME varchar(20) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  DESP varchar(1000) DEFAULT NULL,
  MODULE_ID int(16) DEFAULT NULL,
  DISPLAY_ORDER int(16) DEFAULT NULL,
  RELATED_TABLE int(11) DEFAULT NULL,
  col_span int(11) DEFAULT '1',
  row_span int(11) DEFAULT '1',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=676496296 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table property
--

LOCK TABLES property WRITE;
/*!40000 ALTER TABLE property DISABLE KEYS */;
/*!40000 ALTER TABLE property ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table property_select
--

DROP TABLE IF EXISTS property_select;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE property_select (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CODE varchar(20) DEFAULT NULL,
  NAME varchar(50) DEFAULT NULL,
  PROPERTY_ID int(16) DEFAULT NULL,
  DISPLAY_ORDER int(16) DEFAULT NULL,
  PARENTID int(16) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  value int(11) DEFAULT '1',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table property_select
--

LOCK TABLES property_select WRITE;
/*!40000 ALTER TABLE property_select DISABLE KEYS */;
/*!40000 ALTER TABLE property_select ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table question
--

DROP TABLE IF EXISTS question;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE question (
  ID int(11) NOT NULL AUTO_INCREMENT,
  TITLE varchar(256) NOT NULL,
  CREATE_TIME datetime NOT NULL,
  MAX_OPTION int(11) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table question
--

LOCK TABLES question WRITE;
/*!40000 ALTER TABLE question DISABLE KEYS */;
/*!40000 ALTER TABLE question ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table question_option
--

DROP TABLE IF EXISTS question_option;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE question_option (
  ID int(11) NOT NULL AUTO_INCREMENT,
  QUESTION_ID int(11) NOT NULL,
  TITLE varchar(512) NOT NULL,
  SEQUENCE int(11) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY INDEX_QUESTION_ID (QUESTION_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table question_option
--

LOCK TABLES question_option WRITE;
/*!40000 ALTER TABLE question_option DISABLE KEYS */;
/*!40000 ALTER TABLE question_option ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table recommend
--

DROP TABLE IF EXISTS recommend;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE recommend (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CODE varchar(100) DEFAULT NULL,
  NAME varchar(100) DEFAULT NULL,
  DESP varchar(200) DEFAULT NULL,
  IS_SYSTEM int(16) DEFAULT NULL,
  CSP_ID int(16) DEFAULT NULL,
  TYPE int(16) DEFAULT NULL,
  CHANNEL_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=433979352 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table recommend
--

LOCK TABLES recommend WRITE;
/*!40000 ALTER TABLE recommend DISABLE KEYS */;
INSERT INTO recommend VALUES (10,'top10','热播榜',NULL,999,1,2,-1),(433979345,'slider','宽屏轮显推荐',NULL,1,0,1,-1),(433979346,'news','新闻频道',NULL,2,1,2,474431591),(433979347,'import','重大活动',NULL,999,1,2,474431590),(433979348,'culture','人文频道',NULL,3,1,2,474431592),(433979349,'industry','工业频道',NULL,4,1,2,474431593),(433979350,'international','国际频道',NULL,999,1,2,474431594),(433979351,'diy','原创专区',NULL,999,1,2,474431595);
/*!40000 ALTER TABLE recommend ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table record
--

DROP TABLE IF EXISTS record;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE record (
  ID int(11) NOT NULL AUTO_INCREMENT,
  TITLE varchar(512) NOT NULL,
  TASK_ID int(11) NOT NULL,
  CREATE_TIME datetime NOT NULL,
  STATUS int(11) NOT NULL,
  AUTO_CONTROL int(11) NOT NULL,
  SUFFIX varchar(64) DEFAULT NULL,
  ACTOR varchar(128) DEFAULT NULL,
  INTRO varchar(1024) DEFAULT NULL,
  POSTER varchar(512) DEFAULT NULL,
  AUTO_SNAP int(11) NOT NULL,
  SERVER_ID int(11) DEFAULT NULL,
  TYPE int(11) NOT NULL,
  START_TIME time DEFAULT NULL,
  END_TIME time DEFAULT NULL,
  START_DATE date DEFAULT NULL,
  END_DATE date DEFAULT NULL,
  WEEK_DAY varchar(64) DEFAULT NULL,
  USER_TYPES varchar(256) DEFAULT NULL,
  RECORD_CHANNELS varchar(1024) DEFAULT NULL,
  CSP_ID int(11) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY INDEX_RECORD_TASK (TASK_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table record
--

LOCK TABLES record WRITE;
/*!40000 ALTER TABLE record DISABLE KEYS */;
/*!40000 ALTER TABLE record ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table record_log
--

DROP TABLE IF EXISTS record_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE record_log (
  ID int(11) NOT NULL AUTO_INCREMENT,
  RECORD_ID int(11) NOT NULL,
  START_TIME datetime DEFAULT NULL,
  END_TIME datetime DEFAULT NULL,
  PATH varchar(512) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY INDEX_RL_LIVE (RECORD_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table record_log
--

LOCK TABLES record_log WRITE;
/*!40000 ALTER TABLE record_log DISABLE KEYS */;
/*!40000 ALTER TABLE record_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table related
--

DROP TABLE IF EXISTS related;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE related (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CODE varchar(40) DEFAULT NULL,
  NAME varchar(40) DEFAULT NULL,
  DESP varchar(100) DEFAULT NULL,
  CSP_ID int(16) DEFAULT NULL,
  TYPE int(16) DEFAULT NULL,
  MODULE_ID int(16) DEFAULT NULL,
  PROPERTY_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table related
--

LOCK TABLES related WRITE;
/*!40000 ALTER TABLE related DISABLE KEYS */;
/*!40000 ALTER TABLE related ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table related_property
--

DROP TABLE IF EXISTS related_property;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE related_property (
  ID int(16) NOT NULL AUTO_INCREMENT,
  RELATED_ID int(16) DEFAULT NULL,
  PROPERTY_ID int(16) DEFAULT NULL,
  PROPERTY_VALUE varchar(200) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table related_property
--

LOCK TABLES related_property WRITE;
/*!40000 ALTER TABLE related_property DISABLE KEYS */;
/*!40000 ALTER TABLE related_property ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table related_property_content
--

DROP TABLE IF EXISTS related_property_content;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE related_property_content (
  ID int(16) NOT NULL AUTO_INCREMENT,
  RELATED_PROPERTY_ID int(16) DEFAULT NULL,
  CONTENT_ID int(16) DEFAULT NULL,
  CSP_ID int(16) DEFAULT NULL,
  DISPLAY_ORDER int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table related_property_content
--

LOCK TABLES related_property_content WRITE;
/*!40000 ALTER TABLE related_property_content DISABLE KEYS */;
/*!40000 ALTER TABLE related_property_content ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table role
--

DROP TABLE IF EXISTS role;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE role (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(50) NOT NULL,
  MEMO varchar(100) DEFAULT NULL,
  TYPE int(11) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table role
--

LOCK TABLES role WRITE;
/*!40000 ALTER TABLE role DISABLE KEYS */;
INSERT INTO role VALUES (1,'超户专属',NULL,NULL),(2,'发布视频',NULL,NULL),(3,'车友管理',NULL,NULL),(4,'系统管理','系统维护操作',3);
/*!40000 ALTER TABLE role ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table role_menu
--

DROP TABLE IF EXISTS role_menu;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE role_menu (
  ID int(11) NOT NULL AUTO_INCREMENT,
  ROLE_ID int(11) DEFAULT NULL,
  MENU_ID int(11) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table role_menu
--

LOCK TABLES role_menu WRITE;
/*!40000 ALTER TABLE role_menu DISABLE KEYS */;
INSERT INTO role_menu VALUES (1,1,21),(2,1,22),(3,1,23),(4,1,24),(5,2,2),(6,2,3),(7,2,4),(13,4,12),(14,4,13),(15,4,14),(16,4,15),(17,4,16),(18,4,17),(19,4,18),(20,4,19),(76,2,26),(77,2,27),(78,1,25),(79,3,2),(80,3,12);
/*!40000 ALTER TABLE role_menu ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table role_permission
--

DROP TABLE IF EXISTS role_permission;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE role_permission (
  ID int(16) NOT NULL AUTO_INCREMENT,
  ROLE_ID int(16) DEFAULT NULL,
  PERMISSION_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=544377789 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table role_permission
--

LOCK TABLES role_permission WRITE;
/*!40000 ALTER TABLE role_permission DISABLE KEYS */;
/*!40000 ALTER TABLE role_permission ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table search_log
--

DROP TABLE IF EXISTS search_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE search_log (
  ID int(16) NOT NULL AUTO_INCREMENT,
  SEARCH_VALUE varchar(30) NOT NULL,
  SEARCH_TIME datetime NOT NULL,
  USER_TEL int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table search_log
--

LOCK TABLES search_log WRITE;
/*!40000 ALTER TABLE search_log DISABLE KEYS */;
INSERT INTO search_log VALUES (1,'1','2014-06-13 15:10:33',123),(2,'1','2014-06-13 15:11:00',123),(3,'1','2014-06-13 16:05:08',123);
/*!40000 ALTER TABLE search_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table service_product
--

DROP TABLE IF EXISTS service_product;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE service_product (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(100) DEFAULT NULL,
  TYPE int(16) DEFAULT NULL,
  CSP_ID int(16) DEFAULT NULL,
  PRODUCT_ID int(16) DEFAULT NULL,
  VALID_LENGTH int(16) DEFAULT NULL,
  LENGTH_UNIT int(16) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  AUTO_PAY int(16) DEFAULT NULL,
  SEARCH_EXTRA int(16) DEFAULT NULL,
  DISCOUNT int(16) DEFAULT NULL,
  DISCOUNT_START_TIME datetime DEFAULT NULL,
  DISCOUNT_END_TIME datetime DEFAULT NULL,
  IS_FREE int(16) DEFAULT NULL,
  FREE_START_TIME datetime DEFAULT NULL,
  FREE_END_TIME datetime DEFAULT NULL,
  IS_DISPLAY_MSG int(16) DEFAULT NULL,
  MSG varchar(200) DEFAULT NULL,
  MSG_START_TIME datetime DEFAULT NULL,
  MSG_END_TIME datetime DEFAULT NULL,
  IS_DISPLAY_GIFT int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table service_product
--

LOCK TABLES service_product WRITE;
/*!40000 ALTER TABLE service_product DISABLE KEYS */;
/*!40000 ALTER TABLE service_product ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table service_product_channel
--

DROP TABLE IF EXISTS service_product_channel;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE service_product_channel (
  id int(11) NOT NULL AUTO_INCREMENT,
  service_product_id int(11) NOT NULL,
  channel_id int(11) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table service_product_channel
--

LOCK TABLES service_product_channel WRITE;
/*!40000 ALTER TABLE service_product_channel DISABLE KEYS */;
/*!40000 ALTER TABLE service_product_channel ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table service_product_gift
--

DROP TABLE IF EXISTS service_product_gift;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE service_product_gift (
  ID int(16) NOT NULL AUTO_INCREMENT,
  SERVICE_PRODUCT_ID int(16) DEFAULT NULL,
  GIFT_SERVICE_PRODUCT_ID int(16) DEFAULT NULL,
  START_TIME datetime DEFAULT NULL,
  END_TIME datetime DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table service_product_gift
--

LOCK TABLES service_product_gift WRITE;
/*!40000 ALTER TABLE service_product_gift DISABLE KEYS */;
/*!40000 ALTER TABLE service_product_gift ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table short_message_log
--

DROP TABLE IF EXISTS short_message_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE short_message_log (
  ID int(16) NOT NULL AUTO_INCREMENT,
  SN varchar(64) DEFAULT NULL,
  PHONE_NUMBER varchar(32) DEFAULT NULL,
  MESSAGE varchar(1024) DEFAULT NULL,
  START_TIME datetime DEFAULT NULL,
  STATUS int(11) DEFAULT NULL,
  SMS_IP varchar(32) DEFAULT NULL,
  LOG varchar(1024) DEFAULT NULL,
  RESPONSE_TIME datetime DEFAULT NULL,
  RESPONSE_CODE int(11) DEFAULT NULL,
  RESPONSE_IP varchar(32) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY IDX_SHORT_MESSAGE_START_TIME (START_TIME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table short_message_log
--

LOCK TABLES short_message_log WRITE;
/*!40000 ALTER TABLE short_message_log DISABLE KEYS */;
/*!40000 ALTER TABLE short_message_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table show_machine
--

DROP TABLE IF EXISTS show_machine;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE show_machine (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(255) DEFAULT NULL,
  SN varchar(255) DEFAULT NULL,
  POSITION varchar(255) DEFAULT NULL,
  TYPE int(11) DEFAULT NULL,
  STATUS int(11) DEFAULT NULL,
  TRAIN_ID int(11) DEFAULT NULL,
  TRAIN_NAME varchar(255) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table show_machine
--

LOCK TABLES show_machine WRITE;
/*!40000 ALTER TABLE show_machine DISABLE KEYS */;
INSERT INTO show_machine VALUES (1,'服务器01','01-01',NULL,1,1,1,'动车02'),(2,'媒体机01','02-01',NULL,2,1,1,'动车02');
/*!40000 ALTER TABLE show_machine ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table slb_log
--

DROP TABLE IF EXISTS slb_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE slb_log (
  ID int(16) NOT NULL AUTO_INCREMENT,
  URL varchar(1024) DEFAULT NULL,
  CLIENT_IP varchar(64) DEFAULT NULL,
  SERVER_IP varchar(64) DEFAULT NULL,
  START_TIME datetime DEFAULT NULL,
  STATUS int(11) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table slb_log
--

LOCK TABLES slb_log WRITE;
/*!40000 ALTER TABLE slb_log DISABLE KEYS */;
/*!40000 ALTER TABLE slb_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table syn_file
--

DROP TABLE IF EXISTS syn_file;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE syn_file (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(1024) DEFAULT NULL,
  URL varchar(1024) DEFAULT NULL,
  MD5 varchar(200) DEFAULT NULL,
  TYPE int(16) DEFAULT NULL,
  START_TIME datetime DEFAULT NULL,
  SP_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=2877 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table syn_file
--

LOCK TABLES syn_file WRITE;
/*!40000 ALTER TABLE syn_file DISABLE KEYS */;
/*!40000 ALTER TABLE syn_file ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table syn_task
--

DROP TABLE IF EXISTS syn_task;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE syn_task (
  ID int(16) NOT NULL AUTO_INCREMENT,
  DEVICE_ID int(16) DEFAULT NULL,
  SYN_STATUS int(16) DEFAULT NULL,
  START_POS int(16) DEFAULT NULL,
  END_POS int(16) DEFAULT NULL,
  SYN_FILE_ID int(16) DEFAULT NULL,
  START_TIME datetime DEFAULT NULL,
  SYN_LEVEL int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=2877 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table syn_task
--

LOCK TABLES syn_task WRITE;
/*!40000 ALTER TABLE syn_task DISABLE KEYS */;
/*!40000 ALTER TABLE syn_task ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table system_log
--

DROP TABLE IF EXISTS system_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE system_log (
  ID int(16) NOT NULL AUTO_INCREMENT,
  SYSTEM_LOG_ACTION varchar(255) DEFAULT NULL,
  ADMIN_ID int(16) DEFAULT NULL,
  ADMIN_IP varchar(30) DEFAULT NULL,
  LOG_TIME datetime DEFAULT NULL,
  LOG varchar(4000) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=12514 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table system_log
--

LOCK TABLES system_log WRITE;
/*!40000 ALTER TABLE system_log DISABLE KEYS */;
INSERT INTO system_log VALUES (12501,'AdminAction',1,'124.77.95.25','2015-08-08 15:52:21','用户退出系统，root,超级用户,时间：2015-08-08 15:52:21，IP：124.77.95.25'),(12502,'AdminAction',1,'124.77.95.25','2015-08-08 15:52:33','用户:root,超级用户于2015-08-08 15:52:33在124.77.95.25登录成功！'),(12503,'CarAction',1,'124.77.95.25','2015-08-08 16:38:03','保存Car(主键1)'),(12504,'CarAction',1,'124.77.95.25','2015-08-08 16:38:52','保存Car(主键1)'),(12505,'RoleAction',1,'124.77.95.25','2015-08-08 16:58:25','保存Role:车友管理'),(12506,'RoleAction',1,'124.77.95.25','2015-08-08 16:58:25','保存角色信息： 3,车友管理'),(12507,'AdminAction',1,'124.77.95.25','2015-08-08 16:59:00','保存对管理员的修改：管理员,admin'),(12508,'AdminAction',1,'124.77.95.25','2015-08-08 16:59:15','用户退出系统，root,超级用户,时间：2015-08-08 16:59:15，IP：124.77.95.25'),(12509,'AdminAction',2,'124.77.95.25','2015-08-08 16:59:24','用户:admin,管理员于2015-08-08 16:59:24在124.77.95.25登录成功！'),(12510,'AdminAction',2,'124.77.95.25','2015-08-08 16:59:38','用户退出系统，admin,管理员,时间：2015-08-08 16:59:38，IP：124.77.95.25'),(12511,'AdminAction',1,'124.77.95.25','2015-08-08 16:59:47','用户:root,超级用户于2015-08-08 16:59:47在124.77.95.25登录成功！'),(12512,'MenuAction',1,'124.77.95.25','2015-08-08 17:00:28','保存Menu:车友信息(主键2)'),(12513,'AdminAction',1,'124.77.95.25','2015-08-08 18:09:47','用户:root,超级用户于2015-08-08 18:09:47在124.77.95.25登录成功！');
/*!40000 ALTER TABLE system_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table temp_content_clip
--

DROP TABLE IF EXISTS temp_content_clip;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE temp_content_clip (
  ID int(16) NOT NULL,
  CONTENT_ID int(16) DEFAULT NULL,
  PROPERTY_ID int(16) DEFAULT NULL,
  INT_VALUE int(16) DEFAULT NULL,
  STRING_VALUE varchar(1024) DEFAULT NULL,
  DESP varchar(1024) DEFAULT NULL,
  EXTRA_DATA varchar(4000) DEFAULT NULL,
  SUBCONTENT_ID varchar(100) DEFAULT NULL,
  NAME varchar(100) DEFAULT NULL,
  THUMB_PIC varchar(2014) DEFAULT NULL,
  LENGTH int(16) DEFAULT NULL,
  EXTRA_INT int(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table temp_content_clip
--

LOCK TABLES temp_content_clip WRITE;
/*!40000 ALTER TABLE temp_content_clip DISABLE KEYS */;
/*!40000 ALTER TABLE temp_content_clip ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table temp_visit_log_error_end_time
--

DROP TABLE IF EXISTS temp_visit_log_error_end_time;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE temp_visit_log_error_end_time (
  ID int(16) NOT NULL,
  SP_ID int(16) DEFAULT NULL,
  CP_ID int(16) DEFAULT NULL,
  CHANNEL_ID int(16) DEFAULT NULL,
  CONTENT_ID int(16) DEFAULT NULL,
  CONTENT_PROPERTY_ID int(16) DEFAULT NULL,
  URL varchar(1000) DEFAULT NULL,
  USER_ID varchar(100) DEFAULT NULL,
  USER_IP varchar(20) DEFAULT NULL,
  AREA_ID int(16) DEFAULT NULL,
  IS_FREE int(16) DEFAULT NULL,
  START_TIME datetime DEFAULT NULL,
  END_TIME datetime DEFAULT NULL,
  LENGTH int(16) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  PLAY_VERSION varchar(100) DEFAULT NULL,
  USER_AGENT varchar(500) DEFAULT NULL,
  AVGBAND_WIDTH int(16) DEFAULT NULL,
  S_IP varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table temp_visit_log_error_end_time
--

LOCK TABLES temp_visit_log_error_end_time WRITE;
/*!40000 ALTER TABLE temp_visit_log_error_end_time DISABLE KEYS */;
/*!40000 ALTER TABLE temp_visit_log_error_end_time ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table template
--

DROP TABLE IF EXISTS template;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE template (
  ID int(16) NOT NULL AUTO_INCREMENT,
  NAME varchar(256) NOT NULL,
  TYPE int(11) NOT NULL,
  FILE_NAME varchar(256) NOT NULL,
  CSP_ID int(11) DEFAULT NULL,
  IS_SYSTEM int(11) DEFAULT NULL,
  PAGE_SIZE int(11) DEFAULT '10',
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table template
--

LOCK TABLES template WRITE;
/*!40000 ALTER TABLE template DISABLE KEYS */;
/*!40000 ALTER TABLE template ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table train
--

DROP TABLE IF EXISTS train;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE train (
  ID int(11) NOT NULL AUTO_INCREMENT,
  TRAIN_LINE_ID int(11) DEFAULT NULL,
  NAME varchar(255) DEFAULT NULL,
  SN varchar(255) DEFAULT NULL,
  TRAIN_CODE varchar(32) DEFAULT NULL,
  TYPE int(11) DEFAULT NULL,
  STATUS int(11) DEFAULT NULL,
  DESCRIPTION varchar(1024) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table train
--

LOCK TABLES train WRITE;
/*!40000 ALTER TABLE train DISABLE KEYS */;
INSERT INTO train VALUES (1,22,'动车02','02-02-02, 12580','G2',2,1,'执勤G1/G2线路');
/*!40000 ALTER TABLE train ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table train_line
--

DROP TABLE IF EXISTS train_line;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE train_line (
  ID int(11) NOT NULL AUTO_INCREMENT,
  PARENT_ID int(11) DEFAULT NULL,
  NAME varchar(255) DEFAULT NULL,
  PICTURE varchar(255) DEFAULT NULL,
  DESCRIPTION varchar(1024) DEFAULT NULL,
  STATUS int(11) DEFAULT NULL,
  TYPE int(11) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table train_line
--

LOCK TABLES train_line WRITE;
/*!40000 ALTER TABLE train_line DISABLE KEYS */;
/*!40000 ALTER TABLE train_line ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table user_buy
--

DROP TABLE IF EXISTS user_buy;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE user_buy (
  ID int(16) NOT NULL AUTO_INCREMENT,
  USER_ID varchar(30) DEFAULT NULL,
  USER_IP varchar(50) DEFAULT NULL,
  SP_ID int(16) DEFAULT NULL,
  CHANNEL_ID int(16) DEFAULT NULL,
  CONTENT_ID int(16) DEFAULT NULL,
  CONTENT_PROPERTY_ID int(16) DEFAULT NULL,
  SERVICE_PRODUCT_ID int(16) DEFAULT NULL,
  IS_GIFT int(16) DEFAULT NULL,
  PRICE int(16) DEFAULT NULL,
  START_TIME datetime DEFAULT NULL,
  END_TIME datetime DEFAULT NULL,
  BUY_TIME datetime DEFAULT NULL,
  AREA_ID int(11) DEFAULT NULL,
  PRODUCT_ID varchar(255) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table user_buy
--

LOCK TABLES user_buy WRITE;
/*!40000 ALTER TABLE user_buy DISABLE KEYS */;
/*!40000 ALTER TABLE user_buy ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table user_favorites
--

DROP TABLE IF EXISTS user_favorites;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE user_favorites (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CSP_ID int(16) DEFAULT NULL,
  CONTENT_ID int(16) DEFAULT NULL,
  USER_ID varchar(30) DEFAULT NULL,
  USER_IP varchar(30) DEFAULT NULL,
  TIME datetime DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=143 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table user_favorites
--

LOCK TABLES user_favorites WRITE;
/*!40000 ALTER TABLE user_favorites DISABLE KEYS */;
/*!40000 ALTER TABLE user_favorites ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table user_hot_search
--

DROP TABLE IF EXISTS user_hot_search;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE user_hot_search (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CONTENT varchar(100) DEFAULT NULL,
  CREATETIME datetime DEFAULT NULL,
  SEARCH_COUNT int(16) DEFAULT NULL,
  SEARCH_COUNT_STATUS int(16) DEFAULT NULL,
  TYPE int(16) DEFAULT NULL,
  ADMIN_ID int(16) DEFAULT NULL,
  SEARCH_WEEK_COUNT int(16) DEFAULT NULL,
  SEARCH_MONTH_COUNT int(16) DEFAULT NULL,
  UPDATE_COUNT int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table user_hot_search
--

LOCK TABLES user_hot_search WRITE;
/*!40000 ALTER TABLE user_hot_search DISABLE KEYS */;
INSERT INTO user_hot_search VALUES (1,'1','2014-12-28 00:05:01',3,0,0,0,3,3,0);
/*!40000 ALTER TABLE user_hot_search ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table user_recommand
--

DROP TABLE IF EXISTS user_recommand;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE user_recommand (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CSP_ID int(16) DEFAULT NULL,
  CONTENT_ID int(16) DEFAULT NULL,
  USER_ID varchar(30) DEFAULT NULL,
  USER_IP varchar(30) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  TIME datetime DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table user_recommand
--

LOCK TABLES user_recommand WRITE;
/*!40000 ALTER TABLE user_recommand DISABLE KEYS */;
/*!40000 ALTER TABLE user_recommand ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table user_review
--

DROP TABLE IF EXISTS user_review;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE user_review (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CSP_ID int(16) DEFAULT NULL,
  CONTENT_ID int(16) DEFAULT NULL,
  USER_ID varchar(30) DEFAULT NULL,
  USER_IP varchar(30) DEFAULT NULL,
  DESP varchar(200) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  TIME datetime DEFAULT NULL,
  REFER_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table user_review
--

LOCK TABLES user_review WRITE;
/*!40000 ALTER TABLE user_review DISABLE KEYS */;
/*!40000 ALTER TABLE user_review ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table user_review_keyword
--

DROP TABLE IF EXISTS user_review_keyword;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE user_review_keyword (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CSP_ID int(16) DEFAULT NULL,
  WORD varchar(60) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table user_review_keyword
--

LOCK TABLES user_review_keyword WRITE;
/*!40000 ALTER TABLE user_review_keyword DISABLE KEYS */;
/*!40000 ALTER TABLE user_review_keyword ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table user_review_report
--

DROP TABLE IF EXISTS user_review_report;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE user_review_report (
  ID int(16) NOT NULL AUTO_INCREMENT,
  USER_REVIEW_ID int(16) DEFAULT NULL,
  USER_IP varchar(30) DEFAULT NULL,
  DESP varchar(200) DEFAULT NULL,
  TIME datetime DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table user_review_report
--

LOCK TABLES user_review_report WRITE;
/*!40000 ALTER TABLE user_review_report DISABLE KEYS */;
/*!40000 ALTER TABLE user_review_report ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table user_scoring
--

DROP TABLE IF EXISTS user_scoring;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE user_scoring (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CSP_ID int(16) DEFAULT NULL,
  CONTENT_ID int(16) DEFAULT NULL,
  USER_ID varchar(30) DEFAULT NULL,
  USER_IP varchar(30) DEFAULT NULL,
  SCORE int(16) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  TIME datetime DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table user_scoring
--

LOCK TABLES user_scoring WRITE;
/*!40000 ALTER TABLE user_scoring DISABLE KEYS */;
/*!40000 ALTER TABLE user_scoring ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table user_type
--

DROP TABLE IF EXISTS user_type;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE user_type (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  type varchar(255) DEFAULT NULL,
  level int(11) DEFAULT NULL,
  org_id int(11) DEFAULT NULL,
  org_name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table user_type
--

LOCK TABLES user_type WRITE;
/*!40000 ALTER TABLE user_type DISABLE KEYS */;
INSERT INTO user_type VALUES (1,'院校领导',NULL,NULL,NULL,NULL),(2,'教职员工',NULL,NULL,NULL,NULL),(3,'各类学员',NULL,NULL,NULL,NULL),(4,'网络用户',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE user_type ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table usr_user
--

DROP TABLE IF EXISTS usr_user;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE usr_user (
  ID int(16) NOT NULL AUTO_INCREMENT,
  LOGIN varchar(32) DEFAULT NULL,
  BIRTHDAY datetime DEFAULT NULL,
  GERNATE varchar(2) DEFAULT NULL,
  PASSWORD varchar(64) DEFAULT NULL,
  PASSWORD_HISTORY varchar(255) DEFAULT NULL,
  USER_NAME varchar(255) DEFAULT NULL,
  EMAIL varchar(255) DEFAULT NULL,
  ADDR varchar(255) DEFAULT NULL,
  TEL varchar(64) DEFAULT NULL,
  STATUS int(11) DEFAULT NULL,
  VERIFY_CODE varchar(32) DEFAULT NULL,
  VERIFY_TIME datetime DEFAULT NULL,
  LAST_LOGIN_TIME datetime DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table usr_user
--

LOCK TABLES usr_user WRITE;
/*!40000 ALTER TABLE usr_user DISABLE KEYS */;
/*!40000 ALTER TABLE usr_user ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table usr_user_login
--

DROP TABLE IF EXISTS usr_user_login;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE usr_user_login (
  ID int(16) NOT NULL AUTO_INCREMENT,
  LOGIN varchar(32) DEFAULT NULL,
  ADDR varchar(255) DEFAULT NULL,
  TEL varchar(64) DEFAULT NULL,
  LOGIN_STATUS int(11) DEFAULT NULL,
  LOGIN_TIME datetime DEFAULT NULL,
  DESP varchar(255) DEFAULT NULL,
  AREA_ID int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table usr_user_login
--

LOCK TABLES usr_user_login WRITE;
/*!40000 ALTER TABLE usr_user_login DISABLE KEYS */;
/*!40000 ALTER TABLE usr_user_login ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table vac_log
--

DROP TABLE IF EXISTS vac_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE vac_log (
  ID int(16) NOT NULL,
  USER_ID varchar(60) DEFAULT NULL,
  PRODUCT_ID varchar(60) DEFAULT NULL,
  CHECKPRICE_MESSAGE varchar(2048) DEFAULT NULL,
  CHECKPRICE_RESP varchar(2048) DEFAULT NULL,
  CREATE_TIME datetime DEFAULT NULL,
  SP_ID varchar(60) DEFAULT NULL,
  OPERATIONTYPE int(16) DEFAULT NULL,
  RESULT_CODE int(16) DEFAULT NULL,
  FINISH_TIME datetime DEFAULT NULL,
  DURATION int(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table vac_log
--

LOCK TABLES vac_log WRITE;
/*!40000 ALTER TABLE vac_log DISABLE KEYS */;
/*!40000 ALTER TABLE vac_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table visit_day_area_log
--

DROP TABLE IF EXISTS visit_day_area_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE visit_day_area_log (
  ID int(16) NOT NULL AUTO_INCREMENT,
  SP_ID int(16) DEFAULT NULL,
  CP_ID int(16) DEFAULT NULL,
  CHANNEL_ID int(16) DEFAULT NULL,
  AREA_ID int(16) DEFAULT NULL,
  DAY datetime DEFAULT NULL,
  COUNT int(16) DEFAULT NULL,
  LENGTH int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table visit_day_area_log
--

LOCK TABLES visit_day_area_log WRITE;
/*!40000 ALTER TABLE visit_day_area_log DISABLE KEYS */;
/*!40000 ALTER TABLE visit_day_area_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table visit_day_channel_log
--

DROP TABLE IF EXISTS visit_day_channel_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE visit_day_channel_log (
  ID int(16) NOT NULL AUTO_INCREMENT,
  SP_ID int(16) DEFAULT NULL,
  CP_ID int(16) DEFAULT NULL,
  CHANNEL_ID int(16) DEFAULT NULL,
  DAY datetime DEFAULT NULL,
  COUNT int(16) DEFAULT NULL,
  LENGTH int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table visit_day_channel_log
--

LOCK TABLES visit_day_channel_log WRITE;
/*!40000 ALTER TABLE visit_day_channel_log DISABLE KEYS */;
/*!40000 ALTER TABLE visit_day_channel_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table visit_day_content_log
--

DROP TABLE IF EXISTS visit_day_content_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE visit_day_content_log (
  ID int(16) NOT NULL AUTO_INCREMENT,
  SP_ID int(16) DEFAULT NULL,
  CP_ID int(16) DEFAULT NULL,
  CHANNEL_ID int(16) DEFAULT NULL,
  CONTENT_ID int(16) DEFAULT NULL,
  DAY datetime DEFAULT NULL,
  COUNT int(16) DEFAULT NULL,
  LENGTH int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table visit_day_content_log
--

LOCK TABLES visit_day_content_log WRITE;
/*!40000 ALTER TABLE visit_day_content_log DISABLE KEYS */;
/*!40000 ALTER TABLE visit_day_content_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table visit_day_cp_log
--

DROP TABLE IF EXISTS visit_day_cp_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE visit_day_cp_log (
  ID int(16) NOT NULL AUTO_INCREMENT,
  CP_ID int(16) DEFAULT NULL,
  DAY datetime DEFAULT NULL,
  COUNT int(16) DEFAULT NULL,
  LENGTH int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table visit_day_cp_log
--

LOCK TABLES visit_day_cp_log WRITE;
/*!40000 ALTER TABLE visit_day_cp_log DISABLE KEYS */;
/*!40000 ALTER TABLE visit_day_cp_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table visit_day_sp_log
--

DROP TABLE IF EXISTS visit_day_sp_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE visit_day_sp_log (
  ID int(16) NOT NULL AUTO_INCREMENT,
  SP_ID int(16) DEFAULT NULL,
  DAY datetime DEFAULT NULL,
  COUNT int(16) DEFAULT NULL,
  LENGTH int(16) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table visit_day_sp_log
--

LOCK TABLES visit_day_sp_log WRITE;
/*!40000 ALTER TABLE visit_day_sp_log DISABLE KEYS */;
/*!40000 ALTER TABLE visit_day_sp_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table visit_log
--

DROP TABLE IF EXISTS visit_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE visit_log (
  ID int(16) NOT NULL AUTO_INCREMENT,
  SP_ID int(16) DEFAULT NULL,
  CP_ID int(16) DEFAULT NULL,
  CHANNEL_ID int(16) DEFAULT NULL,
  CONTENT_ID int(16) DEFAULT NULL,
  CONTENT_PROPERTY_ID int(16) DEFAULT NULL,
  URL varchar(1000) DEFAULT NULL,
  USER_ID varchar(100) DEFAULT NULL,
  USER_IP varchar(20) DEFAULT NULL,
  AREA_ID int(16) DEFAULT NULL,
  IS_FREE int(16) DEFAULT NULL,
  START_TIME datetime DEFAULT NULL,
  END_TIME datetime DEFAULT NULL,
  LENGTH int(16) DEFAULT NULL,
  STATUS int(16) DEFAULT NULL,
  PLAY_VERSION varchar(100) DEFAULT NULL,
  USER_AGENT varchar(500) DEFAULT NULL,
  AVGBAND_WIDTH int(16) DEFAULT NULL,
  S_IP varchar(100) DEFAULT NULL,
  BYTES_SEND int(16) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY IDX_VISIT_LOG_END_TIME (END_TIME),
  KEY IDX_VISIT_LOG_START_TIME (START_TIME),
  KEY IDX_VISIT_LOG_USERID (USER_ID),
  KEY visit_log_starttime (START_TIME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table visit_log
--

LOCK TABLES visit_log WRITE;
/*!40000 ALTER TABLE visit_log DISABLE KEYS */;
/*!40000 ALTER TABLE visit_log ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table vote
--

DROP TABLE IF EXISTS vote;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE vote (
  ID int(11) NOT NULL AUTO_INCREMENT,
  TITLE varchar(512) NOT NULL,
  CREATE_TIME datetime NOT NULL,
  START_TIME datetime DEFAULT NULL,
  END_TIME datetime DEFAULT NULL,
  QUESTION_ID int(11) DEFAULT NULL,
  STATUS int(11) NOT NULL,
  LAST_MODIFIED datetime DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY INDEX_QUESTION_ID (QUESTION_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table vote
--

LOCK TABLES vote WRITE;
/*!40000 ALTER TABLE vote DISABLE KEYS */;
/*!40000 ALTER TABLE vote ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table vote_result
--

DROP TABLE IF EXISTS vote_result;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE vote_result (
  ID int(11) NOT NULL AUTO_INCREMENT,
  VOTE_TIME datetime NOT NULL,
  VOTE_ID int(11) NOT NULL,
  USER_ID varchar(64) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY INDEX_VOTE_ID (VOTE_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table vote_result
--

LOCK TABLES vote_result WRITE;
/*!40000 ALTER TABLE vote_result DISABLE KEYS */;
/*!40000 ALTER TABLE vote_result ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table web_visit_log
--

DROP TABLE IF EXISTS web_visit_log;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE web_visit_log (
  ID int(16) NOT NULL AUTO_INCREMENT,
  SP_ID int(16) DEFAULT NULL,
  CP_ID int(16) DEFAULT NULL,
  CHANNEL_ID int(16) DEFAULT NULL,
  CONTENT_ID int(16) DEFAULT NULL,
  USER_IP varchar(20) DEFAULT NULL,
  AREA_ID int(16) DEFAULT NULL,
  VISIT_TIME datetime DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table web_visit_log
--

LOCK TABLES web_visit_log WRITE;
/*!40000 ALTER TABLE web_visit_log DISABLE KEYS */;
/*!40000 ALTER TABLE web_visit_log ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-08-09  9:38:03
