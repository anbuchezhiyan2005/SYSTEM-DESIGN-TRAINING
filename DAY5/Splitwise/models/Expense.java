// Expense class: userName, expenseName, totalExpenseAmount, previousTransactionsList, totalDebtAmount, listOfDebts
package Splitwise.models;

import java.util.List;

public class Expense {
    private String userName;
    private String expenseName;
    private double totalExpenseAmount;
    private double totalDebtAmount;
    private List<Transaction> previousTransactionsList;
    private List<Debt> listOfDebts;

    public Expense(String userName, String expenseName, double totalExpenseAmount, double totalDebtAmount, List<Transaction> previousTransactionsList, List<Debt> listOfDebts) {
        this.userName = userName;
        this.expenseName = expenseName;
        this.totalExpenseAmount = totalExpenseAmount;
        this.totalDebtAmount = totalDebtAmount;
        this.previousTransactionsList = previousTransactionsList;
        this.listOfDebts = listOfDebts;
    }

    public String getUserName() {
        return userName;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public double getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public double getTotalDebtAmount() {
        return totalDebtAmount;
    }

    public List<Transaction> getPreviousTransactionsList() {
        return previousTransactionsList;
    }

    public List<Debt> getListOfDebts() {
        return listOfDebts;
    }
}
