package de.cubepixels.chatgpt.bot.http;

import de.cubepixels.chatgpt.bot.ChatGPTPlugin;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.json.JSONArray;
import org.json.JSONObject;

public class HttpRequestSender {

    private final ChatGPTPlugin plugin;
    private final HttpClient httpClient;

    public HttpRequestSender(ChatGPTPlugin plugin) {
        this.plugin = plugin;
        this.httpClient = HttpClient.newBuilder().build();
    }

    private String makeDefaultRequest(String apiKey, String question, int modelVersion) {
        // Create request
        // https://beta.openai.com/docs/api-reference/making-requests
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.openai.com/v1/completions"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .POST(BodyPublishers.ofString("{\n"
                + "  \"model\": \"text-davinci-003\",\n"
                + "  \"prompt\": \"" + question + "\",\n"
                + "  \"temperature\": 0.12,\n"
                + "  \"max_tokens\": 256,\n"
                + "  \"top_p\": 0.63,\n"
                + "  \"frequency_penalty\": 0,\n"
                + "  \"presence_penalty\": 0\n"
                + "}"))
            .build();

        try {
            // Get response
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            // Convert response in a json object
            JSONObject json = new JSONObject(response.body());

            // Get the value of the "choices" field
            JSONArray choices = json.getJSONArray("choices");

            // Get the first element of the "choices" array
            JSONObject firstChoice = choices.getJSONObject(0);

            // Get the value of the "text" field from the first element of the "choices" array
            return firstChoice.getString("text");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "§cError!";
    }

    public void createRequest(String apiKey, String question) {
        String text = makeDefaultRequest(apiKey, question, 3);
        // Sends the response in the chat
        plugin.sendBotMessage(text);
    }

    public String createQuestionRequest(String apiKey, String question) {
        return makeDefaultRequest(apiKey, question, 3);
    }
}