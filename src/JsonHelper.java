import java.util.List;

/**
 * Event Spectra - Basic JSON Serialization Helper
 * Implemented using pure Core Java (java.lang.StringBuilder)
 * to avoid any third-party framework dependencies.
 */
public class JsonHelper {

    public static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public static String userToJson(User u) {
        if (u == null) return "null";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"userId\":").append(u.getUserId()).append(",");
        sb.append("\"name\":\"").append(escape(u.getName())).append("\",");
        sb.append("\"email\":\"").append(escape(u.getEmail())).append("\",");
        sb.append("\"phone\":\"").append(escape(u.getPhone())).append("\",");
        sb.append("\"role\":\"").append(escape(u.getRole())).append("\",");
        sb.append("\"roleDescription\":\"").append(escape(u.getRoleDescription())).append("\"");

        if (u instanceof Student) {
            Student s = (Student) u;
            sb.append(",\"studentId\":").append(s.getStudentId());
            sb.append(",\"course\":\"").append(escape(s.getCourse())).append("\"");
            sb.append(",\"semester\":").append(s.getSemester());
        } else if (u instanceof ClubOrganizer) {
            ClubOrganizer co = (ClubOrganizer) u;
            sb.append(",\"organizerId\":").append(co.getOrganizerId());
            sb.append(",\"clubName\":\"").append(escape(co.getClubName())).append("\"");
            sb.append(",\"clubDescription\":\"").append(escape(co.getClubDescription())).append("\"");
        } else if (u instanceof Administrator) {
            Administrator a = (Administrator) u;
            sb.append(",\"department\":\"").append(escape(a.getDepartment())).append("\"");
        }

        sb.append("}");
        return sb.toString();
    }

    public static String usersToJson(List<User> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(userToJson(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public static String eventToJson(Event e) {
        if (e == null) return "null";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"eventId\":").append(e.getEventId()).append(",");
        sb.append("\"title\":\"").append(escape(e.getTitle())).append("\",");
        sb.append("\"description\":\"").append(escape(e.getDescription())).append("\",");
        sb.append("\"date\":\"").append(escape(e.getDate())).append("\",");
        sb.append("\"time\":\"").append(escape(e.getTime())).append("\",");
        sb.append("\"venue\":\"").append(escape(e.getVenue())).append("\",");
        sb.append("\"category\":\"").append(escape(e.getCategory())).append("\",");
        sb.append("\"capacity\":").append(e.getCapacity()).append(",");
        sb.append("\"registeredCount\":").append(e.getRegisteredCount()).append(",");
        sb.append("\"organizerClub\":\"").append(escape(e.getOrganizerClub())).append("\",");
        sb.append("\"status\":\"").append(escape(e.getStatus())).append("\",");
        sb.append("\"isHackathon\":").append(e instanceof Hackathon);

        if (e instanceof Hackathon) {
            Hackathon h = (Hackathon) e;
            sb.append(",\"theme\":\"").append(escape(h.getTheme())).append("\"");
            sb.append(",\"duration\":\"").append(escape(h.getDuration())).append("\"");
            sb.append(",\"teamSize\":").append(h.getTeamSize());
        }

        sb.append("}");
        return sb.toString();
    }

    public static String eventsToJson(List<Event> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(eventToJson(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public static String registrationToJson(Registration r) {
        if (r == null) return "null";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"registrationId\":").append(r.getRegistrationId()).append(",");
        sb.append("\"studentId\":").append(r.getStudentId()).append(",");
        sb.append("\"eventId\":").append(r.getEventId()).append(",");
        sb.append("\"registrationDate\":\"").append(escape(r.getRegistrationDate())).append("\",");
        sb.append("\"status\":\"").append(escape(r.getStatus())).append("\"");
        sb.append("}");
        return sb.toString();
    }

    public static String registrationsToJson(List<Registration> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(registrationToJson(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public static String certificateToJson(Certificate c) {
        if (c == null) return "null";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"certificateId\":").append(c.getCertificateId()).append(",");
        sb.append("\"studentId\":").append(c.getStudentId()).append(",");
        sb.append("\"eventId\":").append(c.getEventId()).append(",");
        sb.append("\"studentName\":\"").append(escape(c.getStudentName())).append("\",");
        sb.append("\"eventTitle\":\"").append(escape(c.getEventTitle())).append("\",");
        sb.append("\"issueDate\":\"").append(escape(c.getIssueDate())).append("\",");
        sb.append("\"certificateUrl\":\"").append(escape(c.getCertificateUrl())).append("\",");
        sb.append("\"certificateType\":\"").append(escape(c.getCertificateType())).append("\"");
        sb.append("}");
        return sb.toString();
    }

    public static String certificatesToJson(List<Certificate> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(certificateToJson(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public static String notificationToJson(Notification n) {
        if (n == null) return "null";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"notificationId\":").append(n.getNotificationId()).append(",");
        sb.append("\"userId\":").append(n.getUserId()).append(",");
        sb.append("\"message\":\"").append(escape(n.getMessage())).append("\",");
        sb.append("\"dateTime\":\"").append(escape(n.getDateTime())).append("\",");
        sb.append("\"isRead\":").append(n.isRead());
        sb.append("}");
        return sb.toString();
    }

    public static String notificationsToJson(List<Notification> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(notificationToJson(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public static String reminderToJson(Reminder r) {
        if (r == null) return "null";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"reminderId\":").append(r.getReminderId()).append(",");
        sb.append("\"userId\":").append(r.getUserId()).append(",");
        sb.append("\"eventId\":").append(r.getEventId()).append(",");
        sb.append("\"eventTitle\":\"").append(escape(r.getEventTitle())).append("\",");
        sb.append("\"reminderTime\":\"").append(escape(r.getReminderTime())).append("\",");
        sb.append("\"message\":\"").append(escape(r.getMessage())).append("\",");
        sb.append("\"active\":").append(r.isActive());
        sb.append("}");
        return sb.toString();
    }

    public static String remindersToJson(List<Reminder> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(reminderToJson(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Extracts a string field from a simple JSON string: {"key":"value", ...}
     */
    public static String getJsonString(String json, String key) {
        if (json == null) return "";
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]*)\"";
        java.util.regex.Matcher m = java.util.regex.Pattern.compile(pattern).matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    /**
     * Extracts an int field from a simple JSON string: {"key":123, ...}
     */
    public static int getJsonInt(String json, String key, int defaultVal) {
        if (json == null) return defaultVal;
        String pattern = "\"" + key + "\"\\s*:\\s*([0-9]+)";
        java.util.regex.Matcher m = java.util.regex.Pattern.compile(pattern).matcher(json);
        if (m.find()) {
            try {
                return Integer.parseInt(m.group(1));
            } catch (Exception ex) {
                return defaultVal;
            }
        }
        return defaultVal;
    }
}
