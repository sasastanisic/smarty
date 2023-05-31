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
VALUES (1, 'sasastanisic4@gmail.com', '$2a$12$IV50fkN2dG7PzTnkcvyj2urBuYzSQZ6rXi6KSG7JOn9NRZX7/.uze', 'STUDENT'),
       (2, 'jovanajovic@metropolitan.ac.rs', '$2a$12$XUSPqyH0Mlkgazq5Sy7.X.phGWW9jzRMUOkRlCdSwT5RH33SWXxge',
        'PROFESSOR'),
       (3, 'tamaravukadinovic@metropolitan.ac.rs', '$2a$12$lnHL2rNkmVXXVutHrfyWJO0QwI9jHPw0e6zKqkPPPr4axvMa0kWqa',
        'ASSISTANT');

INSERT INTO student(id, name, surname, `index`, year, semester, average_grade, major_id, study_status_id, account_id)
VALUES (1, 'Sasa', 'Stanisic', 4377, 3, 6, 9.65, 1, 1, 1);

INSERT INTO professor(id, name, surname, years_of_experience, account_id)
VALUES (1, 'Jovana', 'Jovic', 7, 2),
       (2, 'Tamara', 'Vukadinovic', 3, 3);

INSERT INTO engagement(id, professor_id, course_id)
VALUES (1, 2, 1),
       (2, 1, 3),
       (3, 2, 3),
       (4, 2, 4);