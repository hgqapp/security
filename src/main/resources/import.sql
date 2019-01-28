/*!40101 SET NAMES utf8 */;

-- create acl schema
DROP TABLE IF EXISTS acl_entry;
DROP TABLE IF EXISTS acl_object_identity;
DROP TABLE IF EXISTS acl_class;
DROP TABLE IF EXISTS acl_sid;
CREATE TABLE IF NOT EXISTS acl_sid (id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,principal BOOLEAN NOT NULL,sid VARCHAR(100) NOT NULL,UNIQUE KEY unique_acl_sid (sid, principal)) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS acl_class (id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,class VARCHAR(100) NOT NULL,UNIQUE KEY uk_acl_class (class)) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS acl_object_identity (id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,object_id_class BIGINT UNSIGNED NOT NULL,object_id_identity VARCHAR(36) NOT NULL,parent_object BIGINT UNSIGNED,owner_sid BIGINT UNSIGNED,entries_inheriting BOOLEAN NOT NULL,UNIQUE KEY uk_acl_object_identity (object_id_class, object_id_identity),CONSTRAINT fk_acl_object_identity_parent FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id),CONSTRAINT fk_acl_object_identity_class FOREIGN KEY (object_id_class) REFERENCES acl_class (id),CONSTRAINT fk_acl_object_identity_owner FOREIGN KEY (owner_sid) REFERENCES acl_sid (id)) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS acl_entry (id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,acl_object_identity BIGINT UNSIGNED NOT NULL,ace_order INTEGER NOT NULL,sid BIGINT UNSIGNED NOT NULL,mask INTEGER UNSIGNED NOT NULL,granting BOOLEAN NOT NULL,audit_success BOOLEAN NOT NULL,audit_failure BOOLEAN NOT NULL,UNIQUE KEY unique_acl_entry (acl_object_identity, ace_order),CONSTRAINT fk_acl_entry_object FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity (id),CONSTRAINT fk_acl_entry_acl FOREIGN KEY (sid) REFERENCES acl_sid (id)) ENGINE=InnoDB;

-- init data
insert into `users` (`user_id`, `create_time`, `email`, `enabled`, `password`, `phone`, `update_time`, `username`) values('1','1548315730636','','1','{noop}123456','','1548315730636','admin');
insert into `users` (`user_id`, `create_time`, `email`, `enabled`, `password`, `phone`, `update_time`, `username`) values('6','1548384951287','a@qq.com','1','{bcrypt}$2a$10$0zhBxWBG/imbw1dqdhj3a.BUIgGoyyEjGZ.EkS1Viuk5t5df3OxxG','13345667899','1548384951287','admin1');
insert into `users` (`user_id`, `create_time`, `email`, `enabled`, `password`, `phone`, `update_time`, `username`) values('8','1548399420563','ab@qq.com','1','{bcrypt}$2a$10$SDl/e0GI7fXzRmft94P11OZpAC5NTe3bjDzP7OOKZwe45yxuxM5zG','13345667599','1548399420563','ceshi');
