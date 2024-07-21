/* Setting up PAYBUDDY DB */
drop database if exists paybuddy ;

create database paybuddy;
use paybuddy;



create table u_user(
u_id int PRIMARY KEY AUTO_INCREMENT,    
u_email varchar(20) NOT NULL UNIQUE,
u_password varchar(70) NOT NULL,
u_account_balance float(8),
u_self_account varchar(25) NOT NULL,
u_role varchar(12) DEFAULT("USER"));

create table t_transaction(
 t_id int PRIMARY KEY AUTO_INCREMENT,
 t_debited_account int NOT NULL,
 t_credited_account int NOT NULL,
 t_amount float(8) NOT NULL,
 t_description varchar(30) NOT NULL);

 create table b_beneficiary_user(
 b_fk_id_user int,
 b_fk_id_beneficiary int,
 PRIMARY KEY(b_fk_id_user,b_fk_id_beneficiary),
 FOREIGN KEY (b_fk_id_user)
 REFERENCES u_user(u_id) ON DELETE CASCADE,
 FOREIGN KEY (b_fk_id_beneficiary)
 REFERENCES u_user(u_id) ON DELETE CASCADE);

insert into u_user(u_email,u_password,u_account_balance,u_self_account) values('ben@gmail.com','$2a$10$8bnNdaKTiLM80qkCjJBePuYpCiO.3Db.MI602u3lDyPq3YsLXSMyO',1000.50,'ROBO BANK 000111');
insert into u_user(u_email,u_password,u_account_balance,u_self_account) values('lola@gmail.com','$2a$10$5DPuFlMrLq551aWUs7TSOu/2rQuTJjy390w.Qtcj43THkbmzeO0wO',2000.89,'CMB BANK 000222');
insert into u_user(u_email,u_password,u_account_balance,u_self_account) values('greg@gmail.com','$2a$10$Ejtklk/hcN2OfE7HPauFPucY92c6j2YbolIqkqIS486H2Gqbe/dn.',3000.13,'CL BANK 000333');
insert into u_user(u_email,u_password,u_account_balance,u_self_account) values('aria@gmail.com','$2a$10$qUEqMNx6InrnlazunpJapO9NB5l3QTSbS4f94PKuglCEImvrrgBD6',4000.99,'BNP BANK 000444');
insert into b_beneficiary_user(b_fk_id_user,b_fk_id_beneficiary) values(1,2);
insert into b_beneficiary_user(b_fk_id_user,b_fk_id_beneficiary) values(1,3);
insert into b_beneficiary_user(b_fk_id_user,b_fk_id_beneficiary) values(2,3);
insert into b_beneficiary_user(b_fk_id_user,b_fk_id_beneficiary) values(2,4);
insert into b_beneficiary_user(b_fk_id_user,b_fk_id_beneficiary) values(3,1);
insert into b_beneficiary_user(b_fk_id_user,b_fk_id_beneficiary) values(4,3);
insert into t_transaction(t_debited_account,t_credited_account,t_amount,t_description) values(1,2,19,'remboursement cinema');
insert into t_transaction(t_debited_account,t_credited_account,t_amount,t_description) values(3,2,45,'dette jeu');
commit;

