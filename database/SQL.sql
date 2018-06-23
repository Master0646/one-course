BEGIN;
--
-- Create model BaseUser
--
CREATE TABLE "user" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "email" varchar(255) NOT NULL, "password" varchar(255) NOT NULL, "role"
 varchar(16) NOT NULL, "school" varchar(64) NOT NULL, "ps" text NULL);
--
-- Create model Course
--
CREATE TABLE "course" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "school" varchar(64) NOT NULL, "name" varchar(64) NOT NULL, "year" va
rchar(16) NOT NULL, "semester" varchar(16) NOT NULL);
--
-- Create model CourseManagement
--
CREATE TABLE "course_management" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT);
--
-- Create model CoursePost
--
CREATE TABLE "course_post" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "title" varchar(64) NOT NULL, "content" text NOT NULL, "publishe
r_role" varchar(16) NOT NULL, "update_time" datetime NOT NULL, "course_id" integer NOT NULL REFERENCES "course" ("id") DEFERRABLE INITIALLY D
EFERRED);
--
-- Create model CoursePostDiscussion
--
CREATE TABLE "course_post_discussion" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "content" text NOT NULL, "post_id" integer NOT NULL R
EFERENCES "course_post" ("id") DEFERRABLE INITIALLY DEFERRED, "publisher_id" integer NOT NULL REFERENCES "user" ("id") DEFERRABLE INITIALLY D
EFERRED);
--
-- Create model CoursePostFolder
--
CREATE TABLE "course_post_folder" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "name" varchar(16) NOT NULL, "course_id" integer NOT NULL
 REFERENCES "course" ("id") DEFERRABLE INITIALLY DEFERRED);
--
-- Create model CourseResource
--
CREATE TABLE "course_resource" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "res_name" varchar(64) NOT NULL, "file_name" varchar(255) NO
T NULL, "note" varchar(255) NOT NULL, "course_id" integer NOT NULL REFERENCES "course" ("id") DEFERRABLE INITIALLY DEFERRED);
--
-- Create model CourseStudent
--
CREATE TABLE "course_student" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "course_id" integer NOT NULL REFERENCES "course" ("id") DEFER
RABLE INITIALLY DEFERRED);
--
-- Create model InstructorUser
--
CREATE TABLE "instructor_user" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "fullname" varchar(64) NOT NULL, "baseuser_id" integer NOT N
ULL REFERENCES "user" ("id") DEFERRABLE INITIALLY DEFERRED);
--
-- Create model StudentUser
--
CREATE TABLE "student_user" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "nickname" varchar(64) NOT NULL, "major" varchar(64) NOT NULL,
"program" varchar(16) NOT NULL, "school_year" varchar(16) NOT NULL, "graduate_year" varchar(16) NOT NULL, "baseuser_id" integer NOT NULL REFE
RENCES "user" ("id") DEFERRABLE INITIALLY DEFERRED);
--
-- Add field student to coursestudent
--
ALTER TABLE "course_student" RENAME TO "course_student__old";
CREATE TABLE "course_student" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "course_id" integer NOT NULL REFERENCES "course" ("id") DEFER
RABLE INITIALLY DEFERRED, "student_id" integer NOT NULL REFERENCES "student_user" ("id") DEFERRABLE INITIALLY DEFERRED);
INSERT INTO "course_student" ("id", "course_id", "student_id") SELECT "id", "course_id", NULL FROM "course_student__old";
DROP TABLE "course_student__old";
CREATE INDEX "course_post_course_id_322782b9" ON "course_post" ("course_id");
CREATE INDEX "course_post_discussion_post_id_77443910" ON "course_post_discussion" ("post_id");
CREATE INDEX "course_post_discussion_publisher_id_c6ee0d0f" ON "course_post_discussion" ("publisher_id");
CREATE INDEX "course_post_folder_course_id_877364b8" ON "course_post_folder" ("course_id");
CREATE INDEX "course_resource_course_id_47d8cca5" ON "course_resource" ("course_id");
CREATE INDEX "instructor_user_baseuser_id_5b88d788" ON "instructor_user" ("baseuser_id");
CREATE INDEX "student_user_baseuser_id_a37845a0" ON "student_user" ("baseuser_id");
CREATE INDEX "course_student_course_id_b63d402d" ON "course_student" ("course_id");
CREATE INDEX "course_student_student_id_547f38ad" ON "course_student" ("student_id");
--
-- Add field folder to coursepost
--
ALTER TABLE "course_post" RENAME TO "course_post__old";
CREATE TABLE "course_post" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "title" varchar(64) NOT NULL, "content" text NOT NULL, "publishe
r_role" varchar(16) NOT NULL, "update_time" datetime NOT NULL, "course_id" integer NOT NULL REFERENCES "course" ("id") DEFERRABLE INITIALLY D
EFERRED, "folder_id" integer NOT NULL REFERENCES "course_post_folder" ("id") DEFERRABLE INITIALLY DEFERRED);
INSERT INTO "course_post" ("id", "title", "content", "publisher_role", "update_time", "course_id", "folder_id") SELECT "id", "title", "conten
t", "publisher_role", "update_time", "course_id", NULL FROM "course_post__old";
DROP TABLE "course_post__old";
CREATE INDEX "course_post_course_id_322782b9" ON "course_post" ("course_id");
CREATE INDEX "course_post_folder_id_54e37a87" ON "course_post" ("folder_id");
--
-- Add field administrator to coursemanagement
--
ALTER TABLE "course_management" RENAME TO "course_management__old";
CREATE TABLE "course_management" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "administrator_id" integer NOT NULL REFERENCES "instructor
_user" ("id") DEFERRABLE INITIALLY DEFERRED);
INSERT INTO "course_management" ("id", "administrator_id") SELECT "id", NULL FROM "course_management__old";
DROP TABLE "course_management__old";
CREATE INDEX "course_management_administrator_id_9553f468" ON "course_management" ("administrator_id");
--
-- Add field course to coursemanagement
--
ALTER TABLE "course_management" RENAME TO "course_management__old";
CREATE TABLE "course_management" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "administrator_id" integer NOT NULL REFERENCES "instructor
_user" ("id") DEFERRABLE INITIALLY DEFERRED, "course_id" integer NOT NULL REFERENCES "course" ("id") DEFERRABLE INITIALLY DEFERRED);
INSERT INTO "course_management" ("id", "administrator_id", "course_id") SELECT "id", "administrator_id", NULL FROM "course_management__old";
DROP TABLE "course_management__old";
CREATE INDEX "course_management_administrator_id_9553f468" ON "course_management" ("administrator_id");
CREATE INDEX "course_management_course_id_4aa622c5" ON "course_management" ("course_id");
--
-- Add field created_by to course
--
ALTER TABLE "course" RENAME TO "course__old";
CREATE TABLE "course" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "school" varchar(64) NOT NULL, "name" varchar(64) NOT NULL, "year" va
rchar(16) NOT NULL, "semester" varchar(16) NOT NULL, "created_by_id" integer NOT NULL REFERENCES "instructor_user" ("id") DEFERRABLE INITIALL
Y DEFERRED);
INSERT INTO "course" ("id", "school", "name", "year", "semester", "created_by_id") SELECT "id", "school", "name", "year", "semester", NULL FR
OM "course__old";
DROP TABLE "course__old";
CREATE INDEX "course_created_by_id_35db9350" ON "course" ("created_by_id");
COMMIT;
