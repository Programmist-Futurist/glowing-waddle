SET NAMES utf8;

DROP DATABASE IF EXISTS final_project_db;

CREATE DATABASE final_project_db CHARACTER SET utf8 COLLATE utf8_bin;

USE final_project_db;

CREATE TABLE dictionary
(
    id  INT primary key AUTO_INCREMENT,
    eng VARCHAR(1000) NOT NULL,
    rus VARCHAR(1000) NOT NULL
);


CREATE TABLE roles
(
    id      INT primary key AUTO_INCREMENT,
    name    VARCHAR(50) UNIQUE NOT NULL,
    name_id INT UNIQUE         NOT NULL
);



CREATE TABLE users
(
    id       INT primary key AUTO_INCREMENT,
    login    VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50)        NOT NULL,
    role_id  INT                NOT NULL,
    UNIQUE (login, password)

);


CREATE TABLE students
(
    user_id       INT UNIQUE NOT NULL,
    firstName_id  INT        NOT NULL,
    lastName_id   INT        NOT NULL,
    patronymic_id INT        NOT NULL,
    email         VARCHAR(100),
    phone         VARCHAR(20),
    block         BOOLEAN    NOT NULL
);


CREATE TABLE teachers
(
    user_id       INT UNIQUE  NOT NULL,
    firstName_id  INT         NOT NULL,
    lastName_id   INT         NOT NULL,
    patronymic_id INT         NOT NULL,
    experience_id INT         NOT NULL,
    email         VARCHAR(50) NOT NULL,
    phone         VARCHAR(20)
);



CREATE TABLE statuses
(
    id      INT primary key AUTO_INCREMENT,
    name    VARCHAR(50) UNIQUE NOT NULL,
    name_id INT UNIQUE         NOT NULL
);


CREATE TABLE topics
(
    id      INT primary key AUTO_INCREMENT,
    name    VARCHAR(50) UNIQUE NOT NULL,
    name_id INT UNIQUE         NOT NULL
);



CREATE TABLE courses
(
    id                INT primary key AUTO_INCREMENT,
    teacher_id        INT        NOT NULL,

    topic_id          INT        NOT NULL,
    status_id         INT        NOT NULL,

    name_id           INT UNIQUE NOT NULL,
    description_id    INT        NOT NULL,
    duration_in_month INT        NOT NULL,
    start_date        DATE       NOT NULL,
    end_date          DATE       NOT NULL

);



CREATE TABLE journal
(
    id         INT primary key AUTO_INCREMENT,
    course_id  INT NOT NULL,
    student_id INT NOT NULL,
    mark       INT,
    UNIQUE (course_id, student_id)
);


# Dictionary
INSERT INTO dictionary
VALUES (1, 'Andew', 'Андрей');
INSERT INTO dictionary
VALUES (2, 'Sergey', 'Сергей');
INSERT INTO dictionary
VALUES (3, 'Anton', 'Антон');
INSERT INTO dictionary
VALUES (4, 'Alina', 'Алина');
INSERT INTO dictionary
VALUES (5, 'Bogdan', 'Богдан');
INSERT INTO dictionary
VALUES (6, 'Semen', 'Семен');
INSERT INTO dictionary
VALUES (7, 'Mariia', 'Мария');
INSERT INTO dictionary
VALUES (8, 'Olga', 'Ольга');
INSERT INTO dictionary
VALUES (9, 'Dmitriy', 'Дмитрий');
INSERT INTO dictionary
VALUES (10, 'Stas', 'Стас');
INSERT INTO dictionary
VALUES (11, 'Elena', 'Елена');
INSERT INTO dictionary
VALUES (12, 'Sofia', 'София');
INSERT INTO dictionary
VALUES (13, 'Dasha', 'Даша');
INSERT INTO dictionary
VALUES (14, 'Mihail', 'Михаил');
INSERT INTO dictionary
VALUES (15, 'Evgeniy', 'Евгений');

INSERT INTO dictionary
VALUES (16, 'Not started', 'Не начался');
INSERT INTO dictionary
VALUES (17, 'Processing', 'В процессе');
INSERT INTO dictionary
VALUES (18, 'Finished', 'Закончился');

INSERT INTO dictionary
VALUES (19, 'Programming', 'Программирование');
INSERT INTO dictionary
VALUES (20, 'Self-development', 'Саморазвитие');

INSERT INTO dictionary
VALUES (21, '4 years', '4 года"');
INSERT INTO dictionary
VALUES (22, '5 years', '5 года"');
INSERT INTO dictionary
VALUES (23, '6 years', '6 года"');

