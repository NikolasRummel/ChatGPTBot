package de.cubepixels.chatgpt.bot.commands;

import de.cubepixels.chatgpt.bot.ChatGPTPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * The Executor for this plugin's command.
 *
 * @author Surfy
 * @since 24.12.22
 */
public class PluginCommand implements CommandExecutor {

    private final ChatGPTPlugin plugin;

    /**
     * Instantiates the Listener.
     *
     * @param plugin the plugin
     */
    public PluginCommand(ChatGPTPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Command execution
     *
     * @param sender the command sender
     * @param command the command
     * @param label the command's name
     * @param args the command's arguments
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender has the permission to run this command.
        if(!sender.hasPermission(plugin.getCommandsPermission())) {
            sender.sendMessage(plugin.getPrefix() + plugin.getChatColor() + " I'm sorry, but you don't have enough permissions to run this command.");
            return false;
        }

        // Check if the sender is inputting any arguments.
        if(args.length < 1) {
            // TODO: Commands usage including yours

            sender.sendMessage(plugin.getPrefix() + plugin.getChatColor() + " You're missing some params.");
            return false;
        }

        // Check for which argument was input
        if(args[0].equalsIgnoreCase("reload")) {
            // Initiate the config's strings and vars
            plugin.loadConfig();

            sender.sendMessage(plugin.getPrefix() + plugin.getChatColor() + " Config reloaded successfully.");
            return false;
        }
        return false;
    }
}
