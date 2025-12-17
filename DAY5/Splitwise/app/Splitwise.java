package Splitwise.app;
import java.util.*;
import Splitwise.models.*;
import Splitwise.services.PaymentService;

public class Splitwise {
    // private List<Group> groups = new ArrayList<>();
    private List<Debt> allDebts = new ArrayList<>();
    private List<Transaction> allTransactions = new ArrayList<>();
    private PaymentService paymentService;

    public Splitwise() {
        this.paymentService = new PaymentService();
    }

    public void addExpenseToGroup(Group group, User paidBy, String description, double amount) {
        // Bill bill = new Bill("BILL-" + System.currentTimeMillis(), description, amount);
        int memberCount = group.getGroupMembers().size();
        double perPersonShare = amount / memberCount;

        System.out.println("\n " + paidBy.getUserName() + " paid " + amount + " for: " + description);
        System.out.println(" Split equally among " + memberCount + " members: " + perPersonShare + " each");

        for (User member : group.getGroupMembers()) {
            if (!member.equals(paidBy)) {
                Debt debt = new Debt(
                    "DEBT-" + System.currentTimeMillis() + "-" + member.getUserName(),
                    paidBy,
                    perPersonShare,
                    "PENDING"
                );
                allDebts.add(debt);
                System.out.println("  → " + member.getUserName() + " owes " + paidBy.getUserName() + ": " + perPersonShare);
            }
        }
    }

    public void settleDebt(Debt debt, User borrower, String paymentMethod) {
        if (!"PENDING".equals(debt.getDebtStatus())) {
            System.out.println(" Debt already settled");
            return;
        }

        System.out.println("\n Processing payment via " + paymentMethod + "...");
        Transaction txn = paymentService.processPayment(
            paymentMethod,
            borrower,
            debt.getLender(),
            debt.getDebtAmount()
        );

        if (txn != null) {
            debt.setDebtStatus("PAID");
            allTransactions.add(txn);
            System.out.println(" Payment successful! Transaction ID: " + txn.getTransactionID());
        } else {
            System.out.println(" Payment failed");
        }
    }

    public void showPendingDebts(User user) {
        System.out.println("\n === Pending debts for " + user.getUserName() + " ===");
        boolean hasDebts = false;
        
        for (Debt debt : allDebts) {
            if ("PENDING".equals(debt.getDebtStatus())) {
                System.out.println("  Debt ID: " + debt.getDebtID());
                System.out.println("  Owed to: " + debt.getLender().getUserName());
                System.out.println("  Amount: ₹" + debt.getDebtAmount());
                System.out.println("  Time elapsed: " + debt.getFormattedTimeElapsed());
                hasDebts = true;
            }
        }
        
        if (!hasDebts) {
            System.out.println("   No pending debts!");
        }
    }

    public void showUserSummary(User user) {
        System.out.println("\n === Summary for " + user.getUserName() + " ===");
        System.out.println("  Balance: ₹" + user.getUserBalance());
        System.out.println("  Friends: " + user.getUserFriends().size());
    }

    public static void main(String args[]) {
        System.out.println(" ===== SPLITWISE SIMULATION (2 USERS) ===== \n");
        
        Splitwise app = new Splitwise();

        User kamalesh = new User("Kamalesh", "123456789012345678", 5000.00);
        User arun = new User("Arun", "987654321098765432", 3000.00);

        System.out.println(" Step 1: Creating users and adding as friends");
        kamalesh.addFriend(arun);
        arun.addFriend(kamalesh);

        System.out.println("\n Step 2: Creating group 'Goa Trip'");
        Group goaTrip = new Group("GoaTrip", "Trip to Goa", kamalesh);
        goaTrip.addMember(arun);

        System.out.println("\n Step 3: Adding expenses to the group");
        app.addExpenseToGroup(goaTrip, kamalesh, "Hotel booking", 4000.0);
        app.addExpenseToGroup(goaTrip, arun, "Dinner", 1000.0);

        System.out.println("\n Step 4: Checking pending debts");
        app.showPendingDebts(arun);
        app.showPendingDebts(kamalesh);

        System.out.println("\n Step 5: Settling debts");
        if (app.allDebts.size() > 0) {
            Debt arunDebt = app.allDebts.get(0); 
            app.settleDebt(arunDebt, arun, "cash");
        }
        
        if (app.allDebts.size() > 1) {
            Debt kamaleshDebt = app.allDebts.get(1); // Kamalesh's debt to Arun
            app.settleDebt(kamaleshDebt, kamalesh, "cash");
        }

        System.out.println("\n Step 6: Final Summary");
        app.showUserSummary(kamalesh);
        app.showUserSummary(arun);

        System.out.println("\n ===== SIMULATION COMPLETE ===== ");
    }
}
