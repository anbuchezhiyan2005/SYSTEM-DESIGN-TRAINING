// Bill class: billID, billDescription, billAmount, dateIssued

package Splitwise.models;

import java.time.LocalDateTime;
public class Bill {
    private String billID;
    private String billDescription;
    private double billAmount;
    private LocalDateTime dateIssued;

    public Bill(String billID, String billDescription, double billAmount) {
        this.billID = billID;
        this.billDescription = billDescription;
        this.billAmount = billAmount;
        this.dateIssued = LocalDateTime.now();
    }

    public String getBillID() {
        return billID;
    }

    public String getBillDescription() {
        return billDescription;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public LocalDateTime getDateIssued() {
        return dateIssued;
    }
}

    
