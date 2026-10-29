/**
 * Event Spectra - FISAT Events Management System
 * OOP Concept: Abstraction & Encapsulation (Base Class)
 * 
 * Represents a generic user in the Event Spectra application.
 */
public abstract class User {
    // Encapsulated private attributes
    private int userId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role; // "STUDENT", "ORGANIZER", "ADMIN"

    // Default Constructor
    public User() {
    }

    // Parameterized Constructor
    public User(int userId, String name, String email, String password, String phone, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    // Core User Methods from Class Diagram
    public boolean login(String enteredPassword) {
        if (this.password != null && this.password.equals(enteredPassword)) {
            System.out.println("[AUTH] User logged in successfully: " + this.name);
            return true;
        }
        System.out.println("[AUTH] Invalid credentials for: " + this.email);
        return false;
    }

    public void logout() {
        System.out.println("[AUTH] User logged out: " + this.name);
    }

    public void updateProfile(String name, String phone, String password) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
        if (phone != null && !phone.trim().isEmpty()) {
            this.phone = phone;
        }
        if (password != null && !password.trim().isEmpty()) {
            this.password = password;
        }
        System.out.println("[USER] Profile updated for: " + this.name);
    }

    // Abstract method showcasing Polymorphism
    public abstract String getRoleDescription();

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
