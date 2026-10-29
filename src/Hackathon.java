/**
 * Event Spectra - FISAT Events Management System
 * OOP Concept: Inheritance & Polymorphism (Hackathon extends Event)
 * 
 * Specialized event type representing competitive coding hackathons.
 */
public class Hackathon extends Event {
    private String theme;
    private String duration; // e.g., "24 Hours", "36 Hours"
    private int teamSize;    // e.g., 4 members

    public Hackathon() {
        super();
        setCategory("Hackathon");
    }

    public Hackathon(int eventId, String title, String description, String date, String time,
                     String venue, int capacity, String organizerClub,
                     String theme, String duration, int teamSize) {
        super(eventId, title, description, date, time, venue, "Hackathon", capacity, organizerClub);
        this.theme = theme;
        this.duration = duration;
        this.teamSize = teamSize;
    }

    // Polymorphic method override from Event
    @Override
    public String getDetails() {
        return super.getDetails() +
                "\n[HACKATHON SPECIAL]" +
                "\nTheme: " + theme +
                "\nDuration: " + duration +
                "\nTeam Size: " + teamSize + " members/team";
    }

    @Override
    public boolean create() {
        System.out.println("[HACKATHON] Hackathon '" + getTitle() + "' successfully initialized with theme: " + theme);
        return super.create();
    }

    @Override
    public boolean update() {
        System.out.println("[HACKATHON] Hackathon parameters updated for: " + getTitle());
        return super.update();
    }

    // Getters and Setters
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }
}
