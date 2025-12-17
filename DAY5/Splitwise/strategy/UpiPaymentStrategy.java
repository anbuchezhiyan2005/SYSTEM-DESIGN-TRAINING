package Splitwise.strategy;

import Splitwise.models.User;
import Splitwise.strategy.PaymentStrategy;

public class UpiPaymentStrategy implements PaymentStrategy {
    @Override
    public String getPaymentMethod() {
        return "Cash";
    }

    @Override
    public boolean pay(User sender, User receiver, double amount) {
        sender.Pay(amount, "UPI Payment to " + receiver.getUserName());
        receiver.Receive(amount, "Cash Payment from " + sender.getUserName());
        return true;
    }
}

