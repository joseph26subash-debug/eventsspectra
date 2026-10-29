import java.util.ArrayList;
import java.util.List;

/**
 * Event Spectra - FISAT Events Management System
 * OOP Concept: Inheritance (ClubOrganizer extends User)
 * 
 * Represents a Club Coordinator (e.g., ACM, IEEE, CSI, FOSS, GDSC) who organizes events,
 * manages registrations, and issues certificates.
 */
public class ClubOrganizer extends User {
    private int organizerId;
    private String clubName;
    private String clubDescription;

    public ClubOrganizer() {
        super();
        setRole("COORDINATOR");
    }

    public ClubOrganizer(int userId, String name, String email, String password, String phone,
                         int organizerId, String clubName, String clubDescription) {
        super(userId, name, email, password, phone, "COORDINATOR");
        this.organizerId = organizerId;
        this.clubName = clubName;
        this.clubDescription = clubDescription;
    }

    @Override
    public String getRoleDescription() {
        return "Club Coordinator (" + clubName + ")";
    }

    // Methods from Class Diagram
    public Event createEvent(Event event) {
        System.out.println("[CLUB] " + clubName + " created new event: " + event.getTitle());
        event.create();
        return event;
    }

    public boolean updateEvent(Event event) {
        System.out.println("[CLUB] " + clubName + " updated event: " + event.getTitle());
        return event.update();
    }

    public boolean deleteEvent(Event event) {
        if (event != null) {
            System.out.println("[CLUB] " + clubName + " deleted event: " + event.getTitle());
            return event.cancel();
        }
        return false;
    }

    public List<Registration> manageEvent(int eventId, List<Registration> allRegistrations) {
        List<Registration> eventRegs = new ArrayList<>();
        for (Registration reg : allRegistrations) {
            if (reg.getEventId() == eventId) {
                eventRegs.add(reg);
            }
        }
        return eventRegs;
    }

    // Getters and Setters
    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public void setClubDescription(String clubDescription) {
        this.clubDescription = clubDescription;
    }
}
