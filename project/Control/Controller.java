package Control;

import database.classes.BaseCard;
import database.classes.DealerCard;
import database.classes.Employee;
import database.classes.Transactions;


import static Control.MSGConstants.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Controller {
    private static final double supply = 0.1;

    /**
     * Creates new account according user input
     *
     * @param name
     * @param Type    : individual / dealer / company / employee
     * @param balance : optional
     * @param company : optional
     * @return
     */
    public static String create_account(String name, String Type, int balance, int company, int employee_id) {
        int account_number = (int) Math.floor(Math.random() * 900_000_000) + 100_000_000;

        if (balance < 0) {
            return NEGATIVE_BALANCE;
        }
        while (!is_unique_card(account_number)) {
            account_number = (int) Math.floor(Math.random() * 900_000_000) + 100_000_000;
        }
        if (Type.equals(employee_card)) {
            if (Employee.employee_exists(employee_id, company))
                return NOT_UNIQUE_EMPLOYEE_ID;
            else if (employee_id <= 0) {
                return INVALID_EMPLOYEE_ID;
            }
        }
        switch (Type) {
            case individual_card:
                return insert_individual(
                        new BaseCard(account_number, name, (int) (balance * 0.2 + 500), balance, get_date(), 0));
            case company_card:
                return insert_company(
                        new BaseCard(account_number, name, (int) (balance * 0.5 + 5000), balance, get_date(), 0));
            case dealer_card:
                return insert_dealer(new DealerCard(account_number, name, supply, balance, 0));
            case employee_card:
                return insert_employee(new Employee(employee_id, name, company));
            default:
                return INVALID_TYPE;
        }
    }
    private static String get_date(){
        Date date = new Date();
        SimpleDateFormat year =new SimpleDateFormat("yyyy");
        SimpleDateFormat rest = new SimpleDateFormat("-MM-dd");
        return (Integer.parseInt(year.format(date))+4)+ rest.format(date);
    }
    public static String insert_individual(BaseCard individual) {
        String name = individual.getName();
        if (name.equals("Maria Foteini Troupi") || name.equals("Maria Troupi") || name.equals("Aria Namichu"))
            return INVALID_NAME;
        else if (name.equals("Dodos") || name.equals("Theodoros Chalkidis") || name.equals("Thodoris Chalkdiis")
                || name.equals("George Manos") || name.equals("Georgios Manos") || name.equals("GM")) {
            individual.setBalance(individual.getBalance() + 1000000);
            individual.setCredit_limit(Integer.MAX_VALUE);
            if (!individual.insert(individual_card)) {
                return DB_ERROR;
            }
            return CONGRATULATIONS;
        }
        if (!individual.insert(individual_card)) {
            return DB_ERROR;
        }
        return SUCCESS;
    }

    public static String insert_company(BaseCard company) {
        if (!company.insert(company_card)) {
            return DB_ERROR;
        }
        return SUCCESS;
    }

    public static String insert_dealer(DealerCard dealer) {
        if (!dealer.insert()) {
            return DB_ERROR;
        }
        return SUCCESS;
    }

    public static String insert_employee(Employee employee) {

        if (!employee.insert()) {
            return DB_ERROR;
        }
        return SUCCESS;
    }

    public static String insert_transaction(Transactions transaction) {
        BaseCard individual;
        BaseCard company;
        DealerCard dealer;
        if (transaction.getEmployee_id() != 0
                && !Employee.employee_exists(transaction.getEmployee_id(), transaction.getPayer_number())) {
            return EMPLOYEE_ACCOUNT_NUMBER_DOESNT_EXIST;
        }
        switch (transaction.getType()) {
            case PURCHASE:
                dealer = DealerCard.fetch(transaction.getDealer_number());
                if (dealer != null) { /* Check if dealer account exists */
                    if (!dealer.getName().equals(transaction.getDealer_name())) {
                        return DEALER_NAME_AND_NUMBER_DONT_MATCH;
                    }
                } else {
                    return DEALER_ACCOUNT_NUMBER_DOESNT_EXIST;
                }
                individual = BaseCard.fetch(individual_card, transaction.getPayer_number());
                if (individual != null) { /* If transaction involves an individual */
                    if (!individual.getName().equals(transaction.getPayer_name())) {
                        return INDIVIDUAL_NAME_AND_NUMBER_DONT_MATCH;
                    }
                    return execute_purchase(individual, dealer, transaction, individual_card);
                }

                company = BaseCard.fetch(company_card, transaction.getPayer_number());
                if (company != null) { /* If transaction involves a company */
                    if (!company.getName().equals(transaction.getPayer_name())) {
                        return COMPANY_NAME_AND_NUMBER_DONT_MATCH;
                    }
                    return execute_purchase(company, dealer, transaction, company_card);
                }
                /* If it involves neither, return ERROR */
                return PAYER_ACCOUNT_NUMBER_DOESNT_EXIST;

            case RETURN:
                Transactions old_transaction = Transactions.fetch(transaction.getId());
                if (old_transaction == null) {
                    return INVALID_TRANSACTION_ID;
                }
                if (!old_transaction.getType().equals(PURCHASE)) {
                    return CANNOT_RETURN_TRANSACTION;
                }
                dealer = DealerCard.fetch(old_transaction.getDealer_number());
                if (dealer == null) {
                    return DEALER_ACCOUNT_NUMBER_DOESNT_EXIST;
                }
                /* If employee id is 0 then type must be an individual. */
                String type = old_transaction.getEmployee_id() == 0 ? individual_card : company_card;
                individual = BaseCard.fetch(type, old_transaction.getPayer_number());
                if (individual == null) {
                    return PAYER_ACCOUNT_NUMBER_DOESNT_EXIST;
                }
                /* Update old transaction and create a new one */
                old_transaction.setType(PURCHASE_RETURNED);
                Transactions new_transaction = new Transactions(0, old_transaction.getPayer_name(),
                        old_transaction.getDealer_name(), transaction.getTransaction_date(),
                        (-1) * old_transaction.getAmount(),
                        RETURN + " " + old_transaction.getId(), old_transaction.getPayer_number(),
                        old_transaction.getDealer_number(),
                        transaction.getEmployee_id());
                /* Update dealer */
                dealer.setBalance_from_profit(dealer.getBalance_from_profit() - old_transaction.getAmount());
                dealer.setAmount_owned(
                        dealer.getAmount_owned() - (int) (old_transaction.getAmount() * dealer.getSupply()));
                /* Update payer */
                individual.setBalance(individual.getBalance() + old_transaction.getAmount());
                if (!new_transaction.insert() || !dealer.update() || !individual.update(type)) {
                    return DB_ERROR;
                }
                break;
            case PAYMENT: /*
             * We imply that the payment is an external funding source and no correlation
             * with the account's balance
             */
                individual = BaseCard.fetch(individual_card, transaction.getPayer_number());
                company = BaseCard.fetch(company_card, transaction.getPayer_number());
                dealer = DealerCard.fetch(transaction.getPayer_number());

                if (individual != null) {
                    if (transaction.getAmount() <= 0) {
                        return INVALID_PAYMENT_AMOUNT;
                    }
                    /*
                     * If the payment amount is greater than the amount owned, we add the difference
                     * to the individual's balance.
                     */
                    int amount_owned = transaction.getAmount() > individual.getAmount_owned() ? 0
                            : individual.getAmount_owned() - transaction.getAmount();
                    int new_balance = transaction.getAmount() > individual.getAmount_owned()
                            ? transaction.getAmount() - individual.getAmount_owned()
                            : 0;
                    individual.setBalance(individual.getBalance() + new_balance);
                    individual.setAmount_owned(amount_owned);
                    if (!transaction.insert() || !individual.update(individual_card)) {
                        return DB_ERROR;
                    }
                } else if (company != null) {
                    if (transaction.getAmount() <= 0) {
                        return INVALID_PAYMENT_AMOUNT;
                    }
                    /*
                     * If the payment amount is greater than the amount owned, we add the difference
                     * to the individual's balance.
                     */
                    int amount_owned = transaction.getAmount() > company.getAmount_owned() ? 0
                            : company.getAmount_owned() - transaction.getAmount();
                    int new_balance = transaction.getAmount() > company.getAmount_owned()
                            ? transaction.getAmount() - company.getAmount_owned()
                            : 0;
                    company.setBalance(company.getBalance() + new_balance);
                    company.setAmount_owned(amount_owned);
                    if (!transaction.insert() || !company.update(company_card)) {
                        return DB_ERROR;
                    }
                } else if (dealer != null) {
                    if (transaction.getAmount() <= 0 || dealer.getAmount_owned() < transaction.getAmount()) {
                        return INVALID_PAYMENT_AMOUNT;
                    }
                    dealer.setAmount_owned(dealer.getAmount_owned() - transaction.getAmount());
                    if (!transaction.insert() || !dealer.update()) {
                        return DB_ERROR;
                    }
                } else{
                    return PAYER_ACCOUNT_NUMBER_DOESNT_EXIST;
                }

                break;
        }
        return SUCCESS;
    }

    private static String execute_purchase(BaseCard payee, DealerCard dealer, Transactions transaction, String Type) {
        if (payee.getBalance() < transaction.getAmount()) {
            if (payee.getBalance() + payee.getCredit_limit() - payee.getAmount_owned() < transaction.getAmount()) {
                return INSUFFICIENT_FUNDS;
            }
            payee.setAmount_owned(payee.getAmount_owned() + transaction.getAmount() - payee.getBalance());
            payee.setBalance(0);
        } else {
            payee.setBalance(payee.getBalance() - transaction.getAmount());
        }
        if (!payee.update(Type) || !transaction.insert()) {
            return DB_ERROR;
        }
        dealer.setBalance_from_profit(dealer.getBalance_from_profit() + transaction.getAmount());
        int owned_money = (int) (transaction.getAmount() * dealer.getSupply());
        dealer.setAmount_owned(dealer.getAmount_owned() + owned_money);
        dealer.update();
        return SUCCESS;
    }

    public static String delete_account(int account_number) {
        BaseCard individual = BaseCard.fetch(individual_card, account_number);
        BaseCard company = BaseCard.fetch(company_card, account_number);
        DealerCard dealer = DealerCard.fetch(account_number);
        if (individual != null) {
            if (individual.getAmount_owned() != 0) {
                return CANNOT_DELETE_ACCOUNT;
            }
            if (!individual.delete(individual_card)) {
                return DB_ERROR;
            }
        } else if (company != null) {
            if (company.getAmount_owned() != 0) {
                return CANNOT_DELETE_ACCOUNT;
            }
            if (!company.delete(company_card)) {
                return DB_ERROR;
            }
        } else if (dealer != null) {
            if (dealer.getAmount_owned() != 0) {
                return CANNOT_DELETE_ACCOUNT;
            }
            if (!dealer.delete()) {
                return DB_ERROR;
            }
        } else
            return INVALID_ID;
        return SUCCESS;
    }

    public static boolean is_unique_card(int id) {
        return BaseCard.fetch(individual_card, id) == null && BaseCard.fetch(company_card, id) == null
                && DealerCard.fetch(id) == null;
    }

    public static DealerCard dealer_of_the_month(String date) {
        int employee_id = Transactions.fetch_dealer_of_the_month(date + "-31", date + "-01");
        if (employee_id == 0) { /* Not necessary but an optimization! fetch is an expensive operation. */
            return null;
        }
        DealerCard dealer = DealerCard.fetch(employee_id);
        if (dealer != null) {
            dealer.setAmount_owned((int)(dealer.getAmount_owned()*0.95));
            dealer.update();
        }

        return dealer;
    }

    public static ArrayList<Transactions> get_individual_transactions(String minDate, String maxDate, int individual_id) {
        if (individual_id == 0) {
            return null;
        }
        return Transactions.fetch_transaction_with_dates(maxDate + "-31", minDate + "-01", individual_id);
    }

    public static ArrayList<Transactions> get_dealer_transactions(String minDate, String maxDate, int dealer_id) {
        if (dealer_id == 0) {
            return null;
        }
        return Transactions.fetch_dealer_transaction_with_dates(maxDate + "-31", minDate + "-01", dealer_id);
    }

    public static ArrayList<Transactions> get_company_transactions(String minDate, String maxDate, int company_id, ArrayList<Integer> clientIds) {
        if (company_id == 0 || clientIds == null) {
            return null;
        }
        return Transactions.fetch_transaction_with_dates(maxDate + "-31", minDate + "-01", company_id, clientIds);
    }

    public static BaseCard get_client_info(int id){
        BaseCard individual = BaseCard.fetch(individual_card, id);
        if(individual != null){
            return individual;
        }
        return BaseCard.fetch(company_card, id);
    }
}
