package database.classes;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static database.DB_Connection.getInitialConnection;
import static database.DB_Connection.getResultsToJSON;

public class Employee {
    int id;
    String name;
    int company_number;

    public Employee(int id, String name, int company_number) {
        this.id = id;
        this.name = name;
        this.company_number = company_number;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCompany_number() {
        return company_number;
    }
    public void setCompany_number(int company_number) {
        this.company_number = company_number;
    }

    //for testing purpose
    void print(){
        System.out.print(getId()+", ");
        System.out.print(getName()+", ");
        System.out.print(getCompany_number()+"\n");
    }

    public static ArrayList<Employee> database_to_employee(){
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();
            ArrayList<Employee> employee_list = new ArrayList<>();
            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM Employee");
            while (rs.next()){
                String json = getResultsToJSON(rs);
                Gson gson = new Gson();
                Employee _employee = gson.fromJson(json, Employee.class);
                employee_list.add(_employee);
            }
            return employee_list;
        } catch (SQLException e) {
            ArrayList<Employee> employee_list = new ArrayList<>();
            e.printStackTrace();
            return employee_list;
        }
    }

    public boolean insert(){
        Connection conn;
        try {
            conn = getInitialConnection();
            Statement stmt = conn.createStatement();

            stmt.execute("USE  HY360");
            String insert = "INSERT INTO employee(id,name,company_number)"+
                    "VALUES('"+getId()+"','"+
                    getName()+"','"+
                    getCompany_number()+"');";
            stmt.execute(insert);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    /*takes an id and a company card , returns true if there is such employee else false*/
    public static boolean employee_exists(int id , int company_number){
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM employee WHERE company_number='"+company_number+"' AND id='"+id+"'");
            return rs.next();
        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    public static Employee fetch(int company_number,int id){
        try {
            Connection conn = getInitialConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs;
            stmt.execute("USE  HY360");
            rs = stmt.executeQuery("SELECT * FROM employee WHERE company_number='"+company_number+"' AND id='"+id+"'");
            if(!rs.next()){
                return null;
            }
            String json = getResultsToJSON(rs);
            Gson gson = new Gson();
            return gson.fromJson(json, Employee.class);

        } catch (SQLException e) {

            e.printStackTrace();
            return null;
        }
    }
}
