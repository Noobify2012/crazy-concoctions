-- starter data for the concoctions database


INSERT INTO user (email, username, password, firstName, lastName, bio) 
VALUES
('blum.da@northeastern.edu', 'schooper', 'password', 'Daniel', 'Blum', 
  "This is a bio I've written!"),
('greene.matthew@northeastern.edu', 'bro', 'brobro', 'Matt', 'Greene',
  "This is a stupid bio, I didn't write it.");


/*
- 2 ounces vodka
- 1/2 ounce coffee liqueur (Kahlua)
- 1 ounce espresso
- 1/2 ounce simple syrup
*/

INSERT INTO category (name, description) 
VALUES
('caffeinated', 'beverages that have caffeine'),
('alcoholic', 'beverages that contains alcohol');

INSERT INTO unitOfMeasure (name, type)
VALUES
('ounces', 'fluid');

INSERT INTO ingredient (name, type, description, isAlcoholic)
VALUES
('vodka', 'liquor', 'Ukrainian potato water', true),
('coffee liqueur', 'liqueur', 'boozy coffee', true),
('espresso', 'coffee', 'high-octane coffee', false),
('simple syrup', 'syrup', 'sugar water', false);

INSERT INTO drink (userId, categoryId, name, isHot, description)
VALUES
(2, 2, 'espresso martini', false, 'A drink made to wake me up and fuck me up.');


INSERT INTO drink_ingredient (drinkId, ingredientID, uomId, amount) 
VALUES
(1, 1, 1, 2.0),
(1, 2, 1, 0.5),
(1, 3, 1, 1.0),
(1, 4, 1, 0.5);


INSERT INTO comment (userId, drinkId, ranking, commentBody)
VALUES
(1, 1, 4, 'Delicious - but damn, I feel basic.');


INSERT INTO foodItem (name)
VALUES
('sarcasm');

INSERT INTO pairing (foodItemId, drinkId)
VALUES
(1, 1);




