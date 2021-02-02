-- TABLE BEERS
CREATE TABLE IF NOT EXISTS beers (
	beer_id SERIAL,
	name VARCHAR(180) NOT NULL,
	ingredients VARCHAR(400) NOT NULL,
	alcoholContent VARCHAR(5),
	price DECIMAL(9,2) NOT NULL,
	category VARCHAR(60) NOT NULL,
	create_at TIMESTAMP,
	update_at TIMESTAMP,
	CONSTRAINT beer_pk PRIMARY KEY (beer_id)
);