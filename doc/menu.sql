delete from menu;
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('1','车辆管理','/cars/','-1',null,'0','1','car');
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('2','车友信息','/cars/cars.jsp','-1','/cars/car!list.action;/cars/car!save.action;/cars/cars.jsp;/cars/car!delete.action;/cars/carView.jsp;/config/dict!list.action;/cars/car!view.action','1','1',null);
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('11','系统管理','/sys/','-1',null,'0','1','cog|background-color:#feb448;');
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('12','字典管理','/sys/dict.jsp','-1','/sys/dict.jsp','11','1',null);
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('17','管理员管理','/sys/admin.jsp','-1','/security/admin!saveAdmin.action;/security/admin!lock.action;/security/admin!searchAdmin.action;/sys/admin.jsp;/man.jsp;/security/admin!removeAdmin.action;/security/role!list.action','11','1',null);
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('19','系统个性化','#','-1',null,'11','10',null);
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('20','超户专属','/su/','-1',null,'0','1','user|background-color:#9564e2;');
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('21','安全日志','/su/logs.jsp','-1','/su/logs.jsp;/system/systemLog!list.action;/man.jsp','20','1',null);
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('22','角色管理','/su/roles.jsp','-1','/security/role!listRoles.action;/security/role!delete.action;/su/roles.jsp;/security/role!save.action;/security/role!view.action','20','1',null);
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('23','功能管理','/su/menus.jsp','-1','/security/menu!list.action;/security/menu!delete.action;/security/menu!view.action;/security/menu!save.action;/security/menu!listFolderMenus.action;/su/menus.jsp;/man.jsp','20','1',null);
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('24','前台登录日志','/su/userLoginLogs.jsp','-1','/su/userLoginLogs.jsp;/user/userlogin!list.action','20','10',null);
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('25','检查录入','/cars/carConduct.jsp','0','/cars/car!list.action;/cars/conductList.jsp;/conduct/conduct!view.action;/cars/carConduct.jsp;/conduct/conduct!list.action;/conduct/conduct!save.action;/cars/car!view.action;/cars/conductView.jsp','1','1',null);
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('26','维修方案','/repair/repairList.jsp?type=2','0','/repair/repairView.jsp;/repair/repair!list.action;/repair/repair!save.action;/repair/repair!view.action;/repair/repairList.jsp;/man.jsp;/repair/repair!delete.action','28','1',null);
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('27','事故维修方案','/repair/repairList.jsp?type=3','0','/repair/repairView.jsp;/repair/repair!list.action;/repair/repair!save.action;/repair/repair!view.action;/repair/repairList.jsp;/man.jsp;/repair/repair!delete.action','28','1',null);
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('28','维修管理','/repair/','0',null,'0','1','ambulance');
insert into menu(ID,NAME,URL,PERMISSION_ID,PERMISSION_STR,PARENT_ID,STATUS,STYLE)values('29','养护方案','/repair/repairList.jsp?type=1','0','/repair/repairView.jsp;/repair/repair!list.action;/repair/repair!save.action;/repair/repair!view.action;/repair/repairList.jsp;/man.jsp;/repair/repair!delete.action','28','1','car');
delete from role;
insert into role(ID,NAME,MEMO,TYPE)values('1','超户专属',null,null);
insert into role(ID,NAME,MEMO,TYPE)values('2','维修管理员',null,null);
insert into role(ID,NAME,MEMO,TYPE)values('3','车友管理',null,null);
insert into role(ID,NAME,MEMO,TYPE)values('4','系统管理','系统维护操作','3');
DELETE  from role_menu;
insert into role_menu(ID,ROLE_ID,MENU_ID)values('1','1','21');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('2','1','22');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('3','1','23');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('4','1','24');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('13','4','12');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('14','4','13');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('15','4','14');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('16','4','15');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('17','4','16');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('18','4','17');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('19','4','18');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('20','4','19');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('76','2','26');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('77','2','27');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('78','1','25');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('79','3','2');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('80','3','12');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('81','3','25');
insert into role_menu(ID,ROLE_ID,MENU_ID)values('82','2','29');
commit;