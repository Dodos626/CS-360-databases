import swing.mainComponent;

import java.sql.SQLException;

import static database.DB_Connection.initialiseDatabase;
import static database.Fill_database.fill_database;

public class main {
    public static void main(String args[]){
        //if u want to drop the base and create a new one go to DB_connection and uncomment line 29
        //if u want to create the base uncomment
        /*
        try{
            initialiseDatabase();
            fill_database();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        new mainComponent();
    }
}
