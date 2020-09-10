package ml.jadss.jadgens.commands.subcommands;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.MachineLimiter;
import ml.jadss.jadgens.utils.MachineLookup;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommand {

    public InfoCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player pl = (Player) sender;
            MachineLimiter limiter = new MachineLimiter();
            MachineLookup lookup = new MachineLookup();
            for (String s : JadGens.getInstance().getConfig().getStringList("messages.infoCommand.msg")) {
                if (limiter.getMaxLimit(pl) == -1) {
                    s = s.replace("%max%", JadGens.getInstance().getConfig().getString("messages.infoCommand.infinite"));
                } else {
                    s = s.replace("%max%", String.valueOf(limiter.getMaxLimit(pl)));
                }
                s = s.replace("%has%", String.valueOf(lookup.getMachines(pl.getUniqueId())));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("messages.notPLayer")));
        }
    }
}
