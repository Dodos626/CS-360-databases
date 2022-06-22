package swing;

import Control.Controller;
import database.classes.BaseCard;
import database.classes.DealerCard;
import database.classes.Employee;
import database.classes.Transactions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import static Control.MSGConstants.*;
import static database.classes.BaseCard.*;
import static database.classes.DealerCard.*;
import static database.classes.Employee.database_to_employee;
import static database.classes.Transactions.database_to_transaction;

public class mainComponent {
    JFrame f;

    public mainComponent() {
        f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createEntitysButtons();
        createAddButton();
        addTransaction();
        dealerOFTheMonth();
        deletaAccount();
        ListOfTransactions();
        findUser();

        f.setSize(1800, 1500);
        f.setLayout(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        new mainComponent();
    }

    /**
     * created the buttons for every entity of the database
     */
    public void createEntitysButtons() {
        String[] entitysArray = {individual_card, dealer_card, company_card, employee_card, transaction_card,
                GOOD_USERS, BAD_USERS};
        for (int i = 0; i < 7; i++) {
            String str = entitysArray[i];
            JButton i_btn = new JButton(entitysArray[i]);
            i_btn.setBounds(50 + (i * 120), 50, 120, 20);
            i_btn.addActionListener(e -> setUpEntityTableData(str));
            f.add(i_btn);
        }
    }

    /**
     * creates each entity's array and adds it to the frame.
     */
    public void setUpEntityTableData(String entity) {
        String[] column = {};
        switch (entity) {
            case individual_card:
                column = new String[6];
                setColumnsForIndividuals(column);
                break;
            case dealer_card:
                column = new String[5];
                setColumnsForDealers(column);
                break;
            case company_card:
                column = new String[6];
                setColumnsForCompanes(column);
                break;
            case employee_card:
                column = new String[3];
                setColumnsForEmployees(column);
                break;
            case transaction_card:
                column = new String[10];
                setColumnsForTransactions(column);
                break;
            case GOOD_USERS:
            case BAD_USERS:
                column = new String[3];
                setColumnsForGoodBadUsers(column);
                break;
            default:
                System.out.println("Something went wrong with the entity type");
        }
        DefaultTableModel model = new DefaultTableModel(column, 0);
        JTable i_jTable = new JTable(model);
        switch (entity) {
            case individual_card:
                getDataForSimpleCard(model, individual_card);
                break;
            case dealer_card:
                getDataForDealers(model);
                break;
            case company_card:
                getDataForSimpleCard(model, company_card);
                break;
            case employee_card:
                getDataForEmployees(model);
                break;
            case transaction_card:
                getDataForTransactions(model);
                break;
            case GOOD_USERS:
                getDataForGoodUsers(model);
                break;
            case BAD_USERS:
                getDataForBadUsers(model);
                break;
            default:
                System.out.println("Something went wrong with the entity type");
        }
        i_jTable.setBounds(300, 100, 100, 100);
        JScrollPane I_sp = new JScrollPane(i_jTable);
        I_sp.setBounds(50, 100, 800, 300);

        f.add(I_sp, BorderLayout.CENTER);
    }

    /**
     * For every entity they add the data to the array
     *
     * @param model
     * @param type
     */
    public void getDataForSimpleCard(DefaultTableModel model, String type) {
        ArrayList<BaseCard> carlist;
        carlist = database_to_simple_card(type);
        for (BaseCard baseCard : carlist) {
            int acc = baseCard.getAccount_number();
            String name = baseCard.getName();
            int lim = baseCard.getCredit_limit();
            int bal = baseCard.getBalance();
            String exp = baseCard.getExpiration_date();
            int owed = baseCard.getAmount_owned();

            Object[] data = {acc, name, lim, bal, exp, owed};
            model.insertRow(0, data);
        }
    }

    public void getDataForEmployees(DefaultTableModel model) {
        ArrayList<Employee> emplList;
        emplList = database_to_employee();
        for (Employee employee : emplList) {
            int id = employee.getId();
            String name = employee.getName();
            int comNum = employee.getCompany_number();

            Object[] data = {id, name, comNum};
            model.insertRow(0, data);
        }
    }

    public void getDataForDealers(DefaultTableModel model) {
        ArrayList<DealerCard> list;
        list = database_to_dealer_card();
        for (DealerCard dealerCard : list) {
            int acc = dealerCard.getAccount_number();
            String name = dealerCard.getName();
            double sup = dealerCard.getSupply();
            int bal = dealerCard.getBalance_from_profit();
            int owed = dealerCard.getAmount_owned();

            Object[] data = {acc, name, sup, bal, owed};
            model.insertRow(0, data);
        }
    }

    public void getDataForTransactions(DefaultTableModel model) {
        ArrayList<Transactions> list;
        list = database_to_transaction();
        for (Transactions transactions : list) {
            int id = transactions.getId();
            String pname = transactions.getPayer_name();
            String dname = transactions.getDealer_name();
            String trans = transactions.getTransaction_date();
            int am = transactions.getAmount();
            String type = transactions.getType();
            int pnum = transactions.getPayer_number();
            int dnum = transactions.getDealer_number();
            int em = transactions.getEmployee_id();

            Object[] data = {id, pname, dname, trans, am, type, pnum, dnum, em};
            model.insertRow(0, data);
        }
    }

    public void getDataForGoodUsers(DefaultTableModel model) {
        int acc;
        String name;
        String type;
        // dealers
        ArrayList<DealerCard> listDealer;
        listDealer = fetch_good_dealer_cards();
        for (DealerCard dealerCard : listDealer) {
            acc = dealerCard.getAccount_number();
            name = dealerCard.getName();
            type = dealer_card;
            Object[] data = {acc, name, type};
            model.insertRow(0, data);
        }
        // Individual
        ArrayList<BaseCard> list;
        list = fetch_good_base_cards(individual_card);
        for (BaseCard baseCard : list) {
            acc = baseCard.getAccount_number();
            name = baseCard.getName();
            type = individual_card;
            Object[] data = {acc, name, type};
            model.insertRow(0, data);
        }
        // companies
        list = fetch_good_base_cards(company_card);
        for (BaseCard baseCard : list) {
            acc = baseCard.getAccount_number();
            name = baseCard.getName();
            type = company_card;
            Object[] data = {acc, name, type};
            model.insertRow(0, data);
        }

    }

    public void getDataForBadUsers(DefaultTableModel model) {
        int acc;
        String name;
        String type;
        // dealers
        ArrayList<DealerCard> listDealer;
        listDealer = fetch_bad_dealer_cards();
        for (DealerCard dealerCard : listDealer) {
            acc = dealerCard.getAccount_number();
            name = dealerCard.getName();
            type = dealer_card;
            Object[] data = {acc, name, type};
            model.insertRow(0, data);
        }
        // Individual
        ArrayList<BaseCard> list;
        list = fetch_bad_base_cards(individual_card);
        for (BaseCard baseCard : list) {
            acc = baseCard.getAccount_number();
            name = baseCard.getName();
            type = individual_card;
            Object[] data = {acc, name, type};
            model.insertRow(0, data);
        }
        // companies
        list = fetch_bad_base_cards(company_card);
        for (BaseCard baseCard : list) {
            acc = baseCard.getAccount_number();
            name = baseCard.getName();
            type = company_card;
            Object[] data = {acc, name, type};
            model.insertRow(0, data);
        }

    }

    /**
     * For every entity they're setting the headers
     *
     * @param column
     */
    public void setColumnsForEmployees(String[] column) {
        column[0] = "id";
        column[1] = "name";
        column[2] = "company #";
    }

    public void setColumnsForDealers(String[] column) {
        column[0] = "account";
        column[1] = "name";
        column[2] = "supply";
        column[3] = "profit";
        column[4] = "amount owed";
    }

    public void setColumnsForCompanes(String[] column) {
        column[0] = "account";
        column[1] = "name";
        column[2] = "credit limit";
        column[3] = "balance";
        column[4] = "exp date";
        column[5] = "amount owed";
    }

    public void setColumnsForIndividuals(String[] column) {
        column[0] = "account #";
        column[1] = "name";
        column[2] = "credit limit";
        column[3] = "balance";
        column[4] = "exp date";
        column[5] = "amount owed";
    }

    public void setColumnsForTransactions(String[] column) {
        column[0] = "id";
        column[1] = "payer name";
        column[2] = "dealer name";
        column[3] = "trans date";
        column[4] = "amount";
        column[5] = "type";
        column[6] = "payer #";
        column[7] = "dealer #";
        column[8] = "Employee id";
    }

    public void setColumnsForGoodBadUsers(String[] column) {
        column[0] = "account #";
        column[1] = "name";
        column[2] = "typeof user";
    }

    /**
     * input: name
     * type
     * balance (optional)
     * company (optional)
     */
    public void createAddButton() {
        JButton createAccButton = new JButton("Create account");
        createAccButton.setBounds(50, 450, 200, 40);
        createAccButton.addActionListener(e -> {

            // radio button for the type
            JRadioButton rbIndividual = new JRadioButton(individual_card);
            rbIndividual.setBounds(50, 520, 100, 40);
            JRadioButton rbDealer = new JRadioButton(dealer_card);
            rbDealer.setBounds(180, 520, 100, 40);
            JRadioButton rbCompany = new JRadioButton(company_card);
            rbCompany.setBounds(310, 520, 100, 40);
            JRadioButton rbEmployee = new JRadioButton(employee_card);
            rbEmployee.setBounds(440, 520, 100, 40);
            ButtonGroup bg = new ButtonGroup();
            bg.add(rbIndividual);
            bg.add(rbCompany);
            bg.add(rbEmployee);
            bg.add(rbDealer);
            f.add(rbIndividual);
            f.add(rbCompany);
            f.add(rbEmployee);
            f.add(rbDealer);

            // input for balance
            JTextField tbalance = new JTextField("balance");
            tbalance.setBounds(50, 580, 200, 40);
            // input for name
            JTextField tname = new JTextField("name ");
            tname.setBounds(50, 660, 200, 40);
            // input for company
            JTextField tcompany = new JTextField(company_card);
            tcompany.setBounds(50, 720, 200, 40);
            // input employee id
            JTextField tempid = new JTextField("employee ID");
            tempid.setBounds(50, 780, 200, 40);
            f.add(tbalance);
            f.add(tname);
            f.add(tcompany);
            f.add(tempid);

            // submit button
            JButton submit = new JButton("submit");
            submit.setBounds(50, 840, 100, 40);
            // cancel button
            JButton cancel = new JButton("cancel");
            cancel.setBounds(150, 840, 100, 40);

            submit.addActionListener(e1 -> {
                String name = tname.getText();
                String type = "";
                String temp = tcompany.getText();
                int company = Integer.parseInt(temp);
                temp = tempid.getText();
                int emplid = Integer.parseInt(temp);
                temp = tbalance.getText();
                int balance = Integer.parseInt(temp);
                if (rbCompany.isSelected()) {
                    type = company_card;
                } else if (rbIndividual.isSelected()) {
                    type = individual_card;
                } else if (rbDealer.isSelected()) {
                    type = dealer_card;
                } else if (rbEmployee.isSelected()) {
                    type = employee_card;
                }

                /* Fix employee_id */
                printMessages(Controller.create_account(name, type, balance, company, emplid));

                f.remove(submit);
                f.remove(rbIndividual);
                f.remove(rbCompany);
                f.remove(rbDealer);
                f.remove(rbEmployee);
                f.remove(tbalance);
                f.remove(tcompany);
                f.remove(tname);
                f.remove(tempid);
                f.remove(cancel);
                f.revalidate();
                f.repaint();
            });

            cancel.addActionListener(e12 -> {
                f.remove(cancel);
                f.remove(submit);
                f.remove(rbIndividual);
                f.remove(rbCompany);
                f.remove(rbDealer);
                f.remove(rbEmployee);
                f.remove(tbalance);
                f.remove(tcompany);
                f.remove(tname);
                f.remove(tempid);
                f.revalidate();
                f.repaint();
            });
            f.add(cancel);
            f.add(submit);
        });
        f.add(createAccButton);
    }

    /**
     * The users gives the account number that wants to delete
     *
     * @option delete:
     * @option cancel: it deletes the "delete account" button
     */
    public void deletaAccount() {
        JButton deleteAccount = new JButton("Delete account");
        deleteAccount.setBounds(800, 450, 200, 40);
        deleteAccount.addActionListener(e -> {
            // input for date
            JTextField taccount = new JTextField("Account number");
            taccount.setBounds(800, 510, 200, 40);
            // submit button
            JButton submit = new JButton("submit");
            submit.setBounds(800, 570, 100, 40);
            // cancel button
            JButton cancel = new JButton("cancel");
            cancel.setBounds(900, 570, 100, 40);

            submit.addActionListener(e1 -> {
                String input = taccount.getText();
                int accountNum = Integer.parseInt(input);
                String message = Controller.delete_account(accountNum);
                printMessages(message);
                f.remove(submit);
                f.remove(taccount);
                f.remove(cancel);
                f.revalidate();
                f.repaint();
            });

            cancel.addActionListener(e12 -> {
                f.remove(cancel);
                f.remove(taccount);
                f.remove(submit);
                f.revalidate();
                f.repaint();
            });
            f.add(cancel);
            f.add(submit);
            f.add(taccount);
        });
        f.add(deleteAccount);
    }

    public void printMessages(String message) {

        JFrame fmsg = new JFrame();
        JOptionPane.showMessageDialog(fmsg, message);
    }

    /**
     * Creates a button "add transaction," , "submit" , "cancel"
     */
    public void addTransaction() {
        JButton addTransaction = new JButton("Add Transaction");
        addTransaction.setBounds(300, 450, 200, 40);
        addTransaction.addActionListener(e -> {
            // radio button for the type
            JRadioButton payment = new JRadioButton(PAYMENT);
            payment.setBounds(50, 520, 100, 40);
            JRadioButton purchase = new JRadioButton(PURCHASE);
            purchase.setBounds(180, 520, 100, 40);
            JRadioButton returnpur = new JRadioButton(PURCHASE_RETURNED);
            returnpur.setBounds(310, 520, 100, 40);
            ButtonGroup bg = new ButtonGroup();
            bg.add(payment);
            bg.add(returnpur);
            bg.add(purchase);
            f.add(payment);
            f.add(returnpur);
            f.add(purchase);

            // input for payername
            JTextField tpayername = new JTextField("payer name");
            tpayername.setBounds(300, 580, 200, 40);
            // input for dealerName
            JTextField tdealerName = new JTextField("dealer Name ");
            tdealerName.setBounds(300, 640, 200, 40);
            // input for transdate
            JTextField ttransdate = new JTextField("YYYY-MM-DD");
            ttransdate.setBounds(300, 700, 200, 40);
            // input employee id
            JTextField tempid = new JTextField("employee ID");
            tempid.setBounds(300, 760, 200, 40);
            // input amount id
            JTextField tamount = new JTextField("amount");
            tamount.setBounds(300, 820, 200, 40);
            // input payer number
            JTextField tpayernum = new JTextField("payer number");
            tpayernum.setBounds(300, 880, 200, 40);
            // input dealer number
            JTextField tdealernum = new JTextField("dealer number");
            tdealernum.setBounds(300, 940, 200, 40);
            f.add(tamount);
            f.add(tdealernum);
            f.add(tpayernum);
            f.add(tpayername);
            f.add(tdealerName);
            f.add(ttransdate);
            f.add(tempid);

            // submit button
            JButton submit = new JButton("submit");
            submit.setBounds(300, 1000, 100, 40);
            // cancel button
            JButton cancel = new JButton("cancel");
            cancel.setBounds(400, 1000, 100, 40);

            submit.addActionListener(e1 -> {
                String payer_name = tpayername.getText();
                String dealer_name = tdealerName.getText();
                String transaction_date = ttransdate.getText();
                String temp;
                temp = tamount.getText();
                int amount = Integer.parseInt(temp);
                temp = tpayernum.getText();
                int payer_number = Integer.parseInt(temp);
                temp = tdealernum.getText();
                int dealer_number = Integer.parseInt(temp);
                temp = tempid.getText();
                int employee_id = Integer.parseInt(temp);
                temp = tempid.getText();
                String type = "";
                if (payment.isSelected()) {
                    type = PAYMENT;
                } else if (purchase.isSelected()) {
                    type = PURCHASE;
                } else if (returnpur.isSelected()) {
                    type = PURCHASE_RETURNED;
                }

                Transactions transaction = new Transactions(0, payer_name, dealer_name, transaction_date, amount,
                        type, payer_number, dealer_number, employee_id);


                printMessages(Controller.insert_transaction(transaction));

                f.remove(tamount);
                f.remove(tdealernum);
                f.remove(tpayernum);
                f.remove(tpayername);
                f.remove(tdealerName);
                f.remove(ttransdate);
                f.remove(tempid);
                f.remove(payment);
                f.remove(returnpur);
                f.remove(purchase);
                f.remove(cancel);
                f.remove(submit);
                f.revalidate();
                f.repaint();
            });

            cancel.addActionListener(e12 -> {
                f.remove(tamount);
                f.remove(tdealernum);
                f.remove(tpayernum);
                f.remove(tpayername);
                f.remove(tdealerName);
                f.remove(ttransdate);
                f.remove(tempid);
                f.remove(payment);
                f.remove(returnpur);
                f.remove(purchase);
                f.remove(cancel);
                f.remove(submit);
                f.revalidate();
                f.repaint();
            });
            f.add(cancel);
            f.add(submit);
        });
        f.add(addTransaction);
    }

    public void dealerOFTheMonth() {
        JButton showDealer = new JButton("Dealer of the month");
        showDealer.setBounds(550, 450, 200, 40);
        showDealer.addActionListener(e -> {
            // input for date
            JTextField tdate = new JTextField("YYYY-MM");
            tdate.setBounds(550, 510, 200, 40);
            // submit button
            JButton submit = new JButton("submit");
            submit.setBounds(550, 570, 100, 40);
            // cancel button
            JButton cancel = new JButton("cancel");
            cancel.setBounds(650, 570, 100, 40);

            submit.addActionListener(e1 -> {
                String date = tdate.getText();
                DealerCard dealer;
                dealer = Controller.dealer_of_the_month(date);
                if (dealer == null) {
                    printMessages("They were no transaction this period of time");
                    return;
                }
                String message = "The dealer of the month " + date + " is " + dealer.getName();
                printMessages(message);
                f.remove(submit);
                f.remove(tdate);
                f.remove(cancel);
                f.revalidate();
                f.repaint();
            });

            cancel.addActionListener(e12 -> {
                f.remove(cancel);
                f.remove(tdate);
                f.remove(submit);
                f.revalidate();
                f.repaint();
            });
            f.add(cancel);
            f.add(submit);
            f.add(tdate);
        });
        f.add(showDealer);
    }

    public void ListOfTransactions() {
        JButton searchTransaction = new JButton("Search Transactions");
        searchTransaction.setBounds(1050, 450, 200, 40);
        searchTransaction.addActionListener(e -> {
            // input for min date
            JTextField mindate = new JTextField("min date");
            mindate.setBounds(1050, 570, 200, 40);
            // input for max date
            JTextField maxdate = new JTextField("max date");
            maxdate.setBounds(1050, 630, 200, 40);
            // input for account id
            JTextField id = new JTextField("account id");
            id.setBounds(1050, 690, 200, 40);
            // input for empoloyee id
            JTextField employee = new JTextField("employee id (if company)");
            employee.setBounds(1050, 750, 200, 40);
            // submit button
            JButton submit = new JButton("submit");
            submit.setBounds(1050, 810, 100, 40);
            // cancel button
            JButton cancel = new JButton("cancel");
            cancel.setBounds(1150, 810, 100, 40);

            // radio button for the type
            JRadioButton payment = new JRadioButton(individual_card);
            payment.setBounds(1050, 520, 100, 40);
            JRadioButton purchase = new JRadioButton(dealer_card);
            purchase.setBounds(1150, 520, 100, 40);
            JRadioButton returnpur = new JRadioButton(company_card);
            returnpur.setBounds(1250, 520, 100, 40);
            ButtonGroup bg = new ButtonGroup();
            bg.add(payment);
            bg.add(returnpur);
            bg.add(purchase);
            f.add(payment);
            f.add(returnpur);
            f.add(purchase);


            submit.addActionListener(e1 -> {
                String num = id.getText();
                int accountNum = Integer.parseInt(num);
                String empl = employee.getText();
                String[] empls = empl.split(",");
                ArrayList<Integer> ids = new ArrayList<>();
                for (String i : empls) {
                    ids.add(Integer.parseInt(i));
                }
                ArrayList<Transactions> list = null;
                if (payment.isSelected()) {
                    list = Controller.get_individual_transactions(mindate.getText(), maxdate.getText(), accountNum);
                } else if (purchase.isSelected()) {
                    list = Controller.get_dealer_transactions(mindate.getText(), maxdate.getText(), accountNum);
                } else if (returnpur.isSelected()) {
                    list = Controller.get_company_transactions(mindate.getText(), maxdate.getText(), accountNum, ids);
                }

                // list = database_to_transaction();
                // creates new array for the transactions
                String[] column = new String[10];
                setColumnsForTransactions(column);
                DefaultTableModel model = new DefaultTableModel(column, 0);
                JTable i_jTable = new JTable(model);
                if (list != null) {
                    for (Transactions transactions : list) {
                        int id1 = transactions.getId();
                        String pname = transactions.getPayer_name();
                        String dname = transactions.getDealer_name();
                        String trans = transactions.getTransaction_date();
                        int am = transactions.getAmount();
                        String type = transactions.getType();
                        int pnum = transactions.getPayer_number();
                        int dnum = transactions.getDealer_number();
                        int em = transactions.getEmployee_id();

                        Object[] data = {id1, pname, dname, trans, am, type, pnum, dnum, em};
                        model.insertRow(0, data);
                    }
                }
                i_jTable.setBounds(300, 100, 100, 100);
                JScrollPane I_sp = new JScrollPane(i_jTable);
                I_sp.setBounds(50, 100, 800, 300);
                f.add(I_sp);

            });

            cancel.addActionListener(e12 -> {
                f.remove(cancel);
                f.remove(mindate);
                f.remove(maxdate);
                f.remove(payment);
                f.remove(returnpur);
                f.remove(purchase);
                f.remove(employee);
                f.remove(id);
                f.remove(submit);
                f.revalidate();
                f.repaint();
            });
            f.add(cancel);
            f.add(submit);
            f.add(mindate);
            f.add(maxdate);
            f.add(id);
            f.add(employee);
        });
        f.add(searchTransaction);
    }

    public void findUser() {
        JButton showDealer = new JButton("Find Client");
        showDealer.setBounds(1300, 450, 200, 40);
        showDealer.addActionListener(e -> {
            // input for date
            JTextField tdate = new JTextField("Client Id");
            tdate.setBounds(1300, 510, 200, 40);
            // submit button
            JButton submit = new JButton("submit");
            submit.setBounds(1300, 570, 100, 40);
            // cancel button
            JButton cancel = new JButton("cancel");
            cancel.setBounds(1400, 570, 100, 40);

            submit.addActionListener(e1 -> {
                String date = tdate.getText();
                BaseCard individual;
                individual = Controller.get_client_info(Integer.parseInt(date));
                String message = "Users " + individual.getName() + "\naccount number: " + individual.getAccount_number() + "\nBalance: " + individual.getBalance() + "\nCredit Limit: " + individual.getCredit_limit() + "\nExpiration Date: " + individual.getExpiration_date() + "\nAmount owed: " + individual.getAmount_owned();
                printMessages(message);
                f.remove(submit);
                f.remove(tdate);
                f.remove(cancel);
                f.revalidate();
                f.repaint();
            });

            cancel.addActionListener(e12 -> {
                f.remove(cancel);
                f.remove(tdate);
                f.remove(submit);
                f.revalidate();
                f.repaint();
            });
            f.add(cancel);
            f.add(submit);
            f.add(tdate);
        });
        f.add(showDealer);
    }
}
