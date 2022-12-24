package de.cubepixels.chatgpt.bot;

import de.cubepixels.chatgpt.bot.commands.PluginCommand;
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
    private String botName, prefix, chatColor, apiKey, interactPermission, commandsPermission;


    @Override
    public void onEnable() {
        super.saveDefaultConfig();
        this.loadConfig();

        this.questionCollection = new QuestionCollection(this);
        this.requestSender = new HttpRequestSender(this);

        // Register the Chat Event
        super.getServer().getPluginManager().registerEvents(new AsyncChatListener(this), this);

        // Register the Plugin Command
        super.getCommand("gpt").setExecutor(new PluginCommand(this));
    }

    /**
     * Loads the config and checks if the api-key was set.
     */
    public void loadConfig() {

        this.botName = super.getConfig().getString("botName");
        this.prefix = super.getConfig().getString("prefix");
        this.chatColor = super.getConfig().getString("messageColor");
        this.apiKey = super.getConfig().getString("apiKey");
        this.interactPermission = super.getConfig().getString("permissions.interact");
        this.commandsPermission = super.getConfig().getString("permissions.commands");

        assert this.apiKey != null;
        if (this.apiKey.contains("your-api-key")) {
            Bukkit.getConsoleSender().sendMessage(
                "§cThe API-Key was not set! Please add your openai api-key in the config.yml!");
        }
        super.reloadConfig();
    }

    /**
     * First it checks if the question of a user is a QA and if so, it sends the corresponding
     * answer.
     * <p>
     * But if the question doesn't mach to a QA, a normal request to the AI will be sent.
     *
     * @param questionEdited the specific question for the AI to respond correctly
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
