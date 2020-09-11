package ml.jadss.jadgens.utils;

import ml.jadss.jadgens.JadGens;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

public class PurgeMachines {

    public PurgeMachines() { return; }

    public void purgeMachines() {
        Set<String> keys = data().getConfigurationSection("machines").getKeys(false);
        for (String key : keys) {
            int x = data().getInt("machines." + key + ".x");
            int y = data().getInt("machines." + key + ".y");
            int z = data().getInt("machines." + key + ".z");
            World world = Bukkit.getServer().getWorld(data().getString("machines." + key + ".world"));
            if (world == null)  {
                data().set("machines." + key, null);
            } else {
                Location location = new Location(world, x, y, z);
                location.getBlock().setType(Material.AIR);
                data().set("machines." + key, null);
            }
        }
        data().set("machines", null);
        data().set("machines.setup", true);
        data().set("machines.setup", null);
        JadGens.getInstance().getDataFile().saveData();
        JadGens.getInstance().getDataFile().reloadData();
    }

    public boolean removeIfAir(String id) {
        if (id == null) return false;
        if (!JadGens.getInstance().getConfig().getBoolean("machinesConfig.autoDestroy")) return false;
        World world = Bukkit.getServer().getWorld(data().getString("machines." + id + ".world"));
        if (world == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eThe &3&lMachine &ewith &b&lID &a\"" + id + "\" &ewas &c&lRemoved&e!"));
            data().set("machines." + id, null);
        }
        Location loc = new Location(world, data().getInt("machines." + id + ".x"),
                data().getInt("machines." + id + ".y"),
                data().getInt("machines." + id + ".z"));

        if (new MachineLookup().isMachine(loc.getBlock())) {
            if (loc.getBlock().getType().equals(Material.AIR)) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eThe &3&lMachine &ewith &b&lID &a\"" + id + "\" &ewas &c&lRemoved&e!"));
                data().set("machines." + id, null);
            }
        }
        return false;
    }

    public FileConfiguration data() {
        return JadGens.getInstance().getDataFile().data();
    }

}