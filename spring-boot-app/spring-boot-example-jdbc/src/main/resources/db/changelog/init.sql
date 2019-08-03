CREATE TABLE USER_TYPE(ID INT NOT NULL PRIMARY KEY, TITLE VARCHAR(254));
insert into USER_TYPE values (1, 'Человек');
insert into USER_TYPE values (2, 'Хоббит');
insert into USER_TYPE values (3, 'Эльф');
insert into USER_TYPE values (4, 'Гном');
insert into USER_TYPE values (5, 'Энт');
insert into USER_TYPE values (6, 'Троль');
insert into USER_TYPE values (7, 'Гоблин (Орк)');

CREATE TABLE USER (ID BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT, LOGIN VARCHAR(254), PASSWORD VARCHAR(254), NAME VARCHAR(254), CREATE_AT DATE DEFAULT NOW(), LAST_ACCESS DATE, IS_ACTIVE INT(1), COMMENTS VARCHAR(254),
  TYPE_ID int references USER_TYPE);
INSERT INTO USER(LOGIN, PASSWORD, NAME, IS_ACTIVE, TYPE_ID) VALUES ('admin', 'admin', 'Администратор', 1, 1);
INSERT INTO USER(LOGIN, PASSWORD, NAME, IS_ACTIVE, TYPE_ID) VALUES ('guest', 'guest', 'Гость', 1, 2);