INSERT INTO dictionary
VALUES (24, 'Perfect course on programming, here you will learn Java with the best teachers in our city!',
        'Отличный курс по программированию, здась вы будете изучать Java с лучшими преподавателями нашего города!');
INSERT INTO dictionary
VALUES (25,
        'Perfect course on self-development, here you will realize your spiritual world with the best teachers in our city!',
        'Отличный курс по саморазвитию, здась вы позаете свой духовный мир с лучшими учителями нашей страны!');

INSERT INTO dictionary
VALUES (26, 'Java beginning', 'Java начало');
INSERT INTO dictionary
VALUES (27, 'Python beginning', 'Python начало');
INSERT INTO dictionary
VALUES (28, 'Web Design', 'Веб дизайн');
INSERT INTO dictionary
VALUES (29, 'How to achieve your goals', 'Как достигать поставленных целей');
INSERT INTO dictionary
VALUES (30, 'How to motivate yourself', 'Как мотивировать себя');
INSERT INTO dictionary
VALUES (31, '1 month', '1 месяц');
INSERT INTO dictionary
VALUES (32, '2 month', '2 месяца');
INSERT INTO dictionary
VALUES (33, '3 month', '3 месяца');
INSERT INTO dictionary
VALUES (34, '4 month', '4 месяца');

INSERT INTO dictionary
VALUES (35, 'Andreev', 'Андреев');
INSERT INTO dictionary
VALUES (36, 'Andreevich', 'Андреевич');
INSERT INTO dictionary
VALUES (37, 'Sergeev', 'Сергеев');
INSERT INTO dictionary
VALUES (38, 'Sergeevich', 'Сергеевич');
INSERT INTO dictionary
VALUES (39, 'Antonov', 'Антонов');
INSERT INTO dictionary
VALUES (40, 'Antonovich', 'Антонович');
INSERT INTO dictionary
VALUES (41, 'Alinova', 'Алинова');
INSERT INTO dictionary
VALUES (42, 'Alinovna', 'Алиновна');
INSERT INTO dictionary
VALUES (43, 'Bogdanov', 'Богданов');
INSERT INTO dictionary
VALUES (44, 'Bogdanovich', 'Богданович');
INSERT INTO dictionary
VALUES (45, 'Semenov', 'Семенов');
INSERT INTO dictionary
VALUES (46, 'Semenovich', 'Семенович');
INSERT INTO dictionary
VALUES (47, 'Marieva', 'Мариева');
INSERT INTO dictionary
VALUES (48, 'Marievna', 'Мариевна');
INSERT INTO dictionary
VALUES (49, 'Olgova', 'Ольгова');
INSERT INTO dictionary
VALUES (50, 'Olgovna', 'Ольговна');
INSERT INTO dictionary
VALUES (51, 'Dmitriev', 'Дмитриев');
INSERT INTO dictionary
VALUES (52, 'Dmitrievich', 'Дмитриевич');
INSERT INTO dictionary
VALUES (53, 'Stasov', 'Стасов');
INSERT INTO dictionary
VALUES (54, 'Stasovich', 'Стасович');
INSERT INTO dictionary
VALUES (55, 'Elenova', 'Еленова');
INSERT INTO dictionary
VALUES (56, 'Elenovna', 'Еленовна');
INSERT INTO dictionary
VALUES (57, 'Sofieva', 'Софиева');
INSERT INTO dictionary
VALUES (58, 'Sofievna', 'Софиевна');
INSERT INTO dictionary
VALUES (59, 'Dashova', 'Дашова');
INSERT INTO dictionary
VALUES (60, 'Dashovna', 'Дашовна');
INSERT INTO dictionary
VALUES (61, 'Mihailov', 'Михайлов');
INSERT INTO dictionary
VALUES (62, 'Mahailovich', 'Михайлович');
INSERT INTO dictionary
VALUES (63, 'Evgeniev', 'Евгеньев');
INSERT INTO dictionary
VALUES (64, 'Evgenievich', 'Евгеньевич');
INSERT INTO dictionary

VALUES (65, 'Mark', 'Марк');
INSERT INTO dictionary
VALUES (66, 'Markov', 'Марков');
INSERT INTO dictionary
VALUES (67, 'Markovich', 'Маркович');
INSERT INTO dictionary
VALUES (68, 'Natoha', 'Натона');
INSERT INTO dictionary
VALUES (69, 'Natohov', 'Натохов');
INSERT INTO dictionary
VALUES (70, 'Natohovich', 'Натохович');
INSERT INTO dictionary
VALUES (71, 'Artem', 'Артем');
INSERT INTO dictionary
VALUES (72, 'Artemov', 'Артемов');
INSERT INTO dictionary
VALUES (73, 'Artemovich', 'Артемович');
INSERT INTO dictionary
VALUES (74, 'Vitaliy', 'Виталий');
INSERT INTO dictionary
VALUES (75, 'Vitaliev', 'Витальев');
INSERT INTO dictionary
VALUES (76, 'Vitalievich', 'Витальевич');
INSERT INTO dictionary
VALUES (78, 'Java advanced', 'Java продвинутый');
INSERT INTO dictionary
VALUES (79, 'Admin', 'Админ');
INSERT INTO dictionary
VALUES (80, 'Not admin', 'Не админ');


