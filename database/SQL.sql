BEGIN;
--
-- Create model BaseUser
--
	CREATE TABLE `User` (
		`id` INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
		`email` VARCHAR ( 255 ) NOT NULL,
		`password` VARCHAR ( 255 ) NOT NULL,
		`role` VARCHAR ( 16 ) NOT NULL
	);
--
-- Create model Course
--
	CREATE TABLE `Course` (
		`id` INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
		`code` VARCHAR ( 64 ) NOT NULL,
		`name` VARCHAR ( 64 ) NOT NULL,
		`school` VARCHAR ( 64 ) NOT NULL,
		`year` VARCHAR ( 16 ) NOT NULL,
		`semester` VARCHAR ( 16 ) NOT NULL 
	);
--
-- Create model CourseManagement
--
-- 	CREATE TABLE `course_management` (
--         `id` INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY 
--     );
-- --
-- -- Create model CoursePost
-- --
-- 	CREATE TABLE `course_post` (
-- 		`id` INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
-- 		`title` VARCHAR ( 64 ) NOT NULL,
-- 		`content` LONGTEXT NOT NULL,
-- 		`creater_role` VARCHAR ( 16 ) NOT NULL,
-- 		`update_time` datetime ( 6 ) NOT NULL,
-- 		`course_id` INTEGER NOT NULL 
-- 	);
-- --
-- -- Create model CoursePostDiscussion
-- --
-- 	CREATE TABLE `course_post_discussion` ( 
--         `id` INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY, 
--         `content` LONGTEXT NOT NULL, 
--         `post_id` INTEGER NOT NULL, 
--         `user_id` INTEGER NOT NULL 
--     );
-- --
-- -- Create model CoursePostFolder
-- --
-- 	CREATE TABLE `course_post_folder` ( 
--         `id` INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY, 
--         `name` VARCHAR ( 16 ) NOT NULL, 
--         `course_id` INTEGER NOT NULL 
--     );
-- --
-- -- Create model CourseResource
-- --
-- 	CREATE TABLE `course_resource` (
-- 		`id` INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
-- 		`res_name` VARCHAR ( 64 ) NOT NULL,
-- 		`file_name` VARCHAR ( 255 ) NOT NULL,
-- 		`note` VARCHAR ( 255 ) NOT NULL,
-- 		`course_id` INTEGER NOT NULL 
-- 	);
-- --
-- -- Create model CourseStudent
-- --
-- 	CREATE TABLE `course_student` ( 
--         `id` INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY, 
--         `course_id` INTEGER NOT NULL 
--     );
-- --
-- -- Create model InstructorUser
-- --
-- 	CREATE TABLE `instructor_user` ( 
--         `id` INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY, 
--         `fullname` VARCHAR ( 64 ) NOT NULL, 
--         `baseuser_id` INTEGER NOT NULL 
--     );
-- --
-- -- Create model StudentUser
-- --
-- 	CREATE TABLE `student_user` (
-- 		`id` INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
-- 		`nickname` VARCHAR ( 64 ) NOT NULL,
-- 		`major` VARCHAR ( 64 ) NOT NULL,
-- 		`program` VARCHAR ( 16 ) NOT NULL,
-- 		`school_year` VARCHAR ( 16 ) NOT NULL,
-- 		`graduate_year` va rchar ( 16 ) NOT NULL,
-- 		`baseuser_id` INTEGER NOT NULL 
-- 	);
-- --
-- -- Add field student to coursestudent
-- --
-- 	ALTER TABLE `course_student` ADD COLUMN `student_id` INTEGER NOT NULL;
-- --
-- -- Add field folder to coursepost
-- --
-- 	ALTER TABLE `course_post` ADD COLUMN `folder_id` INTEGER NOT NULL;
-- --
-- -- Add field administrator to coursemanagement
-- --
-- 	ALTER TABLE `course_management` ADD COLUMN `administrator_id` INTEGER NOT NULL;
-- --
-- -- Add field course to coursemanagement
-- --
-- 	ALTER TABLE `course_management` ADD COLUMN `course_id` INTEGER NOT NULL;
-- --
-- -- Add field created_by to course
-- --
-- ALTER TABLE `course` ADD COLUMN `created_by_id` INTEGER NOT NULL;
-- ALTER TABLE `course_post` ADD CONSTRAINT `course_post_course_id_322782b9_fk_course_id` FOREIGN KEY ( `course_id` ) REFERENCES `course` ( `id` );
-- ALTER TABLE `course_post_discussion` ADD CONSTRAINT `course_post_discussion_post_id_77443910_fk_course_post_id` FOREIGN KEY ( `post_id` ) REFERENCES `course_post` ( `id` );
-- ALTER TABLE `course_post_discussion` ADD CONSTRAINT `course_post_discussion_publisher_id_c6ee0d0f_fk_user_id` FOREIGN KEY ( `publisher_id` ) REFERENCES `user` ( `id` );
-- ALTER TABLE `course_post_folder` ADD CONSTRAINT `course_post_folder_course_id_877364b8_fk_course_id` FOREIGN KEY ( `course_id` ) REFERENCES `course` ( `id` );
-- ALTER TABLE `course_resource` ADD CONSTRAINT `course_resource_course_id_47d8cca5_fk_course_id` FOREIGN KEY ( `course_id` ) REFERENCES `course` ( `id` );
-- ALTER TABLE `course_student` ADD CONSTRAINT `course_student_course_id_b63d402d_fk_course_id` FOREIGN KEY ( `course_id` ) REFERENCES `course` ( `id` );
-- ALTER TABLE `instructor_user` ADD CONSTRAINT `instructor_user_baseuser_id_5b88d788_fk_user_id` FOREIGN KEY ( `baseuser_id` ) REFERENCES `user` ( `id` );
-- ALTER TABLE `student_user` ADD CONSTRAINT `student_user_baseuser_id_a37845a0_fk_user_id` FOREIGN KEY ( `baseuser_id` ) REFERENCES `user` ( `id` );
-- ALTER TABLE `course_student` ADD CONSTRAINT `course_student_student_id_547f38ad_fk_student_user_id` FOREIGN KEY ( `student_id` ) REFERENCES `student_user` ( `id` );
-- ALTER TABLE `course_post` ADD CONSTRAINT `course_post_folder_id_54e37a87_fk_course_post_folder_id` FOREIGN KEY ( `folder_id` ) REFERENCES `course_post_folder` ( `id` );
-- ALTER TABLE `course_management` ADD CONSTRAINT `course_management_administrator_id_9553f468_fk_instructo` FOREIGN KEY ( `administrator_id` ) REFERENCES `instructor_user` ( `id` );
-- ALTER TABLE `course_management` ADD CONSTRAINT `course_management_course_id_4aa622c5_fk_course_id` FOREIGN KEY ( `course_id` ) REFERENCES `course` ( `id` );
-- ALTER TABLE `course` ADD CONSTRAINT `course_created_by_id_35db9350_fk_instructor_user_id` FOREIGN KEY ( `created_by_id` ) REFERENCES `instructor_user` ( `id` );
COMMIT;