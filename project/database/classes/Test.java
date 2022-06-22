package database.classes;
import java.util.ArrayList;

import static database.classes.BaseCard.*;
import static database.classes.DealerCard.database_to_dealer_card;
import static database.classes.DealerCard.dealer_card_exists;
import static database.classes.Employee.database_to_employee;
import static database.classes.Transactions.database_to_transaction;
import static database.classes.Transactions.fetch_transaction_with_dates;

import static Control.MSGConstants.*;
public class Test {

    //main for testing purpose
    public static void main(String[] args) {
        System.out.println("individual cards");
        for (BaseCard card : database_to_simple_card("individual_card")){
            card.print();
        }

        System.out.println("company cards");
        for (BaseCard card : database_to_simple_card("company_card")){
            card.print();
        }
        System.out.println("Transactions");
        for (Transactions trans: database_to_transaction()){
            trans.print();
        }
        System.out.println("Employee");
        for (Employee _employee: database_to_employee()){
            _employee.print();
        }
        System.out.println("DealerCard");
        for (DealerCard dealer_cards: database_to_dealer_card()){
            dealer_cards.print();
        }

        System.out.println(base_card_exists("individual_card",444,"anastashs"));
        System.out.println(base_card_exists("individual_card",123456789,"Theodoros Petrou"));

        System.out.println(dealer_card_exists(444,"giwrgos"));
        System.out.println(dealer_card_exists(849302012, "Dimitris Sirinas"));


        BaseCard __card = new BaseCard(123456788,"petros papous",0,0,"2022-10-13",0);
        System.out.println(__card.insert(individual_card));

        __card.setBalance(1000000);
        System.out.println(__card.update(individual_card));
        __card.print();

        System.out.println(__card.delete(individual_card));

        Transactions __trans = new Transactions(0,"seto kaiba","dealer","2022-11-13",400,"Payment",537485903,123456667,3);
        System.out.println(__trans.insert());
         __trans = new Transactions(0,"seto kaiba","dealer","2011-11-13",400,"Payment",537485903,123456667,2);
        System.out.println(__trans.insert());

        System.out.println("Transactions with dates");
        for (Transactions trans: fetch_transaction_with_dates("2023-01-11","2012-01-11",537485903)){
            trans.print();
        }
        System.out.println("Transactions with dates");
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(2);
        ids.add(1);
        ids.add(3);
        for (Transactions trans: fetch_transaction_with_dates("2023-01-11","2012-01-11",537485903,ids)){
            trans.print();
        }

        System.out.println("bad company cards");
        for (BaseCard card : fetch_bad_base_cards("company_card")){
            card.print();
        }
        System.out.println("good company cards");

        for (BaseCard card : fetch_good_base_cards("company_card")){
            card.print();
        }
        System.out.println("deleting individual");
        BaseCard __del_individual = new BaseCard(245687542,"",0,0,"",0);
        System.out.println(__del_individual.delete(individual_card));
        System.out.println("deleting company");
        BaseCard __del_company = new BaseCard(203265015,"",0,0,"",0);
        System.out.println(__del_company.delete(company_card));

        System.out.println(Transactions.fetch_dealer_of_the_month("2022-01-31","2022-01-01"));
    }
}