# roles
ALTER TABLE roles
    ADD CONSTRAINT FOREIGN KEY (name_id)
        REFERENCES dictionary (id) ON DELETE CASCADE ON UPDATE RESTRICT;
INSERT INTO roles
VALUES (DEFAULT, 'ADMIN', 79);
INSERT INTO roles
VALUES (DEFAULT, 'NOT_ADMIN', 80);


# users
INSERT INTO users
VALUES (1, 'login1@gmail.com', 'password1', 1);
INSERT INTO users
VALUES (2, 'login2@gmail.com', 'password2', 1);
INSERT INTO users
VALUES (3, 'login3@gmail.com', 'password3', 2);
INSERT INTO users
VALUES (4, 'login4@gmail.com', 'password4', 2);
INSERT INTO users
VALUES (5, 'login5@gmail.com', 'password5', 2);
INSERT INTO users
VALUES (6, 'login6@gmail.com', 'password6', 2);
INSERT INTO users
VALUES (7, 'login7@gmail.com', 'password7', 2);
INSERT INTO users
VALUES (8, 'login8@gmail.com', 'password8', 2);
INSERT INTO users
VALUES (9, 'login9@gmail.com', 'password9', 2);
INSERT INTO users
VALUES (10, 'login10@gmail.com', 'password10', 2);
INSERT INTO users
VALUES (11, 'login11@gmail.com', 'password11', 2);
INSERT INTO users
VALUES (12, 'login12@gmail.com', 'password12', 2);
INSERT INTO users
VALUES (13, 'login13@gmail.com', 'password13', 2);
INSERT INTO users
VALUES (14, 'login14@gmail.com', 'password14', 2);
INSERT INTO users
VALUES (15, 'login15@gmail.com', 'password15', 2);
INSERT INTO users
VALUES (16, 'login16@gmail.com', 'password16', 2);
INSERT INTO users
VALUES (17, 'login17@gmail.com', 'password17', 2);
INSERT INTO users
VALUES (18, 'login18@gmail.com', 'password18', 2);
INSERT INTO users
VALUES (19, 'login19@gmail.com', 'password19', 2);


