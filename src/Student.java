import java.util.ArrayList;
import java.util.List;

/**
 * Event Spectra - FISAT Events Management System
 * OOP Concept: Inheritance (Student extends User)
 * 
 * Represents a student of FISAT who discovers, registers for events,
 * and receives certificates and notifications.
 */
public class Student extends User {
    private int studentId;
    private String course;
    private int semester;

    public Student() {
        super();
        setRole("STUDENT");
    }

    public Student(int userId, String name, String email, String password, String phone, 
                   int studentId, String course, int semester) {
        super(userId, name, email, password, phone, "STUDENT");
        this.studentId = studentId;
        this.course = course;
        this.semester = semester;
    }

    @Override
    public String getRoleDescription() {
        return "Student (" + course + ", Semester " + semester + ")";
    }

    // Methods from Class Diagram
    public Registration registerForEvent(Event event, int nextRegId) {
        if (event == null) return null;
        System.out.println("[STUDENT] Registering " + getName() + " for event: " + event.getTitle());
        Registration reg = new Registration(nextRegId, this.getUserId(), event.getEventId(), "Confirmed");
        return reg;
    }

    public Registration bookEvent(Event event, int nextRegId) {
        return registerForEvent(event, nextRegId);
    }

    public List<Event> viewEvents(List<Event> allEvents) {
        return allEvents;
    }

    public List<Certificate> viewCertificates(List<Certificate> allCertificates) {
        List<Certificate> myCerts = new ArrayList<>();
        for (Certificate cert : allCertificates) {
            if (cert.getStudentId() == this.getUserId()) {
                myCerts.add(cert);
            }
        }
        return myCerts;
    }

    public List<Notification> receiveNotifications(List<Notification> allNotifications) {
        List<Notification> myNotes = new ArrayList<>();
        for (Notification note : allNotifications) {
            if (note.getUserId() == this.getUserId() || note.getUserId() == 0) { // 0 = broadcast to all
                myNotes.add(note);
            }
        }
        return myNotes;
    }

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}
