DROP SCHEMA IF EXISTS prod;
CREATE SCHEMA prod;

-- Create the user_id_seq sequence
CREATE SEQUENCE user_id_seq START 1;

-- Create the 'user' table under the 'prod' schema with the user_id_seq as the default value for the id column
CREATE TABLE prod.user (
    id BIGINT DEFAULT nextval('user_id_seq') PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255)
);

-- Create the portfolio_id_seq sequence
CREATE SEQUENCE portfolio_id_seq START 1;
-- CREATE SEQUENCE portfolio_id_seq START 1;

--- Create the 'portfolio' table under the 'prod' schema with the portfolio_id_seq as the default value for the pid column
CREATE TABLE prod.portfolio (
    pid BIGINT DEFAULT nextval('portfolio_id_seq') PRIMARY KEY,
    id BIGINT REFERENCES prod.user(id),
    portfolioName VARCHAR(255),
    description TEXT,
    creationDate DATE
);

--- Create the 'portfolioStock' table under the 'prod' schema
-- CREATE TABLE prod.portfolioStock (
--     ticker VARCHAR(10),
--     pid BIGINT,
--     dateAdded DATE,
--     PRIMARY KEY (ticker, pid),
--     FOREIGN KEY (pid) REFERENCES prod.portfolio(pid)
-- );


INSERT INTO prod.user (email, username)
VALUES
    ('user1@example.com', 'user1'),
    ('user2@example.com', 'user2'),
    ('user3@example.com', 'user3');

INSERT INTO prod.portfolio (uid, portfolioName, description, creationDate)
VALUES
    (1, 'Flagship Portfolio', 'My companys flagship portfolio.', '2023-10-09'),
    (2, 'ESG Portfolio', 'This is my ESG portfolio entry.', '2023-10-09'),
    (2, 'SGX Portfolio', 'This is a special portfolio entry in Singapore!', '2023-10-09');
