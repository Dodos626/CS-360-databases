package database;

import com.google.gson.JsonObject;

import java.sql.*;

public class DB_Connection {

    private static final String url = "jdbc:mysql://localhost";
    private static final int port = 3306;
    private static final String username = "root";
    private static final String password = "";

    /**
     * Attempts to establish a database connection
     *
     * @return a connection to the database
     * @throws SQLException
     */

    public static Connection getInitialConnection() throws SQLException {
    
        return DriverManager.getConnection(url + ":" + port, username, password);
    }

    public static void initialiseDatabase() throws SQLException {
        Connection conn = getInitialConnection();
        Statement stmt = conn.createStatement();
        //stmt.execute("DROP DATABASE HY360");
        stmt.execute("CREATE DATABASE HY360");
        stmt.execute("USE  HY360");
        String query = "CREATE TABLE Individual_card "
                + "(    account_number integer not null unique,"
                + "     name VARCHAR(50) not null ,"
                + "     credit_limit integer not null,"
                + "     balance integer not null,"
                + "     expiration_date DATE not null,"
                + "     amount_owned integer not null,"
                + " PRIMARY KEY (account_number))";
        stmt.execute(query);

        query = "CREATE TABLE Dealer_card "
                + "(    account_number integer not null unique,"
                + "     name VARCHAR(50) not null,"
                + "     supply double not null,"
                + "     balance_from_profit integer not null,"
                + "     amount_owned integer not null,"
                + " PRIMARY KEY (account_number))";
        stmt.execute(query);

        query = "CREATE TABLE Company_card "
                + "(    account_number integer not null unique,"
                + "     name VARCHAR(50) not null ,"
                + "     credit_limit integer not null,"
                + "     balance integer not null,"
                + "     expiration_date DATE not null,"
                + "     amount_owned integer not null,"
                + " PRIMARY KEY (account_number))";
        stmt.execute(query);

        query = "CREATE TABLE Employee "
                + "(    id INTEGER not NULL ,"
                + "     name VARCHAR(50) not null,"
                + "     company_number INTEGER NOT NULL,"
                + "     CONSTRAINT _company_number"
                + " FOREIGN KEY (company_number) REFERENCES Company_card(account_number)"
                + "ON DELETE CASCADE )";
        stmt.execute(query);

        query = "CREATE TABLE Transaction "
                + "(    id INTEGER not NULL AUTO_INCREMENT unique ,"
                + "     payer_name VARCHAR(50) ,"
                + "     dealer_name VARCHAR(50) ,"
                + "     transaction_date DATE not null,"
                + "     amount integer not null,"
                + "     type VARCHAR(50) not null,"
                + "     payer_number integer,"
                + "     dealer_number integer,"
                + "     employee_id integer,"
                + " PRIMARY KEY (id))";
        stmt.execute(query);


        stmt.close();
        conn.close();
    }
    //gets sql into json format
    public static String getResultsToJSON(ResultSet rs) throws SQLException {
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        JsonObject object = new JsonObject();


        String row = "";
        for (int i = 1; i <= columnCount; i++) {
            String name = metadata.getColumnName(i);
            String value = rs.getString(i);
            object.addProperty(name,value);
        }
        return object.toString();
    }

    public static void main(String[] args) {
        try {
            initialiseDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
