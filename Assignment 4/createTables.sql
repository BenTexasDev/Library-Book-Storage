DROP TABLE IF EXISTS author, publisher,book,book_audit_trail;

CREATE TABLE author(
 id int NOT NULL AUTO_INCREMENT,
 first_name VARCHAR (100),
 last_name VARCHAR (100),
 dob DATE,
 gender VARCHAR (8) NOT NULL,
 web_site VARCHAR (100),
 PRIMARY KEY (  id )
);

CREATE TABLE publisher(
	id int NOT NULL AUTO_INCREMENT,
	publisher_name VARCHAR (255) NOT NULL,
	date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);

CREATE TABLE book(
	id INT NOT NULL AUTO_INCREMENT,
	title VARCHAR (255) NOT NULL,
	summary TEXT,
	year_published INT,
	publisher_id INT NOT NULL DEFAULT 1,
	isbn VARCHAR (20),
	date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (publisher_id) REFERENCES publisher (id) ON DELETE RESTRICT,
	PRIMARY KEY (id)
	
);

CREATE TABLE book_audit_trail(
	id int NOT NULL AUTO_INCREMENT,
	book_id int NOT NULL,
	date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	entry_msg VARCHAR(255),
	FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE,
	PRIMARY KEY (id)
);

ALTER TABLE book_audit_trail ADD INDEX (book_id);

CREATE TRIGGER `audit_trigger` AFTER UPDATE ON book FOR EACH ROW INSERT INTO book_audit_trail(book_id,entry_msg) VALUES (new.id,"entry msg")

INSERT INTO author
VALUES
	('1','John','Smith','1980-12-17','Male','jSmith.com'),
	('2','Jane','Smith','1980-1-17','Female','jSmith.com'),
	('3','Ernest','Hemmingway','1980-2-17','Male','eh.com'),
	('4','Dr','Sues','1980-3-17','Unkown','drsues.com'),
	('5','George','Martin','1980-4-17','Male','gameofthrones.com'),
	('6','John','Smith','1980-5-17','Male','jSmith.com');
	
INSERT INTO publisher (publisher_name)
VALUES
	('Unknown'),
	('Pearsons'),
	('Penguin');
	

