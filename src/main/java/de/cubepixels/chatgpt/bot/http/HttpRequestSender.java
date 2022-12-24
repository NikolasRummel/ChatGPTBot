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

/**
 * @author Nikolas Rummel
 * @since 21.12.22
 */
public class HttpRequestSender {

    private final ChatGPTPlugin plugin;
    private final HttpClient httpClient;

    /**
     * Instantiates a new Http request sender.
     *
     * @param plugin the ChatGPT Plugin
     */
    public HttpRequestSender(ChatGPTPlugin plugin) {
        this.plugin = plugin;
        this.httpClient = HttpClient.newBuilder().build();
    }

    /**
     * Send an HTTP-Request to open.ai's api
     *
     * @param apiKey   the api key
     * @param question the question
     * @return the answer made by the ai
     */
    private String makeDefaultRequest(String apiKey, String question) {
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

    /**
     * Creates a request and broadcasts the answer in the Minecraft chat.
     *
     * @param apiKey   the api key
     * @param question the question
     */
    public void createRequest(String apiKey, String question) {
        String text = makeDefaultRequest(apiKey, question);
        // Sends the response in the chat
        plugin.sendBotMessage(text);
    }

    /**
     * Creates a normal request.
     *
     * @param apiKey   the api key
     * @param question the specific question, to search for similarities in the QA's
     * @return the answer made by openai
     */
    public String createQuestionRequest(String apiKey, String question) {
        return makeDefaultRequest(apiKey, question);
    }
}