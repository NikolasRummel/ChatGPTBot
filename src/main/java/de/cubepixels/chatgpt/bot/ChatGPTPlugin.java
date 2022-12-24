package de.cubepixels.chatgpt.bot;

import de.cubepixels.chatgpt.bot.http.HttpRequestSender;
import de.cubepixels.chatgpt.bot.listener.AsyncChatListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Nikolas Rummel
 * @since 21.12.22
 */
public class ChatGPTPlugin extends JavaPlugin {

    private HttpRequestSender requestSender;
    @Getter private String botName, prefix, chatColor, apiKey, permission;

    @Override
    public void onEnable() {
        this.loadConfig();
        this.requestSender = new HttpRequestSender(this);

        super.getServer().getPluginManager().registerEvents(new AsyncChatListener(this), this);
    }

    private void loadConfig() {
        super.saveDefaultConfig();

        this.botName = super.getConfig().getString("botName");
        this.prefix = super.getConfig().getString("prefix");
        this.chatColor = super.getConfig().getString("messageColor");
        this.apiKey = super.getConfig().getString("apiKey");
        this.permission = super.getConfig().getString("permission");

        assert this.apiKey != null;
        if(this.apiKey.contains("your-api-key")) {
            Bukkit.getConsoleSender().sendMessage("§cThe API-Key was not set! Please add your openai api-key in the config.yml!");
        }
    }

    public void sendRequest(String question) {
        requestSender.createRequest(apiKey, question);
    }

    public void sendBotMessage(String text) {
        // Remove first linebreak
        text = text.substring(1);
        Bukkit.broadcastMessage(prefix + chatColor + text);
    }
}
