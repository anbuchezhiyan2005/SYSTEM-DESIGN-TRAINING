interface Notification {
    void notify(String to, String message);
}   

class EmailNotification implements Notification {
    public void notify(String to, String message) {
        System.out.println("Email sent to " + to + " with message: " + message);
    }
}

class SMSNotification implements Notification {
    public void notify(String to, String message) {
        System.out.println("SMS sent to " + to + " with message: " + message);
    }
}

class NotificationFactory {
    public static Notification createNotification(String type) {
        return switch (type.toLowerCase()) {
            case "email" -> new EmailNotification();
            case "sms" -> new SMSNotification();
            default -> throw new IllegalArgumentException("Unknown notification type: " + type);
        };
    }
}

public class FactoryPattern {
    public static void main(String args[]) {
        Notification notification1 = NotificationFactory.createNotification("sms");
        notification1.notify("1234567890", "SMS notificaiton");
        Notification notifcation2 = NotificationFactory.createNotification("email");
        notifcation2.notify("0987654321", "Email notification");
    }
}