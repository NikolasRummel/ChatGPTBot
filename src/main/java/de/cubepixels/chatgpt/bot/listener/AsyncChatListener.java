package de.cubepixels.chatgpt.bot.listener;

import de.cubepixels.chatgpt.bot.ChatGPTPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Nikolas Rummel
 * @since 21.12.22
 */
public class AsyncChatListener implements Listener {

    private final ChatGPTPlugin plugin;

    public AsyncChatListener(ChatGPTPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!event.getMessage().startsWith(plugin.getBotName())) {
            return;
        }

        // Check if permission was set
        if (plugin.getPermission().length() != 0) {

            // Check if the player has the set permission
            if(!event.getPlayer().hasPermission(plugin.getPermission())) {
                return;
            }
        }

        // Async because the question would be in chat after the answer
        new BukkitRunnable() {
            @Override
            public void run() {
                // removes the bot name from the question
                String question = event.getMessage().replaceAll(plugin.getBotName(), "");

                // removes " char because it manipulates the json string
                question = question.replaceAll("\"", "");

                // Send HttpRequest
                plugin.sendRequest(question);
            }
        }.runTaskLaterAsynchronously(plugin, 1);
    }

}
