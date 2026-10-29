import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Event Spectra - FISAT Events Management System
 * OOP Concept: Encapsulation & Entity Management
 * 
 * Represents an e-certificate generated for a participant upon event completion.
 */
public class Certificate {
    private int certificateId;
    private int studentId;
    private int eventId;
    private String studentName;
    private String eventTitle;
    private String issueDate;
    private String certificateUrl;
    private String certificateType; // "Certificate of Participation", "Certificate of Merit", "Winner"

    public Certificate() {
        this.issueDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public Certificate(int certificateId, int studentId, int eventId, String studentName, 
                       String eventTitle, String certificateType) {
        this.certificateId = certificateId;
        this.studentId = studentId;
        this.eventId = eventId;
        this.studentName = studentName;
        this.eventTitle = eventTitle;
        this.issueDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.certificateType = certificateType;
        this.certificateUrl = "/certificates/cert-" + certificateId + ".pdf";
    }

    // Methods from Class Diagram
    public Certificate generate() {
        System.out.println("[CERTIFICATE] Generated certificate #" + certificateId + " for " + studentName);
        return this;
    }

    public boolean sendEmail(String studentEmail) {
        System.out.println("[EMAIL] Certificate #" + certificateId + " sent to student email: " + studentEmail);
        return true;
    }

    public String download() {
        System.out.println("[DOWNLOAD] Downloading certificate file: " + certificateUrl);
        return certificateUrl;
    }

    // Getters and Setters
    public int getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(int certificateId) {
        this.certificateId = certificateId;
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getCertificateUrl() {
        return certificateUrl;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }
}
