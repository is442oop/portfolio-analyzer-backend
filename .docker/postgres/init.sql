DROP SCHEMA IF EXISTS prod;
DROP SEQUENCE IF EXISTS user_id_seq;
DROP SEQUENCE IF EXISTS portfolio_id_seq;
DROP SEQUENCE IF EXISTS asset_id_seq;
DROP SEQUENCE IF EXISTS portfolio_asset_id_seq;
DROP SEQUENCE IF EXISTS asset_ref_data_id_seq;
DROP TYPE IF EXISTS asset_types;

CREATE SCHEMA prod;

-- Create the user_id_seq sequence
CREATE SEQUENCE user_id_seq START 1;

-- Create the portfolio_id_seq sequence
CREATE SEQUENCE portfolio_id_seq START 1;

-- Create the asset_id_seq sequence
CREATE SEQUENCE asset_id_seq START 1;

-- Create the portfolio_asset_id_seq sequence
CREATE SEQUENCE portfolio_asset_id_seq START 1;

-- Create the asset_ref_data_id_seq sequence
CREATE SEQUENCE asset_ref_data_id_seq START 1;

-- Create the 'user' table under the 'prod' schema with the user_id_seq as the default value for the id column
CREATE TABLE prod.user (
    id BIGINT DEFAULT nextval('user_id_seq') PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255)
);

--- Create the 'portfolio' table under the 'prod' schema with the portfolio_id_seq as the default value for the pid column
CREATE TABLE prod.portfolio (
    pid BIGINT DEFAULT nextval('portfolio_id_seq') PRIMARY KEY,
    user_id BIGINT REFERENCES prod.user(id),
    portfolio_name VARCHAR(255),
    description TEXT
);

CREATE TYPE asset_types AS ENUM ('Stock', 'ETF');

--- Create the 'asset' table under the 'prod' schema with the asset_id_seq as the default value for the pid column
CREATE TABLE prod.asset (
    -- asset_id BIGINT DEFAULT nextval('asset_id_seq') PRIMARY KEY,
    asset_ticker CHAR(15) NOT NULL PRIMARY KEY,
    asset_name VARCHAR(255) NOT NULL,
    asset_description TEXT NOT NULL ,
    asset_industry VARCHAR(255) NOT NULL,
    asset_type asset_types NOT NULL
);

--- Create the 'portfolio_asset' table under the 'prod' schema with the portfolio_asset_id_seq as the default value for the pid column
CREATE TABLE prod.portfolio_asset (
    portfolio_asset_id BIGINT DEFAULT nextval('portfolio_asset_id_seq') PRIMARY KEY,
    portfolio_id BIGINT NOT NULL REFERENCES prod.portfolio(pid),
    -- asset_id BIGINT NOT NULL  REFERENCES prod.asset(asset_id),
    asset_ticker CHAR(15) NOT NULL REFERENCES prod.asset(asset_ticker),
    average_price_decimal DECIMAL NOT NULL,
    quantity INT NOT NULL,
    date_created  INT NOT NULL DEFAULT extract(epoch from now()),
    date_modified INT NOT NULL DEFAULT extract(epoch from now())
);

--- Create the 'asset_ref_data' table under the 'prod' schema with the asset_ref_data_id_seq as the default value for the pid column
-- CREATE TABLE prod.asset_ref_data (
--     asset_ref_data_id BIGINT DEFAULT nextval('asset_ref_data_id_seq') PRIMARY KEY,
--     -- asset_id BIGINT NOT NULL REFERENCES prod.asset(asset_id),
--     asset_ticker CHAR(15) NOT NULL REFERENCES prod.asset(asset_ticker),
--     day_record DATE NOT NULL,
--     adjusted_close_decimal DECIMAL NOT NULL
-- );


INSERT INTO prod.user (email, username) VALUES ('user1@example.com', 'user1');
INSERT INTO prod.user (email, username) VALUES ('user2@example.com', 'user2');
INSERT INTO prod.user (email, username) VALUES ('user3@example.com', 'user3');


INSERT INTO prod.portfolio (user_id, portfolio_name, description) VALUES (1, 'Flagship Portfolio', 'My companys flagship portfolio.');
INSERT INTO prod.portfolio (user_id, portfolio_name, description) VALUES (2, 'ESG Portfolio', 'This is my ESG portfolio entry.');
INSERT INTO prod.portfolio (user_id, portfolio_name, description) VALUES (2, 'SGX Portfolio', 'This is a special portfolio entry in Singapore!');


INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('TSLA', 'Tesla Inc', 'Tesla, Inc. is an American electric vehicle and clean energy company based in Palo Alto, California. Teslas current products include electric cars, battery energy storage from home to grid-scale, solar panels and solar roof tiles, as well as other related products and services. In 2020, Tesla had the highest sales in the plug-in and battery electric passenger car segments, capturing 16% of the plug-in market (which includes plug-in hybrids) and 23% of the battery-electric (purely electric) market. Through its subsidiary Tesla Energy, the company develops and is a major installer of solar photovoltaic energy generation systems in the United States. Tesla Energy is also one of the largest global suppliers of battery energy storage systems, with 3 GWh of battery storage supplied in 2020.', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('SPY', 'SPDR S&P 500 ETF Trust', 'SPDR S&P 500 ETF Trust', 'Finance', 'ETF');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('GOOGL', 'Alphabet Inc Class A', 'Alphabet Inc. is an American multinational conglomerate headquartered in Mountain View, California. It was created through a restructuring of Google on October 2, 2015, and became the parent company of Google and several former Google subsidiaries. The two co-founders of Google remained as controlling shareholders, board members, and employees at Alphabet. Alphabet is the worlds fourth-largest technology company by revenue and one of the worlds most valuable companies.', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('QQQ', 'Invesco QQQ Trust Series 1', 'Invesco QQQ Trust Series 1', 'Finance', 'ETF');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('MSFT', 'Microsoft Corporation', 'Microsoft Corporation is an American multinational technology company which produces computer software, consumer electronics, personal computers, and related services. Its best known software products are the Microsoft Windows line of operating systems, the Microsoft Office suite, and the Internet Explorer and Edge web browsers. Its flagship hardware products are the Xbox video game consoles and the Microsoft Surface lineup of touchscreen personal computers. Microsoft ranked No. 21 in the 2020 Fortune 500 rankings of the largest United States corporations by total revenue; it was the worlds largest software maker by revenue as of 2016. It is considered one of the Big Five companies in the U.S. information technology industry, along with Google, Apple, Amazon, and Facebook.', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('AAPL', 'Apple Inc', 'Apple Inc. is an American multinational technology company that designs, manufactures, and markets consumer electronics, computer software, and personal computers. Its most well-known products include the iPhone, iPad, Mac computers, and the Apple Watch.', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('AMZN', 'Amazon.com Inc', 'Amazon.com, Inc. is an American multinational technology and e-commerce company based in Seattle, Washington. It is one of the Big Five technology companies, and its primary business includes e-commerce, cloud computing, digital streaming, and artificial intelligence.', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('FB', 'Meta Platforms Inc', 'Meta Platforms, Inc. (formerly Facebook, Inc.) is an American technology conglomerate based in Menlo Park, California. It is known for its social media platform Facebook, as well as subsidiaries like Instagram, WhatsApp, and Oculus VR.', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('NVDA', 'NVIDIA Corporation', 'NVIDIA Corporation is an American multinational technology company incorporated in Delaware and based in Santa Clara, California. The company primarily focuses on designing graphics processing units (GPUs) for gaming and professional markets, as well as system-on-chip units for the mobile computing and automotive market.', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('NFLX', 'Netflix Inc', 'Netflix, Inc. is an American subscription-based streaming service that offers online streaming of films and television series. It is one of the most popular streaming platforms worldwide.', 'Entertainment', 'Stock');


INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity, date_created, date_modified) VALUES (1, 'AAPL', 150.75, 10, 1699147279, 1699147279); 
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity, date_created, date_modified) VALUES (1, 'GOOGL', 2750.50, 5, 1699060879, 1667059200);
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity, date_created, date_modified) VALUES (1, 'TSLA', 600.00, 15, 1699096879, 1699096879);
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity, date_created, date_modified) VALUES (1, 'AMZN', 3300.25, 7, 1698405679, 1698405679);
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity, date_created, date_modified) VALUES (2, 'FB', 330.75, 12, 1667318400, 1667318400); 
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity, date_created, date_modified) VALUES (1, 'NVDA', 275.50, 8, 1699010479, 1699010479);
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity, date_created, date_modified) VALUES (1, 'NFLX', 530.40, 9, 1698595200, 1698595200);
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity, date_created, date_modified) VALUES (2, 'AAPL', 150.75, 5, 1698837679, 1698837679);
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity, date_created, date_modified) VALUES (3, 'GOOGL', 2750.50, 10, 1667664000, 1667664000);


-- INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity, date_created, date_modified) VALUES (1, 'TSLA', 100.50, 10, 1643121600, 1643121600);
-- INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity) VALUES (2, 'QQQ', 75.25, 20);
-- INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity) VALUES (2, 'MSFT', 50.75, 15);
-- INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity) VALUES (1, 'GOOGL', 120.00, 5);
-- INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity, date_created, date_modified) VALUES (1, 'MSFT', 90.25, 8, 1668316800, 1668316800);