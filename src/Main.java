import java.io.IOException;

/**
 * Event Spectra - FISAT Events Management System
 * Course: Object Oriented Programming in Java (OOPS Project)
 * 
 * Project Team:
 *  - Joanna Jomon     (Roll No. 46)
 *  - Joel Shantu      (Roll No. 47)
 *  - Joseph P Subash  (Roll No. 48)
 *  - Joshua Jomesh    (Roll No. 49)
 *  - Jovial J Thattil (Roll No. 50)
 * 
 * Main Entry Point:
 *  1. Demonstrates Core OOP Principles (Inheritance, Polymorphism, Encapsulation, Abstraction).
 *  2. Initializes the in-memory DataStore.
 *  3. Launches the built-in lightweight Java HTTP Web Server.
 */
public class Main {
    public static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        printBanner();
        demonstrateOOPConcepts();

        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port specified. Using default: " + DEFAULT_PORT);
            }
        }

        startServer(port);
    }

    private static void printBanner() {
        System.out.println("==================================================================");
        System.out.println("                       EVENT SPECTRA                              ");
        System.out.println("                 All events in a nutshell                         ");
        System.out.println("      A centralized event management application for FISAT        ");
        System.out.println("==================================================================");
        System.out.println("PROJECT TEAM:");
        System.out.println("  1. Joanna Jomon      - Roll No: 46");
        System.out.println("  2. Joel Shantu       - Roll No: 47");
        System.out.println("  3. Joseph P Subash   - Roll No: 48");
        System.out.println("  4. Joshua Jomesh     - Roll No: 49");
        System.out.println("  5. Jovial J Thattil  - Roll No: 50");
        System.out.println("==================================================================");
    }

    /**
     * Demonstrates essential Java OOP principles to satisfy viva / grading criteria.
     */
    private static void demonstrateOOPConcepts() {
        System.out.println("\n[OOP DEMO] Validating Core Object-Oriented Principles in Java...");

        // 1. Abstraction & Inheritance: User is abstract, instantiated via Student
        User studentUser = new Student(48, "Joseph P Subash", "joseph.subash@fisat.ac.in", "pass123", "+91 98471 23456", 48, "B.Tech CSE", 4);
        System.out.println("-> Inheritance & Abstraction: Created User object of type: " + studentUser.getClass().getSimpleName());
        System.out.println("   Role description (abstract method implementation): " + studentUser.getRoleDescription());

        // 2. Polymorphism: Event vs Hackathon
        Event regularEvent = new Event(101, "Deep Learning Workshop", "Hands-on PyTorch", "2026-11-05", "10:00 AM", "Auditorium 2", "Workshop", 80, "IEEE SB FISAT");
        Event hackathonEvent = new Hackathon(102, "HackSpectra 2026", "24hr Coding", "2026-11-14", "09:00 AM", "CCF", 120, "ACM FISAT", "Autonomous AI", "24 Hours", 4);

        System.out.println("\n-> Polymorphism (Runtime Method Overriding):");
        printEventDetailsPolymorphic(regularEvent);
        printEventDetailsPolymorphic(hackathonEvent);

        System.out.println("[OOP DEMO] Verification complete. System ready.\n");
    }

    // Demonstrates runtime polymorphism where 'Event e' calls Hackathon's getDetails() when passed a Hackathon
    private static void printEventDetailsPolymorphic(Event e) {
        System.out.println("   Calling e.getDetails() on " + e.getClass().getSimpleName() + ":");
        String details = e.getDetails().replace("\n", "\n   | ");
        System.out.println("   | " + details);
    }

    private static void startServer(int port) {
        try {
            SimpleHttpServer server = new SimpleHttpServer(port);
            server.start();
            System.out.println("\n>> Open your web browser and navigate to: http://localhost:" + port + "/");
            System.out.println(">> Press Ctrl+C in the console to stop the server.\n");
        } catch (IOException e) {
            System.err.println("Failed to start server on port " + port + ": " + e.getMessage());
            if (port == DEFAULT_PORT) {
                System.out.println("Retrying on alternative port 8081...");
                startServer(8081);
            }
        }
    }
}
