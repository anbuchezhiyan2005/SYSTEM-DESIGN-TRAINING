// STRATEGY INTERFACE
interface PaymentStrategy {
    void pay(double amt);
}

//CONCRETE STRATEGY (CARD)
class CardPaymentStrategy implements PaymentStrategy {
    public void pay(double amt) {
        System.out.println("Paying " + amt + " using card");
    }
}

// CONCRETE STRATEGY (UPI)
class UpiPaymentStrategy implements PaymentStrategy {
    public void pay(double amt) {
        System.out.println("Paying " + amt + " using UPI");
    }
}

// CONTEXT
class Checkout {
    private PaymentStrategy paymentStrategy;
    public Checkout(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    public void doPayment(double amt) {
        paymentStrategy.pay(amt);
    }
}

public class StrategyPattern {
    public static void main(String args[]) {
        Checkout myCheckout = new Checkout(new CardPaymentStrategy());
        myCheckout.doPayment(10.0); 
    }
}