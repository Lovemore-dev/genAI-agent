package agent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ListModels {
    public static void main(String[] args) throws Exception {
        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isBlank()){
            System.err.println("GEMINI_API_KEY environment variable is not set.");
            return;
        }

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models"))
                .header("x-goog-api-key", apiKey)
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());

        JSONObject json = new JSONObject(response.body());
        JSONArray models = json.getJSONArray("models");

        for (int i = 0; i < models.length(); i++) {
            JSONObject m = models.getJSONObject(i);
            String methods = m.getJSONArray("supportedGenerationMethods").toString();
            if (methods.contains("generateContent")){
                System.out.println(m.getString("name"));
            }
        }

    }
}
