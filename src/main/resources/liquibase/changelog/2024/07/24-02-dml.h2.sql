-- liquibase formatted sql

-- changeset white-rabbit:1721903258412-1
INSERT INTO USERS (USERNAME, PASSWORD, ROLE)
VALUES ( 'admin', '$2a$10$zzeIm.2649lKo9HHvB7u7.eV6fLmVVp9TVfmBQyGxeG2CoS1w.xcO', 'ROLE_LIBRARIAN' ),
       ( 'user1', '$2y$10$frt3n1gViWmQ/SU9m8IZ.uGextZ8g63QdWOCtRzRRubwU1mOVgWvm', 'ROLE_USER' ),
       ( 'user2', '$2y$10$u/RGz7bmVnumeSjN4thC5e/F1TkEh.MZn7I3nsovcrV/oE7E51zWG', 'ROLE_USER' );

-- changeset white-rabbit:1721903258412-2
INSERT INTO BOOKS (ISBN, TITLE, AUTHOR, CATEGORY)
VALUES ( '9781848940871', 'IT', 'Stephen King', 'HORROR' ),
       ( '9780553801477', 'A Dance with Dragons', 'George R.R. Martin', 'FANTASY' ),
       ( '9788363471415', 'Solaris', 'Stanisław Lem', 'SCIENCE_FICTION' ),
       ( '9780007491261', 'The Iron King', 'Maurice Druon', 'HISTORICAL_FICTION' ),
       ( '9780809590469', 'Brave New World', 'Aldous Huxley', 'DYSTOPIAN' ),
       ( '9780195805758', 'Robinson Crusoe', 'Daniel Defoe', 'ADVENTURE' );
