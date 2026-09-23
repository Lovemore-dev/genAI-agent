package agent;

public class GeminiKeyTest {
    public static void main(String[] args) {
        String apiKey = System.getenv("GEMINI_API_KEY");

        if (apiKey == null || apiKey.isBlank()){
            System.err.println("GEMINI_API_KEY environment variable is not set.");
            return;
        }

        System.out.println("Key found, length: " + apiKey.length());
    }
}
