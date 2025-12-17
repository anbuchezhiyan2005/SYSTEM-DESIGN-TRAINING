package Splitwise.strategy;

import Splitwise.models.User;

public interface PaymentStrategy {
    String getPaymentMethod();
    boolean pay(User sender, User receiver, double amount);
}
