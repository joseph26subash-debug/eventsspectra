/**
 * Event Spectra - FISAT Events Management System
 * OOP Concept: Inheritance (Administrator extends User)
 * 
 * Represents an administrator managing users, events, and the overall system.
 */
public class Administrator extends User {
    private String department;

    public Administrator() {
        super();
        setRole("ADMIN");
    }

    public Administrator(int userId, String name, String email, String password, String phone, String department) {
        super(userId, name, email, password, phone, "ADMIN");
        this.department = department;
    }

    @Override
    public String getRoleDescription() {
        return "System Administrator (" + department + ")";
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
