/**
 * Event Spectra - FISAT Events Management System
 * OOP Concept: Encapsulation & Scheduling
 * 
 * Represents a reminder scheduled by a student for an upcoming event.
 */
public class Reminder {
    private int reminderId;
    private int userId;
    private int eventId;
    private String eventTitle;
    private String reminderTime;
    private String message;
    private boolean active;

    public Reminder() {
        this.active = true;
    }

    public Reminder(int reminderId, int userId, int eventId, String eventTitle, String reminderTime, String message) {
        this.reminderId = reminderId;
        this.userId = userId;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.reminderTime = reminderTime;
        this.message = message;
        this.active = true;
    }

    // Methods from Class Diagram
    public boolean setReminder() {
        this.active = true;
        System.out.println("[REMINDER SET] User #" + userId + " set reminder for event #" + eventId + " at " + reminderTime);
        return true;
    }

    public boolean cancelReminder() {
        this.active = false;
        System.out.println("[REMINDER CANCELLED] Reminder #" + reminderId);
        return false;
    }

    public void trigger() {
        if (this.active) {
            System.out.println("[REMINDER TRIGGERED] Alert for User #" + userId + ": " + message);
        }
    }

    // Getters and Setters
    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
