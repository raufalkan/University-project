CREATE TABLE IF NOT EXISTS auditories 
  (
     id SERIAL PRIMARY KEY,
     number VARCHAR(20) NOT NULL UNIQUE,
     auditory_type_id INT,
     capacity INT,
     description VARCHAR(200)
  );

CREATE TABLE IF NOT EXISTS auditory_types 
  (
     id SERIAL PRIMARY KEY,
     type VARCHAR(45) NOT NULL UNIQUE
  );

CREATE TABLE IF NOT EXISTS lecturies
  (
     id SERIAL PRIMARY KEY,
     number VARCHAR(5) NOT NULL UNIQUE,
     start_time TIME,
     end_time TIME
  );
  
CREATE TABLE IF NOT EXISTS faculties
  (
     id SERIAL PRIMARY KEY,
     title VARCHAR(20) NOT NULL UNIQUE
  );
  
CREATE TABLE IF NOT EXISTS departments
  (
     id SERIAL PRIMARY KEY,
     title VARCHAR(20) NOT NULL UNIQUE,
     description VARCHAR(200),
     faculty_id INT,
     FOREIGN KEY (faculty_id) REFERENCES faculties (id)
  );
  
CREATE TABLE IF NOT EXISTS groups
  (
     id SERIAL PRIMARY KEY,
     title VARCHAR(20) NOT NULL UNIQUE,
     department_id INT,
     yearEntry DATE
  );

CREATE TABLE IF NOT EXISTS persons
  (
     id SERIAL PRIMARY KEY,
     first_name VARCHAR(20) NOT NULL,
     last_name VARCHAR(20),
     middle_name VARCHAR(20),
     birthday DATE,
     idfees INT NOT NULL UNIQUE
  );
  
CREATE TABLE IF NOT EXISTS students
  (
     person_id INT PRIMARY KEY,
     group_id INT,
     FOREIGN KEY (person_id) REFERENCES persons (id) ON DELETE CASCADE
  );
  
CREATE TABLE IF NOT EXISTS teachers
  (
     person_id INT PRIMARY KEY,
     department_id INT,
     FOREIGN KEY (person_id) REFERENCES persons (id) ON DELETE CASCADE
  );
  
CREATE TABLE IF NOT EXISTS subjects
  (
     id SERIAL PRIMARY KEY,
     title VARCHAR(45) NOT NULL UNIQUE
  );
  
CREATE TABLE IF NOT EXISTS timetable_items
  (
     id SERIAL PRIMARY KEY,
     subject_id INT,
     auditory_id INT,
     lecture_id INT,
     date DATE,
     teacher_id INT
  );
  
CREATE TABLE IF NOT EXISTS groups_timetable_items
  (
     group_id INT,
     timetable_item_id INT,
     CONSTRAINT PK_groups_timetable_item PRIMARY KEY (group_id,timetable_item_id),
     FOREIGN KEY (group_id) REFERENCES groups (id),
     FOREIGN KEY (timetable_item_id) REFERENCES timetable_items (id)
  );
  
CREATE TABLE IF NOT EXISTS teachers_subjects
  (
     subject_id INT,
     teacher_id INT,
     CONSTRAINT PK_teachers_subjects PRIMARY KEY (subject_id,teacher_id),
     FOREIGN KEY (subject_id) REFERENCES subjects (id),
     FOREIGN KEY (teacher_id) REFERENCES teachers (person_id)
  );
