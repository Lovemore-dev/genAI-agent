package agent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeminiClient {
    private final String apiKey;
    private final String model;
    private final HttpClient http = HttpClient.newHttpClient();

    public GeminiClient(String apiKey, String model){
        this.apiKey = apiKey;
        this.model = model;
    }

    private String buildBody(String prompt){
        JSONObject part = new JSONObject().put("text", prompt);
        JSONObject content = new JSONObject().put("parts", new JSONArray().put(part));
        return new JSONObject().put("contents", new JSONArray().put(content)).toString();
    }

    private  String extractText(String responseBody){
        return new JSONObject(responseBody)
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");
    }

    public String ask(String prompt) throws Exception{
        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-goog-api-key", apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(buildBody(prompt)))
                .build();

        int maxAttempts = 4;
        long waitMs = 1000;

        for (int attempt = 1; attempt <=maxAttempts ; attempt++) {
            HttpResponse<String> response =
                    http.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();

            if (status == 200){
                return extractText(response.body());
            }

            boolean temporary = (status == 429 || status == 503);
            if (!temporary || attempt == maxAttempts) {
                throw new RuntimeException("Gemini API error " + status + ": " + response.body());
            }

            System.err.println("Status " + status + ", retrying in " + waitMs + " ms (attempt " + attempt + ")");
            Thread.sleep(waitMs);
            waitMs = waitMs*2;
        }

        throw new IllegalStateException("Unreachable");
    }
}
