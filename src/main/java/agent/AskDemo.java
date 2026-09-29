package agent;

public class AskDemo {
    public static void main(String[] args) throws Exception {
        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isBlank()){
            System.err.println("GEMINI_API_KEY is not set.");
            return;
        }

        GeminiClient client = new GeminiClient(apiKey, "gemini-3.5-flash-lite");
        //GeminiClient client = new GeminiClient(apiKey, "not-a-real-model");
        System.out.println(client.ask("Explain what an AI agent is in two sentences."));
    }
}
