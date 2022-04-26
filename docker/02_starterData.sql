-- starter data for the concoctions database


INSERT INTO user (email, username, password, firstName, lastName, bio) 
VALUES
('blum.da@northeastern.edu', 'schooper', 'password', 'Daniel', 'Blum', 
  "This is a bio I've written!"),
('greene.matthew@northeastern.edu', 'bro', 'brobro', 'Matt', 'Greene',
  "This is a stupid bio, I didn't write it."),
('delete.me@delete.com', 'deleteMe', 'deleteMe', 'Delete', 'Me',
"I'm here only to be deleted. So please, please delete me and let me live out my purpose.")
;


/*
Espresso Martini
- 2 ounces vodka
- 1/2 ounce coffee liqueur (Kahlua)
- 1 ounce espresso
- 1/2 ounce simple syrup
*/

/*
Boulevardier
- 1 1/4 ounce bourbon (or rye)
- 1 ounce Campari
- 1 ounce sweet vermouth
- Garnish: orange twist
*/

INSERT INTO category (name, description) 
VALUES
('caffeinated', 'beverages that have caffeine'),
('alcoholic', 'beverages that contains alcohol'),
('mocktail', "beverages that mimic alcoholic ones, but don't have that same woohoo"),
('delete category', "this category's sole purpose is to be deleted")
;

INSERT INTO unitOfMeasure (name, type)
VALUES
('ounces', 'fluid'),
('quantity', 'physical'),
('uom deleted', 'ephemeral');

INSERT INTO type (name, description)
VALUES
('liquor', 'distilled spirits'),
('liqueur', 'A strong, sweet alcoholic liquor, usually drunk after a meal.'),
('coffee', 'the elixir of life'),
('syrup', 'sugary water'),
('wine', 'jesus water'),
('garnish', 'a little something something to add some something'),
('delete type', 'just end this type');

INSERT INTO ingredient (name, typeId, description, isAlcoholic)
VALUES
('vodka', 1, 'Ukrainian potato water', true),
('coffee liqueur', 2, 'boozy coffee', true),
('espresso', 3, 'high-octane coffee', false),
('simple syrup', 4, 'sugar water', false),
('bourbon', 1, 'American corn water.', true),
('Campari', 2, 'The standard bitter Italian aperitif', true),
('sweet vermouth', 5, 'Wine, but fortified against attackers', true),
('orange twist', 6, 'A piece of the the orange rind which IS ALSO NEEDED', false),
('delete ingredient', 7, 'an ingredient that will be used in nothing', false);

INSERT INTO drink (userId, categoryId, name, isHot, description)
VALUES
(2, 2, 'espresso martini', false, 'A drink made to wake me up and fuck me up.'),
(1, 1, 'boulevardier', false, "the better old fashioned. It's American, but it's French too."),
(3, 4, 'the worst drink ever', true, "this drink exists to not exists. Remove it and fulfil your duty.");


INSERT INTO drink_ingredient (drinkId, ingredientID, uomId, amount) 
VALUES
(1, 1, 1, 2.0),
(1, 2, 1, 0.5),
(1, 3, 1, 1.0),
(1, 4, 1, 0.5),
(2, 5, 1, 1.25),
(2, 6, 1, 1.0),
(2, 7, 1, 1.0),
(2, 8, 2, 1.0),
(3, 9, 3, 0.0);


INSERT INTO comment (userId, drinkId, ranking, commentBody)
VALUES
(1, 1, 4, 'Delicious - but damn, I feel basic.'),
(2, 1, 5, 'I make this daily because i like to balance my uppers and downers'),
(1, 2, 5, 'Not only does this taste better than an old fashioned, you sound fancier when saying it.'),
(3, 3, 1, "I'm only here to troll. This comment should be deleted.");


INSERT INTO foodItem (name)
VALUES
('sarcasm'),
('fava beans'),
('Camembert'),
('pancakes'),
('delete this food item');

INSERT INTO pairing (foodItemId, drinkId)
VALUES
(1, 1),
(5, 3);


