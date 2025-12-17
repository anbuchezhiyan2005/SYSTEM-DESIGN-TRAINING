class LegacyPaymentService {
    void LegacyPaymentMethod(double amt) {
        System.out.println("Processing payment of $" + amt + " using Legacy Payment Service");
    }
}

interface NewPaymentPattern {
    void pay(double amt);
}

class NewPaymentService implements NewPaymentPattern {
    LegacyPaymentService legacyPaymentService = new LegacyPaymentService();
    public void pay(double amt) {
        legacyPaymentService.LegacyPaymentMethod(amt);
    }
}

public class AdapterPattern {
    public static void main(String args[]) {
        NewPaymentPattern paymentService = new NewPaymentService();
        paymentService.pay(25.00);
    }
}
