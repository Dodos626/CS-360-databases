package database.classes;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static database.DB_Connection.getInitialConnection;
import static database.DB_Connection.getResultsToJSON;

public class DealerCard {

    int account_number;
    String name;
    double supply;
    int balance_from_profit;
    int amount_owned;

    public DealerCard(){}
    public DealerCard(int account_number, String name, double supply, int balance_from_profit, int amount_owned) {
        this.account_number = account_number;
        this.name = name;
        this.supply = supply;
        this.balance_from_profit = balance_from_profit;
        this.amount_owned = amount_owned;
    }
    public int getAccount_number() {
        return account_number;
    }
    public void setAccount_number(int account_number) {
        this.account_number = account_number;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getSupply() {
        return supply;
    }
    public void setSupply(int supply) {
        this.supply = supply;
    }
    public int getBalance_from_profit() {
        return balance_from_profit;
    }
    public void setBalance_from_profit(int balance_from_profit) {
        this.balance_from_profit = balance_from_profit;
    }
    public int getAmount_owned() {
        return amount_owned;
    }
    public void setAmount_owned(int amount_owned) {
        this.amount_owned = amount_owned;
    }


    //for testing purpose
    void print(){
        System.out.print(getAccount_number()+", ");
        System.out.print(getName()+", ");
        System.out.print(getSupply()+", ");
        System.out.print(getBalance_from_profit()+", ");
        System.out.print(getAmount_owned()+"\n");
    }


    public static ArrayList<DealerCard> database_to_dealer_card(){
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();
            ArrayList<DealerCard> dealer_cards_list = new ArrayList<>();
            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM dealer_card");
            while (rs.next()){
                String json = getResultsToJSON(rs);
                Gson gson = new Gson();
                DealerCard dealerCard_single = gson.fromJson(json, DealerCard.class);
                dealer_cards_list.add(dealerCard_single);
            }
            return dealer_cards_list;
        } catch (SQLException e) {
            ArrayList<DealerCard> dealer_cards_list = new ArrayList<>();
            e.printStackTrace();
            return dealer_cards_list;
        }
    }

    public static DealerCard database_to_dealer_card_with_number(int account_number){
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM dealer_card WHERE account_number='"+account_number+"'");
            if(!rs.next()){
                return null;
            }
            String json = getResultsToJSON(rs);
            Gson gson = new Gson();
            return gson.fromJson(json, DealerCard.class);

        } catch (SQLException e) {

            e.printStackTrace();
            return null;
        }
    }

    public static DealerCard fetch(int account_number){
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM dealer_card WHERE account_number='"+account_number+"'");
            if(!rs.next()){
                return null;
            }
            String json = getResultsToJSON(rs);
            Gson gson = new Gson();
            return gson.fromJson(json, DealerCard.class);

        } catch (SQLException e) {

            e.printStackTrace();
            return null;
        }
    }

    public static boolean dealer_card_exists(int account_number , String name){
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM dealer_card WHERE account_number='"+account_number+"' AND name='"+name+"'");
            return rs.next();
        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    public boolean update(){
        String update = "UPDATE dealer_card SET balance_from_profit = '"+getBalance_from_profit()
                +"', amount_owned='"+getAmount_owned()
                +"' WHERE account_number='"+getAccount_number()+"'";
        Connection conn;
        try {
            conn = getInitialConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("USE  HY360");
            stmt.executeUpdate(update);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean insert(){
        Connection conn;
        try {
            conn = getInitialConnection();
            Statement stmt = conn.createStatement();

            stmt.execute("USE  HY360");
            String insert = "INSERT INTO dealer_card(account_number,name,supply,balance_from_profit,amount_owned)"+
                    "VALUES('"+getAccount_number()+"','"+
                    getName()+"','"+
                    getSupply()+"','"+
                    getBalance_from_profit()+"','"+
                    getAmount_owned()+"');";
            stmt.execute(insert);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean delete(){
        Connection conn;
        try {
            conn = getInitialConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("USE  HY360");

            String update="DELETE FROM dealer_card WHERE account_number='"+getAccount_number()+"'";
            stmt.execute(update);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<DealerCard> fetch_bad_dealer_cards(){
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();
            ArrayList<DealerCard> cards = new ArrayList<>();
            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM dealer_card WHERE amount_owned!=0");
            while (rs.next()){
                String json = getResultsToJSON(rs);
                Gson gson = new Gson();
                DealerCard user = gson.fromJson(json, DealerCard.class);
                cards.add(user);
            }
            return cards;
        } catch (SQLException e) {
            ArrayList<DealerCard> cards = new ArrayList<>();
            e.printStackTrace();
            return cards;
        }
    }

    public static ArrayList<DealerCard> fetch_good_dealer_cards(){
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();
            ArrayList<DealerCard> cards = new ArrayList<>();
            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM dealer_card WHERE amount_owned=0");
            while (rs.next()){
                String json = getResultsToJSON(rs);
                Gson gson = new Gson();
                DealerCard user = gson.fromJson(json, DealerCard.class);
                cards.add(user);
            }
            return cards;
        } catch (SQLException e) {
            ArrayList<DealerCard> cards = new ArrayList<>();
            e.printStackTrace();
            return cards;
        }
    }

}
