package ml.jadss.jadgens.commands.subcommands;

import ml.jadss.jadgens.JadGens;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand {

    public HelpCommand(CommandSender sender) {
        for (String s : JadGens.getInstance().getConfig().getStringList("messages.helpMessages")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }
}
