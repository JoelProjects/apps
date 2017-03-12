INSERT INTO member_info (member_id, first_name, last_name, c_first_name, c_last_name, home_phone, cell_phone, work_phone, email) VALUES (1,'System Administrator',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO account_info (member_id, username, password) VALUES (1,'admin',"");
INSERT INTO account_role_info (account_role_id, account_role_name) VALUES (1,'system_admin');
INSERT INTO account_role_info (account_role_id, account_role_name) VALUES (2,'store_admin');
INSERT INTO account_role_info (account_role_id, account_role_name) VALUES (3,'lib_admin');
INSERT INTO account_role_info (account_role_id, account_role_name) VALUES (4,'store_staff');
INSERT INTO account_role_info (account_role_id, account_role_name) VALUES (5,'lib_staff');
INSERT INTO account_role (username, account_role_id) VALUES ('admin',1);
