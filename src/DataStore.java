import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Event Spectra - Centralized In-Memory Data Store & OOP Service
 * Manages students, organizers, admins, events, hackathons, registrations,
 * certificates, notifications, and reminders.
 */
public class DataStore {
    private static DataStore instance;

    private List<User> users = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private List<Registration> registrations = new ArrayList<>();
    private List<Certificate> certificates = new ArrayList<>();
    private List<Notification> notifications = new ArrayList<>();
    private List<Reminder> reminders = new ArrayList<>();

    private AtomicInteger eventSeq = new AtomicInteger(100);
    private AtomicInteger regSeq = new AtomicInteger(500);
    private AtomicInteger certSeq = new AtomicInteger(800);
    private AtomicInteger notifSeq = new AtomicInteger(300);
    private AtomicInteger remSeq = new AtomicInteger(400);

    // Singleton Pattern (Basic OOP Design Pattern)
    private DataStore() {
        seedInitialData();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    /**
     * Seeds the system with the FISAT Project Team, Sample Clubs, and Events.
     */
    private void seedInitialData() {
        // 1. PROJECT TEAM (Page 2 of requirements PDF)
        Student s1 = new Student(48, "Joseph P Subash", "joseph.subash@fisat.ac.in", "pass123", "+91 98471 23456", 48, "B.Tech Computer Science & Engineering", 4);
        Student s2 = new Student(46, "Joanna Jomon", "joanna.jomon@fisat.ac.in", "pass123", "+91 94471 67890", 46, "B.Tech Computer Science & Engineering", 4);
        Student s3 = new Student(47, "Joel Shantu", "joel.shantu@fisat.ac.in", "pass123", "+91 94951 12345", 47, "B.Tech Computer Science & Engineering", 4);
        Student s4 = new Student(49, "Joshua Jomesh", "joshua.jomesh@fisat.ac.in", "pass123", "+91 97451 98765", 49, "B.Tech Computer Science & Engineering", 4);
        Student s5 = new Student(50, "Jovial J Thattil", "jovial.thattil@fisat.ac.in", "pass123", "+91 96331 54321", 50, "B.Tech Computer Science & Engineering", 4);

        users.add(s1);
        users.add(s2);
        users.add(s3);
        users.add(s4);
        users.add(s5);

        // 2. CLUB ORGANIZERS (Page 6 of requirements PDF)
        ClubOrganizer c1 = new ClubOrganizer(101, "ACM FISAT Student Chapter", "acm@fisat.ac.in", "acm123", "+91 98460 11223", 201, "FISAT ACM Chapter", "Premier computing society chapter fostering computer science innovation.");
        ClubOrganizer c2 = new ClubOrganizer(102, "IEEE SB FISAT", "ieee@fisat.ac.in", "ieee123", "+91 98460 33445", 202, "IEEE Student Branch FISAT", "Advancing technology for humanity through technical workshops and hackathons.");
        ClubOrganizer c3 = new ClubOrganizer(103, "FOSS Cell FISAT", "foss@fisat.ac.in", "foss123", "+91 98460 55667", 203, "Free and Open Source Software Cell", "Promoting open-source culture, Linux, and community software development.");
        ClubOrganizer c4 = new ClubOrganizer(104, "CSI FISAT Chapter", "csi@fisat.ac.in", "csi123", "+91 98460 77889", 204, "Computer Society of India FISAT", "National IT professionals body encouraging research, quizzes, and code jams.");

        users.add(c1);
        users.add(c2);
        users.add(c3);
        users.add(c4);

        // 3. ADMINISTRATOR (Page 6 of requirements PDF)
        Administrator admin = new Administrator(1, "FISAT Events Administrator", "admin@fisat.ac.in", "admin123", "+91 484 2725272", "Office of Student Affairs & Dean Academics");
        users.add(admin);

        // 4. EVENTS & HACKATHONS (Polymorphic objects!)
        // Event 1: Hackathon (Inherits Event)
        Hackathon h1 = new Hackathon(
                eventSeq.incrementAndGet(),
                "HackSpectra 2026: 24-Hour AI & Web3 Hackathon",
                "Flagship hackathon of FISAT. Build groundbreaking AI agents, decentralized applications, and smart campus tools within 24 hours.",
                "2026-11-14",
                "09:00 AM",
                "Central Computing Facility (CCF)",
                120,
                "FISAT ACM Chapter",
                "Autonomous AI & Web3 Innovation",
                "24 Hours",
                4
        );
        h1.setRegisteredCount(45);
        events.add(h1);

        // Event 2: Workshop
        Event e2 = new Event(
                eventSeq.incrementAndGet(),
                "Workshop on Deep Learning & LLMs with PyTorch",
                "Hands-on workshop exploring prompt engineering, fine-tuning local models, and deploying intelligent assistants on GPU clusters.",
                "2026-11-05",
                "10:00 AM",
                "Auditorium 2, APJ Block",
                "Workshop",
                80,
                "IEEE SB FISAT"
        );
        e2.setRegisteredCount(68);
        events.add(e2);

        // Event 3: Today's Event
        Event e3 = new Event(
                eventSeq.incrementAndGet(),
                "TechFest Inaugural Keynote & Campus Project Expo",
                "Kick-off ceremony for the Annual Engineering Expo featuring innovative student prototypes and industry keynote speech.",
                "Today",
                "02:00 PM",
                "Main Auditorium",
                "Technical",
                250,
                "FISAT ACM Chapter"
        );
        e3.setStatus("Today");
        e3.setRegisteredCount(190);
        events.add(e3);

        // Event 4: Competition
        Event e4 = new Event(
                eventSeq.incrementAndGet(),
                "Algorhythm: State-Level Competitive Coding Contest",
                "Speed coding duel testing algorithmic prowess, dynamic programming, and data structure speed. Exciting cash prizes!",
                "2026-11-20",
                "01:30 PM",
                "Software Lab 3",
                "Competition",
                60,
                "CSI FISAT Chapter"
        );
        e4.setRegisteredCount(34);
        events.add(e4);

        // Event 5: FOSS Workshop
        Event e5 = new Event(
                eventSeq.incrementAndGet(),
                "Linux Kernel & Git Mastery Bootcamp",
                "Comprehensive hands-on session on Linux command line, kernel compilation basics, and collaborative Git workflows.",
                "2026-11-28",
                "11:00 AM",
                "Seminar Hall 1",
                "Workshop",
                50,
                "Free and Open Source Software Cell"
        );
        e5.setRegisteredCount(28);
        events.add(e5);

        // 5. REGISTRATIONS
        // Register Joseph P Subash (ID 48) for HackSpectra and Deep Learning Workshop
        Registration r1 = new Registration(regSeq.incrementAndGet(), 48, h1.getEventId(), "2026-10-25 14:30", "Confirmed");
        Registration r2 = new Registration(regSeq.incrementAndGet(), 48, e2.getEventId(), "2026-10-26 10:15", "Confirmed");
        Registration r3 = new Registration(regSeq.incrementAndGet(), 46, h1.getEventId(), "2026-10-26 11:00", "Confirmed");
        Registration r4 = new Registration(regSeq.incrementAndGet(), 47, e3.getEventId(), "2026-10-27 09:30", "Confirmed");
        registrations.add(r1);
        registrations.add(r2);
        registrations.add(r3);
        registrations.add(r4);

        // 6. CERTIFICATES
        Certificate cCert1 = new Certificate(
                certSeq.incrementAndGet(),
                48,
                e3.getEventId(),
                "Joseph P Subash",
                "TechFest Inaugural Keynote & Campus Project Expo",
                "Certificate of Participation"
        );
        Certificate cCert2 = new Certificate(
                certSeq.incrementAndGet(),
                48,
                100, // Past Event
                "Joseph P Subash",
                "National Web Development Hackathon 2025",
                "Certificate of Merit (First Runner-Up)"
        );
        certificates.add(cCert1);
        certificates.add(cCert2);

        // 7. NOTIFICATIONS
        Notification n1 = new Notification(
                notifSeq.incrementAndGet(),
                0, // All
                "Welcome to Event Spectra! Centralized event portal for FISAT students.",
                "2026-10-28 09:00",
                false
        );
        Notification n2 = new Notification(
                notifSeq.incrementAndGet(),
                48,
                "Reminder: TechFest Inaugural Keynote is scheduled for Today at 02:00 PM in Main Auditorium.",
                "2026-10-30 08:00",
                false
        );
        Notification n3 = new Notification(
                notifSeq.incrementAndGet(),
                48,
                "Your registration for 'HackSpectra 2026' has been confirmed by FISAT ACM Chapter.",
                "2026-10-29 17:45",
                true
        );
        notifications.add(n1);
        notifications.add(n2);
        notifications.add(n3);

        // 8. REMINDERS
        Reminder rem1 = new Reminder(
                remSeq.incrementAndGet(),
                48,
                h1.getEventId(),
                h1.getTitle(),
                "2026-11-14 08:30 AM",
                "Report to CCF with laptop and college ID card."
        );
        reminders.add(rem1);
    }

    // Accessors
    public synchronized List<User> getUsers() {
        return users;
    }

    public synchronized List<Event> getEvents() {
        return events;
    }

    public synchronized List<Registration> getRegistrations() {
        return registrations;
    }

    public synchronized List<Certificate> getCertificates() {
        return certificates;
    }

    public synchronized List<Notification> getNotifications() {
        return notifications;
    }

    public synchronized List<Reminder> getReminders() {
        return reminders;
    }

    // Lookup Methods
    public synchronized User findUserById(int userId) {
        for (User u : users) {
            if (u.getUserId() == userId) return u;
        }
        return null;
    }

    public synchronized User findUserByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email.trim())) return u;
        }
        return null;
    }

    public synchronized Event findEventById(int eventId) {
        for (Event e : events) {
            if (e.getEventId() == eventId) return e;
        }
        return null;
    }

    // Event Management
    public synchronized Event addEvent(Event event) {
        event.setEventId(eventSeq.incrementAndGet());
        events.add(0, event); // newest first
        // Broadcast notification
        addNotification(0, "New Event Announced: '" + event.getTitle() + "' organized by " + event.getOrganizerClub());
        return event;
    }

    public synchronized boolean deleteEvent(int eventId) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getEventId() == eventId) {
                events.get(i).cancel();
                return true;
            }
        }
        return false;
    }

    // Registration Management
    public synchronized Registration registerStudentForEvent(int studentId, int eventId) {
        // Check if already registered
        for (Registration reg : registrations) {
            if (reg.getStudentId() == studentId && reg.getEventId() == eventId && !"Cancelled".equalsIgnoreCase(reg.getStatus())) {
                return reg; // Already registered
            }
        }
        Event ev = findEventById(eventId);
        User u = findUserById(studentId);
        if (ev != null && u != null) {
            ev.incrementRegistered();
            Registration reg = new Registration(regSeq.incrementAndGet(), studentId, eventId, "Confirmed");
            registrations.add(0, reg);

            // Add notification
            addNotification(studentId, "Successfully registered for '" + ev.getTitle() + "'. We look forward to your participation!");
            return reg;
        }
        return null;
    }

    public synchronized boolean cancelRegistration(int studentId, int eventId) {
        for (Registration reg : registrations) {
            if (reg.getStudentId() == studentId && reg.getEventId() == eventId && !"Cancelled".equalsIgnoreCase(reg.getStatus())) {
                reg.cancelRegistration();
                Event ev = findEventById(eventId);
                if (ev != null) ev.decrementRegistered();
                addNotification(studentId, "Your registration for '" + (ev != null ? ev.getTitle() : "Event") + "' has been cancelled.");
                return true;
            }
        }
        return false;
    }

    // Certificate Management
    public synchronized Certificate issueCertificate(int studentId, int eventId, String certType) {
        User u = findUserById(studentId);
        Event ev = findEventById(eventId);
        String sName = (u != null) ? u.getName() : "Student #" + studentId;
        String eTitle = (ev != null) ? ev.getTitle() : "Event #" + eventId;

        Certificate cert = new Certificate(
                certSeq.incrementAndGet(),
                studentId,
                eventId,
                sName,
                eTitle,
                certType
        );
        cert.generate();
        if (u != null) {
            cert.sendEmail(u.getEmail());
        }
        certificates.add(0, cert);
        addNotification(studentId, "Your Certificate for '" + eTitle + "' is now available for download!");
        return cert;
    }

    // Notification & Reminder Helpers
    public synchronized Notification addNotification(int userId, String message) {
        Notification note = new Notification(notifSeq.incrementAndGet(), userId, message);
        notifications.add(0, note);
        note.send();
        return note;
    }

    public synchronized Reminder addReminder(int userId, int eventId, String reminderTime, String message) {
        Event ev = findEventById(eventId);
        String title = (ev != null) ? ev.getTitle() : "Event #" + eventId;
        Reminder rem = new Reminder(remSeq.incrementAndGet(), userId, eventId, title, reminderTime, message);
        rem.setReminder();
        reminders.add(0, rem);
        return rem;
    }
}
