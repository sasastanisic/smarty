INSERT INTO account(id, email, password, role)
VALUES (1, 'sasastanisic4@gmail.com', '$2a$12$IV50fkN2dG7PzTnkcvyj2urBuYzSQZ6rXi6KSG7JOn9NRZX7/.uze', 'STUDENT');

INSERT INTO student(id, name, surname, `index`, year, semester, average_grade, account_id)
VALUES (1, 'Sasa', 'Stanisic', 4377, 3, 6, 9.65, 1);