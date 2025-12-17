package Splitwise.strategy;

import Splitwise.models.User;
import Splitwise.strategy.PaymentStrategy;

public class NetBankingStrategy implements PaymentStrategy {
    @Override
    public String getPaymentMethod() {
        return "Net Banking";
    }

    @Override
    public boolean pay(User sender, User receiver, double amount) {
        sender.Pay(amount, "Net Banking Payment to " + receiver.getUserName());
        receiver.Receive(amount, "Net Banking Payment from " + sender.getUserName());
        return true;
    }
    
}
