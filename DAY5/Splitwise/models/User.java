// User class: userName, userAccountNumber, userBalance, userFriends
package Splitwise.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userName;
    private String userAccountNumber;
    private double userBalance;
    private List<User> userFriends;

    public User(String userName, String userAccountNumber, double userBalance) {
        this.userName = userName;
        this.userAccountNumber = userAccountNumber;
        this.userBalance = userBalance;
        this.userFriends = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAccountNumber() {
        return userAccountNumber;
    }

    public double getUserBalance() {
        return userBalance;
    }

    public List<User> getUserFriends() {
        return userFriends;
    }

    public void addFriend(User friend) {
        this.userFriends.add(friend);
    }

    public void removeFriend(User friend) {
        this.userFriends.remove(friend);
    }

    public void Receive(double amount, String fromAccountNumber) {
        this.userBalance += amount;
        System.out.println(this.userName + " received " + amount + " from account " + fromAccountNumber);
    }

    public void Pay(double amount, String toAccountNumber) {
        this.userBalance -= amount;
        System.out.println(this.userName + " paid " + amount + " to account " + toAccountNumber);
    }

    

}
