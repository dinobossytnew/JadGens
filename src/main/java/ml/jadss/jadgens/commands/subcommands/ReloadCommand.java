package ml.jadss.jadgens.commands.subcommands;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.tasks.ProduceMachineDelayTask;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class ReloadCommand { //messages.reloadMessages.permission | pluginReloaded

    public ReloadCommand(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender || sender.hasPermission(JadGens.getInstance().getConfig().getString("messages.reloadMessages.permission"))) {
            JadGens.getInstance().getTask().cancel();
            JadGens.getInstance().reloadConfig();
            JadGens.getInstance().getDataFile().reloadData();
            JadGens.getInstance().setupAPIDebug();
            JadGens.getInstance().setTask(new ProduceMachineDelayTask().runTaskTimer(JadGens.getInstance(), 0L, JadGens.getInstance().getConfig().getLong("machinesConfig.machinesDelay") * 20));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("messages.reloadMessages.pluginReloaded")));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("messages.noPermission")));
        }
    }
}
