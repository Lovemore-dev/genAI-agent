package agent;

import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeminiKeyTest {
    public static void main(String[] args) throws Exception {
        String apiKey = System.getenv("GEMINI_API_KEY");

        if (apiKey == null || apiKey.isBlank()){
            System.err.println("GEMINI_API_KEY environment variable is not set.");
            return;
        }

        System.out.println("Key found, length: " + apiKey.length());

        JSONObject part = new JSONObject().put("text", "Say hello in one short sentence");
        JSONObject content = new JSONObject().put("parts", new JSONArray().put(part));
        JSONObject body = new JSONObject().put("contents", new JSONArray().put(content));

        System.out.println("Request body: " + body.toString());

        String model = "gemini-3.5-flash-lite";
        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-goog-api-key", apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        //System.out.println("Body: " + response.body());
        JSONObject json = new JSONObject(response.body());
        String reply = json.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");

        System.out.println("Reply: " + reply);
    }
}
