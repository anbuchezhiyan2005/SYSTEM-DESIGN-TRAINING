// Transaction class: transactionID, sender, reciever, transactionAmount, transactionDateTime

package Splitwise.models;

import java.time.LocalDateTime;

public class Transaction {
    private String transactionID;
    private User sender;
    private User reciever;
    private double transactionAmount;
    private LocalDateTime transactionDateTime;

    public Transaction(String transactionID, User sender, User reciever, double transactionAmount) {
        this.transactionID = transactionID;
        this.sender = sender;
        this.reciever = reciever;
        this.transactionAmount = transactionAmount;
        this.transactionDateTime = LocalDateTime.now();
    }

    public String getTransactionID() {
        return transactionID;
    }

    public User getSender() {
        return sender;
    }

    public User getReciever() {
        return reciever;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }
}

