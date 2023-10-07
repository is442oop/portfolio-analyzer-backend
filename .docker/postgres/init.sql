CREATE SCHEMA prod;

-- Create the user_id_seq sequence
CREATE SEQUENCE user_id_seq START 1;

-- Create the 'user' table under the 'prod' schema with the user_id_seq as the default value for the id column
CREATE TABLE prod.user (
    id BIGINT DEFAULT nextval('user_id_seq') PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255)
);

INSERT INTO prod.user (email, username) VALUES ('user1@example.com', 'user1');
INSERT INTO prod.user (email, username) VALUES ('user2@example.com', 'user2');
INSERT INTO prod.user (email, username) VALUES ('user3@example.com', 'user3');
