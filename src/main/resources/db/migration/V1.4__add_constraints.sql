ALTER TABLE timetable_items
  ADD CONSTRAINT FK_timetable_subject
  FOREIGN KEY (subject_id) REFERENCES subjects(id);
  
ALTER TABLE timetable_items
  ADD CONSTRAINT FK_timetable_auditory
  FOREIGN KEY (auditory_id) REFERENCES auditories(id);
  
ALTER TABLE timetable_items
  ADD CONSTRAINT FK_timetable_lecture
  FOREIGN KEY (lecture_id) REFERENCES lecturies(id);
  
ALTER TABLE timetable_items
  ADD CONSTRAINT FK_timetable_teacher
  FOREIGN KEY (teacher_id) REFERENCES teachers(person_id);

ALTER TABLE auditories
  ADD CONSTRAINT FK_auditory_type_auditory
  FOREIGN KEY (auditory_type_id) REFERENCES auditory_types(id);

ALTER TABLE departments
  ADD CONSTRAINT FK_department_faculty
  FOREIGN KEY (faculty_id) REFERENCES faculties(id);

ALTER TABLE groups
  ADD CONSTRAINT FK_groups_department
  FOREIGN KEY (department_id) REFERENCES departments(id);
