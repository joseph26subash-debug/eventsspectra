import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Event Spectra - MySQL Database Manager
 * Uses Java JDBC (Java Database Connectivity) to manage persistence in MySQL.
 * 
 * Features:
 *  - Auto-creates database 'event_spectra' if not exists
 *  - Auto-creates relational tables (users, events, registrations, certificates, notifications, reminders)
 *  - Auto-seeds initial data (Students 46-50, Clubs, Admin, Flagship events)
 *  - Handles authentication (Email/Username + Password)
 *  - Supports persistence so users can log back in anytime and restore past history
 *  - Provides seamless fallback if MySQL is offline or password not configured
 */
public class DatabaseManager {
    private static DatabaseManager instance;

    private String dbHost = "localhost";
    private int dbPort = 3306;
    private String dbName = "event_spectra";
    private String dbUser = "root";
    private String dbPassword = "root";

    private boolean isConnected = false;
    private Connection connection;

    private DatabaseManager() {
        loadProperties();
        initDatabase();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void loadProperties() {
        Properties props = new Properties();
        File propFile = new File("database.properties");
        if (!propFile.exists()) {
            propFile = new File("../database.properties");
        }

        if (propFile.exists()) {
            try (InputStream is = new FileInputStream(propFile)) {
                props.load(is);
                dbHost = props.getProperty("db.host", dbHost);
                dbPort = Integer.parseInt(props.getProperty("db.port", String.valueOf(dbPort)));
                dbName = props.getProperty("db.name", dbName);
                dbUser = props.getProperty("db.user", dbUser);
                dbPassword = props.getProperty("db.password", dbPassword);
                System.out.println("[DATABASE] Loaded database.properties (Host: " + dbHost + ", Port: " + dbPort + ", User: " + dbUser + ")");
            } catch (Exception e) {
                System.out.println("[DATABASE] Warning reading database.properties: " + e.getMessage());
            }
        } else {
            System.out.println("[DATABASE] database.properties not found, using default credentials (root/root).");
        }
    }

    public void updateCredentials(String user, String password) {
        this.dbUser = user;
        this.dbPassword = password;
        initDatabase();
    }

    /**
     * Initializes MySQL database connection and tables.
     */
    public synchronized void initDatabase() {
        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 1. First connect without db name to ensure database exists
            String serverUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            try (Connection conn = DriverManager.getConnection(serverUrl, dbUser, dbPassword);
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                System.out.println("[DATABASE] Database '" + dbName + "' verified/created successfully.");
            }

            // 2. Connect to the database
            String dbUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            this.connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            this.isConnected = true;
            System.out.println("[DATABASE] Connected to MySQL Server at " + dbUrl);

            // 3. Create tables
            createTables();

            // 4. Seed initial data if tables are empty
            seedInitialDataIfEmpty();

        } catch (ClassNotFoundException e) {
            System.err.println("[DATABASE ERROR] MySQL JDBC Driver (mysql-connector-j.jar) not loaded: " + e.getMessage());
            isConnected = false;
        } catch (SQLException e) {
            System.err.println("[DATABASE WARNING] Could not connect to MySQL server (" + e.getMessage() + ").");
            System.err.println("[DATABASE INFO] Please verify your MySQL server is running and check 'database.properties'.");
            System.err.println("[DATABASE INFO] Operating in in-memory DataStore mode as graceful fallback.");
            isConnected = false;
        }
    }

