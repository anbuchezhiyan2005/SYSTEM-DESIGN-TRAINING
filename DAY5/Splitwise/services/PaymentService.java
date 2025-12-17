package Splitwise.services;

import java.util.HashMap;
import java.util.Map;
import Splitwise.models.Transaction;
import Splitwise.models.User;
import Splitwise.strategy.PaymentStrategy;
import Splitwise.strategy.UpiPaymentStrategy;

public class PaymentService {
	private final Map<String, PaymentStrategy> strategies = new HashMap<>();

	public PaymentService() {
		registerStrategy(new UpiPaymentStrategy());
	}

	public void registerStrategy(PaymentStrategy strategy) {
		strategies.put(strategy.getPaymentMethod().toLowerCase(), strategy);
	}

	public Transaction processPayment(String paymentMethod, User sender, User receiver, double amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount must be positive");
		}
		if (sender == null || receiver == null) {
			throw new IllegalArgumentException("Sender and receiver are required");
		}

		PaymentStrategy strategy = strategies.get(paymentMethod.toLowerCase());
		if (strategy == null) {
			throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
		}

		boolean success = strategy.pay(sender, receiver, amount);
		if (!success) {
            System.out.println("Payment failed using method: " + paymentMethod);
			return null; 
		}

		return new Transaction(
			"TXN-" + System.currentTimeMillis(),
			sender,
			receiver,
			amount
		);
	}
}