# students
ALTER TABLE students
    ADD CONSTRAINT FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE students
    ADD CONSTRAINT FOREIGN KEY (firstName_id)
        REFERENCES dictionary (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE students
    ADD CONSTRAINT FOREIGN KEY (lastName_id)
        REFERENCES dictionary (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE students
    ADD CONSTRAINT FOREIGN KEY (patronymic_id)
        REFERENCES dictionary (id) ON DELETE CASCADE ON UPDATE RESTRICT;
INSERT INTO students
VALUES (4, 4, 41, 42, 'login4@gmail.com', '+380954823727', false);
INSERT INTO students
VALUES (5, 5, 43, 44, 'login5@gmail.com', '+380954823727', false);
INSERT INTO students
VALUES (6, 6, 45, 46, 'login6@gmail.com', '+380954823727', false);
INSERT INTO students
VALUES (7, 7, 47, 48, 'login7@gmail.com', '+380954823727', false);
INSERT INTO students
VALUES (8, 8, 49, 50, 'login8@gmail.com', '+380954823727', false);
INSERT INTO students
VALUES (9, 9, 51, 52, 'login9@gmail.com', '+380954823727', false);
INSERT INTO students
VALUES (10, 10, 53, 54, 'login10@gmail.com', '+380954823727', false);
INSERT INTO students
VALUES (11, 11, 55, 56, 'login11@gmail.com', '+380954823727', false);
INSERT INTO students
VALUES (12, 12, 57, 58, 'login12@gmail.com', '+380954823727', false);
INSERT INTO students
VALUES (13, 13, 59, 60, 'login13@gmail.com', '+380954823727', false);
INSERT INTO students
VALUES (14, 14, 61, 62, 'login14@gmail.com', '+380954823727', false);
INSERT INTO students
VALUES (15, 15, 63, 64, 'login15@gmail.com', '+380954823727', false);
INSERT INTO students
VALUES (16, 65, 66, 67, 'login16@gmail.com', '+380954823727', false);
INSERT INTO students
VALUES (17, 68, 69, 70, 'login17@gmail.com', '+380954823727', false);
INSERT INTO students
VALUES (18, 71, 72, 73, 'login18@gmail.com', '+380954823727', false);
INSERT INTO students
VALUES (19, 74, 75, 76, 'login19@gmail.com', '+380954823727', false);


# teachers
ALTER TABLE teachers
    ADD CONSTRAINT FOREIGN KEY (firstName_id)
        REFERENCES dictionary (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE teachers
    ADD CONSTRAINT FOREIGN KEY (lastName_id)
        REFERENCES dictionary (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE teachers
    ADD CONSTRAINT FOREIGN KEY (patronymic_id)
        REFERENCES dictionary (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE teachers
    ADD CONSTRAINT FOREIGN KEY (experience_id)
        REFERENCES dictionary (id) ON DELETE CASCADE ON UPDATE RESTRICT;

INSERT INTO teachers
VALUES (1, 1, 35, 36, 21, 'login1@gmail.com', '+380954823727');
INSERT INTO teachers
VALUES (2, 2, 37, 38, 22, 'login2@gmail.com', '+380954823728');
INSERT INTO teachers
VALUES (3, 3, 39, 40, 23, 'login3@gmail.com', '+380954823729');


# statuses
ALTER TABLE statuses
    ADD CONSTRAINT FOREIGN KEY (name_id)
        REFERENCES dictionary (id) ON DELETE CASCADE ON UPDATE RESTRICT;
INSERT INTO statuses
VALUES (DEFAULT, 'NOT_STARTED', 16);
INSERT INTO statuses
VALUES (DEFAULT, 'PROCESSING', 17);
INSERT INTO statuses
VALUES (DEFAULT, 'FINISHED', 18);


# topics
ALTER TABLE topics
    ADD CONSTRAINT FOREIGN KEY (name_id)
        REFERENCES dictionary (id) ON DELETE CASCADE ON UPDATE RESTRICT;
INSERT INTO topics
VALUES (DEFAULT, 'PROGRAMMING', 19);
INSERT INTO topics
VALUES (DEFAULT, 'SELF_DEVELOPMENT', 20);


# courses
ALTER TABLE courses
    ADD CONSTRAINT FOREIGN KEY (teacher_id)
        REFERENCES teachers (user_id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE courses
    ADD CONSTRAINT FOREIGN KEY (topic_id)
        REFERENCES topics (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE courses
    ADD CONSTRAINT FOREIGN KEY (status_id)
        REFERENCES statuses (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE courses
    ADD CONSTRAINT FOREIGN KEY (name_id)
        REFERENCES dictionary (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE courses
    ADD CONSTRAINT FOREIGN KEY (description_id)
        REFERENCES dictionary (id) ON DELETE CASCADE ON UPDATE RESTRICT;


INSERT INTO courses
VALUES (1, 1, 1, 1, 26, 24, 1, '2020.06.05', '2020.07.12');
INSERT INTO courses
VALUES (2, 2, 1, 2, 27, 24, 2, '2020.05.12', '2020.07.14');
INSERT INTO courses
VALUES (3, 3, 2, 3, 29, 24, 4, '2019.07.03', '2019.11.03');
INSERT INTO courses
VALUES (4, 1, 1, 2, 78, 24, 3, '2020.04.03', '2020.07.03');


# journal
ALTER TABLE journal
    ADD CONSTRAINT FOREIGN KEY (student_id)
        REFERENCES students (user_id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE journal
    ADD CONSTRAINT FOREIGN KEY (course_id)
        REFERENCES courses (id) ON DELETE CASCADE ON UPDATE RESTRICT;

INSERT INTO journal
VALUES (1, 1, 4, 10);
INSERT INTO journal
VALUES (2, 1, 5, 8);
INSERT INTO journal
VALUES (3, 1, 6, 9);
INSERT INTO journal
VALUES (4, 1, 7, 10);
INSERT INTO journal
VALUES (5, 2, 8, 11);
INSERT INTO journal
VALUES (6, 2, 9, 12);
INSERT INTO journal
VALUES (7, 2, 10, 18);
INSERT INTO journal
VALUES (8, 2, 11, 11);
INSERT INTO journal
VALUES (9, 3, 12, 10);
INSERT INTO journal
VALUES (10, 3, 13, 10);
INSERT INTO journal
VALUES (11, 3, 14, 11);
INSERT INTO journal
VALUES (12, 3, 15, 9);
INSERT INTO journal
VALUES (13, 3, 6, 10);