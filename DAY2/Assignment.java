// BLUEPRINT FOR ALL NOTIFICATION SYSTEMS
interface Notification {
    void notify(String to, String message);
}   

// EMAIL NOTIFICATION SYSTEM
class EmailNotification implements Notification {
    public void notify(String to, String message) {
        System.out.println("Email sent to " + to + " with message: " + message);
    }
}

// SMS NOTIFICATION SYSTEM
class SMSNotification implements Notification {
    public void notify(String to, String message) {
        System.out.println("SMS sent to " + to + " with message: " + message);
    }
}

// PUSH NOTIFICATION SYSTEM
class PushNotification implements Notification {
    public void notify(String to, String message) {
        System.out.println("Push Notification sent to " + to + " with message: " + message);
    }
}

// DECORATOR TO ADD COST TO SMS NOTIFICATION
class SMSDecorator implements Notification {
    private Notification notification;

    public SMSDecorator(Notification notification) {
        this.notification = notification;
    }

    public void notify(String to, String message) {
        notification.notify(to, message);
        System.out.println("Message cost is: " + 1.0);
    }
}

// NOTIFICATION FACTORY TO CHOOSE NOTIFICATION TYPE
class NotificationServiceFactory {
    public static Notification createNotification(String type, boolean withCost) {
        Notification base =  switch (type.toLowerCase()) {
            case "email" -> new EmailNotification();
            case "sms" -> new SMSNotification();
            case "push" -> new PushNotification();
            default -> throw new IllegalArgumentException("Unknown notification type: " + type);
        };

        return withCost && "sms".equalsIgnoreCase(type)? new SMSDecorator(base) : base;
    }
}

public class Assignment {
    public static void main(String args[]) {
        Notification notification1 = NotificationServiceFactory.createNotification("sms", true);
        notification1.notify("1234567890", "SMS notificaiton");
        Notification notifcation2 = NotificationServiceFactory.createNotification("email", false);
        notifcation2.notify("0987654321", "Email notification");
        Notification notification3 = NotificationServiceFactory.createNotification("push", false);
        notification3.notify("1122334455", "Push notification");
    }
}