    public boolean isConnected() {
        try {
            return isConnected && connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Table: users
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(120) NOT NULL, " +
                    "email VARCHAR(120) UNIQUE NOT NULL, " +
                    "password VARCHAR(100) NOT NULL, " +
                    "phone VARCHAR(30), " +
                    "role VARCHAR(30) NOT NULL, " + // STUDENT, COORDINATOR, ADMIN
                    "student_id INT, " +
                    "course VARCHAR(120), " +
                    "semester INT, " +
                    "organizer_id INT, " +
                    "club_name VARCHAR(120), " +
                    "club_description TEXT, " +
                    "department VARCHAR(120), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ") ENGINE=InnoDB");

            // Table: events
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS events (" +
                    "event_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(255) NOT NULL, " +
                    "description TEXT, " +
                    "event_date VARCHAR(60), " +
                    "event_time VARCHAR(60), " +
                    "venue VARCHAR(120), " +
                    "category VARCHAR(60), " +
                    "capacity INT DEFAULT 60, " +
                    "registered_count INT DEFAULT 0, " +
                    "organizer_club VARCHAR(120), " +
                    "status VARCHAR(50) DEFAULT 'Upcoming', " +
                    "is_hackathon BOOLEAN DEFAULT FALSE, " +
                    "theme VARCHAR(150), " +
                    "duration VARCHAR(60), " +
                    "team_size INT DEFAULT 4, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ") ENGINE=InnoDB");

            // Table: registrations
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS registrations (" +
                    "registration_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "event_id INT NOT NULL, " +
                    "registration_date VARCHAR(60), " +
                    "status VARCHAR(50) DEFAULT 'Confirmed', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ") ENGINE=InnoDB");

            // Table: certificates
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS certificates (" +
                    "certificate_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "event_id INT NOT NULL, " +
                    "student_name VARCHAR(120), " +
                    "event_title VARCHAR(255), " +
                    "issue_date VARCHAR(60), " +
                    "certificate_url VARCHAR(255), " +
                    "certificate_type VARCHAR(100), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ") ENGINE=InnoDB");

            // Table: notifications
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS notifications (" +
                    "notification_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT DEFAULT 0, " + // 0 = broadcast to all
                    "message TEXT NOT NULL, " +
                    "date_time VARCHAR(60), " +
                    "is_read BOOLEAN DEFAULT FALSE, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ") ENGINE=InnoDB");

            // Table: reminders
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS reminders (" +
                    "reminder_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "event_id INT NOT NULL, " +
                    "event_title VARCHAR(255), " +
                    "reminder_time VARCHAR(100), " +
                    "message TEXT, " +
                    "active BOOLEAN DEFAULT TRUE, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ") ENGINE=InnoDB");

            System.out.println("[DATABASE] All relational tables verified in MySQL.");
        }
    }

    private void seedInitialDataIfEmpty() {
        try {
            // Check users
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("[DATABASE] Seeding default users into MySQL...");
                    // Project Team Students (Page 2 of requirements)
                    insertUser("Joseph P Subash", "joseph.subash@fisat.ac.in", "pass123", "+91 98471 23456", "STUDENT", 48, "B.Tech Computer Science & Engineering", 4, 0, null, null, null);
                    insertUser("Joanna Jomon", "joanna.jomon@fisat.ac.in", "pass123", "+91 94471 67890", "STUDENT", 46, "B.Tech Computer Science & Engineering", 4, 0, null, null, null);
                    insertUser("Joel Shantu", "joel.shantu@fisat.ac.in", "pass123", "+91 94951 12345", "STUDENT", 47, "B.Tech Computer Science & Engineering", 4, 0, null, null, null);
                    insertUser("Joshua Jomesh", "joshua.jomesh@fisat.ac.in", "pass123", "+91 97451 98765", "STUDENT", 49, "B.Tech Computer Science & Engineering", 4, 0, null, null, null);
                    insertUser("Jovial J Thattil", "jovial.thattil@fisat.ac.in", "pass123", "+91 96331 54321", "STUDENT", 50, "B.Tech Computer Science & Engineering", 4, 0, null, null, null);

                    // Club Coordinators
                    insertUser("ACM FISAT Student Chapter", "acm@fisat.ac.in", "acm123", "+91 98460 11223", "COORDINATOR", 0, null, 0, 201, "FISAT ACM Chapter", "Premier computing society chapter fostering computer science innovation.", null);
                    insertUser("IEEE SB FISAT", "ieee@fisat.ac.in", "ieee123", "+91 98460 33445", "COORDINATOR", 0, null, 0, 202, "IEEE SB FISAT", "Advancing technology for humanity through technical workshops and hackathons.", null);
                    insertUser("FOSS Cell FISAT", "foss@fisat.ac.in", "foss123", "+91 98460 55667", "COORDINATOR", 0, null, 0, 203, "FOSS Cell FISAT", "Promoting open-source culture, Linux, and community software development.", null);
                    insertUser("CSI FISAT Chapter", "csi@fisat.ac.in", "csi123", "+91 98460 77889", "COORDINATOR", 0, null, 0, 204, "CSI FISAT Chapter", "National IT professionals body encouraging research.", null);

                    // Administrator
                    insertUser("FISAT Events Administrator", "admin@fisat.ac.in", "admin123", "+91 484 2725272", "ADMIN", 0, null, 0, 0, null, null, "Office of Student Affairs & Dean Academics");
                }
            }

            // Check events
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM events")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("[DATABASE] Seeding default events into MySQL...");
                    insertEvent("HackSpectra 2026: 24-Hour AI & Web3 Hackathon", "Flagship hackathon of FISAT. Build groundbreaking AI agents, decentralized applications, and smart campus tools within 24 hours.", "2026-11-14", "09:00 AM", "Central Computing Facility (CCF)", "Hackathon", 120, 45, "FISAT ACM Chapter", "Upcoming", true, "Autonomous AI & Web3 Innovation", "24 Hours", 4);
                    insertEvent("Workshop on Deep Learning & LLMs with PyTorch", "Hands-on workshop exploring prompt engineering, fine-tuning local models, and deploying intelligent assistants on GPU clusters.", "2026-11-05", "10:00 AM", "Auditorium 2, APJ Block", "Workshop", 80, 68, "IEEE SB FISAT", "Upcoming", false, null, null, 0);
                    insertEvent("TechFest Inaugural Keynote & Campus Project Expo", "Kick-off ceremony for the Annual Engineering Expo featuring innovative student prototypes and industry keynote speech.", "Today", "02:00 PM", "Main Auditorium", "Technical", 250, 190, "FISAT ACM Chapter", "Today", false, null, null, 0);
                    insertEvent("Algorhythm: State-Level Competitive Coding Contest", "Speed coding duel testing algorithmic prowess, dynamic programming, and data structure speed. Exciting cash prizes!", "2026-11-20", "01:30 PM", "Software Lab 3", "Competition", 60, 34, "CSI FISAT Chapter", "Upcoming", false, null, null, 0);
                    insertEvent("Linux Kernel & Git Mastery Bootcamp", "Comprehensive hands-on session on Linux command line, kernel compilation basics, and collaborative Git workflows.", "2026-11-28", "11:00 AM", "Seminar Hall 1", "Workshop", 50, 28, "FOSS Cell FISAT", "Upcoming", false, null, null, 0);
                }
            }

            // Check registrations
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM registrations")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    insertRegistration(1, 1, "2026-10-25 14:30", "Confirmed");
                    insertRegistration(1, 2, "2026-10-26 10:15", "Confirmed");
                    insertRegistration(2, 1, "2026-10-26 11:00", "Confirmed");
                    insertRegistration(3, 3, "2026-10-27 09:30", "Confirmed");
                }
            }

            // Check certificates
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM certificates")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    insertCertificate(1, 3, "Joseph P Subash", "TechFest Inaugural Keynote & Campus Project Expo", "2026-10-30", "/certificates/cert-1.pdf", "Certificate of Participation");
                    insertCertificate(1, 100, "Joseph P Subash", "National Web Development Hackathon 2025", "2026-09-15", "/certificates/cert-2.pdf", "Certificate of Merit (First Runner-Up)");
                }
            }

            // Check notifications
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM notifications")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    insertNotification(0, "Welcome to Event Spectra! Centralized event portal for FISAT students.", "2026-10-28 09:00", true);
                    insertNotification(1, "Reminder: TechFest Inaugural Keynote is scheduled for Today at 02:00 PM in Main Auditorium.", "2026-10-30 08:00", false);
                    insertNotification(1, "Your registration for 'HackSpectra 2026' has been confirmed by FISAT ACM Chapter.", "2026-10-29 17:45", true);
                }
            }

            // Check reminders
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM reminders")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    insertReminder(1, 1, "HackSpectra 2026: 24-Hour AI & Web3 Hackathon", "2026-11-14 08:30 AM", "Report to CCF with laptop and college ID card.", true);
                }
            }
        } catch (SQLException e) {
            System.err.println("[DATABASE SEED ERROR] " + e.getMessage());
        }
    }

    // --- Authentication & User Operations ---

    public User authenticate(String emailOrUsername, String password) {
        if (!isConnected()) {
            for (User u : DataStore.getInstance().getUsers()) {
                if ((u.getEmail().equalsIgnoreCase(emailOrUsername.trim()) ||
                     u.getName().equalsIgnoreCase(emailOrUsername.trim()) ||
                     String.valueOf(u.getUserId()).equals(emailOrUsername.trim())) &&
                     u.login(password)) {
                    return u;
                }
            }
            return null;
        }

        String sql = "SELECT * FROM users WHERE (email = ? OR name = ?) AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, emailOrUsername.trim());
            ps.setString(2, emailOrUsername.trim());
            ps.setString(3, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[AUTH ERROR] " + e.getMessage());
        }
        return null;
    }

    public User registerUser(String name, String email, String password, String phone, String course, int semester) {
        if (!isConnected()) {
            int newId = 100 + (int)(Math.random() * 900);
            Student s = new Student(newId, name, email, password, phone, newId, course, semester);
            DataStore.getInstance().getUsers().add(s);
            return s;
        }

        String sql = "INSERT INTO users (name, email, password, phone, role, student_id, course, semester) VALUES (?, ?, ?, ?, 'STUDENT', ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int studentRoll = 51 + (int)(Math.random() * 50);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, phone);
            ps.setInt(5, studentRoll);
            ps.setString(6, course);
            ps.setInt(7, semester);
            ps.executeUpdate();

            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    int newUserId = gk.getInt(1);
                    return new Student(newUserId, name, email, password, phone, studentRoll, course, semester);
                }
            }
        } catch (SQLException e) {
            System.err.println("[REGISTER ERROR] " + e.getMessage());
        }
        return null;
    }

    public List<User> getAllUsers() {
        if (!isConnected()) return DataStore.getInstance().getUsers();

        List<User> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users ORDER BY user_id ASC")) {
            while (rs.next()) {
                list.add(mapUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[GET USERS ERROR] " + e.getMessage());
        }
        return list;
    }

    public User getUserById(int userId) {
        if (!isConnected()) return DataStore.getInstance().findUserById(userId);

        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE user_id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("[GET USER BY ID ERROR] " + e.getMessage());
        }
        return null;
    }

    private User mapUserFromResultSet(ResultSet rs) throws SQLException {
        int uId = rs.getInt("user_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String pass = rs.getString("password");
        String phone = rs.getString("phone");
        String role = rs.getString("role");

        if ("STUDENT".equalsIgnoreCase(role)) {
            int sId = rs.getInt("student_id");
            String course = rs.getString("course");
            int sem = rs.getInt("semester");
            return new Student(uId, name, email, pass, phone, sId, course, sem);
        } else if ("COORDINATOR".equalsIgnoreCase(role)) {
            int orgId = rs.getInt("organizer_id");
            String clubName = rs.getString("club_name");
            String clubDesc = rs.getString("club_description");
            return new ClubOrganizer(uId, name, email, pass, phone, orgId, clubName, clubDesc);
        } else {
            String dept = rs.getString("department");
            return new Administrator(uId, name, email, pass, phone, dept != null ? dept : "Office of Student Affairs");
        }
    }

    // --- Events Operations ---

    public List<Event> getAllEvents() {
        if (!isConnected()) return DataStore.getInstance().getEvents();

        List<Event> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM events ORDER BY event_id DESC")) {
            while (rs.next()) {
                int id = rs.getInt("event_id");
                String title = rs.getString("title");
                String desc = rs.getString("description");
                String date = rs.getString("event_date");
                String time = rs.getString("event_time");
                String venue = rs.getString("venue");
                String category = rs.getString("category");
                int cap = rs.getInt("capacity");
                int regCount = rs.getInt("registered_count");
                String club = rs.getString("organizer_club");
                String status = rs.getString("status");
                boolean isHack = rs.getBoolean("is_hackathon");

                Event ev;
                if (isHack) {
                    String theme = rs.getString("theme");
                    String dur = rs.getString("duration");
                    int tSize = rs.getInt("team_size");
                    ev = new Hackathon(id, title, desc, date, time, venue, cap, club, theme, dur, tSize);
                } else {
                    ev = new Event(id, title, desc, date, time, venue, category, cap, club);
                }
                ev.setRegisteredCount(regCount);
                ev.setStatus(status);
                list.add(ev);
            }
        } catch (SQLException e) {
            System.err.println("[GET EVENTS ERROR] " + e.getMessage());
        }
        return list;
    }

    public Event createEvent(Event e) {
        if (!isConnected()) return DataStore.getInstance().addEvent(e);

        String sql = "INSERT INTO events (title, description, event_date, event_time, venue, category, capacity, registered_count, organizer_club, status, is_hackathon, theme, duration, team_size) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getTitle());
            ps.setString(2, e.getDescription());
            ps.setString(3, e.getDate());
            ps.setString(4, e.getTime());
            ps.setString(5, e.getVenue());
            ps.setString(6, e.getCategory());
            ps.setInt(7, e.getCapacity());
            ps.setInt(8, 0);
            ps.setString(9, e.getOrganizerClub());
            ps.setString(10, e.getStatus());

            if (e instanceof Hackathon) {
                Hackathon h = (Hackathon) e;
                ps.setBoolean(11, true);
                ps.setString(12, h.getTheme());
                ps.setString(13, h.getDuration());
                ps.setInt(14, h.getTeamSize());
            } else {
                ps.setBoolean(11, false);
                ps.setString(12, null);
                ps.setString(13, null);
                ps.setInt(14, 0);
            }

            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    e.setEventId(gk.getInt(1));
                    // broadcast notification in DB
                    createNotification(0, "New Event Announced: '" + e.getTitle() + "' organized by " + e.getOrganizerClub());
                    return e;
                }
            }
        } catch (SQLException ex) {
            System.err.println("[CREATE EVENT ERROR] " + ex.getMessage());
        }
        return e;
    }

    public boolean cancelEvent(int eventId) {
        if (!isConnected()) return DataStore.getInstance().deleteEvent(eventId);

        String sql = "UPDATE events SET status = 'Cancelled' WHERE event_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CANCEL EVENT ERROR] " + e.getMessage());
            return false;
        }
    }

    // --- Registrations Operations ---

    public List<Registration> getAllRegistrations() {
        if (!isConnected()) return DataStore.getInstance().getRegistrations();

        List<Registration> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM registrations ORDER BY registration_id DESC")) {
            while (rs.next()) {
                int rId = rs.getInt("registration_id");
                int sId = rs.getInt("student_id");
                int eId = rs.getInt("event_id");
                String date = rs.getString("registration_date");
                String status = rs.getString("status");
                list.add(new Registration(rId, sId, eId, date, status));
            }
        } catch (SQLException e) {
            System.err.println("[GET REGISTRATIONS ERROR] " + e.getMessage());
        }
        return list;
    }

    public List<Registration> getRegistrationsForStudent(int studentId) {
        List<Registration> all = getAllRegistrations();
        List<Registration> result = new ArrayList<>();
        for (Registration r : all) {
            if (r.getStudentId() == studentId) result.add(r);
        }
        return result;
    }

    public Registration registerStudent(int studentId, int eventId) {
        if (!isConnected()) return DataStore.getInstance().registerStudentForEvent(studentId, eventId);

        // Check if already registered
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM registrations WHERE student_id = ? AND event_id = ? AND status != 'Cancelled'")) {
            ps.setInt(1, studentId);
            ps.setInt(2, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Registration(rs.getInt("registration_id"), studentId, eventId, rs.getString("registration_date"), rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[CHECK REG ERROR] " + e.getMessage());
        }

        // Insert new
        String sql = "INSERT INTO registrations (student_id, event_id, registration_date, status) VALUES (?, ?, NOW(), 'Confirmed')";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, studentId);
            ps.setInt(2, eventId);
            ps.executeUpdate();

            // Increment registered_count on event
            try (PreparedStatement ups = connection.prepareStatement("UPDATE events SET registered_count = registered_count + 1 WHERE event_id = ?")) {
                ups.setInt(1, eventId);
                ups.executeUpdate();
            }

            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    int rId = gk.getInt(1);
                    Registration reg = new Registration(rId, studentId, eventId, "Confirmed");
                    // Add notification
                    createNotification(studentId, "Successfully booked participation for Event #" + eventId + "!");
                    return reg;
                }
            }
        } catch (SQLException e) {
            System.err.println("[REGISTER STUDENT ERROR] " + e.getMessage());
        }
        return null;
    }

    public boolean cancelRegistration(int studentId, int eventId) {
        if (!isConnected()) return DataStore.getInstance().cancelRegistration(studentId, eventId);

        try (PreparedStatement ps = connection.prepareStatement("UPDATE registrations SET status = 'Cancelled' WHERE student_id = ? AND event_id = ? AND status != 'Cancelled'")) {
            ps.setInt(1, studentId);
            ps.setInt(2, eventId);
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (PreparedStatement ups = connection.prepareStatement("UPDATE events SET registered_count = GREATEST(0, registered_count - 1) WHERE event_id = ?")) {
                    ups.setInt(1, eventId);
                    ups.executeUpdate();
                }
                createNotification(studentId, "Your booking for Event #" + eventId + " has been cancelled.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[CANCEL REG ERROR] " + e.getMessage());
        }
        return false;
    }

    // --- Certificates Operations ---

    public List<Certificate> getAllCertificates() {
        if (!isConnected()) return DataStore.getInstance().getCertificates();

        List<Certificate> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM certificates ORDER BY certificate_id DESC")) {
            while (rs.next()) {
                Certificate c = new Certificate(
                        rs.getInt("certificate_id"),
                        rs.getInt("student_id"),
                        rs.getInt("event_id"),
                        rs.getString("student_name"),
                        rs.getString("event_title"),
                        rs.getString("certificate_type")
                );
                c.setIssueDate(rs.getString("issue_date"));
                c.setCertificateUrl(rs.getString("certificate_url"));
                list.add(c);
            }
        } catch (SQLException e) {
            System.err.println("[GET CERTS ERROR] " + e.getMessage());
        }
        return list;
    }

    public List<Certificate> getCertificatesForStudent(int studentId) {
        List<Certificate> all = getAllCertificates();
        List<Certificate> result = new ArrayList<>();
        for (Certificate c : all) {
            if (c.getStudentId() == studentId) result.add(c);
        }
        return result;
    }

    public Certificate issueCertificate(int studentId, int eventId, String certType) {
        if (!isConnected()) return DataStore.getInstance().issueCertificate(studentId, eventId, certType);

        User u = getUserById(studentId);
        String sName = (u != null) ? u.getName() : "Student #" + studentId;
        String eTitle = "Event #" + eventId;
        List<Event> allEv = getAllEvents();
        for (Event ev : allEv) {
            if (ev.getEventId() == eventId) {
                eTitle = ev.getTitle();
                break;
            }
        }

        String sql = "INSERT INTO certificates (student_id, event_id, student_name, event_title, issue_date, certificate_url, certificate_type) VALUES (?, ?, ?, ?, CURDATE(), ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, studentId);
            ps.setInt(2, eventId);
            ps.setString(3, sName);
            ps.setString(4, eTitle);
            ps.setString(5, "/certificates/cert-" + eventId + ".pdf");
            ps.setString(6, certType);
            ps.executeUpdate();

            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    int cId = gk.getInt(1);
                    Certificate cert = new Certificate(cId, studentId, eventId, sName, eTitle, certType);
                    createNotification(studentId, "Your Certificate for '" + eTitle + "' is now available for download!");
                    return cert;
                }
            }
        } catch (SQLException e) {
            System.err.println("[ISSUE CERT ERROR] " + e.getMessage());
        }
        return null;
    }

    // --- Notifications Operations ---

    public List<Notification> getAllNotifications() {
        if (!isConnected()) return DataStore.getInstance().getNotifications();

        List<Notification> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM notifications ORDER BY notification_id DESC")) {
            while (rs.next()) {
                list.add(new Notification(
                        rs.getInt("notification_id"),
                        rs.getInt("user_id"),
                        rs.getString("message"),
                        rs.getString("date_time"),
                        rs.getBoolean("is_read")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[GET NOTIFS ERROR] " + e.getMessage());
        }
        return list;
    }

    public Notification createNotification(int userId, String message) {
        if (!isConnected()) return DataStore.getInstance().addNotification(userId, message);

        String sql = "INSERT INTO notifications (user_id, message, date_time, is_read) VALUES (?, ?, NOW(), FALSE)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    return new Notification(gk.getInt(1), userId, message);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CREATE NOTIF ERROR] " + e.getMessage());
        }
        return null;
    }

    public void markNotificationRead(int notificationId) {
        if (!isConnected()) {
            for (Notification n : DataStore.getInstance().getNotifications()) {
                if (n.getNotificationId() == notificationId) n.markAsRead();
            }
            return;
        }

        try (PreparedStatement ps = connection.prepareStatement("UPDATE notifications SET is_read = TRUE WHERE notification_id = ?")) {
            ps.setInt(1, notificationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[MARK READ ERROR] " + e.getMessage());
        }
    }

    // --- Reminders Operations ---

    public List<Reminder> getAllReminders() {
        if (!isConnected()) return DataStore.getInstance().getReminders();

        List<Reminder> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM reminders WHERE active = TRUE ORDER BY reminder_id DESC")) {
            while (rs.next()) {
                list.add(new Reminder(
                        rs.getInt("reminder_id"),
                        rs.getInt("user_id"),
                        rs.getInt("event_id"),
                        rs.getString("event_title"),
                        rs.getString("reminder_time"),
                        rs.getString("message")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[GET REMINDERS ERROR] " + e.getMessage());
        }
        return list;
    }

    public Reminder createReminder(int userId, int eventId, String reminderTime, String message) {
        if (!isConnected()) return DataStore.getInstance().addReminder(userId, eventId, reminderTime, message);

        String eventTitle = "Event #" + eventId;
        List<Event> all = getAllEvents();
        for (Event e : all) {
            if (e.getEventId() == eventId) {
                eventTitle = e.getTitle();
                break;
            }
        }

        String sql = "INSERT INTO reminders (user_id, event_id, event_title, reminder_time, message, active) VALUES (?, ?, ?, ?, ?, TRUE)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            ps.setString(3, eventTitle);
            ps.setString(4, reminderTime);
            ps.setString(5, message);
            ps.executeUpdate();

            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    return new Reminder(gk.getInt(1), userId, eventId, eventTitle, reminderTime, message);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CREATE REMINDER ERROR] " + e.getMessage());
        }
        return null;
    }

    // --- Helper Insert Methods for Seeding ---

    private void insertUser(String name, String email, String password, String phone, String role,
                            int studentId, String course, int semester,
                            int orgId, String clubName, String clubDesc, String department) throws SQLException {
        String sql = "INSERT INTO users (name, email, password, phone, role, student_id, course, semester, organizer_id, club_name, club_description, department) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, phone);
            ps.setString(5, role);
            ps.setInt(6, studentId);
            ps.setString(7, course);
            ps.setInt(8, semester);
            ps.setInt(9, orgId);
            ps.setString(10, clubName);
            ps.setString(11, clubDesc);
            ps.setString(12, department);
            ps.executeUpdate();
        }
    }

    private void insertEvent(String title, String desc, String date, String time, String venue, String cat,
                             int cap, int regCount, String club, String status, boolean isHack,
                             String theme, String duration, int teamSize) throws SQLException {
        String sql = "INSERT INTO events (title, description, event_date, event_time, venue, category, capacity, registered_count, organizer_club, status, is_hackathon, theme, duration, team_size) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, desc);
            ps.setString(3, date);
            ps.setString(4, time);
            ps.setString(5, venue);
            ps.setString(6, cat);
            ps.setInt(7, cap);
            ps.setInt(8, regCount);
            ps.setString(9, club);
            ps.setString(10, status);
            ps.setBoolean(11, isHack);
            ps.setString(12, theme);
            ps.setString(13, duration);
            ps.setInt(14, teamSize);
            ps.executeUpdate();
        }
    }

    private void insertRegistration(int studentId, int eventId, String date, String status) throws SQLException {
        String sql = "INSERT INTO registrations (student_id, event_id, registration_date, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, eventId);
            ps.setString(3, date);
            ps.setString(4, status);
            ps.executeUpdate();
        }
    }

    private void insertCertificate(int studentId, int eventId, String studentName, String eventTitle, String date, String url, String type) throws SQLException {
        String sql = "INSERT INTO certificates (student_id, event_id, student_name, event_title, issue_date, certificate_url, certificate_type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, eventId);
            ps.setString(3, studentName);
            ps.setString(4, eventTitle);
            ps.setString(5, date);
            ps.setString(6, url);
            ps.setString(7, type);
            ps.executeUpdate();
        }
    }

    private void insertNotification(int userId, String message, String dateTime, boolean isRead) throws SQLException {
        String sql = "INSERT INTO notifications (user_id, message, date_time, is_read) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.setString(3, dateTime);
            ps.setBoolean(4, isRead);
            ps.executeUpdate();
        }
    }

    private void insertReminder(int userId, int eventId, String title, String time, String message, boolean active) throws SQLException {
        String sql = "INSERT INTO reminders (user_id, event_id, event_title, reminder_time, message, active) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            ps.setString(3, title);
            ps.setString(4, time);
            ps.setString(5, message);
            ps.setBoolean(6, active);
            ps.executeUpdate();
        }
    }
}
