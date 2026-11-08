  INSERT INTO faculties (title) VALUES
    ('faculty1'),
    ('faculty2'),
    ('faculty3'),
    ('faculty4');

  INSERT INTO departments (title, description, faculty_id) VALUES
    ('department1', 'bla bla bla 1', 1),
    ('department2', 'bla bla bla 2', 1),
    ('department3', 'bla bla bla 3', 1),
    ('department4', 'bla bla bla 4', 2),
    ('department5', 'bla bla bla 5', 2);

  INSERT INTO groups (title, department_id, yearEntry) VALUES
    ('G151', 1, 2015),
    ('G152', 1, 2015),
    ('G153', 1, 2015),
    ('G154', 1, 2015),
    ('G155', 1, 2015),
    ('G251', 2, 2015),
    ('G252', 2, 2015),
    ('G253', 2, 2015),
    ('G254', 2, 2015),
    ('G351', 3, 2015),
    ('G352', 3, 2015),
    ('G353', 3, 2015),
    ('G354', 3, 2015),
    ('G451', 4, 2015),
    ('G452', 4, 2015),
    ('G453', 4, 2015),
    ('G454', 4, 2015),
    ('G161', 1, 2016),
    ('G162', 1, 2016),
    ('G163', 1, 2016),
    ('G164', 1, 2016),
    ('G165', 1, 2016),
    ('G261', 2, 2016),
    ('G262', 2, 2016),
    ('G263', 2, 2016),
    ('G264', 2, 2016),
    ('G361', 3, 2016),
    ('G362', 3, 2016),
    ('G363', 3, 2016),
    ('G364', 3, 2016),
    ('G461', 4, 2016),
    ('G462', 4, 2016),
    ('G463', 4, 2016),
    ('G464', 4, 2016);

  INSERT INTO subjects (title) VALUES
    ('Programming'),
    ('Phisics'),
    ('Mathmatics'),
    ('Biologic');


  INSERT INTO persons (first_name, last_name, middle_name, birthday, idfees) VALUES
    ('firstname1', 'lastname1', 'middlename1', '1991-02-01', 111111111),
    ('firstname2', 'lastname2', 'middlename2', '1992-02-02', 111111112),
    ('firstname3', 'lastname3', 'middlename3', '1993-02-03', 111111113),
    ('firstname4', 'lastname4', 'middlename4', '1994-02-04', 111111114),
    ('firstname5', 'lastname5', 'middlename5', '1995-02-05', 111111115),
    ('firstname6', 'lastname6', 'middlename6', '1996-02-06', 111111116),
    ('firstname7', 'lastname7', 'middlename7', '1997-02-07', 111111117),
    ('firstname8', 'lastname8', 'middlename8', '1998-02-08', 111111118),
    ('firstname9', 'lastname9', 'middlename9', '1999-02-09', 111111119),
    ('firstname10', 'lastname10', 'middlename10', '2000-02-10', 111111120),
    ('firstNameTe1', 'lastNameTe1', 'middleNameTe1', '1980-06-25', 111111166),
    ('firstNameTe2', 'lastNameTe2', 'middleNameTe2', '1966-06-25', 211111111),
    ('firstNameTe3', 'lastNameTe3', 'middleNameTe3', '1950-06-25', 333111111),
    ('firstNameTe4', 'lastNameTe4', 'middleNameTe4', '1971-06-25', 411111333),
    ('firstNameTe5', 'lastNameTe5', 'middleNameTe5', '1975-06-25', 511111333),
    ('firstNameTe6', 'lastNameTe6', 'middleNameTe6', '1976-06-25', 611111333),
    ('firstNameTe7', 'lastNameTe7', 'middleNameTe7', '1977-06-25', 711111333),
    ('firstNameTe8', 'lastNameTe8', 'middleNameTe8', '1978-06-25', 811111333),
    ('firstNameTe9', 'lastNameTe9', 'middleNameTe9', '1979-06-25', 911111333);

  INSERT INTO students (person_id, group_id) VALUES
    (1,1),
    (2,1),
    (3,2),
    (4,2),
    (5,3),
    (6,3),
    (7,4),
    (8,4),
    (9,5),
    (10,5);

  INSERT INTO teachers (person_id, department_id) VALUES
    (11,1),
    (12,1),
    (13,2),
    (14,2),
    (15,3),
    (16,3),
    (17,4),
    (18,4),
    (19,5);

  INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES
    (11,1),
    (12,1),
    (13,2),
    (14,2),
    (15,3),
    (16,3),
    (17,3),
    (18,4),
    (19,4);

  INSERT INTO auditory_types (type) VALUES
    ('Lecture'),
    ('Practic'),
    ('Laboratory'),
    ('Lecture small'),
    ('Holl'),
    ('Recriation');

  INSERT INTO lecturies (number, start_time, end_time) VALUES
    ('1', '07:45:00', '09:20:00'),
    ('2', '09:30:00', '11:05:00'),
    ('3', '11:15:00', '12:50:00'),
    ('4', '13:10:00', '14:45:00'),
    ('5', '14:55:00', '16:30:00'),
    ('6', '16:40:00', '18:15:00');

  INSERT INTO auditories (number, auditory_type_id, capacity, description) VALUES
    ('101a', 1, 100, 'bla bla bla 1'),
    ('102a', 2, 50, 'bla bla bla 2'),
    ('201a', 3, 10, 'bla bla bla 3'),
    ('103a', 4, 30, 'bla bla bla 4'),
    ('405a', 1, 14, 'bla bla bla 5'),
    ('161a', 2, 20, 'bla bla bla 6');

  INSERT INTO timetable_items (subject_id, auditory_id, lecture_id, date, teacher_id) VALUES
    (1, 1, 1, '2020-06-25', 11),
    (2, 2, 1, '2020-06-25', 12),
    (2, 2, 2, '2020-06-25', 12),
    (3, 3, 2, '2020-06-25', 13),
    (3, 3, 3, '2020-06-25', 13),
    (4, 4, 1, '2020-06-25', 13),
    (4, 4, 2, '2020-06-25', 13),
    (1, 5, 5, '2020-06-25', 11),
    (2, 4, 5, '2020-06-25', 12),
    (3, 3, 6, '2020-06-25', 13);

  INSERT INTO groups_timetable_items (group_id, timetable_item_id) VALUES
    (1, 1),
    (1, 4),
    (1, 9),
    (2, 1),
    (2, 3),
    (2, 5),
    (2, 6),
    (3, 2),
    (3, 7),
    (4, 2),
    (4, 8),
    (4, 10);
