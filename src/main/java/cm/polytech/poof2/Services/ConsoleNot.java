package cm.polytech.poof2.Services;

public class ConsoleNot implements NotificationService {
    @Override
    public void envoyerNotification(String message) {
        System.out.println("Notification: " + message);
    }
}
