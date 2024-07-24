-- Drop tables if they exist
DROP TABLE IF EXISTS `NOTIFICATION`;
DROP TABLE IF EXISTS `ACHIEVEMENT`;
DROP TABLE IF EXISTS `N_SCHEDULE_DETAIL`;
DROP TABLE IF EXISTS `N_SCHEDULE`;
DROP TABLE IF EXISTS `F_SCHEDULE_DETAIL`;
DROP TABLE IF EXISTS `F_SCHEDULE`;
DROP TABLE IF EXISTS `V_SCHEDULE`;
DROP TABLE IF EXISTS `SCHEDULE`;
DROP TABLE IF EXISTS `BOARD_ATTACHMENT`;
DROP TABLE IF EXISTS `REPLY_HEARTS`;
DROP TABLE IF EXISTS `BOARD_SCRAP`;
DROP TABLE IF EXISTS `BOARD_HEARTS`;
DROP TABLE IF EXISTS `REPLY`;
DROP TABLE IF EXISTS `BOARD`;
DROP TABLE IF EXISTS `MEMBER_ROLE`;
DROP TABLE IF EXISTS `MEMBER`;

-- 회원
CREATE TABLE `MEMBER`
(
    `member_id`  bigint       NOT NULL AUTO_INCREMENT,
    `username`   varchar(20)  NOT NULL,
    `nickname`   varchar(40)  NOT NULL,
    `email`      varchar(80)  NOT NULL UNIQUE,
    `password`   varchar(100) NOT NULL,
    `gender`     ENUM('MALE', 'FEMALE') NOT NULL,
    `mode`       ENUM('MILD', 'SPICY') NOT NULL,
    `image_file` varchar(255) NULL,
    `created_at`  datetime(6) NULL,
    `created_by` varchar(80) NULL,
    `updated_at` datetime(6) NULL,
    `updated_by` varchar(80) NULL,
    `score` DOUBLE NULL DEFAULT 0,
    `phone_number` varchar(11) NOT NULL UNIQUE,
    `auth_email` boolean NULL DEFAULT false,
    `auth_phone` boolean NULL DEFAULT false,
    PRIMARY KEY (`member_id`)
);

