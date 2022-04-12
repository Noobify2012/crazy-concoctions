CREATE DATABASE IF NOT EXISTS concoctionsDB;

use concoctionsDB;


DROP TABLE IF EXISTS user;
CREATE TABLE user (
  userId      int NOT NULL AUTO_INCREMENT,
  email       varchar(255) NOT NULL,
  username    varchar(255) NOT NULL,
  password    varchar(255) NOT NULL,
  firstName   varchar(255) NOT NULL,
  lastName    varchar(255) NOT NULL,
  bio         text,
  CONSTRAINT user_pk PRIMARY KEY (userId),
  CONSTRAINT user_unique UNIQUE (email)
);

INSERT INTO user (email, username, password, firstName, lastName, bio) 
VALUES
('blum.da@northeastern.edu', 'schooper', 'password', 'Daniel', 'Blum', "This is a bio I've written!");



