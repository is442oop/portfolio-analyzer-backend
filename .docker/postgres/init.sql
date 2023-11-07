DROP SCHEMA IF EXISTS prod;
DROP SEQUENCE IF EXISTS portfolio_id_seq;
DROP SEQUENCE IF EXISTS asset_id_seq;
DROP SEQUENCE IF EXISTS portfolio_asset_id_seq;
DROP SEQUENCE IF EXISTS asset_ref_data_id_seq;
DROP TYPE IF EXISTS asset_types;

CREATE SCHEMA prod;

-- Create the portfolio_id_seq sequence
CREATE SEQUENCE portfolio_id_seq START 1;

-- Create the asset_id_seq sequence
CREATE SEQUENCE asset_id_seq START 1;

-- Create the portfolio_asset_id_seq sequence
CREATE SEQUENCE portfolio_asset_id_seq START 1;

-- Create the asset_ref_data_id_seq sequence
CREATE SEQUENCE asset_ref_data_id_seq START 1;

-- Create the watchlist_id_seq sequence
CREATE SEQUENCE watchlist_id_seq START 1;

-- Create the 'user' table under the 'prod' schema with the user_id_seq as the default value for the id column
CREATE TABLE prod.user (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255)
);

--- Create the 'portfolio' table under the 'prod' schema with the portfolio_id_seq as the default value for the pid column
CREATE TABLE prod.portfolio (
    pid BIGINT DEFAULT nextval('portfolio_id_seq') PRIMARY KEY,
    user_id VARCHAR(36) REFERENCES prod.user(id),
    portfolio_name VARCHAR(255),
    description TEXT
);

CREATE TYPE asset_types AS ENUM ('Stock', 'ETF');

--- Create the 'asset' table under the 'prod' schema with the asset_id_seq as the default value for the pid column
CREATE TABLE prod.asset (
    -- asset_id BIGINT DEFAULT nextval('asset_id_seq') PRIMARY KEY,
    asset_ticker VARCHAR(15) NOT NULL PRIMARY KEY,
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
    asset_ticker VARCHAR(15) NOT NULL REFERENCES prod.asset(asset_ticker),
    price DECIMAL NOT NULL,
    quantity INT NOT NULL,
    date_created  INT NOT NULL DEFAULT extract(epoch from now()),
    date_modified INT NOT NULL DEFAULT extract(epoch from now())
);

-- Create the 'watchlist' table under the 'prod' schema with the watchlist_id_seq as the default value for the watchlist_id column
CREATE TABLE prod.watchlist (
    watchlist_id BIGINT DEFAULT nextval('watchlist_id_seq') PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES prod.user(id),
    watchlist_assets VARCHAR(15)[] NOT NULL
);

--- Create the 'asset_ref_data' table under the 'prod' schema with the asset_ref_data_id_seq as the default value for the pid column
-- CREATE TABLE prod.asset_ref_data (
--     asset_ref_data_id BIGINT DEFAULT nextval('asset_ref_data_id_seq') PRIMARY KEY,
--     -- asset_id BIGINT NOT NULL REFERENCES prod.asset(asset_id),
--     asset_ticker CHAR(15) NOT NULL REFERENCES prod.asset(asset_ticker),
--     day_record DATE NOT NULL,
--     adjusted_close_decimal DECIMAL NOT NULL
-- );

INSERT INTO prod.user (id, email, username) VALUES ('d988bdd8-e569-4026-970a-dd6c286ebe6d', 'test@test.com', 'user1');
INSERT INTO prod.user (id, email, username) VALUES ('bfda515e-8e06-41c8-b157-56fc1b7ee301', 'test2@test.com', 'user2');
INSERT INTO prod.user (id, email, username) VALUES ('a3c7dc58-4d0f-4fa3-9271-c851073d6371', 'test3@test.com', 'user3');


