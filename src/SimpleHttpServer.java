import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Event Spectra - Built-in Java HTTP Server & REST API
 * Powered by standard Java SE com.sun.net.httpserver and MySQL JDBC.
 * 
 * Exposes REST endpoints for:
 *  - Authentication (/api/auth/login, /api/auth/register)
 *  - User History Restoration (/api/my-history)
 *  - Events & Hackathons CRUD (/api/events)
 *  - Registrations & Bookings (/api/registrations, /api/register, /api/cancel-registration)
 *  - Official E-Certificates (/api/certificates, /api/certificates/issue)
 *  - Notifications & Campus Alerts (/api/notifications)
 *  - Scheduled Reminders (/api/reminders)
 *  - Database Status (/api/db-status)
 */
public class SimpleHttpServer {
    private int port;
    private HttpServer server;
    private File webDir;

    public SimpleHttpServer(int port) {
        this.port = port;
        // Check web directory location (either ./web or current root .)
        File dir = new File("web");
        if (!dir.exists() || !new File(dir, "index.html").exists()) {
            dir = new File(".");
        }
        this.webDir = dir;
    }

    public void start() throws IOException {
        // Ensure DatabaseManager is initialized
        DatabaseManager.getInstance();

        server = HttpServer.create(new InetSocketAddress(port), 0);

        // Authentication & Session API
        server.createContext("/api/auth/login", new AuthLoginHandler());
        server.createContext("/api/auth/register", new AuthRegisterHandler());
        server.createContext("/api/my-history", new UserHistoryHandler());
        server.createContext("/api/db-status", new DbStatusHandler());

        // Domain Entity APIs
        server.createContext("/api/users", new UsersHandler());
        server.createContext("/api/events", new EventsHandler());
        server.createContext("/api/registrations", new RegistrationsHandler());
        server.createContext("/api/register", new RegisterHandler());
        server.createContext("/api/cancel-registration", new CancelRegistrationHandler());
        server.createContext("/api/certificates", new CertificatesHandler());
        server.createContext("/api/certificates/issue", new IssueCertificateHandler());
        server.createContext("/api/notifications", new NotificationsHandler());
        server.createContext("/api/notifications/read", new MarkNotificationReadHandler());
        server.createContext("/api/reminders", new RemindersHandler());
        server.createContext("/api/team", new TeamHandler());

        // Static Web UI Context
        server.createContext("/", new StaticFileHandler());

        server.setExecutor(null); // default executor
        server.start();
        System.out.println("===============================================================");
        System.out.println(" Event Spectra Server is LIVE!");
        System.out.println(" URL: http://localhost:" + port + "/");
        System.out.println(" Database: MySQL (" + (DatabaseManager.getInstance().isConnected() ? "CONNECTED" : "IN-MEMORY FALLBACK") + ")");
        System.out.println(" Web root: " + webDir.getAbsolutePath());
        System.out.println("===============================================================");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Server stopped.");
        }
    }

    // --- Helper Methods ---

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private static String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null) return result;
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                try {
                    result.put(URLDecoder.decode(entry[0], "UTF-8"), URLDecoder.decode(entry[1], "UTF-8"));
                } catch (Exception e) {
                    result.put(entry[0], entry[1]);
                }
            } else if (entry.length == 1) {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    // --- Handlers ---

    // 1. Static Files (HTML, CSS, JS)
    class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path == null || path.equals("/") || path.isEmpty()) {
                path = "/index.html";
            } else if (path.equals("/admin") || path.equals("/admin/")) {
                path = "/admin.html";
            }

            File file = new File(webDir, path);
            if (!file.exists() || file.isDirectory()) {
                file = new File(webDir, "index.html");
            }

            if (file.exists() && !file.isDirectory()) {
                String mime = "text/html";
                if (path.endsWith(".css")) mime = "text/css";
                else if (path.endsWith(".js")) mime = "application/javascript";
                else if (path.endsWith(".png")) mime = "image/png";
                else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) mime = "image/jpeg";
                else if (path.endsWith(".svg")) mime = "image/svg+xml";

                byte[] data = Files.readAllBytes(file.toPath());
                exchange.getResponseHeaders().set("Content-Type", mime + "; charset=UTF-8");
                exchange.sendResponseHeaders(200, data.length);
                OutputStream os = exchange.getResponseBody();
                os.write(data);
                os.close();
            } else {
                String notFound = "<h1>404 Not Found</h1><p>File not found: " + path + "</p>";
                exchange.sendResponseHeaders(404, notFound.length());
                OutputStream os = exchange.getResponseBody();
                os.write(notFound.getBytes());
                os.close();
            }
        }
    }

    // 2. Auth Login Handler (POST /api/auth/login)
    static class AuthLoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 200, "{}");
                return;
            }

            String body = readRequestBody(exchange);
            String emailOrUser = JsonHelper.getJsonString(body, "email");
            if (emailOrUser.isEmpty()) {
                emailOrUser = JsonHelper.getJsonString(body, "username");
            }
            String password = JsonHelper.getJsonString(body, "password");

            User u = DatabaseManager.getInstance().authenticate(emailOrUser, password);
            if (u != null) {
                String resp = "{\"success\":true,\"user\":" + JsonHelper.userToJson(u) + "}";
                sendJsonResponse(exchange, 200, resp);
            } else {
                sendJsonResponse(exchange, 401, "{\"success\":false,\"error\":\"Invalid credentials. Please verify your email/username and password.\"}");
            }
        }
    }

    // 3. Auth Register Handler (POST /api/auth/register)
    static class AuthRegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 200, "{}");
                return;
            }

            String body = readRequestBody(exchange);
            String name = JsonHelper.getJsonString(body, "name");
            String email = JsonHelper.getJsonString(body, "email");
            String password = JsonHelper.getJsonString(body, "password");
            String phone = JsonHelper.getJsonString(body, "phone");
            String course = JsonHelper.getJsonString(body, "course");
            int semester = JsonHelper.getJsonInt(body, "semester", 4);

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Name, email, and password are required.\"}");
                return;
            }

            User newUser = DatabaseManager.getInstance().registerUser(name, email, password, phone, course.isEmpty() ? "B.Tech Computer Science & Engineering" : course, semester);
            if (newUser != null) {
                String resp = "{\"success\":true,\"user\":" + JsonHelper.userToJson(newUser) + "}";
                sendJsonResponse(exchange, 201, resp);
            } else {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Registration failed or email already registered.\"}");
            }
        }
    }

    // 4. User History Handler (GET /api/my-history?userId=...)
    // Retrieves past registrations, certificates, reminders, notifications from MySQL!
    static class UserHistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 200, "{}");
                return;
            }

            Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
            int userId = 48;
            if (params.containsKey("userId")) {
                try {
                    userId = Integer.parseInt(params.get("userId"));
                } catch (Exception ignored) {}
            }

            List<Registration> regs = DatabaseManager.getInstance().getRegistrationsForStudent(userId);
            List<Certificate> certs = DatabaseManager.getInstance().getCertificatesForStudent(userId);

            List<Notification> allNotes = DatabaseManager.getInstance().getAllNotifications();
            List<Notification> myNotes = new java.util.ArrayList<>();
            for (Notification n : allNotes) {
                if (n.getUserId() == userId || n.getUserId() == 0) myNotes.add(n);
            }

            List<Reminder> allRems = DatabaseManager.getInstance().getAllReminders();
            List<Reminder> myRems = new java.util.ArrayList<>();
            for (Reminder r : allRems) {
                if (r.getUserId() == userId) myRems.add(r);
            }

            StringBuilder sb = new StringBuilder("{");
            sb.append("\"registrations\":").append(JsonHelper.registrationsToJson(regs)).append(",");
            sb.append("\"certificates\":").append(JsonHelper.certificatesToJson(certs)).append(",");
            sb.append("\"notifications\":").append(JsonHelper.notificationsToJson(myNotes)).append(",");
            sb.append("\"reminders\":").append(JsonHelper.remindersToJson(myRems));
            sb.append("}");

            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    // 5. Db Status Handler (GET /api/db-status)
    static class DbStatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            boolean connected = DatabaseManager.getInstance().isConnected();
            String json = "{\"connected\":" + connected + ",\"type\":\"MySQL\",\"database\":\"event_spectra\"}";
            sendJsonResponse(exchange, 200, json);
        }
    }

    // 6. Users API
    static class UsersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 200, "{}");
                return;
            }
            List<User> list = DatabaseManager.getInstance().getAllUsers();
            sendJsonResponse(exchange, 200, JsonHelper.usersToJson(list));
        }
    }

    // 7. Events API
    static class EventsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendJsonResponse(exchange, 200, "{}");
                return;
            }

            if ("GET".equalsIgnoreCase(method)) {
                List<Event> list = DatabaseManager.getInstance().getAllEvents();
                sendJsonResponse(exchange, 200, JsonHelper.eventsToJson(list));
            } else if ("POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                String title = JsonHelper.getJsonString(body, "title");
                String desc = JsonHelper.getJsonString(body, "description");
                String date = JsonHelper.getJsonString(body, "date");
                String time = JsonHelper.getJsonString(body, "time");
                String venue = JsonHelper.getJsonString(body, "venue");
                String category = JsonHelper.getJsonString(body, "category");
                int capacity = JsonHelper.getJsonInt(body, "capacity", 50);
                String club = JsonHelper.getJsonString(body, "organizerClub");
                boolean isHackathon = "Hackathon".equalsIgnoreCase(category) || body.contains("\"isHackathon\":true");

                Event newEvent;
                if (isHackathon) {
                    String theme = JsonHelper.getJsonString(body, "theme");
                    String duration = JsonHelper.getJsonString(body, "duration");
                    int teamSize = JsonHelper.getJsonInt(body, "teamSize", 4);
                    newEvent = new Hackathon(0, title, desc, date, time, venue, capacity, club,
                            theme.isEmpty() ? "General Innovation" : theme,
                            duration.isEmpty() ? "24 Hours" : duration,
                            teamSize);
                } else {
                    newEvent = new Event(0, title, desc, date, time, venue, category, capacity, club);
                }

                DatabaseManager.getInstance().createEvent(newEvent);
                sendJsonResponse(exchange, 201, JsonHelper.eventToJson(newEvent));
            } else if ("DELETE".equalsIgnoreCase(method)) {
                Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
                if (params.containsKey("id")) {
                    int eventId = Integer.parseInt(params.get("id"));
                    boolean deleted = DatabaseManager.getInstance().cancelEvent(eventId);
                    sendJsonResponse(exchange, 200, "{\"success\":" + deleted + "}");
                } else {
                    sendJsonResponse(exchange, 400, "{\"error\":\"Missing id parameter\"}");
                }
            }
        }
    }

    // 8. Registrations API
    static class RegistrationsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 200, "{}");
                return;
            }
            Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
            if (params.containsKey("studentId")) {
                int sId = Integer.parseInt(params.get("studentId"));
                List<Registration> list = DatabaseManager.getInstance().getRegistrationsForStudent(sId);
                sendJsonResponse(exchange, 200, JsonHelper.registrationsToJson(list));
            } else {
                List<Registration> list = DatabaseManager.getInstance().getAllRegistrations();
                sendJsonResponse(exchange, 200, JsonHelper.registrationsToJson(list));
            }
        }
    }

    // 9. Register Handler
    static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 200, "{}");
                return;
            }
            String body = readRequestBody(exchange);
            int studentId = JsonHelper.getJsonInt(body, "studentId", 48);
            int eventId = JsonHelper.getJsonInt(body, "eventId", 0);

            Registration reg = DatabaseManager.getInstance().registerStudent(studentId, eventId);
            if (reg != null) {
                sendJsonResponse(exchange, 200, JsonHelper.registrationToJson(reg));
            } else {
                sendJsonResponse(exchange, 400, "{\"error\":\"Registration failed or event not found\"}");
            }
        }
    }

    // 10. Cancel Registration Handler
    static class CancelRegistrationHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 200, "{}");
                return;
            }
            String body = readRequestBody(exchange);
            int studentId = JsonHelper.getJsonInt(body, "studentId", 48);
            int eventId = JsonHelper.getJsonInt(body, "eventId", 0);

            boolean success = DatabaseManager.getInstance().cancelRegistration(studentId, eventId);
            sendJsonResponse(exchange, 200, "{\"success\":" + success + "}");
        }
    }

    // 11. Certificates API
    static class CertificatesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 200, "{}");
                return;
            }
            Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
            if (params.containsKey("studentId")) {
                int sId = Integer.parseInt(params.get("studentId"));
                List<Certificate> list = DatabaseManager.getInstance().getCertificatesForStudent(sId);
                sendJsonResponse(exchange, 200, JsonHelper.certificatesToJson(list));
            } else {
                List<Certificate> list = DatabaseManager.getInstance().getAllCertificates();
                sendJsonResponse(exchange, 200, JsonHelper.certificatesToJson(list));
            }
        }
    }

    // 12. Issue Certificate Handler
    static class IssueCertificateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 200, "{}");
                return;
            }
            String body = readRequestBody(exchange);
            int studentId = JsonHelper.getJsonInt(body, "studentId", 48);
            int eventId = JsonHelper.getJsonInt(body, "eventId", 0);
            String type = JsonHelper.getJsonString(body, "certificateType");
            if (type.isEmpty()) type = "Certificate of Participation";

            Certificate cert = DatabaseManager.getInstance().issueCertificate(studentId, eventId, type);
            if (cert != null) {
                sendJsonResponse(exchange, 201, JsonHelper.certificateToJson(cert));
            } else {
                sendJsonResponse(exchange, 500, "{\"error\":\"Failed to issue certificate\"}");
            }
        }
    }

    // 13. Notifications API
    static class NotificationsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendJsonResponse(exchange, 200, "{}");
                return;
            }

            if ("GET".equalsIgnoreCase(method)) {
                Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
                List<Notification> list = DatabaseManager.getInstance().getAllNotifications();
                if (params.containsKey("userId")) {
                    int uId = Integer.parseInt(params.get("userId"));
                    List<Notification> filtered = new java.util.ArrayList<>();
                    for (Notification n : list) {
                        if (n.getUserId() == uId || n.getUserId() == 0) filtered.add(n);
                    }
                    sendJsonResponse(exchange, 200, JsonHelper.notificationsToJson(filtered));
                } else {
                    sendJsonResponse(exchange, 200, JsonHelper.notificationsToJson(list));
                }
            } else if ("POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                int userId = JsonHelper.getJsonInt(body, "userId", 0);
                String msg = JsonHelper.getJsonString(body, "message");
                Notification n = DatabaseManager.getInstance().createNotification(userId, msg);
                sendJsonResponse(exchange, 201, JsonHelper.notificationToJson(n));
            }
        }
    }

    // 14. Mark Notification Read Handler
    static class MarkNotificationReadHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 200, "{}");
                return;
            }
            String body = readRequestBody(exchange);
            int id = JsonHelper.getJsonInt(body, "notificationId", 0);
            DatabaseManager.getInstance().markNotificationRead(id);
            sendJsonResponse(exchange, 200, "{\"success\":true}");
        }
    }

    // 15. Reminders API
    static class RemindersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendJsonResponse(exchange, 200, "{}");
                return;
            }

            if ("GET".equalsIgnoreCase(method)) {
                Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
                List<Reminder> list = DatabaseManager.getInstance().getAllReminders();
                if (params.containsKey("userId")) {
                    int uId = Integer.parseInt(params.get("userId"));
                    List<Reminder> filtered = new java.util.ArrayList<>();
                    for (Reminder r : list) {
                        if (r.getUserId() == uId) filtered.add(r);
                    }
                    sendJsonResponse(exchange, 200, JsonHelper.remindersToJson(filtered));
                } else {
                    sendJsonResponse(exchange, 200, JsonHelper.remindersToJson(list));
                }
            } else if ("POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                int userId = JsonHelper.getJsonInt(body, "userId", 48);
                int eventId = JsonHelper.getJsonInt(body, "eventId", 0);
                String time = JsonHelper.getJsonString(body, "reminderTime");
                String msg = JsonHelper.getJsonString(body, "message");

                Reminder rem = DatabaseManager.getInstance().createReminder(userId, eventId, time, msg);
                sendJsonResponse(exchange, 201, JsonHelper.reminderToJson(rem));
            }
        }
    }

    // 16. Project Team Handler (From Page 2 of Presentation)
    static class TeamHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 200, "{}");
                return;
            }
            String teamJson = "{\"project\":\"EVENT SPECTRA\",\"institution\":\"Federal Institute of Science and Technology (FISAT)\",\"team\":[" +
                    "{\"name\":\"Joanna Jomon\",\"rollNo\":46,\"role\":\"UI/UX & Registration Module\"}," +
                    "{\"name\":\"Joel Shantu\",\"rollNo\":47,\"role\":\"Notification & Reminder Module\"}," +
                    "{\"name\":\"Joseph P Subash\",\"rollNo\":48,\"role\":\"Backend Architecture & OOP Modeling\"}," +
                    "{\"name\":\"Joshua Jomesh\",\"rollNo\":49,\"role\":\"Certificate & Verification Module\"}," +
                    "{\"name\":\"Jovial J Thattil\",\"rollNo\":50,\"role\":\"Event & Club Management Module\"}" +
                    "]}";
            sendJsonResponse(exchange, 200, teamJson);
        }
    }
}
