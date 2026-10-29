/**
 * Event Spectra - FISAT Events Management System
 * OOP Concept: Base Class for Events & Encapsulation
 * 
 * Represents an event happening at FISAT.
 */
public class Event {
    private int eventId;
    private String title;
    private String description;
    private String date; // YYYY-MM-DD
    private String time; // HH:MM AM/PM
    private String venue;
    private String category; // Workshop, Competition, Seminar, Technical, Cultural
    private int capacity;
    private String organizerClub;
    private String status; // "Upcoming", "Today", "Completed", "Cancelled"
    private int registeredCount;

    public Event() {
        this.status = "Upcoming";
        this.registeredCount = 0;
    }

    public Event(int eventId, String title, String description, String date, String time,
                 String venue, String category, int capacity, String organizerClub) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.category = category;
        this.capacity = capacity;
        this.organizerClub = organizerClub;
        this.status = "Upcoming";
        this.registeredCount = 0;
    }

    // Methods from Class Diagram
    public boolean create() {
        this.status = "Upcoming";
        System.out.println("[EVENT] Event published: " + title);
        return true;
    }

    public boolean update() {
        System.out.println("[EVENT] Event details updated: " + title);
        return true;
    }

    public boolean cancel() {
        this.status = "Cancelled";
        System.out.println("[EVENT] Event cancelled: " + title);
        return true;
    }

    // Overridable polymorphic method
    public String getDetails() {
        return "Event ID: " + eventId +
                "\nTitle: " + title +
                "\nClub: " + organizerClub +
                "\nCategory: " + category +
                "\nDate & Time: " + date + " at " + time +
                "\nVenue: " + venue +
                "\nCapacity: " + registeredCount + "/" + capacity +
                "\nStatus: " + status;
    }

    // Helper registration increment/decrement
    public boolean incrementRegistered() {
        if (registeredCount < capacity) {
            registeredCount++;
            return true;
        }
        return false;
    }

    public void decrementRegistered() {
        if (registeredCount > 0) {
            registeredCount--;
        }
    }

    // Getters and Setters
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getOrganizerClub() {
        return organizerClub;
    }

    public void setOrganizerClub(String organizerClub) {
        this.organizerClub = organizerClub;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRegisteredCount() {
        return registeredCount;
    }

    public void setRegisteredCount(int registeredCount) {
        this.registeredCount = registeredCount;
    }
}
