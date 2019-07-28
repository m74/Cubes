--liquibase formatted sql

--changeset Smirnov:init stripComments:false failOnError:true
--comment init database
insert into Role(id, title) values('user', 'Пользователь');
insert into Role(id, title) values('superuser', 'Администратор');
insert into User(fullName, login, password, active, createAt, uuid) values('Администратор', 'admin', '$2a$10$C2rPKy6ktX1mHL9cSUCfV.KRxc5GHdmqhf6nEplQaSSCcHwq.qS4O', 1, now(), uuid());
insert into User_Role(User_id, roles_id)  values (last_insert_id(), 'superuser');
--rollback delete from User_Role;
--rollback delete from Role;
--rollback delete from User;

