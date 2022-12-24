package de.cubepixels.chatgpt.bot;

import de.cubepixels.chatgpt.bot.http.HttpRequestSender;
import de.cubepixels.chatgpt.bot.listener.AsyncChatListener;
import de.cubepixels.chatgpt.bot.question.QuestionCollection;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The Main class of this Plugin.
 *
 * @author Nikolas Rummel
 * @since 21.12.22
 */
@Getter
public class ChatGPTPlugin extends JavaPlugin {

    private QuestionCollection questionCollection;
    private HttpRequestSender requestSender;
    private String botName, prefix, chatColor, apiKey, permission;


    @Override
    public void onEnable() {
        this.loadConfig();

        this.questionCollection = new QuestionCollection(this);
        this.requestSender = new HttpRequestSender(this);

        super.getServer().getPluginManager().registerEvents(new AsyncChatListener(this), this);
    }

    /**
     * Loads the config and check if the api-key was set.
     */
    private void loadConfig() {
        super.saveDefaultConfig();

        this.botName = super.getConfig().getString("botName");
        this.prefix = super.getConfig().getString("prefix");
        this.chatColor = super.getConfig().getString("messageColor");
        this.apiKey = super.getConfig().getString("apiKey");
        this.permission = super.getConfig().getString("permission");

        assert this.apiKey != null;
        if (this.apiKey.contains("your-api-key")) {
            Bukkit.getConsoleSender().sendMessage(
                "§cThe API-Key was not set! Please add your openai api-key in the config.yml!");
        }
    }

    /**
     * First it checks if the question of a user is a QA and if so, it sends the corresponding
     * answer.
     * <p>
     * But if the question doesn't mach to a QA, a normal request to the ai will be sent.
     *
     * @param questionEdited the specific question for the ai to respond correctly
     * @param question       the question a user made
     */
    public void sendRequest(String questionEdited, String question) {
        //First check if the message is similar to one of the QA's
        String response = requestSender.createQuestionRequest(apiKey, questionEdited);

        // if there is no match, create a normal request
        if (response.contains("No match found")) {
            requestSender.createRequest(apiKey, question);

        } else {
            // If there is a match, send the corresponding answer
            questionCollection.sendAnswer(response);
        }
    }

    /**
     * Broadcast a message in the chat.
     *
     * @param text the text
     */
    public void sendBotMessage(String text) {
        // Remove first linebreak
        text = text.substring(1);
        Bukkit.broadcastMessage(prefix + chatColor + text);
    }
}
