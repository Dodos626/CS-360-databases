package database.classes;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static database.DB_Connection.getInitialConnection;
import static database.DB_Connection.getResultsToJSON;


//individual and company cards
public class BaseCard {
    int account_number;
    String name;
    int credit_limit;
    int balance;
    String expiration_date;
    int amount_owned;
    
    public BaseCard(int account_number, String name, int credit_limit, int balance, String expiration_date, int amount_owned){
        setAccount_number(account_number);
        setBalance(balance);
        setAmount_owned(amount_owned);
        setCredit_limit(credit_limit);
        setName(name);
        setExpiration_date(expiration_date);
    }
    public BaseCard() {
    }
    public int getAccount_number() {
        return account_number;
    }
    public void setAccount_number(int account_number) {
        this.account_number = account_number;
    }
    public int getAmount_owned() {
        return amount_owned;
    }
    public void setAmount_owned(int amount_owned) {
        this.amount_owned = amount_owned;
    }
    public int getBalance() {
        return balance;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }
    public int getCredit_limit() {
        return credit_limit;
    }
    public void setCredit_limit(int credit_limit) {
        this.credit_limit = credit_limit;
    }
    public String getExpiration_date() {
        return expiration_date;
    }
    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    //print for testing purpose
    public void print(){
        System.out.print(getAccount_number()+", ");
        System.out.print(getName()+", ");
        System.out.print(getBalance()+", ");
        System.out.print(getAmount_owned()+", ");
        System.out.print(getCredit_limit()+", ");
        System.out.print(getExpiration_date()+"\n");
    }


    public static ArrayList<BaseCard> database_to_simple_card(String type){
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();
            ArrayList<BaseCard> cards = new ArrayList<>();
            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM "+type);
            while (rs.next()){
                String json = getResultsToJSON(rs);
                Gson gson = new Gson();
                BaseCard user = gson.fromJson(json, BaseCard.class);
                cards.add(user);
            }
            return cards;
        } catch (SQLException e) {
            ArrayList<BaseCard> cards = new ArrayList<>();
            e.printStackTrace();
            return cards;
        }
    }

    public static BaseCard fetch(String type,int account_number){
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM "+type+" WHERE account_number='"+account_number+"'");
            if(!rs.next()){
                return null;
            }
                String json = getResultsToJSON(rs);
                Gson gson = new Gson();
            return gson.fromJson(json, BaseCard.class);

        } catch (SQLException e) {

            e.printStackTrace();
            return null;
        }
    }

    public static boolean base_card_exists(String type , int account_number , String name){
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM "+type + " WHERE account_number='"+account_number+"' AND name='"+name+"'");
            return rs.next();
        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    public boolean update(String type ){
        String update = "UPDATE "+type+" SET credit_limit = '"+getCredit_limit()
                +"', balance='"+getBalance()
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

    public boolean insert(String type){
        Connection conn;
        try {
            conn = getInitialConnection();
            Statement stmt = conn.createStatement();

            stmt.execute("USE  HY360");
            String insert = "INSERT INTO "+type+"(account_number, name, credit_limit, balance, expiration_date, amount_owned)"+
                    "VALUES('"+getAccount_number()+"','"+
                    getName()+"','"+
                    getCredit_limit()+"','"+
                    getBalance()+"','"+
                    getExpiration_date()+"','"+
                    getAmount_owned()+"');";
            stmt.execute(insert);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean delete(String type){
        Connection conn;
        try {
            conn = getInitialConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("USE  HY360");

            String update="DELETE FROM "+type+" WHERE account_number='"+account_number+"'";
            stmt.execute(update);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<BaseCard> fetch_good_base_cards(String type){
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();
            ArrayList<BaseCard> cards = new ArrayList<>();
            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM "+type+" WHERE amount_owned=0");
            while (rs.next()){
                String json = getResultsToJSON(rs);
                Gson gson = new Gson();
                BaseCard user = gson.fromJson(json, BaseCard.class);
                cards.add(user);
            }
            return cards;
        } catch (SQLException e) {
            ArrayList<BaseCard> cards = new ArrayList<>();
            e.printStackTrace();
            return cards;
        }
    }
    public static ArrayList<BaseCard> fetch_bad_base_cards(String type){
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();
            ArrayList<BaseCard> cards = new ArrayList<>();
            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM "+type+" WHERE amount_owned!=0");
            while (rs.next()){
                String json = getResultsToJSON(rs);
                Gson gson = new Gson();
                BaseCard user = gson.fromJson(json, BaseCard.class);
                cards.add(user);
            }
            return cards;
        } catch (SQLException e) {
            ArrayList<BaseCard> cards = new ArrayList<>();
            e.printStackTrace();
            return cards;
        }
    }

}
