import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Event Spectra - FISAT Events Management System
 * OOP Concept: Association (links Student and Event)
 * 
 * Represents a student's registration / booking for an event.
 */
public class Registration {
    private int registrationId;
    private int studentId;
    private int eventId;
    private String registrationDate;
    private String status; // "Confirmed", "Pending", "Cancelled"

    public Registration() {
        this.registrationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        this.status = "Confirmed";
    }

    public Registration(int registrationId, int studentId, int eventId, String status) {
        this.registrationId = registrationId;
        this.studentId = studentId;
        this.eventId = eventId;
        this.registrationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        this.status = status;
    }

    public Registration(int registrationId, int studentId, int eventId, String registrationDate, String status) {
        this.registrationId = registrationId;
        this.studentId = studentId;
        this.eventId = eventId;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    // Methods from Class Diagram
    public boolean register() {
        this.status = "Confirmed";
        System.out.println("[REGISTRATION] New registration created: #" + registrationId);
        return true;
    }

    public boolean cancelRegistration() {
        this.status = "Cancelled";
        System.out.println("[REGISTRATION] Registration #" + registrationId + " cancelled.");
        return true;
    }

    public boolean confirmBooking() {
        this.status = "Confirmed";
        System.out.println("[REGISTRATION] Booking confirmed for #" + registrationId);
        return true;
    }

    // Getters and Setters
    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
