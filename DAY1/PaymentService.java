// public class PaymentService {
//     private class PaymentMethod;
//     public PaymentService(String type) {
//         if(type.equals("Credit Card")) {
//             PaymentMethod = new CreditCardService(); 
//         }

//         if(type.equals("PayPal")) {
//             PaymentMethod = new PayPalService();
//         }

//         if(type.equals("Netbanking")) {
//             PaymentMethod = new NetbankingService();
//         }
//     }
// }

// public class main() {
//     public static void main(String args[]) {
//         class PaymentService payment = new PaymentService("Credit Card");
//         payment.pay();
//     }
// }


// interface Human {
//     void work();
//     void eat();
// }

// interface Robot {
//     void work();
// }

// public class Alan extends Human() {
//     public void work() {
//         System.out.println("Alan is working");
//     }

//     public void eat() {
//         System.out.println("Alan is eating");
//     }
// }

// public class Drone extends Robot() {
//     public void work() {
//         System.out.println("Drone is working");
//     }
// }


// public class BookingService {
//     Customer customer;
//     Show show;
//     Ticket ticket;
//     int numberOfSeats;

//     public BookingService(Customer customer, Show show, int numberOfSeats) {
//         this.customer = customer;
//         this.show = show;
//         this.numberOfSeats = numberOfSeats;
//     }

//     public void generateTicket() {
//         double totalAmount = show.getSeatPrice() * numberOfSeats;
//         ticket = new Ticket(customer, show, numberOfSeats, totalAmount);
//     }
// }


// interface Payment {
//     boolean pay(double amount);
// }

// public class CreditCardService implements Payment {
//     public boolean pay(double amount) {
//         System.out.println("Paid " + amount + " using Credit Card");
//         return true;
//     }
// }

// public class PayPalService implements Payment {
//     public boolean pay(double amount) {
//         System.out.println("Paid " + amount + " using PayPal");
//         return true;
//     }
// }

// public class Main {
//     public static void main(String args[]) {
//         class Customer customer = new Customer("Alan", 1234567890);
//         class Show show = new show("movie1", 75, "9:15 PM", "14A");
//         class BookingService booking = new BookingService(customer, show, 2);
//         System.out.println("Choose payment method (Credit Card, PayPal, Netbanking): ");
//         Scanner scanner = new Scanner(System.in);
//         String method = scanner.nextLine();
//         if(method.equals("Credit Card")) {
//             Payment payment = new CreditCardService();
//             payment.pay(100.0);
//         } else if(method.equals("PayPal")) {
//             Payment payment = new PayPalService();
//             payment.pay(100.0);
//         } else {
//             System.out.println("Invalid payment method");
//         }
//     }
// }

// coffee vending machine

