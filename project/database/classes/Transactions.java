package database.classes;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static database.DB_Connection.getInitialConnection;
import static database.DB_Connection.getResultsToJSON;

public class Transactions {
    int id;
    String payer_name;
    String dealer_name;
    String transaction_date;
    int amount;
    String type;
    int payer_number;
    int dealer_number;
    int employee_id;

    public Transactions(int id, String payer_name, String dealer_name, String transaction_date, int amount, String type, int payer_number, int dealer_number, int employee_id) {
        this.id = id;
        this.payer_name = payer_name;
        this.dealer_name = dealer_name;
        this.transaction_date = transaction_date;
        this.amount = amount;
        this.type = type;
        this.payer_number = payer_number;
        this.dealer_number = dealer_number;
        this.employee_id = employee_id;
    }

    public static ArrayList<Transactions> database_to_transaction() {
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();
            ArrayList<Transactions> transaction_list = new ArrayList<>();
            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM transaction");
            while (rs.next()) {
                String json = getResultsToJSON(rs);
                Gson gson = new Gson();
                Transactions single_transaction = gson.fromJson(json, Transactions.class);
                transaction_list.add(single_transaction);
            }
            return transaction_list;
        } catch (SQLException e) {
            ArrayList<Transactions> transaction_list = new ArrayList<>();
            e.printStackTrace();
            return transaction_list;
        }
    }

    public static Transactions fetch(int id) {
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM transaction WHERE id='" + id + "'");
            if (!rs.next()) {
                return null;
            }
            String json = getResultsToJSON(rs);
            Gson gson = new Gson();

            return gson.fromJson(json, Transactions.class);

        } catch (SQLException e) {

            e.printStackTrace();
            return null;
        }
    }

    //takes 2 dates and the payer_number
    public static ArrayList<Transactions> fetch_transaction_with_dates(String max_date, String min_date, int payer_number) {
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();
            ArrayList<Transactions> transaction_list = new ArrayList<>();
            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM transaction WHERE payer_number='" + payer_number +
                    "' AND transaction_date BETWEEN '" + min_date + "' AND '" + max_date + "'");
            while (rs.next()) {
                String json = getResultsToJSON(rs);
                Gson gson = new Gson();
                Transactions single_transaction = gson.fromJson(json, Transactions.class);
                transaction_list.add(single_transaction);
            }
            return transaction_list;
        } catch (SQLException e) {
            ArrayList<Transactions> transaction_list = new ArrayList<>();
            e.printStackTrace();
            return transaction_list;
        }
    }

    //takes 2 dates and the payer_number and the employee_id
    public static ArrayList<Transactions> fetch_transaction_with_dates(String max_date, String min_date, int payer_number, ArrayList<Integer> employee_id) {
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();
            ArrayList<Transactions> transaction_list = new ArrayList<>();
            ResultSet rs;
            StringBuilder query = new StringBuilder("SELECT * FROM transaction WHERE payer_number='" + payer_number +
                    "' AND transaction_date BETWEEN '" + min_date + "' AND '" + max_date + "' AND(");
            int i;
            for (i = 0; i < employee_id.size() - 1; i++) {
                query.append(" employee_id='").append(employee_id.get(i)).append("' OR");
            }
            query.append(" employee_id='").append(employee_id.get(i)).append("')");

            stmt.execute("USE  HY360");
            rs = stmt.executeQuery(query.toString());
            while (rs.next()) {
                String json = getResultsToJSON(rs);
                Gson gson = new Gson();
                Transactions single_transaction = gson.fromJson(json, Transactions.class);
                transaction_list.add(single_transaction);
            }
            return transaction_list;
        } catch (SQLException e) {
            ArrayList<Transactions> transaction_list = new ArrayList<>();
            e.printStackTrace();
            return transaction_list;
        }
    }

    //takes 2 dates and the payer_number
    public static ArrayList<Transactions> fetch_dealer_transaction_with_dates(String max_date, String min_date, int dealer_number) {
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();
            ArrayList<Transactions> transaction_list = new ArrayList<>();
            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM transaction WHERE dealer_number='" + dealer_number +
                    "' AND transaction_date BETWEEN '" + min_date + "' AND '" + max_date + "'");
            while (rs.next()) {
                String json = getResultsToJSON(rs);
                Gson gson = new Gson();
                Transactions single_transaction = gson.fromJson(json, Transactions.class);
                transaction_list.add(single_transaction);
            }
            return transaction_list;
        } catch (SQLException e) {
            ArrayList<Transactions> transaction_list = new ArrayList<>();
            e.printStackTrace();
            return transaction_list;
        }
    }

    public static int fetch_dealer_of_the_month(String max_date, String min_date) {

        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs;
            stmt.execute("USE  HY360");

            rs = stmt.executeQuery("SELECT dealer_number, COUNT(dealer_number) AS `count` " +
                    "FROM transaction WHERE type ='payment' AND transaction_date BETWEEN '" + min_date + "' AND '" + max_date + "'" +
                    "GROUP BY dealer_number " +
                    "ORDER BY `count` DESC " +
                    "LIMIT 1;");
            if (!rs.next()) {
                return 0;
            }
            return Integer.parseInt(rs.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayer_name() {
        return payer_name;
    }

    public void setPayer_name(String payer_name) {
        this.payer_name = payer_name;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPayer_number() {
        return payer_number;
    }

    public void setPayer_number(int payer_number) {
        this.payer_number = payer_number;
    }

    public int getDealer_number() {
        return dealer_number;
    }

    public void setDealer_number(int dealer_number) {
        this.dealer_number = dealer_number;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    //print for testing purpose
    void print() {
        System.out.print(getId() + ", ");
        System.out.print(getPayer_name() + ", ");
        System.out.print(getDealer_name() + ", ");
        System.out.print(getTransaction_date() + ", ");
        System.out.print(getAmount() + ", ");
        System.out.print(getType() + ", ");
        System.out.print(getPayer_number() + ", ");
        System.out.print(getDealer_number() + ", ");
        System.out.print(getEmployee_id() + "\n");
    }

    public boolean insert() {
        Connection conn;
        try {
            conn = getInitialConnection();
            Statement stmt = conn.createStatement();

            stmt.execute("USE  HY360");
            String insert = "INSERT INTO transaction(payer_name,dealer_name,transaction_date,amount,type, payer_number, dealer_number,employee_id)" +
                    "VALUES('" + getPayer_name() + "','" +
                    getDealer_name() + "','" +
                    getTransaction_date() + "','" +
                    getAmount() + "','" +
                    getType() + "','" +
                    getPayer_number() + "','" +
                    getDealer_number() + "','" +
                    getEmployee_id() + "');";
            stmt.execute(insert);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
