package api;

import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiService {
    private static final String BASE_URL = "http://localhost:8080/api";
    private static String authToken = null; // To store the JWT token

    // Private helper for making standard JSON API calls
    private static String makeRequest(String method, String endpoint, JSONObject jsonBody, boolean requiresAuth) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        if (requiresAuth) {
            if (authToken == null) {
                throw new Exception("Not authenticated. Please log in.");
            }
            con.setRequestProperty("Authorization", "Bearer " + authToken);
        }

        if (jsonBody != null) {
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                os.write(jsonBody.toString().getBytes(StandardCharsets.UTF_8));
            }
        }

        int code = con.getResponseCode();
        InputStream is = (code >= 200 && code < 300) ? con.getInputStream() : con.getErrorStream();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            if (code >= 200 && code < 300) {
                return response.toString();
            } else {
                throw new Exception("Error: " + response.toString() + " (HTTP " + code + ")");
            }
        }
    }

    public static JSONObject login(String username, String password) throws Exception {
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);
        String response = makeRequest("POST", "/auth/login", body, false);
        JSONObject responseJson = new JSONObject(response);

        if (responseJson.has("token")) {
            authToken = responseJson.getString("token");
        }
        return responseJson;
    }

    public static String registerMentor(String username, String password, String name) throws Exception {
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);
        body.put("name", name);
        return makeRequest("POST", "/auth/register-mentor", body, false);
    }

    public static String registerStudent(String mentorUsername, String mentorPassword,
                                         String studentUsername, String studentPassword, String studentName) throws Exception {
        JSONObject body = new JSONObject();
        body.put("mentorUsername", mentorUsername);
        body.put("mentorPassword", mentorPassword);
        body.put("studentUsername", studentUsername);
        body.put("studentPassword", studentPassword);
        body.put("studentName", studentName);
        return makeRequest("POST", "/auth/register-student", body, false);
    }

    // --- MENTOR APIS ---
    public static String createSnippet(String title, String code) throws Exception {
        JSONObject body = new JSONObject();
        body.put("title", title);
        body.put("code", code); // CORRECT: Key is "code"
        return makeRequest("POST", "/mentor/snippets/create", body, true);
    }

    public static String getAllSubmissions() throws Exception {
        return makeRequest("GET", "/mentor/submissions", null, true);
    }

    // --- STUDENT APIS ---
    public static String getAvailableSnippets() throws Exception {
        return makeRequest("GET", "/student/snippets", null, true);
    }

    public static String submitCode(String username, String snippetTitle, String submittedCode) throws Exception {
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("snippetTitle", snippetTitle);
        body.put("submittedCode", submittedCode);
        return makeRequest("POST", "/student/submit", body, true);
    }

    // --- VIVA APIS ---
    public static String generateVivaQuestion(String submittedCode) throws Exception {
        JSONObject body = new JSONObject();
        body.put("code", submittedCode);
        return makeRequest("POST", "/viva/generate-question", body, true);
    }

    public static String evaluateViva(long submissionId, String code, String question, String answer) throws Exception {
        JSONObject body = new JSONObject();
        body.put("submissionId", submissionId);
        body.put("code", code);
        body.put("question", question);
        body.put("answer", answer);
        return makeRequest("POST", "/viva/evaluate-answer", body, true);
    }

    // --- FILE DOWNLOAD API ---
    public static InputStream downloadSubmissionsFile() throws Exception {
        URL url = new URL(BASE_URL + "/mentor/submissions/export");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        if (authToken == null) {
            throw new Exception("Not authenticated. Please log in.");
        }
        con.setRequestProperty("Authorization", "Bearer " + authToken);

        int code = con.getResponseCode();
        if (code >= 200 && code < 300) {
            return con.getInputStream();
        } else {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    errorResponse.append(responseLine.trim());
                }
                throw new IOException("Failed to download file: " + errorResponse.toString() + " (HTTP " + code + ")");
            } catch (Exception e) {
                throw new IOException("Failed to download file: HTTP " + code);
            }
        }
    }
}