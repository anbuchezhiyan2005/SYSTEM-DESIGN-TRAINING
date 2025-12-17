// Debt class: debtID, lender(User), debtAmount, issuedDateAndTime, timeElapsed

package Splitwise.models;
import java.time.LocalDateTime;
import java.time.Duration;

public class Debt {
    private String debtID;
    private User lender;
    private double debtAmount;
    private LocalDateTime issuedDateAndTime;
    private String debtStatus;

    public Debt(String debtID, User lender, double debtAmount, String debtStatus) {
        this.debtID = debtID;
        this.lender = lender;
        this.debtAmount = debtAmount;
        this.issuedDateAndTime = LocalDateTime.now();
        this.debtStatus = debtStatus;
    }

    public String getFormattedTimeElapsed() {
        Duration duration = Duration.between(issuedDateAndTime, LocalDateTime.now());
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;

        StringBuilder timeElapsed = new StringBuilder();
        if (days > 0) {
            timeElapsed.append(days).append(" days ");
        }
        if (hours > 0) {
            timeElapsed.append(hours).append(" hours ");
        }
        if (minutes > 0) {
            timeElapsed.append(minutes).append(" minutes");
        }

        return timeElapsed.toString().trim();
    }

    public String getDebtStatus() {
        return debtStatus;
    }

    public void setDebtStatus(String debtStatus) {
        this.debtStatus = debtStatus;
    }

    public String getDebtID() {
        return debtID;
    }

    public User getLender() {
        return lender;
    }

    public double getDebtAmount() {
        return debtAmount;
    }

    public LocalDateTime getIssuedDateAndTime() {
        return issuedDateAndTime;
    }

}