INSERT INTO prod.portfolio (user_id, portfolio_name, description) VALUES ('d988bdd8-e569-4026-970a-dd6c286ebe6d', 'Flagship Portfolio', 'My companys flagship portfolio.');
INSERT INTO prod.portfolio (user_id, portfolio_name, description) VALUES ('bfda515e-8e06-41c8-b157-56fc1b7ee301', 'ESG Portfolio', 'This is my ESG portfolio entry.');
INSERT INTO prod.portfolio (user_id, portfolio_name, description) VALUES ('bfda515e-8e06-41c8-b157-56fc1b7ee301', 'SGX Portfolio', 'This is a special portfolio entry in Singapore!');


INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('SPY', 'SPDR S&P 500 ETF Trust', 'SPDR S&P 500 ETF Trust', 'Finance', 'ETF');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('QQQ', 'Invesco QQQ Trust Series 1', 'Invesco QQQ Trust Series 1', 'Finance', 'ETF');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('AAPL', 'Apple Inc', 'Apple Inc. is an American multinational technology company that designs, manufactures, and markets consumer electronics, computer software, and personal computers. Its most well-known products include the iPhone, iPad, Mac computers, and the Apple Watch.', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('MSFT', 'Microsoft Corporation', 'Microsoft Corporation is an American multinational technology company which produces computer software, consumer electronics, personal computers, and related services. Its best known software products are the Microsoft Windows line of operating systems, the Microsoft Office suite, and the Internet Explorer and Edge web browsers. Its flagship hardware products are the Xbox video game consoles and the Microsoft Surface lineup of touchscreen personal computers. Microsoft ranked No. 21 in the 2020 Fortune 500 rankings of the largest United States corporations by total revenue; it was the worlds largest software maker by revenue as of 2016. It is considered one of the Big Five companies in the U.S. information technology industry, along with Google, Apple, Amazon, and Facebook.', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('GOOGL', 'Alphabet Inc Class A', 'Alphabet Inc. is an American multinational conglomerate headquartered in Mountain View, California. It was created through a restructuring of Google on October 2, 2015, and became the parent company of Google and several former Google subsidiaries. The two co-founders of Google remained as controlling shareholders, board members, and employees at Alphabet. Alphabet is the world''s fourth-largest technology company by revenue and one of the worlds most valuable companies.', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('AMZN', 'Amazon.com Inc', 'Amazon.com, Inc. is an American multinational technology and e-commerce company based in Seattle, Washington. It is one of the Big Five technology companies, and its primary business includes e-commerce, cloud computing, digital streaming, and artificial intelligence.', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('NVDA', 'NVIDIA Corporation', 'NVIDIA Corporation is an American multinational technology company incorporated in Delaware and based in Santa Clara, California. The company primarily focuses on designing graphics processing units (GPUs) for gaming and professional markets, as well as system-on-chip units for the mobile computing and automotive market.', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('META', 'Meta Platforms Inc', 'Meta Platforms, Inc. (formerly Facebook, Inc.) is an American technology conglomerate based in Menlo Park, California. It is known for its social media platform Facebook, as well as subsidiaries like Instagram, WhatsApp, and Oculus VR.', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('BRK.B', 'Berkshire Hathaway Inc.', 'Diversified holding company led by Warren Buffet', 'Financials', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('TSLA', 'Tesla Inc', 'Tesla, Inc. is an American electric vehicle and clean energy company based in Palo Alto, California. Teslas current products include electric cars, battery energy storage from home to grid-scale, solar panels and solar roof tiles, as well as other related products and services. In 2020, Tesla had the highest sales in the plug-in and battery electric passenger car segments, capturing 16% of the plug-in market (which includes plug-in hybrids) and 23% of the battery-electric (purely electric) market. Through its subsidiary Tesla Energy, the company develops and is a major installer of solar photovoltaic energy generation systems in the United States. Tesla Energy is also one of the largest global suppliers of battery energy storage systems, with 3 GWh of battery storage supplied in 2020.', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('LLY', 'Eli Lilly and Company', 'Pharmaceutical company developing medicines and treatments', 'Healthcare', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('V', 'Visa Inc.', 'Global payments technology company facilitating digital transactions', 'Financial Services', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('UNH', 'UnitedHealth Group Incorporated', 'Diversified healthcare company providing health insurance services', 'Healthcare', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('TSM', 'Taiwan Semiconductor Manufacturing Company Limited', 'Leading semiconductor manufacturing and technology service provider', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('WMT', 'Walmart Inc.', 'One of the world''s largest retail chains with extensive global operations', 'Consumer Services', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('NVO', 'Novo Nordisk A/S', 'Pharmaceutical company specializing in diabetes care and other chronic diseases', 'Healthcare', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('XOM', 'Exxon Mobil Corporation', 'One of the largest multinational oil and gas corporations', 'Energy', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('JPM', 'JPMorgan Chase & Co.', 'Leading global financial services firm and one of the largest banking institutions in the US', 'Financial Services', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('JNJ', 'Johnson & Johnson', 'Multinational corporation that produces medical devices, pharmaceuticals, and consumer packaged goods', 'Healthcare', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('AVGO', 'Broadcom Inc.', 'Global technology company specializing in semiconductors and infrastructure software products', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('MA', 'Mastercard Incorporated', 'Multinational financial services corporation facilitating electronic funds transfers worldwide', 'Financial Services', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('PG', 'The Procter & Gamble Company', 'Multinational consumer goods corporation specializing in a wide range of personal health/consumer health, and personal care and hygiene products', 'Consumer Goods', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('ORCL', 'Oracle Corporation', 'Multinational computer technology corporation specializing in database software and technology, cloud engineered systems, and enterprise software products', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('HD', 'The Home Depot, Inc.', 'Largest home improvement retailer in the US, supplying tools, construction products, and services', 'Consumer Services', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('CVX', 'Chevron Corporation', 'Multinational energy corporation engaged in every aspect of the oil, natural gas, and geothermal energy industries', 'Energy', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('MRK', 'Merck & Co., Inc.', 'Leading pharmaceutical company providing innovative health solutions through its prescription medicines, vaccines, biologic therapies, and animal health products', 'Healthcare', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('ADBE', 'Adobe Inc.', 'Software company known for its Adobe Flash web software ecosystem, Photoshop image editing software, Adobe Illustrator vector graphics editor, Acrobat Reader, and the Portable Document Format (PDF)', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('TM', 'Toyota Motor Corporation', 'Multinational automotive manufacturer headquartered in Japan', 'Consumer Goods', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('ASML', 'ASML Holding N.V.', 'Dutch company and currently the largest supplier in the world of photolithography systems for the semiconductor industry', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('ABBV', 'AbbVie Inc.', 'Biopharmaceutical company that discovers, develops, and markets both biopharmaceuticals and small molecule drugs', 'Healthcare', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('COST', 'Costco Wholesale Corporation', 'International chain of membership warehouses that carry quality, brand name merchandise at substantially lower prices than typically found at conventional wholesale or retail sources', 'Consumer Services', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('KO', 'The Coca-Cola Company', 'The world''s largest beverage company, offering over 500 brands in more than 200 countries', 'Consumer Goods', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('PEP', 'PepsiCo, Inc.', 'Multinational food, snack, and beverage corporation', 'Consumer Goods', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('BAC', 'Bank of America Corporation', 'American multinational investment bank and financial services holding company', 'Financial Services', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('SHEL', 'Shell plc', 'Multinational oil and gas company, one of the oil and gas "supermajors"', 'Energy', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('BABA', 'Alibaba Group Holding Limited', 'Chinese multinational technology company specializing in e-commerce, retail, internet, and technology', 'Consumer Services', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('FMX', 'Fomento Económico Mexicano, SAB de CV', 'Mexican multinational beverage and retail company', 'Consumer Goods', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('CSCO', 'Cisco Systems, Inc.', 'American multinational technology conglomerate headquartered in San Jose, California, in the center of Silicon Valley, developing, manufacturing, and selling networking hardware, software, telecommunications equipment and other high-technology services and products', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('CRM', 'Salesforce, Inc.', 'American cloud-based software company providing customer relationship management service and also provides a complementary suite of enterprise applications focused on customer service, marketing automation, analytics, and application development', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('AZN', 'AstraZeneca PLC', 'British-Swedish multinational pharmaceutical and biopharmaceutical company with a focus on the discovery, development, and commercialization of prescription medicines', 'Healthcare', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('ACN', 'Accenture PLC', 'Irish multinational professional services company that provides services in strategy, consulting, digital, technology, and operations', 'Consulting & Professional Services', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('MCD', 'McDonald''s Corporation', 'American fast food company, founded in 1940 as a restaurant operated by Richard and Maurice McDonald, in San Bernardino, California, United States', 'Consumer Services', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('LIN', 'Linde PLC', 'Multinational chemical company which is the largest industrial gas company by market share and revenue', 'Chemicals', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('NVS', 'Novartis AG', 'Swiss multinational pharmaceutical company based in Basel, Switzerland, one of the largest pharmaceutical companies by both market capitalization and sales', 'Healthcare', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('NFLX', 'Netflix Inc', 'Netflix, Inc. is an American subscription-based streaming service that offers online streaming of films and television series. It is one of the most popular streaming platforms worldwide.', 'Entertainment', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('AMD', 'Advanced Micro Devices, Inc.', 'American multinational semiconductor company based in Santa Clara, California', 'Technology', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('CMCSA', 'Comcast Corporation', 'Multinational media and technology company providing consumer entertainment and communication products', 'Communication Services', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('PFE', 'Pfizer Inc.', 'Global biopharmaceutical company developing medicines and vaccines', 'Healthcare', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('TMO', 'Thermo Fisher Scientific Inc.', 'Provider of scientific instrumentation, reagents and consumables, and software services', 'Healthcare', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('TMUS', 'T-Mobile US, Inc.', 'Mobile communications company offering wireless voice and data services', 'Communication Services', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('ABT', 'Abbott Laboratories', 'Healthcare company focused on diagnostics, medical devices, nutritionals, and branded generic medicines', 'Healthcare', 'Stock');
INSERT INTO prod.asset (asset_ticker, asset_name, asset_description, asset_industry, asset_type) VALUES ('NKE', 'NIKE, Inc.', 'Global leader in athletic footwear, apparel, equipment, and accessories', 'Consumer Goods', 'Stock');




INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, price, quantity, date_created, date_modified) VALUES (1, 'AAPL', 150.75, 10, 1699285756, 1699285756);
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, price, quantity, date_created, date_modified) VALUES (1, 'AAPL', 130.75, 20, 1699026556, 1699026556);
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, price, quantity, date_created, date_modified) VALUES (1, 'GOOGL', 2750.50, 5, 1699026556, 1699026556);
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, price, quantity, date_created, date_modified) VALUES (1, 'TSLA', 600.00, 15, 1698940156, 1698940156);
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, price, quantity, date_created, date_modified) VALUES (1, 'AMZN', 3300.25, 7, 1698940156, 1698940156);
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, price, quantity, date_created, date_modified) VALUES (2, 'META', 330.75, 12, 1667318400, 1667318400); 
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, price, quantity, date_created, date_modified) VALUES (1, 'NVDA', 275.50, 8, 1698248956, 1698248956);
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, price, quantity, date_created, date_modified) VALUES (1, 'NFLX', 530.40, 9, 1698248956, 1698248956);
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, price, quantity, date_created, date_modified) VALUES (2, 'AAPL', 150.75, 5, 1667577600, 1667577600);
INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, price, quantity, date_created, date_modified) VALUES (3, 'GOOGL', 2750.50, 10, 1667664000, 1667664000);


INSERT INTO prod.watchlist (watchlist_id, user_id, watchlist_assets) VALUES (1, 'd988bdd8-e569-4026-970a-dd6c286ebe6d', ARRAY['AAPL', 'GOOGL', 'TSLA', 'AMZN', 'NVDA', 'NFLX']);
-- INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity, date_created, date_modified) VALUES (1, 'TSLA', 100.50, 10, 1643121600, 1643121600);
-- INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity) VALUES (2, 'QQQ', 75.25, 20);
-- INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity) VALUES (2, 'MSFT', 50.75, 15);
-- INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity) VALUES (1, 'GOOGL', 120.00, 5);
-- INSERT INTO prod.portfolio_asset (portfolio_id, asset_ticker, average_price_decimal, quantity, date_created, date_modified) VALUES (1, 'MSFT', 90.25, 8, 1668316800, 1668316800);