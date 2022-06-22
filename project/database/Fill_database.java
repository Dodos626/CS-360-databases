package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static database.DB_Connection.getInitialConnection;

public class Fill_database {
    public static void fill_database() throws SQLException {
        Connection conn = getInitialConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("USE  HY360");
        stmt.execute("INSERT INTO individual_card(account_number, name, credit_limit, balance, expiration_date, amount_owned)" +
                "VALUES ('123456789', 'Theodoros Petrou', '200', '500', '2036-12-03', '0')," +
                " ('223003120', 'Mixahl Zanhs', '600', '300', '2024-02-20', '100')," +
                " ('245687542', 'Perpetoua Vounotripidou', '350', '0', '2022-10-13', '1000')," +
                " ('548963226', 'Nikolaos Euaggelatos', '650', '1240', '2025-11-11', '20')," +
                " ('345656758', 'Baggelis Marinakis', '2000', '1500000', '2023-05-14', '0');");

        stmt.execute("INSERT INTO dealer_card (account_number,name,supply,balance_from_profit,amount_owned)" +
                "VALUES ('849302012', 'Dimitris Sirinas', '5.5', '200', '10')," +
                "        ('344123090', 'Sakis Tolhs', '2.0', '1000', '20')," +
                "        ('841111012', 'Marianna Mprisimintzi', '12', '100', '12')," +
                "        ('849305544', 'Nikoleta Eustathiaadou', '10', '300', '30')," +
                "        ('505050501', 'Petros Nikolaou', '1', '230', '2');");

        stmt.execute("INSERT INTO company_card(account_number, name, credit_limit,balance, expiration_date,amount_owned)" +
                "VALUES('537485903', 'Seto Kaiba', '5000', '29384758', '2027-11-12','0')," +
                "      ('569856412', 'Konami', '570', '114623', '2027-11-03','10')," +
                "      ('498620132', 'Google', '300', '11223', '2027-11-01','63')," +
                "      ('203265015', 'Amazoom', '3000', '1123', '2027-11-12','100')," +
                "      ('452032630', 'Abra Katabra', '5000', '345634', '2027-11-12','666');");

        stmt.execute("INSERT INTO Employee(id,name,company_number)\n" +
                "VALUES('1', 'Aria Namichu', '537485903'),\n" +
                "      ('2', 'Mpampis Gadadoogas', '569856412'),\n" +
                "      ('33', 'Stever Strange', '498620132'),\n" +
                "      ('44', 'Tony Stark', '203265015'),\n" +
                "      ('55', 'Natasa Romanof', '452032630');");


        stmt.execute("INSERT INTO transaction(payer_name,\tdealer_name,\ttransaction_date,\tamount,\ttype, payer_number, dealer_number,\temployee_id)\n" +
                "VALUES('Seto Kaiba', 'Sakis Tolhs', '2022-01-11', '200', 'payment', '537485903', '344123090', '1'),\n" +
                "      ('Konami', 'Dimitris Sirinas', '2022-02-11', '2100', 'payment', '569856412', '849302012', '2'),\n" +
                "      ('Amazoom', 'Sakis Tolhs', '2022-01-30', '30', 'payment', '203265015', '344123090', '44'),\n" +
                "      ('Nikolaos Euaggelatos', 'Marianna Mprisimintzi', '2022-01-17', '5', 'payment', '548963226', '841111012', '0'),\n" +
                "      ('Baggelis Marinakis', 'Petros Nikolaou', '2022-01-16', '125', 'payment', '345656758', '505050501', '0');");


        stmt.close();
        conn.close();
    }
}
