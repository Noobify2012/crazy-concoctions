CREATE DATABASE IF NOT EXISTS concoctionsDB;

use concoctionsDB;

DROP TABLE IF EXISTS user;
CREATE TABLE user (
  userId      int NOT NULL AUTO_INCREMENT,
  email       varchar(255)  NOT NULL,
  username    varchar(255)  NOT NULL,
  password    varchar(255)  NOT NULL,
  firstName   varchar(255)  NOT NULL,
  lastName    varchar(255)  NOT NULL,
  bio         text,
  CONSTRAINT  user_pk PRIMARY KEY (userId),
  CONSTRAINT  user_unique UNIQUE (email)
);

DROP TABLE IF EXISTS category;
CREATE TABLE category (
  categoryId  int NOT NULL  AUTO_INCREMENT,
  name        varchar(255)  NOT NULL,
  description text,

  CONSTRAINT category_pk  PRIMARY KEY (categoryId)
);

DROP TABLE IF EXISTS unitOfMeasure;
CREATE TABLE unitOfMeasure (
  uomId int NOT NULL  AUTO_INCREMENT,
  name  varchar(255)  NOT NULL,
  type  varchar(255)  NOT NULL,

  CONSTRAINT unitOfMeasure_pk PRIMARY KEY (uomId)
);

DROP TABLE IF EXISTS ingredient;
CREATE TABLE ingredient (
  ingredientId  int NOT NULL  AUTO_INCREMENT,
  name          varchar(255)  NOT NULL,
  type          varchar(255),
  description   text,
  isAlcoholic   boolean,

  CONSTRAINT ingredient_pk  PRIMARY KEY (ingredientId)
);

DROP TABLE IF EXISTS drink;
CREATE TABLE drink (
  drinkId     int NOT NULL  AUTO_INCREMENT,
  userId      int NOT NULL,
  categoryId  int NOT NULL,
  name        varchar(255)  NOT NULL,
  isHot       boolean,
  description text,

  CONSTRAINT drink_pk 
    PRIMARY KEY (drinkId),
  CONSTRAINT drink_userFk 
    FOREIGN KEY (userId) REFERENCES user (userId),
  CONSTRAINT drunk_categoryFk
    FOREIGN KEY (categoryId) REFERENCES category (categoryId)
);

DROP TABLE IF EXISTS drink_ingredient;
CREATE TABLE drink_ingredient (
  drinkId       int NOT NULL,
  ingredientId  int NOT NULL,
  uomId         int NOT NULL,
  amount        double NOT NULL,

  CONSTRAINT drink_ingredient_pk PRIMARY KEY (drinkId, ingredientId, uomId),
  CONSTRAINT drink_ingredient_drinkFk
    FOREIGN KEY (drinkId) REFERENCES drink (drinkId),
  CONSTRAINT drink_ingredient_ingredientFk
    FOREIGN KEY (ingredientId) REFERENCES ingredient (ingredientId),
  CONSTRAINT drink_ingredient_uomFk
    FOREIGN KEY (uomId) REFERENCES unitOfMeasure (uomId)
);


DROP TABLE IF EXISTS comment;
CREATE TABLE comment (
  commentId   int   NOT NULL  AUTO_INCREMENT,
  userId      int   NOT NULL,
  drinkId     int   NOT NULL,
  ranking     int   NOT NULL,
  commentBody text  NOT NULL,

  CONSTRAINT comment_pk PRIMARY KEY (commentId),
  CONSTRAINT comment_user_fk
    FOREIGN KEY (userId) REFERENCES user (userId),
  CONSTRAINT comment_drink_fk
    FOREIGN KEY (drinkId) REFERENCES drink (drinkId)
);

DROP TABLE IF EXISTS foodItem;
CREATE TABLE foodItem (
  foodItemId  int NOT NULL  AUTO_INCREMENT,
  name        varchar(255)  NOT NULL,

  CONSTRAINT foodItem_pk  PRIMARY KEY (foodItemId)
);

DROP TABLE IF EXISTS pairing;
CREATE TABLE pairing (
  foodItemId  INT NOT NULL,
  drinkId     INT NOT NULL,

  CONSTRAINT pairing_foodItemFk
    FOREIGN KEY (foodItemId) REFERENCES foodItem (foodItemId),
  CONSTRAINT pairing_drinkFk
    FOREIGN KEY (drinkId) REFERENCES drink (drinkId)
);