CREATE TABLE `MEMBER_ROLE`
(
    `member_role_id` bigint NOT NULL AUTO_INCREMENT,
    `member_id`      bigint NOT NULL,
    `role`           ENUM('ADMIN', 'NORMAL_USER', 'PUNCTUAL_USER') NOT NULL DEFAULT 'NORMAL_USER',
    `created_at`     datetime(6) NULL,
    `created_by`     varchar(80) NULL,
    `updated_at`     datetime(6) NULL,
    `updated_by`     varchar(80) NULL,
    PRIMARY KEY (`member_role_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

-- 게시판, 댓글
CREATE TABLE `BOARD`
(
    `board_id`    bigint       NOT NULL AUTO_INCREMENT,
    `member_id`   bigint       NOT NULL,
    `title`       varchar(255) NOT NULL,
    `content`     TEXT         NOT NULL,
    `created_at`  datetime(6)   NULL,
    `updated_at`  datetime(6)   NULL,
    `total_like`  bigint       NOT NULL DEFAULT 0,
    `total_scrap` bigint       NOT NULL DEFAULT 0,
    PRIMARY KEY (`board_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `REPLY`
(
    `reply_id`   bigint NOT NULL AUTO_INCREMENT,
    `member_id`  bigint NOT NULL,
    `board_id`   bigint NOT NULL,
    `parent_id`  bigint NULL,
    `content`    TEXT   NOT NULL,
    `created_at`  datetime(6)   NULL,
    `updated_at` datetime(6)    NULL,
    PRIMARY KEY (`reply_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`),
    FOREIGN KEY (`board_id`) REFERENCES `BOARD` (`board_id`),
    FOREIGN KEY (`parent_id`) REFERENCES `REPLY` (`reply_id`)
);

CREATE TABLE `BOARD_HEARTS`
(
    `board_hearts_id` bigint NOT NULL AUTO_INCREMENT,
    `board_id`        bigint NOT NULL,
    `member_id`       bigint NOT NULL,
    PRIMARY KEY (`board_hearts_id`),
    FOREIGN KEY (`board_id`) REFERENCES `BOARD` (`board_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `BOARD_SCRAP`
(
    `board_scrap_id` bigint NOT NULL AUTO_INCREMENT,
    `board_id`       bigint NOT NULL,
    `member_id`      bigint NOT NULL,
    PRIMARY KEY (`board_scrap_id`),
    FOREIGN KEY (`board_id`) REFERENCES `BOARD` (`board_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `REPLY_HEARTS`
(
    `reply_hearts_id` bigint NOT NULL AUTO_INCREMENT,
    `reply_id`        bigint NOT NULL,
    `member_id`       bigint NOT NULL,
    PRIMARY KEY (`reply_hearts_id`),
    FOREIGN KEY (`reply_id`) REFERENCES `REPLY` (`reply_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `BOARD_ATTACHMENT`
(
    `board_attachment_id` bigint       NOT NULL AUTO_INCREMENT,
    `board_id`            bigint       NOT NULL,
    `file_name`           varchar(255) NOT NULL,
    PRIMARY KEY (`board_attachment_id`),
    FOREIGN KEY (`board_id`) REFERENCES `BOARD` (`board_id`)
);

-- 일정
CREATE TABLE `SCHEDULE`
(
    `schedule_id` bigint       NOT NULL AUTO_INCREMENT,
    `member_id`   bigint       NOT NULL,
    `title`       varchar(255) NOT NULL,
    `common_description` TEXT NOT NULL,
    `start_date`  datetime(6)    NOT NULL,
    `end_date`    datetime(6)    NOT NULL,
    `created_at`  datetime(6) NULL,
    `created_by`  varchar(80) NULL,
    `updated_at`  datetime(6) NULL,
    `updated_by`  varchar(80) NULL,
    `dtype` varchar(40) NOT NULL,
    PRIMARY KEY (`schedule_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

CREATE TABLE `N_SCHEDULE`
(
    `schedule_id`   bigint      NOT NULL,
    `category_unit` ENUM('PAGE', 'CHAPTER','LECTURE','PROJECT','WORKOUT','NONE') NOT NULL DEFAULT 'NONE',
    `total_amount`         int      NULL,
    `buffer_time`   TIME        NOT NULL DEFAULT '00:00:00',
    PRIMARY KEY (`schedule_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `SCHEDULE` (`schedule_id`)
);

CREATE TABLE `F_SCHEDULE`
(
    `schedule_id` bigint      NOT NULL,
    PRIMARY KEY (`schedule_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `SCHEDULE` (`schedule_id`)
);

CREATE TABLE `V_SCHEDULE`
(
    `schedule_id`   bigint     NOT NULL,
    `complete_status` boolean    NOT NULL DEFAULT false,
    PRIMARY KEY (`schedule_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `SCHEDULE` (`schedule_id`)
);

CREATE TABLE `N_SCHEDULE_DETAIL`
(
    `n_schedule_detail_id` bigint     NOT NULL AUTO_INCREMENT,
    `schedule_id`          bigint     NOT NULL,
    `start_date`           datetime(6)       NOT NULL,
    `end_date`             datetime(6)       NOT NULL,
    `complete_status`      boolean    NOT NULL DEFAULT false,
    `detail_description`   TEXT NULL,
    `daily_amount`               double NULL,
    `updated_by`           varchar(80) NULL,
    `updated_at`           datetime(6) NOT NULL,
    PRIMARY KEY (`n_schedule_detail_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `N_SCHEDULE` (`schedule_id`)
);

CREATE TABLE `F_SCHEDULE_DETAIL`
(
    `f_schedule_detail_id` bigint     NOT NULL AUTO_INCREMENT,
    `schedule_id`          bigint     NOT NULL,
    `complete_status`      boolean    NOT NULL DEFAULT false,
    `detail_description`   TEXT NULL,
    `start_date`           datetime(6)       NOT NULL,
    `end_date`             datetime(6)       NOT NULL,
    `updated_by`           varchar(80) NULL,
    `updated_at`           datetime(6) NOT NULL,
    PRIMARY KEY (`f_schedule_detail_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `F_SCHEDULE` (`schedule_id`)
);

-- 성취도
CREATE TABLE `ACHIEVEMENT`
(
    `achievement_id`   bigint NOT NULL AUTO_INCREMENT,
    `member_id`        bigint NOT NULL,
    `achievement_date` date   NOT NULL,
    `achievement_rate` double NOT NULL,
    `created_at`        datetime(6) NULL,
    `updated_at`       datetime(6) NULL,
    `created_by`       varchar(80) NULL,
    `updated_by`       varchar(80) NULL,
    PRIMARY KEY (`achievement_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

-- 알림
CREATE TABLE `NOTIFICATION`
(
    `notification_id` bigint  NOT NULL AUTO_INCREMENT,
    `member_id`       bigint  NOT NULL,
    `message`         TEXT    NOT NULL,
    `is_read`         boolean NOT NULL DEFAULT false,
    `entity_id`       bigint NULL,
    `created_at`    datetime(6) NULL,
    `type`            varchar(255) NULL COMMENT 'null 이면 일반 메시지',
    PRIMARY KEY (`notification_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`member_id`)
);

-- MEMBER 테이블에 데이터 삽입
INSERT INTO MEMBER (username, nickname, email, password, gender, mode, image_file, created_at, created_by, updated_at, updated_by, score, phone_number, auth_email, auth_phone)
VALUES
    ('user1', 'nick1', 'user1@example.com', '$2a$12$vVyp1MKvgHaS68VKu/gyjeaFqHiXzKiu8Cq5A8jeoLZzHM900.0X2', 'MALE', 'MILD', NULL, NOW(), 'system', NOW(), 'system', 10, '01012345678', true, false),
    ('user2', 'nick2', 'user2@example.com', '$2a$12$u2/pqTqGrM1RnV2EE7Js5Oi4ObvmED284iPiFlOw20ATRKbfuCvq2', 'FEMALE', 'SPICY', NULL, NOW(), 'system', NOW(), 'system', 20, '01023456789', true, true),
    ('user3', 'nick3', 'user3@example.com', '$2a$12$G/1E6IsBkKxQyCUGChLUG.AtUgKyn.eQlGs1HV3uCxlnsXHMvIdRK', 'MALE', 'MILD', NULL, NOW(), 'system', NOW(), 'system', 30, '01034567890', false, true);

-- MEMBER_ROLE 테이블에 데이터 삽입
INSERT INTO MEMBER_ROLE (member_id, role, created_at, created_by, updated_at, updated_by)
VALUES
    (1, 'NORMAL_USER', NOW(), 'system', NOW(), 'system'),
    (2, 'ADMIN', NOW(), 'system', NOW(), 'system'),
    (3, 'PUNCTUAL_USER', NOW(), 'system', NOW(), 'system');

-- BOARD 테이블에 데이터 삽입
INSERT INTO BOARD (member_id, title, content, created_at, updated_at, total_like, total_scrap)
VALUES
    (1, 'First Board Post', 'This is the content of the first board post.', NOW(), NOW(), 5, 2),
    (2, 'Second Board Post', 'This is the content of the second board post.', NOW(), NOW(), 3, 1),
    (3, 'Third Board Post', 'This is the content of the third board post.', NOW(), NOW(), 4, 0);

-- REPLY 테이블에 데이터 삽입
INSERT INTO REPLY (member_id, board_id, parent_id, content, created_at, updated_at)
VALUES
    (1, 1, NULL, 'This is a comment on the first board post.', NOW(), NOW()),
    (2, 1, 1, 'This is a reply to the first comment.', NOW(), NOW()),
    (3, 2, NULL, 'This is a comment on the second board post.', NOW(), NOW());

-- BOARD_HEARTS 테이블에 데이터 삽입
INSERT INTO BOARD_HEARTS (board_id, member_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 1);

-- BOARD_SCRAP 테이블에 데이터 삽입
INSERT INTO BOARD_SCRAP (board_id, member_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3);

-- REPLY_HEARTS 테이블에 데이터 삽입
INSERT INTO REPLY_HEARTS (reply_id, member_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3);

-- BOARD_ATTACHMENT 테이블에 데이터 삽입
INSERT INTO BOARD_ATTACHMENT (board_id, file_name)
VALUES
    (1, 'attachment1.jpg'),
    (2, 'attachment2.jpg'),
    (3, 'attachment3.jpg');

-- SCHEDULE 테이블에 데이터 삽입
INSERT INTO SCHEDULE (member_id, title, common_description, start_date, end_date, created_at, created_by, dtype, updated_at, updated_by)
VALUES
    (1, 'fixed schedule1', 'Description of the first schedule.', '2024-01-01 00:00:00.000000', '2024-01-07 00:00:00.000000', NOW(), 'system', 'F', NOW(), 'system'),
    (1, 'fixed schedule2', 'Description of the second schedule.', '2024-02-01 00:00:00.000000', '2024-02-14 00:00:00.000000', NOW(), 'system', 'F', NOW(), 'system'),
    (1, 'fixed schedule3', 'Description of the third schedule.', '2024-03-01 00:00:00.000000', '2024-03-21 00:00:00.000000', NOW(), 'system', 'F', NOW(), 'system'),
    (2, 'normal schedule1', 'Description of the fourth schedule.', '2024-05-01 00:00:00.000000', '2024-05-07 00:00:00.000000', NOW(), 'system', 'N', NOW(), 'system'),
    (2, 'normal schedule2', 'Description of the fifth schedule.', '2024-06-01 00:00:00.000000', '2024-06-14 00:00:00.000000', NOW(), 'system', 'N', NOW(), 'system'),
    (2, 'normal schedule3', 'Description of the sixth schedule.', '2024-07-01 00:00:00.000000', '2024-07-22 00:00:00.000000', NOW(), 'system', 'N', NOW(), 'system'),
    (3, 'variable Schedule1', 'Description of the seventh schedule.', '2024-08-01 00:00:00.000000', '2024-08-01 00:00:00.000000', NOW(), 'system', 'V', NOW(), 'system'),
    (3, 'variable Schedule2', 'Description of the eighth schedule.', '2024-09-01 00:00:00.000000', '2024-09-01 00:00:00.000000', NOW(), 'system', 'V', NOW(), 'system'),
    (3, 'variable Schedule3', 'Description of the ninth schedule.', '2024-10-01 00:00:00.000000', '2024-10-01 00:00:00.000000', NOW(), 'system', 'V', NOW(), 'system');

INSERT INTO N_SCHEDULE (schedule_id,buffer_time,total_amount)
VALUES
    (4,'02:00:00',100),
    (5,'01:00:00',200),
    (6,'00:30:00',300);
-- N_SCHEDULE_DETAIL 테이블에 데이터 삽입
INSERT INTO N_SCHEDULE_DETAIL (schedule_id, start_date, end_date, complete_status, detail_description, updated_by, updated_at, daily_amount)
VALUES
    (4, '2024-05-01 09:00:00', '2024-05-01 10:00:00', false, 'Detail of the first N_SCHEDULE.', 'system', NOW(),20),
    (4, '2024-05-02 10:00:00', '2024-05-02 11:00:00', false, 'Detail of the first N_SCHEDULE.', 'system', NOW(),20),
    (4, '2024-05-03 11:00:00', '2024-05-03 12:00:00', false, 'Detail of the first N_SCHEDULE.', 'system', NOW(),20),
    (4, '2024-05-04 12:00:00', '2024-05-04 13:00:00', false, 'Detail of the first N_SCHEDULE.', 'system', NOW(),20),
    (4, '2024-05-05 13:00:00', '2024-05-05 14:00:00', false, 'Detail of the first N_SCHEDULE.', 'system', NOW(),20),
    (5, '2024-06-01 09:00:00', '2024-06-01 10:00:00', false, 'Detail of the second N_SCHEDULE.', 'system', NOW(),40),
    (5, '2024-06-02 10:00:00', '2024-06-02 11:00:00', false, 'Detail of the second N_SCHEDULE.', 'system', NOW(),40),
    (5, '2024-06-03 11:00:00', '2024-06-03 12:00:00', false, 'Detail of the second N_SCHEDULE.', 'system', NOW(),40),
    (5, '2024-06-04 12:00:00', '2024-06-04 13:00:00', false, 'Detail of the second N_SCHEDULE.', 'system', NOW(),40),
    (5, '2024-06-05 13:00:00', '2024-06-05 14:00:00', false, 'Detail of the second N_SCHEDULE.', 'system', NOW(),40),
    (6, '2024-07-01 09:00:00', '2024-07-01 10:00:00', false, 'Detail of the third N_SCHEDULE.', 'system', NOW(),60),
    (6, '2024-07-02 10:00:00', '2024-07-02 11:00:00', false, 'Detail of the third N_SCHEDULE.', 'system', NOW(),60),
    (6, '2024-07-03 11:00:00', '2024-07-03 12:00:00', false, 'Detail of the third N_SCHEDULE.', 'system', NOW(),60),
    (6, '2024-07-04 12:00:00', '2024-07-04 13:00:00', false, 'Detail of the third N_SCHEDULE.', 'system', NOW(),60),
    (6, '2024-07-05 13:00:00', '2024-07-05 14:00:00', false, 'Detail of the third N_SCHEDULE.', 'system', NOW(),60);

-- F_SCHEDULE 테이블에 데이터 삽입
INSERT INTO F_SCHEDULE (schedule_id)
VALUES
    (1),
    (2),
    (3);

-- F_SCHEDULE_DETAIL 테이블에 데이터 삽입
INSERT INTO F_SCHEDULE_DETAIL (schedule_id, complete_status, detail_description, updated_by, updated_at,start_date,end_date)
VALUES
    (1, false, 'Detail of the first F_SCHEDULE.', 'system', NOW(),'2024-01-01 10:00:00.000000','2024-01-01 14:00:00.000000'),
    (1, false, 'Detail of the first F_SCHEDULE.', 'system', NOW(),'2024-01-01 17:30:00.000000','2024-01-01 18:00:00.000000'),
    (1, false, 'Detail of the first F_SCHEDULE.', 'system', NOW(),'2024-01-01 21:30:00.000000','2024-01-01 22:00:00.000000'),
    (1, false, 'Detail of the first F_SCHEDULE.', 'system', NOW(),'2024-01-03 10:00:00.000000','2024-01-03 14:00:00.000000'),
    (1, false, 'Detail of the first F_SCHEDULE.', 'system', NOW(),'2024-01-04 10:00:00.000000','2024-01-04 14:00:00.000000'),
    (1, false, 'Detail of the first F_SCHEDULE.', 'system', NOW(),'2024-01-05 10:00:00.000000','2024-01-05 14:00:00.000000'),
    (2, false, 'Detail of the second F_SCHEDULE.', 'system', NOW(),'2024-02-01 10:00:00.000000','2024-02-01 14:00:00.000000'),
    (2, false, 'Detail of the second F_SCHEDULE.', 'system', NOW(),'2024-02-08 10:00:00.000000','2024-02-08 14:00:00.000000'),
    (2, false, 'Detail of the second F_SCHEDULE.', 'system', NOW(),'2024-02-14 10:00:00.000000','2024-02-14 14:00:00.000000'),
    (2, false, 'Detail of the second F_SCHEDULE.', 'system', NOW(),'2024-02-03 10:00:00.000000','2024-02-03 14:00:00.000000'),
    (2, false, 'Detail of the second F_SCHEDULE.', 'system', NOW(),'2024-02-10 10:00:00.000000','2024-02-10 14:00:00.000000'),
    (3, false, 'Detail of the third F_SCHEDULE.', 'system', NOW(),'2024-03-01 10:00:00.000000','2024-03-01 14:00:00.000000'),
    (3, false, 'Detail of the third F_SCHEDULE.', 'system', NOW(),'2024-03-08 10:00:00.000000','2024-03-08 14:00:00.000000'),
    (3, false, 'Detail of the third F_SCHEDULE.', 'system', NOW(),'2024-03-14 10:00:00.000000','2024-03-14 14:00:00.000000'),
    (3, false, 'Detail of the third F_SCHEDULE.', 'system', NOW(),'2024-03-21 10:00:00.000000','2024-03-21 14:00:00.000000');


-- V_SCHEDULE 테이블에 데이터 삽입
INSERT INTO V_SCHEDULE (schedule_id, complete_status)
VALUES
    (7, false),
    (8, false),
    (9, false);

-- ACHIEVEMENT 테이블에 데이터 삽입
INSERT INTO ACHIEVEMENT (member_id, achievement_date, achievement_rate, created_at, updated_at, created_by, updated_by)
VALUES
    (1, '2024-01-01', 90, NOW(), NOW(), 'system', 'system'),
    (2, '2024-01-02', 80, NOW(), NOW(), 'system', 'system'),
    (3, '2024-01-03', 70, NOW(), NOW(), 'system', 'system');

-- CAT_UNIT_ACH 테이블에 데이터 삽입
INSERT INTO CAT_UNIT_ACH (member_id, category_unit, achievement_rate, created_at, updated_at, created_by, updated_by)
VALUES
    (1, 'PAGE', 90, NOW(), NOW(), 'system', 'system'),
    (1, 'CHAPTER', 80, NOW(), NOW(), 'system', 'system'),
    (1, 'LECTURE', 70, NOW(), NOW(), 'system', 'system');

-- NOTIFICATION 테이블에 데이터 삽입
INSERT INTO NOTIFICATION (member_id, message, is_read, entity_id, created_at, type)
VALUES
    (1, 'You have a new message!', false, NULL, NOW(), NULL),
    (2, 'Your schedule is updated.', false, 1, NOW(), 'schedule'),
    (3, 'Your post received a new comment.', false, 2, NOW(), 'comment');