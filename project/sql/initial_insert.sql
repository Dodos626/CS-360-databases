USE HY360;

INSERT INTO individual_card (account_number, name,	credit_limit,	balance, expiration_date,	amount_owned)
VALUES('123456789', 'Theodoros Petrou', '200', '500', '2036-12-03', '0'),
      ('223003120', 'Mixahl Zanhs', '600', '300', '2024-02-20', '100'),
      ('245687542', 'Perpetoua Vounotripidou', '350', '0', '2022-10-13', '1000'),
      ('548963226', 'Nikolaos Euaggelatos', '650', '1240', '2025-11-11', '20'),
      ('345656758', 'Baggelis Marinakis', '2000', '1500000', '2023-05-14', '0');

INSERT INTO dealer_card (account_number,	name,	supply, balance_from_profit, amount_owned)
VALUES('849302012', 'Dimitris Sirinas', '5', '200', '10'),
      ('344123090', 'Sakis Tolhs', '2', '1000', '20'),
      ('841111012', 'Marianna Mprisimintzi', '12', '100', '12'),
      ('849305544', 'Nikoleta Eustathiaadou', '10', '300', '30'),
      ('505050501', 'Petros Nikolaou', '1', '230', '2');

INSERT INTO company_card(account_number, name, credit_limit,	balance, expiration_date,amount_owned)
VALUES('537485903', 'Seto Kaiba', '5000', '29384758', '2027-11-12','0'),
      ('569856412', 'Konami', '570', '114623', '2027-11-03','10'),
      ('498620132', 'Google', '300', '11223', '2027-11-01','63'),
      ('203265015', 'Amazoom', '3000', '1123', '2027-11-12','100'),
      ('452032630', 'Abra Katabra', '5000', '345634', '2027-11-12','666');

INSERT INTO employee(id,name,company_number)
VALUES('1', 'Aria Namichu', '537485903'),
      ('2', 'Mpampis Gadadoogas', '569856412'),
      ('33', 'Stever Strange', '498620132'),
      ('44', 'Tony Stark', '203265015'),
      ('55', 'Natasa Romanof', '452032630');

INSERT INTO transaction(payer_name,	dealer_name,	transaction_date,	amount,	type, payer_number, dealer_number,	employee_id)
VALUES('Seto Kaiba', 'Sakis Tolhs', '2022-01-11', '200', 'payment', '537485903', '344123090', '1'),
      ('Konami', 'Dimitris Sirinas', '2022-02-11', '2100', 'payment', '569856412', '849302012', '2'),
      ('Amazoom', 'Sakis Tolhs', '2022-01-30', '30', 'payment', '203265015', '344123090', '44'),
      ('Nikolaos Euaggelatos', 'Marianna Mprisimintzi', '2022-01-17', '5', 'payment', '548963226', '841111012', '0'),
      ('Baggelis Marinakis', 'Petros Nikolaou', '2022-01-16', '125', 'payment', '345656758', '505050501', '0');


/*individual card queries, type is either company_card or individual_card and is passed down as an argument*/

/*fetch all cards*/
SELECT * FROM type;

/*fetch a card based on account_number*/
SELECT * FROM type WHERE account_number='account_number';

/*fetch a card based on account_number and name*/
SELECT * FROM type WHERE account_number='account_number' AND name='name';

/*update a card's field*/
UPDATE type SET credit_limit = 'getCredit_limit()' ,
                balance='getBalance()',
                amount_owned='getAmount_owned()'
WHERE account_number='getAccount_number()';

/*insert a card into the database*/
INSERT INTO type(account_number, name, credit_limit, balance, expiration_date, amount_owned)
                    VALUES('getAccount_number()',
                           'getName()',
                           'getCredit_limit()',
                           'getBalance()',
                           'getExpiration_date()',
                           'getAmount_owned()');

/*delete a card from the database*/
DELETE FROM type WHERE account_number='account_number';

/*fetch good cards*/
SELECT * FROM type WHERE amount_owned=0;

/*fetch bad cards*/
SELECT * FROM type WHERE amount_owned!=0;


/*dealer cards queries*/

/*fetch all cards*/
SELECT * FROM dealer_card;

/*fetch a card based on account_number*/
SELECT * FROM dealer_card WHERE account_number='account_number';

/*fetch a card based on account_number and name*/
SELECT * FROM dealer_card WHERE account_number='account_number' AND name='name';

/*update a card into the database*/
UPDATE dealer_card SET balance_from_profit = 'getBalance_from_profit()',
                       amount_owned='getAmount_owned()'
WHERE account_number='getAccount_number()';

/*insert a card into the database*/
INSERT INTO dealer_card(account_number,name,supply,balance_from_profit,amount_owned)
                    VALUES('getAccount_number()',
                           'getName()',
                           'getSupply()',
                           'getBalance_from_profit()',
                           'getAmount_owned()');

/*delete a card from the database*/
DELETE FROM dealer_card WHERE account_number='getAccount_number()';

/*fetch good cards*/
SELECT * FROM dealer_card WHERE amount_owned=0;

/*fetch bad cards*/
SELECT * FROM dealer_card WHERE amount_owned!=0;


/*employee table queries*/

/*fetch all employee*/
SELECT * FROM employee;

/*insert an employee into the database*/
INSERT INTO employee(id,name,company_number)
                    VALUES('getId()',
                           'getName()',
                           'getCompany_number()');

/*delete an employee from the database based on company number and id of the employee*/
SELECT * FROM employee WHERE company_number='company_number' AND id='id';

/*fetch a single employee*/
SELECT * FROM employee WHERE company_number='company_number' AND id='id'


/*transaction table queries*/

/*fetch all transactions*/
SELECT * FROM transaction;

/*fetch a transactions with id*/
SELECT * FROM transaction WHERE id='id';

/*fetch transactions between two dates*/
SELECT * FROM transaction WHERE payer_number=' payer_number' AND transaction_date BETWEEN 'min_date' AND 'max_date';

/*fetch transactions between two dates and the employee_id*/
SELECT * FROM transaction WHERE payer_number=' payer_number'
                            AND transaction_date BETWEEN 'min_date' AND 'max_date'
                            AND employee_id='employee_id';

/*fetch transactions between two dates and the payer_number*/
SELECT * FROM transaction WHERE payer_number=' payer_number'
                            AND transaction_date BETWEEN 'min_date' AND 'max_date'
                            AND payer_number='payer_number';

/*fetch dealer of the month*/
SELECT dealer_number, COUNT(dealer_number) AS 'count'
FROM transaction WHERE type ='payment' AND transaction_date BETWEEN 'min_date' AND 'max_date'
GROUP BY dealer_number
ORDER BY 'count' DESC LIMIT 1;

/*insert dealer into the database*/
INSERT INTO transaction(payer_name,dealer_name,transaction_date,amount,type, payer_number, dealer_number,employee_id)
VALUES('getPayer_name()',
       'getDealer_name()',
       'getTransaction_date()',
       'getAmount()',
       'getType()',
       'getPayer_number()',
       'getDealer_number()',
       'getEmployee_id()');