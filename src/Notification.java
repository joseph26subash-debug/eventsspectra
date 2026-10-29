import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Event Spectra - FISAT Events Management System
 * OOP Concept: Encapsulation & Messaging
 * 
 * Represents an announcement or update sent to students.
 */
public class Notification {
    private int notificationId;
    private int userId; // 0 = broadcast to all students
    private String message;
    private String dateTime;
    private boolean isRead;

    public Notification() {
        this.dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        this.isRead = false;
    }

    public Notification(int notificationId, int userId, String message) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        this.isRead = false;
    }

    public Notification(int notificationId, int userId, String message, String dateTime, boolean isRead) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.dateTime = dateTime;
        this.isRead = isRead;
    }

    // Methods from Class Diagram
    public boolean send() {
        System.out.println("[NOTIFICATION SENT] To User ID: " + userId + " | " + message);
        return true;
    }

    public void markAsRead() {
        this.isRead = true;
        System.out.println("[NOTIFICATION READ] ID: " + notificationId);
    }

    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
}
