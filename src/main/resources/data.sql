INSERT INTO study_status (id, type)
VALUES (1, 'Traditional'),
       (2, 'Online'),
       (3, 'Hybrid');

INSERT INTO major(id, code, full_name, description, duration)
VALUES (1, 'SE', 'Software engineering', 'Software engineering major', 4),
       (2, 'IT', 'Information technology', 'Information technology major', 4);

INSERT INTO course (id, code, full_name, points, year, semester, description)
VALUES (1, 'CS103', 'Algorithms and data structures', 8, 2, 3,
        'Course about learning algorithms and data structures in programming language Java'),
       (2, 'IT255', 'Web Systems 1', 8, 3, 5, 'Course about learning frontend framework Angular'),
       (3, 'IT355', 'Web Systems 2', 8, 3, 6, 'Course about learning backend framework Spring and Spring Boot'),
       (4, 'CS323', 'C & C++', 8, 3, 6, 'Course about learning programming languages C and C++');

INSERT INTO account(id, email, password, role)
VALUES (1, 'sasastanisic4@gmail.com', '$2a$12$IV50fkN2dG7PzTnkcvyj2urBuYzSQZ6rXi6KSG7JOn9NRZX7/.uze', 'STUDENT');

INSERT INTO student(id, name, surname, `index`, year, semester, average_grade, major_id, study_status_id, account_id)
VALUES (1, 'Sasa', 'Stanisic', 4377, 3, 6, 9.65, 1, 1, 1);