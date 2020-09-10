package ml.jadss.jadgens.commands.subcommands;

import ml.jadss.jadgens.JadGens;
import ml.jadss.jadgens.utils.Fuel;
import ml.jadss.jadgens.utils.Machine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GiveCommand { // /jadgens give <type> <id> <player>

    public GiveCommand(CommandSender sender, String[] args) {
        if (sender.hasPermission(JadGens.getInstance().getConfig().getString("messages.giveMessages.permission")) || sender instanceof ConsoleCommandSender) {
            if (args.length == 4) {
                if (args[1].equalsIgnoreCase("fuel") || args[1].equalsIgnoreCase("f")) {
                    if (JadGens.getInstance().getConfig().isConfigurationSection("fuels." + args[2])) {
                        Player target = Bukkit.getPlayer(args[3]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("messages.playerNotFound")));
                        } else {
                            target.getInventory().addItem(new Fuel().createItem(Integer.parseInt(args[2])));
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("messages.giveMessages.givenFuel")));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("messages.giveMessages.idNotFound")));
                    }
                } else if (args[1].equalsIgnoreCase("machine") || args[1].equalsIgnoreCase("m")) {
                    if (JadGens.getInstance().getConfig().isConfigurationSection("machines." + args[2])) {
                        Player target = Bukkit.getPlayer(args[3]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("messages.playerNotFound")));
                        } else {
                            target.getInventory().addItem(new Machine().createItem(Integer.parseInt(args[2])));
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("messages.giveMessages.givenMachine")));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("messages.giveMessages.idNotFound")));
                    }
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("messages.giveMessages.usage")));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', JadGens.getInstance().getConfig().getString("messages.noPermission")));
        }
    }
}
